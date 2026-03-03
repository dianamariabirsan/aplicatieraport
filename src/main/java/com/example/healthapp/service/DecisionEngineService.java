package com.example.healthapp.service;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.repository.DecisionLogRepository;
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
 * Motor decizional: evaluează administrările pacientului față de contraindicațiile/interacțiunile
 * medicamentului propus și creează automat un DecisionLog cu scorul calculat (0-100).
 * Opțional folosește un model SMILE RandomForest dacă a fost antrenat.
 */
@Service
@Transactional
public class DecisionEngineService {

    private static final int MIN_TOKEN_LENGTH = 4;
    /** Score threshold above which ML predicts a positive outcome (y=1). */
    private static final double ML_POSITIVE_THRESHOLD = 70.0;
    /** Score threshold below which ML predicts a negative outcome (y=0). */
    private static final double ML_NEGATIVE_THRESHOLD = 40.0;
    /** Delimiters used to split drug name tokens from free-text SmPC fields. */
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

    /** Opțional: model SMILE RandomForest setat de MlTrainingService după antrenare. */
    private volatile Object smileModel = null;

    public DecisionEngineService(AdministrareRepository administrareRepository, DecisionLogRepository decisionLogRepository) {
        this.administrareRepository = administrareRepository;
        this.decisionLogRepository = decisionLogRepository;
    }

    /** Called by MlTrainingService after training. Accepts a smile.classification.RandomForest. */
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
     * Evaluate and return result WITHOUT persisting a DecisionLog.
     * Call persistAudit() separately if you need the log.
     */
    public DecisionResult evaluate(AlocareTratament alocare) {
        if (alocare == null || alocare.getPacient() == null || alocare.getMedicament() == null) {
            return new DecisionResult(0.0, "DATE_INSUFICIENTE", List.of("DATE_INSUFICIENTE"), List.of("DATE_INSUFICIENTE"), null);
        }

        Pacient p = alocare.getPacient();
        Medicament m = alocare.getMedicament();

        List<String> warnings = new ArrayList<>();
        List<String> reguli = new ArrayList<>();

        List<Administrare> administrari = administrareRepository.findAllByPacientIdOrderByDataAdministrareDesc(p.getId());

        Set<String> concomitente = new HashSet<>();
        for (Administrare a : administrari) {
            if (a.getTipTratament() != null) concomitente.add(a.getTipTratament().toLowerCase(Locale.ROOT));
            if (a.getObservatii() != null) concomitente.add(a.getObservatii().toLowerCase(Locale.ROOT));
        }

        String contra = safeLower(m.getContraindicatii());
        String inter = safeLower(m.getInteractiuni());
        String avert = safeLower(m.getAvertizari());

        boolean smpcConfigured = !contra.isBlank() || !inter.isBlank();
        String externalChecks = smpcConfigured ? "SmPC_PARSE=OK" : "SmPC_PARSE=NOT_CONFIGURED";

        for (Administrare a : administrari) {
            String tip = safeLower(a.getTipTratament());
            if (tip.isBlank()) continue;

            if (!contra.isBlank() && containsWord(contra, tip)) {
                String ruleName = classifyRule(contra, tip, "CONTRAINDICATIE");
                reguli.add(ruleName);
                warnings.add("Posibilă contraindicație: " + a.getTipTratament() + " apare în SmPC.contraindicatii");
            }
            if (!inter.isBlank() && containsWord(inter, tip)) {
                String ruleName = classifyRule(inter, tip, "INTERACTIUNE");
                reguli.add(ruleName);
                warnings.add("Posibilă interacțiune: " + a.getTipTratament() + " apare în SmPC.interactiuni");
            }
            if (!avert.isBlank() && containsWord(avert, tip)) {
                reguli.add("AVERTIZARE:" + firstToken(tip).toUpperCase(Locale.ROOT));
                warnings.add("Avertizare: " + a.getTipTratament() + " menționat în SmPC.avertizari");
            }
        }

        DecisionFeatures fx = buildFeatures(p, m, administrari, concomitente);
        double ruleScore = scoreFromRules(warnings, fx);
        double finalScore = trySmilePredict(fx, ruleScore);

        String recomandare = buildRecomandare(m.getDenumire(), warnings, reguli);

        return new DecisionResult(finalScore, recomandare, warnings, reguli, fx);
    }

    /**
     * Persist a DecisionLog for the given evaluation result.
     */
    public DecisionLog persistAudit(AlocareTratament alocare, DecisionResult result, ActorType actorType) {
        String semanticFeatures = null;
        if (result.features() != null) {
            DecisionFeatures fx = result.features();
            semanticFeatures = String.format(
                java.util.Locale.ROOT,
                "{\"varsta\":%d,\"sex_feminin\":%d,\"greutate\":%.1f,\"inaltime\":%.1f," +
                "\"has_diabet\":%d,\"has_hta\":%d,\"admin_count\":%d," +
                "\"has_metformin\":%d,\"has_insulina\":%d,\"is_wegovy\":%d,\"is_mounjaro\":%d}",
                fx.varsta(),
                fx.sexF(),
                fx.greutate(),
                fx.inaltime(),
                fx.hasDiabet(),
                fx.hasHTA(),
                fx.adminCount(),
                fx.hasMetformin(),
                fx.hasInsulina(),
                fx.isWegovy(),
                fx.isMounjaro()
            );
        }

        DecisionLog log = new DecisionLog()
            .timestamp(Instant.now())
            .actorType(actorType)
            .recomandare(result.recomandare())
            .modelScore(result.score())
            .reguliTriggered(result.reguliTriggered().isEmpty() ? null : String.join(" | ", result.reguliTriggered()))
            .externalChecks(semanticFeatures)
            .alocare(alocare);

        return decisionLogRepository.save(log);
    }

