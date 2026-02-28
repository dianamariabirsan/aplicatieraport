package com.example.healthapp.service;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Monitorizare;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.MonitorizareRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.dto.EvidentaStudiuClinicDTO;
import com.example.healthapp.service.dto.EvaluareDecizieCerereDTO;
import com.example.healthapp.service.dto.EvaluareDecizieRezultatDTO;
import com.example.healthapp.service.dto.EvaluareMedicamentDTO;
import com.example.healthapp.service.dto.RecomandareAbCerereDTO;
import com.example.healthapp.service.dto.RecomandareAbRezultatDTO;
import com.example.healthapp.service.dto.SigurantaExternaDTO;
import com.example.healthapp.service.dto.StudiiCliniceStatisticaDTO;
import com.example.healthapp.service.dto.ValidarePropunereCerereDTO;
import com.example.healthapp.service.dto.ValidarePropunereRezultatDTO;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor decizional principal — independent de CRUD.
 *
 * <p>Implementează trei straturi logice:
 * <ol>
 *   <li>Reguli clinice (hard/soft) pe baza SMPC + istoric pacient</li>
 *   <li>Scor predictiv rule-based (stub ML — poate fi delegat unui microserviciu)</li>
 *   <li>Meta-decisor: funcție de agregare justificabilă</li>
 * </ol>
 *
 * <p>Persistă deciziile în {@link AlocareTratament} + {@link DecisionLog} pentru audit complet.
 */
@Service
@Transactional
public class MotorDecizionalService {

    private static final Logger LOG = LoggerFactory.getLogger(MotorDecizionalService.class);

    private static final double SCOR_INITIAL = 100.0;
    private static final double PENALIZARE_CONTRAINDICATIE_HARD = 40.0;
    private static final double PENALIZARE_INTERACTIUNE = 15.0;
    private static final double PENALIZARE_TOLERANTA_SLABA = 10.0;
    private static final double PENALIZARE_VARSTA_AVANSATA = 5.0;
    private static final double PENALIZARE_REACTIE_ADVERSA_SEVERA = 20.0;
    private static final double PENALIZARE_TA_RIDICATA = 8.0;
    private static final double BONUS_INFO_EXTERN = 5.0;
    private static final double PRAG_RECOMANDARE = 10.0;

    private final PacientRepository pacientRepository;
    private final MedicamentRepository medicamentRepository;
    private final MedicRepository medicRepository;
    private final ReactieAdversaRepository reactieAdversaRepository;
    private final MonitorizareRepository monitorizareRepository;
    private final AlocareTratamentRepository alocareTratamentRepository;
    private final DecisionLogRepository decisionLogRepository;
    private final EvidentaStudiiCliniceService evidentaStudiiCliniceService;

    public MotorDecizionalService(
        PacientRepository pacientRepository,
        MedicamentRepository medicamentRepository,
        MedicRepository medicRepository,
        ReactieAdversaRepository reactieAdversaRepository,
        MonitorizareRepository monitorizareRepository,
        AlocareTratamentRepository alocareTratamentRepository,
        DecisionLogRepository decisionLogRepository,
        EvidentaStudiiCliniceService evidentaStudiiCliniceService
    ) {
        this.pacientRepository = pacientRepository;
        this.medicamentRepository = medicamentRepository;
        this.medicRepository = medicRepository;
        this.reactieAdversaRepository = reactieAdversaRepository;
        this.monitorizareRepository = monitorizareRepository;
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.decisionLogRepository = decisionLogRepository;
        this.evidentaStudiiCliniceService = evidentaStudiiCliniceService;
    }

    // =========================================================================
    // ENDPOINT 1: POST /api/decizie/valideaza-propunere
    // Medicul propune — sistemul validează
    // =========================================================================

