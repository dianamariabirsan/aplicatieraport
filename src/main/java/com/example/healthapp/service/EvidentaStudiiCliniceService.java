package com.example.healthapp.service;

import com.example.healthapp.service.dto.EvidentaStudiuClinicDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Serviciu pentru obținerea evidenței statistice din ClinicalTrials.gov.
 * Nivel 1: status, faza, înrolare, date (defensibil scientific).
 */
@Service
public class EvidentaStudiiCliniceService {

    private static final Logger LOG = LoggerFactory.getLogger(EvidentaStudiiCliniceService.class);

    private static final String CLINICAL_TRIALS_API =
        "https://clinicaltrials.gov/api/v2/studies?query.term=%s&pageSize=1&format=json&fields=NCTId,BriefTitle,OverallStatus,Phase,EnrollmentCount,StartDate,CompletionDate,LeadSponsorName";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EvidentaStudiiCliniceService(
        @Qualifier("pdfRestTemplate") RestTemplate restTemplate,
        ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Obține primul studiu clinic relevant pentru un medicament.
     * Returnează un DTO cu datele de nivel 1 (status, faza, înrolare, date).
     *
     * @param denumireMedicament denumirea medicamentului (ex: "Mounjaro")
     * @return EvidentaStudiuClinicDTO populat, sau un DTO gol cu linkUrl dacă fetch-ul eșuează
     */
    public EvidentaStudiuClinicDTO obtineEvidentaPrimulStudiu(String denumireMedicament) {
        String linkCautare = "https://clinicaltrials.gov/search?query=" +
            URLEncoder.encode(denumireMedicament, StandardCharsets.UTF_8);

        EvidentaStudiuClinicDTO dto = new EvidentaStudiuClinicDTO();
        dto.setLinkUrl(linkCautare);

        try {
            String url = String.format(CLINICAL_TRIALS_API,
                URLEncoder.encode(denumireMedicament, StandardCharsets.UTF_8));

            String raspuns = restTemplate.getForObject(url, String.class);
            if (raspuns == null || raspuns.isBlank()) {
                return dto;
            }

            JsonNode radacina = objectMapper.readTree(raspuns);
            JsonNode studii = radacina.path("studies");
            if (studii.isArray() && studii.size() > 0) {
                JsonNode studiu = studii.get(0);
                JsonNode protocol = studiu.path("protocolSection");

                dto.setNctId(textSau(protocol.path("identificationModule").path("nctId")));
                dto.setTitlu(textSau(protocol.path("identificationModule").path("briefTitle")));
                dto.setStatus(textSau(protocol.path("statusModule").path("overallStatus")));
                dto.setDataInceput(textSau(protocol.path("statusModule").path("startDateStruct").path("date")));
                dto.setDataFinal(textSau(protocol.path("statusModule").path("completionDateStruct").path("date")));
                dto.setSponsor(textSau(protocol.path("sponsorCollaboratorsModule").path("leadSponsor").path("name")));

                JsonNode modulDesign = protocol.path("designModule");
                if (!modulDesign.isMissingNode()) {
                    JsonNode faze = modulDesign.path("phases");
                    if (faze.isArray() && faze.size() > 0) {
                        dto.setFaza(faze.get(0).asText());
                    }
                    JsonNode inrolare = modulDesign.path("enrollmentInfo").path("count");
                    if (!inrolare.isMissingNode()) {
                        dto.setInrolare(inrolare.asInt());
                    }
                }

                if (dto.getNctId() != null) {
                    dto.setLinkUrl("https://clinicaltrials.gov/study/" + dto.getNctId());
                }
            }
        } catch (Exception e) {
            LOG.warn("Nu s-a putut obține evidența studiilor clinice pentru '{}': {}", denumireMedicament, e.getMessage());
        }

        return dto;
    }

    private String textSau(JsonNode nod) {
        return (nod == null || nod.isMissingNode() || nod.isNull()) ? null : nod.asText(null);
    }
}
