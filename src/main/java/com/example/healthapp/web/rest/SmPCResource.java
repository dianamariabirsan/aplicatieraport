package com.example.healthapp.web.rest;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.service.SmPCSyncService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller pentru upload/sincronizare SmPC (prospect medicament).
 *
 * <p>Endpoint-uri:
 * <ul>
 *   <li>POST /api/medicamente/{id}/smpc/upload — upload PDF direct</li>
 *   <li>POST /api/medicamente/{id}/smpc/url    — descarcă PDF de la URL și sincronizează</li>
 * </ul>
 */
@RestController
@RequestMapping("/api")
public class SmPCResource {

    private static final Logger LOG = LoggerFactory.getLogger(SmPCResource.class);

    private final SmPCSyncService smpcSyncService;
    private final RestTemplate restTemplate;

    public SmPCResource(SmPCSyncService smpcSyncService) {
        this.smpcSyncService = smpcSyncService;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Upload PDF SmPC și sincronizează contraindicatii/interactiuni/avertizari în Medicament.
     *
     * @param medicamentId ID-ul medicamentului
     * @param file         fișierul PDF SmPC
     * @return Medicamentul actualizat
     */
    @PostMapping(value = "/medicamente/{id}/smpc/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Medicament> uploadSmPC(@PathVariable("id") Long medicamentId, @RequestPart("file") MultipartFile file) {
        LOG.debug("REST request to upload SmPC PDF for Medicament : {}", medicamentId);
        byte[] pdfBytes;
        try {
            pdfBytes = file.getBytes();
        } catch (Exception e) {
            LOG.error("Failed to read uploaded SmPC PDF: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        Medicament updated = smpcSyncService.syncFromPdf(medicamentId, pdfBytes, null);
        return ResponseEntity.ok(updated);
    }

    /**
     * Download PDF SmPC from URL and synchronize the drug record.
     */
    @PostMapping("/medicamente/{id}/smpc/url")
    public ResponseEntity<Medicament> syncFromUrl(@PathVariable("id") Long medicamentId, @RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        LOG.debug("REST request to sync SmPC from URL {} for Medicament : {}", url, medicamentId);
        byte[] pdfBytes;
        try {
            pdfBytes = restTemplate.getForObject(url, byte[].class);
        } catch (Exception e) {
            LOG.error("Failed to download SmPC PDF from URL {}: {}", url, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        if (pdfBytes == null || pdfBytes.length == 0) {
            LOG.warn("Empty PDF downloaded from URL {}", url);
            return ResponseEntity.badRequest().build();
        }
        Medicament updated = smpcSyncService.syncFromPdf(medicamentId, pdfBytes, url);
        return ResponseEntity.ok(updated);
    }
}