    /**
     * Validează propunerea medicului pentru un pacient.
     * Persistă un {@link DecisionLog} cu actorType=MEDIC.
     */
    @Transactional
    public ValidarePropunereRezultatDTO valideazaPropunere(ValidarePropunereCerereDTO cerere) {
        Pacient pacient = gasestePacient(cerere.getPacientId());
        Medicament medicament = gasesteMedicament(cerere.getMedicamentPropusId());

        List<String> reguli = new ArrayList<>();
        List<String> avertismente = new ArrayList<>();
        List<String> contraindicatii = new ArrayList<>();
        List<String> interactiuni = new ArrayList<>();

        // Verificare contraindicații (reguli hard → invalidare)
        boolean areContraindicatieHard = verificaContraindicatii(pacient, medicament, contraindicatii, reguli);

        // Verificare interacțiuni (soft → avertisment)
        verificaInteractiuni(pacient, medicament, interactiuni, avertismente);

        // Verificare reacții adverse anterioare la același medicament
        verificaReactiiAdverseIstorice(pacient, medicament, avertismente, reguli);

        // Verificare monitorizare recentă
        verificaMonitorizareRecenta(pacient, avertismente);

        boolean propunereValida = !areContraindicatieHard;

        // Persistare DecisionLog (actorType = MEDIC)
        String rezumatReguli = String.join("; ", reguli);
        String rezumatExtern = "Propunere medic: " + medicament.getDenumire();
        DecisionLog logMedic = persistaDecisionLog(
            null,
            ActorType.MEDIC,
            propunereValida ? "VALID: " + medicament.getDenumire() : "INVALID: " + medicament.getDenumire(),
            null,
            rezumatReguli,
            rezumatExtern
        );

        ValidarePropunereRezultatDTO rezultat = new ValidarePropunereRezultatDTO();
        rezultat.setPropunereValida(propunereValida);
        rezultat.setMedicamentDenumire(medicament.getDenumire());
        rezultat.setReguli(reguli);
        rezultat.setAvertismente(avertismente);
        rezultat.setContraindicatiiGasite(contraindicatii);
        rezultat.setInteractiuniGasite(interactiuni);
        rezultat.setMotiv(propunereValida
            ? "Propunerea este conformă cu profilul pacientului."
            : "Contraindicații detectate: " + String.join(", ", contraindicatii));
        rezultat.setDecisionLogId(logMedic.getId());

        return rezultat;
    }

    // =========================================================================
    // ENDPOINT 2: POST /api/decizie/recomanda-ab
    // Sistemul recomandă între A și B
    // =========================================================================

    /**
     * Recomandă între tratamentul A și tratamentul B.
     * Persistă {@link DecisionLog} cu actorType=SISTEM_AI și {@link AlocareTratament}.
     */
    @Transactional
    public RecomandareAbRezultatDTO recomandaAb(RecomandareAbCerereDTO cerere) {
        Pacient pacient = gasestePacient(cerere.getPacientId());
        Medicament medicamentA = gasesteMedicament(cerere.getMedicamentAId());
        Medicament medicamentB = gasesteMedicament(cerere.getMedicamentBId());

        // Calculare scoruri
        RezultatScoring scoringA = calculeazaScor(pacient, medicamentA);
        RezultatScoring scoringB = calculeazaScor(pacient, medicamentB);

        // Obținere evidență studii clinice
        EvidentaStudiuClinicDTO evidentaA = evidentaStudiiCliniceService
            .obtineEvidentaPrimulStudiu(medicamentA.getDenumire());
        EvidentaStudiuClinicDTO evidentaB = evidentaStudiiCliniceService
            .obtineEvidentaPrimulStudiu(medicamentB.getDenumire());

        // Meta-decisor
        String recomandat = determinaRecomandat(
            medicamentA.getDenumire(), scoringA.scor,
            medicamentB.getDenumire(), scoringB.scor
        );
        Medicament medicamentRecomandat = recomandat.equals(medicamentA.getDenumire())
            ? medicamentA : medicamentB;
        double scorRecomandat = recomandat.equals(medicamentA.getDenumire())
            ? scoringA.scor : scoringB.scor;

        List<String> motive = construiesteMotive(
            medicamentA.getDenumire(), scoringA,
            medicamentB.getDenumire(), scoringB,
            recomandat, evidentaA, evidentaB
        );

        // Persistare AlocareTratament
        AlocareTratament alocare = persistaAlocareTratament(
            pacient, medicamentRecomandat, cerere.getMedicId(),
            recomandat, scorRecomandat, String.join("; ", motive)
        );

        // Persistare DecisionLog (actorType = SISTEM_AI)
        String reguliCombinate = "A: [" + String.join("; ", scoringA.reguli) +
            "] | B: [" + String.join("; ", scoringB.reguli) + "]";
        DecisionLog logSistem = persistaDecisionLog(
            alocare, ActorType.SISTEM_AI,
            recomandat, scorRecomandat,
            reguliCombinate,
            "ClinicalTrials: A=" + (evidentaA.getNctId() != null ? evidentaA.getNctId() : "N/A") +
            ", B=" + (evidentaB.getNctId() != null ? evidentaB.getNctId() : "N/A")
        );

        RecomandareAbRezultatDTO rezultat = new RecomandareAbRezultatDTO();
        rezultat.setDenumireA(medicamentA.getDenumire());
        rezultat.setDenumireB(medicamentB.getDenumire());
        rezultat.setScorA(scoringA.scor);
        rezultat.setScorB(scoringB.scor);
        rezultat.setRecomandat(recomandat);
        rezultat.setMotive(motive);
        rezultat.setReguliA(scoringA.reguli);
        rezultat.setReguliB(scoringB.reguli);
        rezultat.setEvidentaStudiuA(evidentaA);
        rezultat.setEvidentaStudiuB(evidentaB);
        rezultat.setDecisionLogId(logSistem.getId());
        rezultat.setAlocareTratamentId(alocare.getId());

        return rezultat;
    }