    /**
     * Convenience method: evaluate + persist in one call.
     * Kept for backward compatibility.
     */
    public EngineResult evaluateAndLog(AlocareTratament alocare) {
        DecisionResult r = evaluate(alocare);
        if (alocare != null && alocare.getId() != null) {
            persistAudit(alocare, r, ActorType.SISTEM_AI);
        }
        return new EngineResult(r.score(), r.reguliTriggered(), r.recomandare());
    }

    // -----------------------------------------------------------------------
    // Private helpers
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
        int hasMetformin = containsAnyToken(concomitente, "metformin") ? 1 : 0;
        int hasInsulina = containsAnyToken(concomitente, "insulin") ? 1 : 0;

        String den = safeLower(m != null ? m.getDenumire() : null);
        int isWegovy = den.contains("wegovy") ? 1 : 0;
        int isMounjaro = den.contains("mounjaro") ? 1 : 0;

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

    private double scoreFromRules(List<String> warnings, DecisionFeatures fx) {
        long contraCount = warnings.stream().filter(w -> w.contains("contraindicație")).count();
        long interCount = warnings.stream().filter(w -> w.contains("interacțiune")).count();
        long avertCount = warnings.stream().filter(w -> w.contains("Avertizare")).count();

        double score = 100.0;
        score -= contraCount * 60.0;
        score -= interCount * 30.0;
        score -= avertCount * 10.0;
        if (fx != null) {
            if (fx.varsta() > 65) score -= 10.0;
            if (fx.hasDiabet() == 1) score -= 5.0;
        }
        return Math.max(0.0, Math.min(100.0, score));
    }

    private double trySmilePredict(DecisionFeatures fx, double ruleScore) {
        if (smileModel == null || fx == null) return ruleScore;
        try {
            // Use reflection to avoid hard compile-time dependency on SMILE
            var clazz = smileModel.getClass();
            // Build a single-row DataFrame for prediction via SMILE
            double[][] data = { fx.toArray() };
            var dfClass = Class.forName("smile.data.DataFrame");
            var ofMethod = dfClass.getMethod("of", double[][].class, String[].class);
            Object df = ofMethod.invoke(null, data, FEATURE_NAMES);
            var getMethod = dfClass.getMethod("get", int.class);
            Object tuple = getMethod.invoke(df, 0);
            java.lang.reflect.Method predictMethodRef = clazz.getMethod("predict", tuple.getClass());
            int pred = (int) predictMethodRef.invoke(smileModel, tuple);
            double mlScore = (pred == 1) ? Math.max(ruleScore, ML_POSITIVE_THRESHOLD) : Math.min(ruleScore, ML_NEGATIVE_THRESHOLD);
            return Math.max(0.0, Math.min(100.0, mlScore));
        } catch (Exception e) {
            return ruleScore;
        }
    }

    private String classifyRule(String smpcText, String tip, String defaultPrefix) {
        String token = firstToken(tip).toUpperCase(Locale.ROOT);
        if (smpcText.contains("hipoglicemie") || tip.contains("insulin") || tip.contains("sulfonilure")) {
            return "RISC_HIPOGLICEMIE";
        }
        if (smpcText.contains("hepatic") || tip.contains("hepat")) {
            return "RISC_HEPATIC";
        }
        if (smpcText.contains("renal") || tip.contains("renal") || tip.contains("nefro")) {
            return "RISC_RENAL";
        }
        if (smpcText.contains("cardiac") || tip.contains("cardiac") || tip.contains("aritmie")) {
            return "RISC_CARDIAC";
        }
        return defaultPrefix + ":" + token;
    }

    private String buildRecomandare(String denumire, List<String> warnings, List<String> reguli) {
        long contraCount = warnings.stream().filter(w -> w.contains("contraindicație")).count();
        long interCount = warnings.stream().filter(w -> w.contains("interacțiune")).count();
        String name = denumire != null ? denumire : "medicamentul propus";

        if (contraCount > 0) {
            return "NU recomand " + name + ": contraindicații detectate (" + contraCount + ").";
        } else if (interCount > 0) {
            return "Propune " + name + " cu monitorizare";
        } else {
            return "Propune " + name + " cu monitorizare";
        }
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }

    private String firstToken(String s) {
        if (s == null || s.isBlank()) return s == null ? "" : s;
        return s.split(TOKEN_DELIMITER.pattern())[0].trim();
    }

    private boolean containsWord(String haystack, String needle) {
        String token = firstToken(needle);
        if (token.length() < MIN_TOKEN_LENGTH) return false;
        String pattern = "(?s).*(?:^|\\W)" + java.util.regex.Pattern.quote(token) + "(?:\\W|$).*";
        return haystack.matches(pattern);
    }

    private boolean containsAnyToken(Set<String> set, String needle) {
        String n = needle.toLowerCase(Locale.ROOT);
        for (String s : set) if (s != null && s.contains(n)) return true;
        return false;
    }

    public record DecisionResult(
        double score,
        String recomandare,
        List<String> warnings,
        List<String> reguliTriggered,
        DecisionFeatures features
    ) {}

    /** Kept for backward compatibility with AlocareTratamentService. */
    public record EngineResult(Double score, List<String> reguli, String recomandare) {}
}
