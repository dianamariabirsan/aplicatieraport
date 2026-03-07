package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviciu care parsează un PDF SmPC și persistă secțiunile relevante
 * (contraindicatii, interactiuni, avertizari, indicatii) în entitatea Medicament,
 * actualizând totodată ExternalDrugInfo cu metadatele sursei și JSON-ul extras.
 */
@Service
@Transactional
public class SmPCSyncService {

    private static final Logger LOG = LoggerFactory.getLogger(SmPCSyncService.class);

    private final MedicamentRepository medicamentRepository;
    private final ExternalDrugInfoRepository externalDrugInfoRepository;
    private final SmPCExtragereSectiuneService extractor;
    private final ObjectMapper objectMapper;

    public SmPCSyncService(
        MedicamentRepository medicamentRepository,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        SmPCExtragereSectiuneService extractor,
        ObjectMapper objectMapper
    ) {
        this.medicamentRepository = medicamentRepository;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.extractor = extractor;
        this.objectMapper = objectMapper;
    }

    /**
     * Parsează PDF-ul SmPC și persistă secțiunile în Medicament + ExternalDrugInfo.
     * Secțiunile extrase sunt serializate ca JSON în ExternalDrugInfo.productSummary,
     * astfel încât auto-fill-ul din MedicamentService.populateMedicamentFromExternalInfo()
     * să poată citi același format mai târziu.
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
        med.setIndicatii(joinLines(sectiuni, "indicatii"));

        ExternalDrugInfo info = med.getInfoExtern();
        if (info == null) {
            info = new ExternalDrugInfo();
            info.setSource("SmPC");
        }
        info.setSourceUrl(sourceUrl);
        info.setLastUpdated(Instant.now());
        info.setProductSummary(serializeSectiuni(sectiuni));

        info = externalDrugInfoRepository.save(info);
        med.setInfoExtern(info);

        return medicamentRepository.save(med);
    }

    private String joinLines(Map<String, List<String>> sectiuni, String key) {
        List<String> lines = sectiuni.getOrDefault(key, List.of());
        return lines.isEmpty() ? null : String.join("\n", lines);
    }

    private String serializeSectiuni(Map<String, List<String>> sectiuni) {
        try {
            return objectMapper.writeValueAsString(sectiuni);
        } catch (Exception e) {
            LOG.warn("Could not serialize SmPC sections to JSON: {}", e.getMessage());
            return null;
        }
    }
}