    // =========================================================================
    // ENDPOINT 3: POST /api/decizie-tratament (existent, extins)
    // Evaluare combinată (backward-compatible)
    // =========================================================================

    /**
     * Evaluare combinată: validează propunerea medicului + recomandă A vs B.
     * Compatibil cu endpoint-ul existent.
     */
    @Transactional
    public EvaluareDecizieRezultatDTO evalueaza(EvaluareDecizieCerereDTO cerere) {
        Pacient pacient = gasestePacient(cerere.getPacientId());

        Medicament medicamentA = medicamentRepository
            .findOneByDenumireIgnoreCase(cerere.getTratamentA())
            .orElse(null);
        Medicament medicamentB = medicamentRepository
            .findOneByDenumireIgnoreCase(cerere.getTratamentB())
            .orElse(null);

        RezultatScoring scoringA = medicamentA != null
            ? calculeazaScor(pacient, medicamentA)
            : new RezultatScoring(0.0, List.of("Medicament A inexistent în baza de date"));
        RezultatScoring scoringB = medicamentB != null
            ? calculeazaScor(pacient, medicamentB)
            : new RezultatScoring(0.0, List.of("Medicament B inexistent în baza de date"));

        EvaluareMedicamentDTO evalA = construiesteEvaluareMedicament(cerere.getTratamentA(), medicamentA, scoringA);
        EvaluareMedicamentDTO evalB = construiesteEvaluareMedicament(cerere.getTratamentB(), medicamentB, scoringB);

        String recomandat = determinaRecomandat(
            cerere.getTratamentA(), scoringA.scor,
            cerere.getTratamentB(), scoringB.scor
        );

        List<String> motive = new ArrayList<>();
        motive.add("Scor " + cerere.getTratamentA() + ": " + String.format("%.1f", scoringA.scor));
        motive.add("Scor " + cerere.getTratamentB() + ": " + String.format("%.1f", scoringB.scor));
        scoringA.reguli.forEach(r -> motive.add("[A] " + r));
        scoringB.reguli.forEach(r -> motive.add("[B] " + r));

        // ClinicalTrials links
        String query = cerere.getTratamentA() + " OR " + cerere.getTratamentB();
        String linkTrials = "https://clinicaltrials.gov/search?query=" +
            URLEncoder.encode(query, StandardCharsets.UTF_8);

        StudiiCliniceStatisticaDTO st = new StudiiCliniceStatisticaDTO();
        st.setInterogare(query);
        st.setLinkClinicalTrials(linkTrials);

        // Evidență studii clinice
        EvidentaStudiuClinicDTO evidentaA = evidentaStudiiCliniceService
            .obtineEvidentaPrimulStudiu(cerere.getTratamentA());
        EvidentaStudiuClinicDTO evidentaB = evidentaStudiiCliniceService
            .obtineEvidentaPrimulStudiu(cerere.getTratamentB());

        SigurantaExternaDTO siguranta = new SigurantaExternaDTO();
        siguranta.setContraindicatiiGasite(extrageContraindicatiiGasite(scoringA, scoringB));
        siguranta.setAvertismente(motive);

        // Persistare dacă avem cel puțin un medicament valid
        Long alocareTratamentId = null;
        Long decisionLogId = null;
        Medicament medicamentRecomandat = recomandat.equals(cerere.getTratamentA()) ? medicamentA : medicamentB;
        double scorRecomandat = recomandat.equals(cerere.getTratamentA()) ? scoringA.scor : scoringB.scor;

        if (medicamentRecomandat != null) {
            AlocareTratament alocare = persistaAlocareTratament(
                pacient, medicamentRecomandat, cerere.getMedicId(),
                recomandat, scorRecomandat, String.join("; ", motive)
            );
            DecisionLog log = persistaDecisionLog(
                alocare, ActorType.SISTEM_AI, recomandat, scorRecomandat,
                "A: " + String.join("; ", scoringA.reguli) + " | B: " + String.join("; ", scoringB.reguli),
                "ClinicalTrials: " + linkTrials
            );
            alocareTratamentId = alocare.getId();
            decisionLogId = log.getId();
        }

        EvaluareDecizieRezultatDTO rezultat = new EvaluareDecizieRezultatDTO();
        rezultat.setEvaluareA(evalA);
        rezultat.setEvaluareB(evalB);
        rezultat.setScorA(scoringA.scor);
        rezultat.setScorB(scoringB.scor);
        rezultat.setStudiiClinice(st);
        rezultat.setSigurantaExterna(siguranta);
        rezultat.setRecomandare(recomandat);
        rezultat.setMotivare(String.join("; ", motive));
        rezultat.setEvidentaStudiuA(evidentaA);
        rezultat.setEvidentaStudiuB(evidentaB);
        rezultat.setAlocareTratamentId(alocareTratamentId);
        rezultat.setDecisionLogId(decisionLogId);
        rezultat.getLinkuri().add(linkTrials);

        return rezultat;
    }

