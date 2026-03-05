package com.example.healthapp.service;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.domain.enumeration.SeveritateReactie;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.ml.DecisionFeatures;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor decizional cu rezoluție de conflicte bazată pe priorități explicite.
 *
 * <p>Ordinea priorităților (mai mic = mai prioritar):
 * <ol>
 *   <li>P1 CONTRAINDICATION      – oprire totală; contraindicație absolută detectată în SmPC</li>
 *   <li>P2 SEVERE_AE             – reacție adversă severă în istoricul pacientului</li>
 *   <li>P3 MAJOR_INTERACTION     – interacțiune medicamentoasă majoră detectată în SmPC</li>
 *   <li>P4 INSUFFICIENT_EFFICACY – eficacitate insuficientă raportată în istoricul pacientului</li>
 *   <li>P5 AI_SCORE              – model ML (dacă e antrenat)</li>
 *   <li>P6 DEFAULT               – nicio regulă specifică; scor bazat pe factori clinici</li>
 * </ol>
 *
 * <p>Scor final:
 * <ul>
 *   <li>P1 → 0.0 (hard stop)</li>
 *   <li>P2 → min(20.0, scor_baza)</li>
 *   <li>P3 → max(0.0, scor_baza − 30×nr_interacțiuni)</li>
 *   <li>P4 → max(0.0, scor_baza − 15.0)</li>
 *   <li>P5/P6 → scor_baza (eventual ajustat de model ML)</li>
 * </ul>
 * unde scor_baza = 100 − factori_clinici (vârstă, DZ2, …) ∈ [0, 100].
 */
@Service
@Transactional
public class DecisionEngineService {

    /** Priority levels — ordinal determines winner (lower ordinal = higher clinical priority). */
    public enum RulePriority {
        P1_CONTRAINDICATION,
        P2_SEVERE_AE,
        P3_MAJOR_INTERACTION,
        P4_INSUFFICIENT_EFFICACY,
        P5_AI_SCORE,
        P6_DEFAULT,
    }

    // Score caps/penalties by priority
    private static final double P1_SCORE = 0.0;
    private static final double P2_SCORE_CAP = 20.0;
    private static final double P3_INTERACTION_PENALTY = 30.0;
    private static final double P4_EFFICACY_PENALTY = 15.0;

    // Clinical-factor score penalties (applied before priority adjustment)
    private static final double PENALTY_ELDERLY = 10.0; // age > 65
    private static final double PENALTY_DIABETES = 5.0; // DZ2 present

    private static final int MIN_TOKEN_LENGTH = 4;
    private static final double ML_POSITIVE_THRESHOLD = 70.0;
    private static final double ML_NEGATIVE_THRESHOLD = 40.0;

    private static final java.util.regex.Pattern TOKEN_DELIMITER = java.util.regex.Pattern.compile("[\\s;,/]+");

    public static final String[] FEATURE_NAMES = {
        "varsta",
        "sexF",
        "greutate",
        "inaltime",
        "hasDiabet",
        "hasHTA",
        "adminCount",
        "hasMetformin",
        "hasInsulina",
        "isWegovy",
        "isMounjaro",
    };

    private final AdministrareRepository administrareRepository;
    private final DecisionLogRepository decisionLogRepository;
    private final ReactieAdversaRepository reactieAdversaRepository;

    /** Optional SMILE RandomForest model, set by MlTrainingService after training. */
    private volatile Object smileModel = null;

    public DecisionEngineService(
        AdministrareRepository administrareRepository,
        DecisionLogRepository decisionLogRepository,
        ReactieAdversaRepository reactieAdversaRepository
    ) {
        this.administrareRepository = administrareRepository;
        this.decisionLogRepository = decisionLogRepository;
        this.reactieAdversaRepository = reactieAdversaRepository;
    }

    public void setSmileModel(Object model) {
        this.smileModel = model;
    }

