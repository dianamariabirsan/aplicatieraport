package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.dto.*;
import com.example.healthapp.service.InfoExterneMedicamentSyncService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecizieTratamentService {

    private final PacientRepository pacientRepository;
    private final MedicamentRepository medicamentRepository;
    private final InfoExterneMedicamentSyncService syncService;

    public DecizieTratamentService(
        PacientRepository pacientRepository,
        MedicamentRepository medicamentRepository,
        InfoExterneMedicamentSyncService syncService
    ) {
        this.pacientRepository = pacientRepository;
        this.medicamentRepository = medicamentRepository;
        this.syncService = syncService;
    }

    @Transactional(readOnly = true)
    public EvaluareDecizieRezultatDTO evalueaza(EvaluareDecizieCerereDTO cerere) {
        Pacient pacient = pacientRepository
            .findById(cerere.getPacientId())
            .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent: " + cerere.getPacientId()));

        // A și B - ideal există ca Medicament în DB cu denumire "Mounjaro" / "Wegovy"
        Medicament a = medicamentRepository
            .findOneByDenumireIgnoreCase(cerere.getTratamentA())
            .orElse(null);

        Medicament b = medicamentRepository
            .findOneByDenumireIgnoreCase(cerere.getTratamentB())
            .orElse(null);

        EvaluareMedicamentDTO evalA = construiesteEvaluare(cerere.getTratamentA(), a);
        EvaluareMedicamentDTO evalB = construiesteEvaluare(cerere.getTratamentB(), b);

        EvaluareDecizieRezultatDTO out = new EvaluareDecizieRezultatDTO();
        out.setEvaluareA(evalA);
        out.setEvaluareB(evalB);

        // ClinicalTrials link (simplu, robust)
        String query = cerere.getTratamentA() + " OR " + cerere.getTratamentB();
        String linkTrials = "https://clinicaltrials.gov/search?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

        StudiiCliniceStatisticaDTO st = new StudiiCliniceStatisticaDTO();
        st.setInterogare(query);
        st.setLinkClinicalTrials(linkTrials);
        st.setNumarStudii(null); // dacă implementezi fetch real, îl setezi
        out.setStudiiClinice(st);

        out.getLinkuri().add(linkTrials);

        // Siguranță externă (rezumat) - deocamdată doar include sursele rezumatelor
        SigurantaExternaDTO safe = new SigurantaExternaDTO();
        // dacă vrei, parsezi JSON-ul din productSummary și populăm listele (pasul 2)
        out.setSigurantaExterna(safe);

        // ✅ Aici pui regulile tale medicale:
        out.setRecomandare("Nedeterminat");
        out.setMotivare("Completează regulile de alocare pe baza datelor pacientului (varsta, comorbiditati, toleranta etc.) și a info externe.");

        return out;
    }

    private EvaluareMedicamentDTO construiesteEvaluare(String denumire, Medicament medicament) {
        EvaluareMedicamentDTO dto = new EvaluareMedicamentDTO();
        dto.setDenumire(denumire);

        if (medicament == null) {
            dto.setRezumatSiguranta("Medicamentul nu există în baza de date. Creează-l ca entitate Medicament cu denumirea corectă.");
            dto.setScor(null);
            return dto;
        }

        ExternalDrugInfo info = medicament.getInfoExtern();
        if (info != null) {
            dto.setSursaUrl(info.getSourceUrl());
            dto.setRezumatSiguranta(info.getProductSummary());
        } else {
            dto.setRezumatSiguranta("Nu există info externă sincronizată încă.");
        }

        return dto;
    }

    /**
     * Dacă vrei sincronizare la cerere, poți expune o metodă:
     * - syncService.sincronizeazaDupaDenumire("Mounjaro")
     * - syncService.sincronizeazaDupaDenumire("Wegovy")
     * și apoi refaci read-only eval.
     */
}