    // =========================================================================
    // LOGICA INTERNĂ DE SCORING
    // =========================================================================

    /**
     * Calculează scorul unui medicament pentru un pacient.
     * Scor inițial: 100. Penalizări pentru contraindicații, interacțiuni,
     * reacții adverse istorice, factori demografici.
     */
    private RezultatScoring calculeazaScor(Pacient pacient, Medicament medicament) {
        double scor = SCOR_INITIAL;
        List<String> reguli = new ArrayList<>();

        // --- Reguli hard: contraindicații ---
        if (medicament.getContraindicatii() != null && pacient.getComorbiditati() != null) {
            for (String contra : split(medicament.getContraindicatii())) {
                if (pacient.getComorbiditati().toLowerCase().contains(contra.toLowerCase())) {
                    scor -= PENALIZARE_CONTRAINDICATIE_HARD;
                    reguli.add("CONTRAINDICATIE_HARD: " + contra);
                }
            }
        }

        // --- Reguli soft: interacțiuni cu istoricul de tratament ---
        if (medicament.getInteractiuni() != null && pacient.getIstoricTratament() != null) {
            for (String inter : split(medicament.getInteractiuni())) {
                if (pacient.getIstoricTratament().toLowerCase().contains(inter.toLowerCase())) {
                    scor -= PENALIZARE_INTERACTIUNE;
                    reguli.add("INTERACTIUNE_DETECTATA: " + inter);
                }
            }
        }

        // --- Toleranță slabă ---
        if (pacient.getToleranta() != null) {
            String toleranta = pacient.getToleranta().toLowerCase();
            if (toleranta.contains("slab") || toleranta.contains("intoleranta") || toleranta.contains("redusa")) {
                scor -= PENALIZARE_TOLERANTA_SLABA;
                reguli.add("TOLERANTA_SLABA");
            }
        }

        // --- Factori demografici: vârstă ---
        if (pacient.getVarsta() != null && pacient.getVarsta() > 75) {
            scor -= PENALIZARE_VARSTA_AVANSATA;
            reguli.add("VARSTA_AVANSATA: " + pacient.getVarsta() + " ani");
        }

        // --- Reacții adverse anterioare la același medicament ---
        List<ReactieAdversa> reactii = reactieAdversaRepository.findByPacientId(pacient.getId());
        long reactiiSevere = reactii.stream()
            .filter(r -> medicament.getId().equals(r.getMedicament() != null ? r.getMedicament().getId() : null))
            .filter(r -> "severa".equalsIgnoreCase(r.getSeveritate()) || "grava".equalsIgnoreCase(r.getSeveritate()))
            .count();
        if (reactiiSevere > 0) {
            scor -= PENALIZARE_REACTIE_ADVERSA_SEVERA * reactiiSevere;
            reguli.add("REACTII_ADVERSE_SEVERE_ANTERIOARE: " + reactiiSevere);
        }

        // --- Monitorizare recentă: tensiune arterială ridicată ---
        Optional<Monitorizare> ultimaMonitorizare = monitorizareRepository
            .findTopByPacientIdOrderByDataInstantDesc(pacient.getId());
        if (ultimaMonitorizare.isPresent()) {
            Monitorizare m = ultimaMonitorizare.get();
            if (m.getTensiuneSist() != null && m.getTensiuneSist() > 160) {
                scor -= PENALIZARE_TA_RIDICATA;
                reguli.add("TA_SISTOLICA_RIDICATA: " + m.getTensiuneSist());
            }
        }

        // --- Bonus: informații externe disponibile (SMPC sincronizat) ---
        if (medicament.getInfoExtern() != null) {
            scor += BONUS_INFO_EXTERN;
            reguli.add("INFO_EXTERN_DISPONIBIL: " + medicament.getDenumire());
        }

        // Normalizare scor în intervalul [0, 100]
        scor = Math.max(0.0, Math.min(100.0, scor));

        return new RezultatScoring(Math.round(scor * 10.0) / 10.0, reguli);
    }

