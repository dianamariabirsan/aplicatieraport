package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviciu care parsează un PDF SmPC și persistă secțiunile relevante
 * (contraindicatii, interactiuni, avertizari) în entitatea Medicament,
 * actualizând totodată ExternalDrugInfo cu metadatele sursei.
 */
@Service
@Transactional
public class SmPCSyncService {

    private final MedicamentRepository medicamentRepository;
    private final ExternalDrugInfoRepository externalDrugInfoRepository;
    private final SmPCExtragereSectiuneService extractor;

    public SmPCSyncService(
        MedicamentRepository medicamentRepository,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        SmPCExtragereSectiuneService extractor
    ) {
        this.medicamentRepository = medicamentRepository;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.extractor = extractor;
    }

    /**
     * Parsează PDF-ul SmPC și persistă secțiunile în Medicament + ExternalDrugInfo.
     *
     * @param medicamentId ID-ul medicamentului din DB
     * @param pdfBytes     conținutul binar al PDF-ului SmPC
     * @param sourceUrl    URL-ul de unde a fost descărcat PDF-ul (poate fi null dacă e upload direct)
     * @return entitatea Medicament actualizată
     */
    public Medicament syncFromPdf(Long medicamentId, byte[] pdfBytes, String sourceUrl) {
        Medicament med = medicamentRepository
            .findOneWithInfoExternById(medicamentId)
            .orElseThrow(() -> new IllegalArgumentException("Medicament not found: " + medicamentId));

        Map<String, List<String>> sectiuni = extractor.extrageSectiuni(pdfBytes);

        med.setContraindicatii(joinLines(sectiuni, "contraindicatii"));
        med.setInteractiuni(joinLines(sectiuni, "interactiuni"));
        med.setAvertizari(joinLines(sectiuni, "avertizari"));

        ExternalDrugInfo info = med.getInfoExtern();
        if (info == null) {
            info = new ExternalDrugInfo();
            info.setSource("SmPC");
            info.setProductSummary("SmPC parsed & synced");
        }
        info.setSourceUrl(sourceUrl);
        info.setLastUpdated(Instant.now());

        info = externalDrugInfoRepository.save(info);
        med.setInfoExtern(info);

        return medicamentRepository.save(med);
    }

    private String joinLines(Map<String, List<String>> sectiuni, String key) {
        List<String> lines = sectiuni.getOrDefault(key, List.of());
        return lines.isEmpty() ? null : String.join("\n", lines);
    }
}
