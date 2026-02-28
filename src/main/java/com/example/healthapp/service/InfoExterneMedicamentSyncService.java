package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InfoExterneMedicamentSyncService {

    private final MedicamentRepository medicamentRepository;
    private final ExternalDrugInfoRepository externalDrugInfoRepository;
    private final ActualPdfSursaMedicamentService pdfService;
    private final SmPCExtragereSectiuneService extractor;
    private final ObjectMapper objectMapper;

    public InfoExterneMedicamentSyncService(
        MedicamentRepository medicamentRepository,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        ActualPdfSursaMedicamentService pdfService,
        SmPCExtragereSectiuneService extractor,
        ObjectMapper objectMapper
    ) {
        this.medicamentRepository = medicamentRepository;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.pdfService = pdfService;
        this.extractor = extractor;
        this.objectMapper = objectMapper;
    }

    public ExternalDrugInfo sincronizeazaDupaDenumire(String denumireMedicament) {
        Medicament medicament = medicamentRepository
            .findOneByDenumireIgnoreCase(denumireMedicament)
            .orElseThrow(() -> new IllegalArgumentException("Medicament inexistent: " + denumireMedicament));

        String sursaUrl = determinaSursaUrlOficiala(medicament.getDenumire());
        byte[] pdf = pdfService.descarcaPdf(sursaUrl);

        Map<String, ?> sectiuni = extractor.extrageSectiuni(pdf);
        String rezumatJson = serializeazaJson(sectiuni);

        ExternalDrugInfo info = medicament.getInfoExtern();
        if (info == null) {
            info = new ExternalDrugInfo();
            info.setMedicament(medicament);
        }

        info.setSource("SmPC/Prospect oficial");
        info.setSourceUrl(sursaUrl);
        info.setProductSummary(rezumatJson);
        info.setLastUpdated(Instant.now());

        return externalDrugInfoRepository.save(info);
    }

    private String serializeazaJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String determinaSursaUrlOficiala(String denumire) {
        String d = denumire == null ? "" : denumire.toLowerCase();

        if (d.contains("mounjaro")) {
            return "https://EXEMPLU-OFICIAL/MOUNJARO.pdf";
        }
        if (d.contains("wegovy")) {
            return "https://EXEMPLU-OFICIAL/WEGOVY.pdf";
        }
        throw new IllegalArgumentException("Nu exista URL oficial configurat pentru medicamentul: " + denumire);
    }
}