    /**
     * Meta-decisor: determină tratamentul recomandat pe baza scorurilor.
     * Dacă diferența este sub prag → ambele opțiuni sunt echivalente.
     */
    private String determinaRecomandat(String denumireA, double scorA, String denumireB, double scorB) {
        if (scorA <= 0 && scorB <= 0) {
            return "Niciun tratament recomandat (contraindicații majore detectate)";
        }
        if (scorA - scorB > PRAG_RECOMANDARE) {
            return denumireA;
        }
        if (scorB - scorA > PRAG_RECOMANDARE) {
            return denumireB;
        }
        return "Ambele opțiuni sunt echivalente (diferență scor: " +
            String.format("%.1f", Math.abs(scorA - scorB)) + ")";
    }

    // =========================================================================
    // VERIFICĂRI INDEPENDENTE (pentru validate-proposal)
    // =========================================================================

    private boolean verificaContraindicatii(
        Pacient pacient, Medicament medicament,
        List<String> contraindicatiiGasite, List<String> reguli
    ) {
        boolean areContraindicatieHard = false;
        if (medicament.getContraindicatii() != null && pacient.getComorbiditati() != null) {
            for (String contra : split(medicament.getContraindicatii())) {
                if (pacient.getComorbiditati().toLowerCase().contains(contra.toLowerCase())) {
                    contraindicatiiGasite.add(contra);
                    reguli.add("CONTRAINDICATIE_HARD: " + contra);
                    areContraindicatieHard = true;
                }
            }
        }
        return areContraindicatieHard;
    }

    private void verificaInteractiuni(
        Pacient pacient, Medicament medicament,
        List<String> interactiuniGasite, List<String> avertismente
    ) {
        if (medicament.getInteractiuni() != null && pacient.getIstoricTratament() != null) {
            for (String inter : split(medicament.getInteractiuni())) {
                if (pacient.getIstoricTratament().toLowerCase().contains(inter.toLowerCase())) {
                    interactiuniGasite.add(inter);
                    avertismente.add("AVERTISMENT_INTERACTIUNE: " + inter);
                }
            }
        }
    }

    private void verificaReactiiAdverseIstorice(
        Pacient pacient, Medicament medicament,
        List<String> avertismente, List<String> reguli
    ) {
        List<ReactieAdversa> reactii = reactieAdversaRepository.findByPacientId(pacient.getId());
        long reactiiSevere = reactii.stream()
            .filter(r -> medicament.getId().equals(r.getMedicament() != null ? r.getMedicament().getId() : null))
            .filter(r -> "severa".equalsIgnoreCase(r.getSeveritate()) || "grava".equalsIgnoreCase(r.getSeveritate()))
            .count();
        if (reactiiSevere > 0) {
            avertismente.add("ATENTIE: " + reactiiSevere + " reacție/reacții adverse severe anterioare la acest medicament");
            reguli.add("REACTII_ADVERSE_SEVERE_ISTORICE: " + reactiiSevere);
        }
    }

    private void verificaMonitorizareRecenta(Pacient pacient, List<String> avertismente) {
        monitorizareRepository.findTopByPacientIdOrderByDataInstantDesc(pacient.getId())
            .ifPresent(m -> {
                if (m.getTensiuneSist() != null && m.getTensiuneSist() > 160) {
                    avertismente.add("TA sistolică ridicată: " + m.getTensiuneSist() + " mmHg");
                }
                if (m.getGlicemie() != null && m.getGlicemie() > 200) {
                    avertismente.add("Glicemie ridicată: " + m.getGlicemie() + " mg/dL");
                }
            });
    }

    // =========================================================================
    // PERSISTARE
    // =========================================================================