    public boolean hasSmileModel() {
        return smileModel != null;
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Evaluates the proposed treatment allocation and returns a scored decision result.
     * Does NOT persist anything — call {@link #persistAudit} separately.
     */
    public DecisionResult evaluate(AlocareTratament alocare) {
        if (alocare == null || alocare.getPacient() == null || alocare.getMedicament() == null) {
            return new DecisionResult(
                0.0,
                "DATE_INSUFICIENTE",
                List.of("DATE_INSUFICIENTE"),
                List.of("DATE_INSUFICIENTE"),
                null,
                "DATE_INSUFICIENTE",
                ActorType.SISTEM_AI,
                "Date insuficiente pentru evaluare",
                "Pacient sau medicament lipsă"
            );
        }

        Pacient p = alocare.getPacient();
        Medicament m = alocare.getMedicament();
        String medDen = m.getDenumire() != null ? m.getDenumire() : "medicamentul propus";

        // Fetch administrations with their linked medicament (eager, avoids N+1)
        List<Administrare> administrari = administrareRepository.findAllByPacientIdWithMedicamentOrderByDataAdministrareDesc(p.getId());

        // Fetch severe adverse events with medicament (P2 check)
        List<ReactieAdversa> reactiiSevere = reactieAdversaRepository.findByPacientIdAndSeveritate(p.getId(), SeveritateReactie.SEVERA);

        // Text-based concomitent set (fallback when Administrare.medicament is null)
        Set<String> concomitente = new HashSet<>();
        for (Administrare a : administrari) {
            if (a.getTipTratament() != null) concomitente.add(a.getTipTratament().toLowerCase(Locale.ROOT));
            if (a.getObservatii() != null) concomitente.add(a.getObservatii().toLowerCase(Locale.ROOT));
        }

        String contraSmpc = safeLower(m.getContraindicatii());
        String interSmpc = safeLower(m.getInteractiuni());
        String avertSmpc = safeLower(m.getAvertizari());

        List<String> warnings = new ArrayList<>();
        List<String> reguriCoduri = new ArrayList<>();
        RulePriority winningPriority = RulePriority.P6_DEFAULT;
        String overrideReason = null;
        int interactionCount = 0;

        // ---- P1: Absolute contraindications ----------------------------------------
        for (Administrare a : administrari) {
            if (matchesSmpc(a, contraSmpc)) {
                String ruleCode = "P1:" + classifyRule(contraSmpc, safeLower(a.getTipTratament()), "CONTRAINDIC");
                if (!reguriCoduri.contains(ruleCode)) {
                    reguriCoduri.add(ruleCode);
                    String drugName = resolvedName(a);
                    warnings.add("Contraindicație absolută: " + drugName + " apare în contraindicațiile SmPC ale " + medDen);
                    winningPriority = RulePriority.P1_CONTRAINDICATION;
                    overrideReason = "Contraindicație absolută detectată: " + drugName;
                }
            }
        }

        // ---- P2: Severe adverse events in history ----------------------------------
        if (winningPriority.ordinal() > RulePriority.P2_SEVERE_AE.ordinal()) {
            for (ReactieAdversa ra : reactiiSevere) {
                if (ra.getMedicament() != null && sameSubstance(ra.getMedicament(), m)) {
                    String code = "P2:AE_SEVER:" + safeCode(ra.getMedicament().getDenumire());
                    if (!reguriCoduri.contains(code)) {
                        reguriCoduri.add(code);
                        warnings.add("Reacție adversă severă în istoric: " + ra.getDescriere() + " la " + ra.getMedicament().getDenumire());
                        winningPriority = RulePriority.P2_SEVERE_AE;
                        overrideReason = "Reacție adversă severă la " + ra.getMedicament().getDenumire();
                    }
                }
            }
        }

        // ---- P3: Major drug interactions -------------------------------------------
        for (Administrare a : administrari) {
            if (matchesSmpc(a, interSmpc)) {
                String code = "P3:" + classifyRule(interSmpc, safeLower(a.getTipTratament()), "INTERACTIUNE");
                if (!reguriCoduri.contains(code)) {
                    reguriCoduri.add(code);
                    interactionCount++;
                    String drugName = resolvedName(a);
                    warnings.add("Interacțiune majoră: " + drugName + " apare în interacțiunile SmPC ale " + medDen);
                    if (winningPriority.ordinal() > RulePriority.P3_MAJOR_INTERACTION.ordinal()) {
                        winningPriority = RulePriority.P3_MAJOR_INTERACTION;
                        overrideReason = "Interacțiune medicamentoasă majoră cu " + drugName;
                    }
                }
            }
            // Warnings (avertizari) — informational only, not a priority escalation
            String tipLower = safeLower(a.getTipTratament());
            if (!avertSmpc.isBlank() && !tipLower.isBlank() && containsWord(avertSmpc, tipLower)) {
                String code = "P3:AVERTIZARE_" + firstToken(tipLower).toUpperCase(Locale.ROOT);
                if (!reguriCoduri.contains(code)) {
                    reguriCoduri.add(code);
                    warnings.add("Avertizare SmPC: " + resolvedName(a) + " menționat în avertizările " + medDen);
                }
            }
        }

        // ---- P4: Insufficient efficacy ---------------------------------------------
        if (winningPriority.ordinal() > RulePriority.P4_INSUFFICIENT_EFFICACY.ordinal()) {
            String toleranta = safeLower(p.getToleranta());
            String istoricTrat = safeLower(p.getIstoricTratament());
            if (
                containsAnyOf(
                    toleranta + " " + istoricTrat,
                    "slab",
                    "ineficient",
                    "fara raspuns",
                    "fără răspuns",
                    "rezistenta",
                    "rezistență"
                )
            ) {
                reguriCoduri.add("P4:EFICACITATE_INSUFICIENTA");
                warnings.add("Eficacitate insuficientă indicată în istoricul de tratament al pacientului");
                winningPriority = RulePriority.P4_INSUFFICIENT_EFFICACY;
                overrideReason = "Eficacitate insuficientă înregistrată în istoricul pacientului";
            }
        }

        // ---- Validation markers when no clinical rules P1-P3 triggered -------------
        boolean hasRuleCode = reguriCoduri
            .stream()
            .anyMatch(c -> c.startsWith("P1") || c.startsWith("P2") || c.startsWith("P3") || c.startsWith("P4"));
        if (!hasRuleCode) {
            reguriCoduri.add(0, "VERIFICAT:FARA_INTERACTIUNI");
            reguriCoduri.add(0, "VERIFICAT:FARA_CONTRAINDIC");
        }

        // ---- Clinical context factors (always appended) ----------------------------
        DecisionFeatures fx = buildFeatures(p, m, administrari, concomitente);
        addClinicalFactors(reguriCoduri, fx);

        // ---- Compute final score ---------------------------------------------------
        double baseScore = computeBaseScore(fx);
        double finalScore = applyPriorityScore(winningPriority, baseScore, interactionCount);
        finalScore = trySmilePredict(fx, finalScore, winningPriority);

        // ---- Audit coduri append (winning rule + final score) ----------------------
        reguriCoduri.add("PRIORITATE:" + winningPriority.name());
        reguriCoduri.add("SCOR:" + String.format(Locale.ROOT, "%.1f", finalScore));

        String recomandare = buildRecomandare(medDen, winningPriority, warnings);
        String auditSummary = buildAuditSummary(p, m, fx, administrari.size(), reactiiSevere.size());

        return new DecisionResult(
            finalScore,
            recomandare,
            warnings,
            reguriCoduri,
            fx,
            recomandare,
            ActorType.SISTEM_AI,
            overrideReason,
            auditSummary
        );
    }

    /**
     * Persists a {@link DecisionLog} for the given evaluation result.
     * Must be called AFTER the AlocareTratament has been saved (needs a valid FK).
     */
    public DecisionLog persistAudit(AlocareTratament alocare, DecisionResult result, ActorType actorType) {
        if (alocare == null || alocare.getId() == null) {
            throw new IllegalArgumentException("persistAudit: alocare is null or has no id");
        }
        if (result == null) {
            throw new IllegalArgumentException("persistAudit: result is null");
        }
        if (actorType == null) {
            actorType = ActorType.SISTEM_AI;
        }

        String reguliText = result.reguliTriggered() == null || result.reguliTriggered().isEmpty()
            ? "VERIFICAT:NICIO_REGULA_SPECIFICA"
            : String.join(" | ", result.reguliTriggered());

        DecisionLog log = new DecisionLog()
            .timestamp(Instant.now())
            .actorType(actorType)
            .recomandare(result.recomandare())
            .modelScore(result.score())
            .reguliTriggered(reguliText)
            .externalChecks(result.auditSummary())
            .finalDecision(result.finalDecision())
            .decisionSource(result.decisionSource() != null ? result.decisionSource() : ActorType.SISTEM_AI)
            .overrideReason(result.overrideReason())
            .alocare(alocare);

        return decisionLogRepository.save(log);
    }

    /** Convenience: evaluate + persist in one call. Kept for backward compatibility. */
    public EngineResult evaluateAndLog(AlocareTratament alocare) {
        DecisionResult r = evaluate(alocare);
        if (alocare != null && alocare.getId() != null) {
            persistAudit(alocare, r, ActorType.SISTEM_AI);
        }
        return new EngineResult(r.score(), r.reguliTriggered(), r.recomandare());
    }

    // -----------------------------------------------------------------------
    // Score computation
    // -----------------------------------------------------------------------

    /**
     * Base score derived from clinical factors only (no priority adjustments).
     * Score ∈ [0, 100].
     */
    private double computeBaseScore(DecisionFeatures fx) {
        double score = 100.0;
        if (fx != null) {
            if (fx.varsta() > 65) score -= PENALTY_ELDERLY;
            if (fx.hasDiabet() == 1) score -= PENALTY_DIABETES;
        }
        return Math.max(0.0, Math.min(100.0, score));
    }

    /**
     * Applies priority-level adjustment on top of the base score.
     *
     * <ul>
     *   <li>P1 → 0.0 (absolute hard stop)</li>
     *   <li>P2 → capped at {@value #P2_SCORE_CAP} (severe adverse event)</li>
     *   <li>P3 → base minus {@value #P3_INTERACTION_PENALTY} per distinct interaction, min 0</li>
     *   <li>P4 → base minus {@value #P4_EFFICACY_PENALTY}, min 0</li>
     *   <li>P5/P6 → base unchanged (ML adjustment applied afterwards)</li>
     * </ul>
     */
    private double applyPriorityScore(RulePriority priority, double baseScore, int interactionCount) {
        switch (priority) {
            case P1_CONTRAINDICATION:
                return P1_SCORE;
            case P2_SEVERE_AE:
                return Math.min(P2_SCORE_CAP, baseScore);
            case P3_MAJOR_INTERACTION:
                return Math.max(0.0, baseScore - P3_INTERACTION_PENALTY * interactionCount);
            case P4_INSUFFICIENT_EFFICACY:
                return Math.max(0.0, baseScore - P4_EFFICACY_PENALTY);
            default:
                return baseScore;
        }
    }

    /**
     * Optionally adjusts the rule-based score using the SMILE ML model.
     * The ML model is only consulted when no high-priority rule (P1-P4) has triggered,
     * to avoid overriding safety rules with a potentially miscalibrated model.
     */
    private double trySmilePredict(DecisionFeatures fx, double ruleScore, RulePriority priority) {
        // Do NOT let the ML model override safety-critical priority rules
        if (priority.ordinal() <= RulePriority.P4_INSUFFICIENT_EFFICACY.ordinal()) return ruleScore;
        if (smileModel == null || fx == null) return ruleScore;
        try {
            var clazz = smileModel.getClass();
            double[][] data = { fx.toArray() };
            var dfClass = Class.forName("smile.data.DataFrame");
            var ofMethod = dfClass.getMethod("of", double[][].class, String[].class);
            Object df = ofMethod.invoke(null, data, FEATURE_NAMES);
            var getMethod = dfClass.getMethod("get", int.class);
            Object tuple = getMethod.invoke(df, 0);
            java.lang.reflect.Method predictMethod = clazz.getMethod("predict", tuple.getClass());
            int pred = (int) predictMethod.invoke(smileModel, tuple);
            double mlScore = (pred == 1) ? Math.max(ruleScore, ML_POSITIVE_THRESHOLD) : Math.min(ruleScore, ML_NEGATIVE_THRESHOLD);
            return Math.max(0.0, Math.min(100.0, mlScore));
        } catch (Exception e) {
            return ruleScore;
        }
    }

    // -----------------------------------------------------------------------
    // SmPC matching helpers
    // -----------------------------------------------------------------------

    /**
     * Returns true if the administered drug's substance/name appears in the given SmPC text.
     * Checks by linked Medicament entity first (reliable); falls back to tipTratament text.
     */
    private boolean matchesSmpc(Administrare a, String smpcText) {
        if (smpcText.isBlank()) return false;
        if (a.getMedicament() != null) {
            String subst = safeLower(a.getMedicament().getSubstanta());
            String den = safeLower(a.getMedicament().getDenumire());
            if (!subst.isBlank() && smpcText.contains(subst)) return true;
            if (!den.isBlank() && smpcText.contains(den)) return true;
        }
        String tip = safeLower(a.getTipTratament());
        return !tip.isBlank() && containsWord(smpcText, tip);
    }

    /**
     * Returns true if two medicaments share the same active substance or brand identity.
     * Used for P2 check (severe AE linked to same drug).
     */
    private boolean sameSubstance(Medicament a, Medicament b) {
        if (a == null || b == null) return false;
        if (a.getId() != null && a.getId().equals(b.getId())) return true;
        String sA = safeLower(a.getSubstanta());
        String sB = safeLower(b.getSubstanta());
        if (!sA.isBlank() && !sB.isBlank() && sA.equals(sB)) return true;
        String dA = safeLower(a.getDenumire());
        String dB = safeLower(b.getDenumire());
        return !dA.isBlank() && !dB.isBlank() && (dA.contains(dB) || dB.contains(dA));
    }

    // -----------------------------------------------------------------------
    // Feature building
    // -----------------------------------------------------------------------

    private DecisionFeatures buildFeatures(Pacient p, Medicament m, List<Administrare> administrari, Set<String> concomitente) {
        int varsta = p.getVarsta() != null ? p.getVarsta() : 0;
        int sexF = p.getSex() != null && p.getSex().toLowerCase(Locale.ROOT).startsWith("f") ? 1 : 0;
        double greutate = p.getGreutate() != null ? p.getGreutate() : 0.0;
        double inaltime = p.getInaltime() != null ? p.getInaltime() : 0.0;

        String com = safeLower(p.getComorbiditati());
        int hasDiabet = com.contains("diabet") ? 1 : 0;
        int hasHTA = (com.contains("hta") || com.contains("hipertensi")) ? 1 : 0;
        int adminCount = administrari.size();

        // Prefer substance-based matching via linked Medicament; text fallback for legacy data
        int hasMetformin = administrari
                .stream()
                .anyMatch(
                    a ->
                        (a.getMedicament() != null && safeLower(a.getMedicament().getSubstanta()).contains("metformin")) ||
                        concomitente.stream().anyMatch(s -> s.contains("metformin"))
                )
            ? 1
            : 0;
        int hasInsulina = administrari
                .stream()
                .anyMatch(
                    a ->
                        (a.getMedicament() != null && safeLower(a.getMedicament().getSubstanta()).contains("insulin")) ||
                        concomitente.stream().anyMatch(s -> s.contains("insulin"))
                )
            ? 1
            : 0;

        String den = safeLower(m != null ? m.getDenumire() : null);
        String subst = safeLower(m != null ? m.getSubstanta() : null);
        int isWegovy = (den.contains("wegovy") || subst.contains("semaglutida")) ? 1 : 0;
        int isMounjaro = (den.contains("mounjaro") || subst.contains("tirzepatida")) ? 1 : 0;

        return new DecisionFeatures(
            varsta,
            sexF,
            greutate,
            inaltime,
            hasDiabet,
            hasHTA,
            adminCount,
            hasMetformin,
            hasInsulina,
            isWegovy,
            isMounjaro
        );
    }

    private void addClinicalFactors(List<String> reguriCoduri, DecisionFeatures fx) {
        if (fx == null) return;
        if (fx.varsta() > 65) reguriCoduri.add("FACTOR:VARSTA_INAINTATA");
        if (fx.hasDiabet() == 1) reguriCoduri.add("FACTOR:DZ2_PREZENT");
        if (fx.hasHTA() == 1) reguriCoduri.add("FACTOR:HTA_PREZENTA");
        if (fx.greutate() > 100) reguriCoduri.add("FACTOR:OBEZITATE_SEVERA");
        else if (fx.greutate() > 80) reguriCoduri.add("FACTOR:SUPRAPONDERE");
        if (fx.hasMetformin() == 1) reguriCoduri.add("FACTOR:METFORMIN_CONCOMITENT");
        if (fx.hasInsulina() == 1) reguriCoduri.add("FACTOR:INSULINA_CONCOMITENTA");
        if (fx.adminCount() == 0) reguriCoduri.add("FACTOR:TRATAMENT_NOU");
        else reguriCoduri.add("FACTOR:ADMIN_ANTERIOARE_" + fx.adminCount());
    }

    // -----------------------------------------------------------------------
    // Text / recommendation helpers
    // -----------------------------------------------------------------------

    private String buildRecomandare(String denumire, RulePriority priority, List<String> warnings) {
        switch (priority) {
            case P1_CONTRAINDICATION:
                return "NU recomand " + denumire + ": contraindicație absolută detectată.";
            case P2_SEVERE_AE:
                return "ATENȚIE: " + denumire + " contraindicat – reacție adversă severă în istoric.";
            case P3_MAJOR_INTERACTION:
                return "PRECAUȚIE: " + denumire + " – interacțiune majoră detectată. Necesită monitorizare strictă.";
            case P4_INSUFFICIENT_EFFICACY:
                return "Reevaluați " + denumire + " – eficacitate insuficientă înregistrată.";
            default:
                return "Propune " + denumire + " cu monitorizare";
        }
    }

    private String buildAuditSummary(Pacient p, Medicament m, DecisionFeatures fx, int adminCount, int reactiiSevereCount) {
        if (p == null) return "Pacient: necunoscut";
        StringBuilder sb = new StringBuilder();
        int v = fx != null ? fx.varsta() : (p.getVarsta() != null ? p.getVarsta() : 0);
        double g = fx != null ? fx.greutate() : (p.getGreutate() != null ? p.getGreutate() : 0.0);
        double h = fx != null ? fx.inaltime() : (p.getInaltime() != null ? p.getInaltime() : 0.0);
        sb
            .append("Pacient: ")
            .append(v)
            .append(" ani, ")
            .append(p.getSex() != null ? p.getSex() : "?")
            .append(", ")
            .append(g)
            .append(" kg, ")
            .append(h)
            .append(" cm");

        List<String> com = new ArrayList<>();
        if (fx != null && fx.hasDiabet() == 1) com.add("DZ2");
        if (fx != null && fx.hasHTA() == 1) com.add("HTA");
        if (!com.isEmpty()) sb.append(" | Comorbidități: ").append(String.join(", ", com));

        sb.append(" | Administrări anterioare: ").append(adminCount);
        if (reactiiSevereCount > 0) sb.append(" | Reacții adverse severe: ").append(reactiiSevereCount);
        if (m != null) {
            sb.append(" | Medicament evaluat: ").append(m.getDenumire() != null ? m.getDenumire() : "?");
            if (m.getSubstanta() != null) sb.append(" (").append(m.getSubstanta()).append(")");
        }
        return sb.toString();
    }

    private String classifyRule(String smpcText, String tip, String defaultPrefix) {
        if (smpcText.contains("hipoglicemie") || tip.contains("insulin") || tip.contains("sulfonilure")) return (
            defaultPrefix + "_RISC_HIPOGLICEMIE"
        );
        if (smpcText.contains("hepatic") || tip.contains("hepat")) return defaultPrefix + "_RISC_HEPATIC";
        if (smpcText.contains("renal") || tip.contains("renal") || tip.contains("nefro")) return defaultPrefix + "_RISC_RENAL";
        if (smpcText.contains("cardiac") || tip.contains("cardiac") || tip.contains("aritmie")) return defaultPrefix + "_RISC_CARDIAC";
        return defaultPrefix + "_" + firstToken(tip).toUpperCase(Locale.ROOT);
    }

    private String resolvedName(Administrare a) {
        if (a.getMedicament() != null && a.getMedicament().getDenumire() != null) return a.getMedicament().getDenumire();
        return a.getTipTratament() != null ? a.getTipTratament() : "medicament necunoscut";
    }

    private String safeCode(String name) {
        if (name == null) return "NECUNOSCUT";
        return name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "_");
    }

    private boolean containsAnyOf(String text, String... needles) {
        if (text == null || text.isBlank()) return false;
        String lower = text.toLowerCase(Locale.ROOT);
        for (String n : needles) if (lower.contains(n)) return true;
        return false;
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }

    private String firstToken(String s) {
        if (s == null || s.isBlank()) return "";
        return s.split(TOKEN_DELIMITER.pattern())[0].trim();
    }

    private boolean containsWord(String haystack, String needle) {
        String token = firstToken(needle);
        if (token.length() < MIN_TOKEN_LENGTH) return false;
        String pattern = "(?s).*(?:^|\\W)" + java.util.regex.Pattern.quote(token) + "(?:\\W|$).*";
        return haystack.matches(pattern);
    }

    // -----------------------------------------------------------------------
    // Public result types
    // -----------------------------------------------------------------------

    /**
     * Full evaluation result.
     *
     * @param score            calculated decision score ∈ [0, 100]
     * @param recomandare      human-readable recommendation text
     * @param warnings         list of clinical warnings generated during evaluation
     * @param reguliTriggered  list of rule codes (always non-empty)
     * @param features         ML feature vector (may be null for insufficient data)
     * @param finalDecision    same as recomandare when automated; can be overridden by medic
     * @param decisionSource   who produced this result (always SISTEM_AI when automated)
     * @param overrideReason   null if no priority rule triggered; otherwise explanation
     * @param auditSummary     human-readable patient/drug context for the DecisionLog
     */
    public record DecisionResult(
        double score,
        String recomandare,
        List<String> warnings,
        List<String> reguliTriggered,
        DecisionFeatures features,
        String finalDecision,
        ActorType decisionSource,
        String overrideReason,
        String auditSummary
    ) {}

    /** Kept for backward compatibility with AlocareTratamentService. */
    public record EngineResult(Double score, List<String> reguli, String recomandare) {}
}
