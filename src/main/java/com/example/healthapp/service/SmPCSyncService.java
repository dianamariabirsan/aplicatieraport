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
    private final MedicamentService medicamentService;

    public SmPCSyncService(
        MedicamentRepository medicamentRepository,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        SmPCExtragereSectiuneService extractor,
        ObjectMapper objectMapper,
        MedicamentService medicamentService
    ) {
        this.medicamentRepository = medicamentRepository;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.extractor = extractor;
        this.objectMapper = objectMapper;
        this.medicamentService = medicamentService;
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

        ExternalDrugInfo info = med.getInfoExtern();
        if (info == null) {
            info = new ExternalDrugInfo();
            info.setSource("SmPC");
        }

        try {
            info.setProductSummary(objectMapper.writeValueAsString(sectiuni));
        } catch (Exception e) {
            throw new IllegalStateException("Nu am putut serializa productSummary", e);
        }

        info.setSourceUrl(sourceUrl);
        info.setLastUpdated(Instant.now());
        info = externalDrugInfoRepository.save(info);

        med.setInfoExtern(info);
        medicamentService.populateMedicamentFromExternalInfo(med, info);

        return medicamentRepository.save(med);
    }
}