    private AlocareTratament persistaAlocareTratament(
        Pacient pacient, Medicament medicament, Long medicId,
        String tratamentPropus, double scor, String motiv
    ) {
        AlocareTratament alocare = new AlocareTratament();
        alocare.setDataDecizie(Instant.now());
        alocare.setTratamentPropus(tratamentPropus);
        alocare.setMotivDecizie(motiv);
        alocare.setScorDecizie(scor);
        alocare.setDecizieValidata(false);
        alocare.setPacient(pacient);
        alocare.setMedicament(medicament);
        if (medicId != null) {
            medicRepository.findById(medicId).ifPresent(alocare::setMedic);
        }
        return alocareTratamentRepository.save(alocare);
    }

    private DecisionLog persistaDecisionLog(
        AlocareTratament alocare, ActorType actorType,
        String recomandare, Double scor, String reguli, String verificariExterne
    ) {
        DecisionLog log = new DecisionLog();
        log.setTimestamp(Instant.now());
        log.setActorType(actorType);
        log.setRecomandare(recomandare);
        log.setModelScore(scor);
        log.setReguliTriggered(reguli);
        log.setExternalChecks(verificariExterne);
        log.setAlocare(alocare);
        return decisionLogRepository.save(log);
    }

    // =========================================================================
    // UTILITARE
    // =========================================================================

    private EvaluareMedicamentDTO construiesteEvaluareMedicament(
        String denumire, Medicament medicament, RezultatScoring scoring
    ) {
        EvaluareMedicamentDTO dto = new EvaluareMedicamentDTO();
        dto.setDenumire(denumire);
        dto.setScor(scoring.scor);
        dto.setReguliTriggered(scoring.reguli);

        if (medicament == null) {
            dto.setRezumatSiguranta("Medicamentul nu există în baza de date.");
            return dto;
        }

        dto.setContraindicatii(medicament.getContraindicatii());
        dto.setInteractiuni(medicament.getInteractiuni());
        dto.setIndicatii(medicament.getIndicatii());

        if (medicament.getInfoExtern() != null) {
            dto.setSursaUrl(medicament.getInfoExtern().getSourceUrl());
            dto.setRezumatSiguranta(medicament.getInfoExtern().getProductSummary());
        } else {
            dto.setRezumatSiguranta("Nu există info externă sincronizată.");
        }

        return dto;
    }

    private List<String> construiesteMotive(
        String denumireA, RezultatScoring scoringA,
        String denumireB, RezultatScoring scoringB,
        String recomandat,
        EvidentaStudiuClinicDTO evidentaA,
        EvidentaStudiuClinicDTO evidentaB
    ) {
        List<String> motive = new ArrayList<>();
        motive.add("Scor " + denumireA + ": " + String.format("%.1f", scoringA.scor) + "/100");
        motive.add("Scor " + denumireB + ": " + String.format("%.1f", scoringB.scor) + "/100");

        if (evidentaA.getFaza() != null) {
            motive.add("Trial " + denumireA + " (faza " + evidentaA.getFaza() +
                ", înrolare: " + (evidentaA.getInrolare() != null ? evidentaA.getInrolare() : "N/A") + ")");
        }
        if (evidentaB.getFaza() != null) {
            motive.add("Trial " + denumireB + " (faza " + evidentaB.getFaza() +
                ", înrolare: " + (evidentaB.getInrolare() != null ? evidentaB.getInrolare() : "N/A") + ")");
        }

        motive.add("Recomandat: " + recomandat);
        return motive;
    }

    private List<String> extrageContraindicatiiGasite(RezultatScoring scoringA, RezultatScoring scoringB) {
        List<String> lista = new ArrayList<>();
        scoringA.reguli.stream()
            .filter(r -> r.startsWith("CONTRAINDICATIE"))
            .forEach(lista::add);
        scoringB.reguli.stream()
            .filter(r -> r.startsWith("CONTRAINDICATIE"))
            .forEach(lista::add);
        return lista;
    }

    private String[] split(String text) {
        if (text == null || text.isBlank()) return new String[0];
        return text.split("[,;\\n]+");
    }

    private Pacient gasestePacient(Long id) {
        return pacientRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent: " + id));
    }

    private Medicament gasesteMedicament(Long id) {
        return medicamentRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Medicament inexistent: " + id));
    }

    /**
     * Clasă internă imutabilă pentru rezultatul scoring-ului.
     */
    private static class RezultatScoring {
        final double scor;
        final List<String> reguli;

        RezultatScoring(double scor, List<String> reguli) {
            this.scor = scor;
            this.reguli = reguli;
        }
    }
}
