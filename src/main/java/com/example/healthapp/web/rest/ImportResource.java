package com.example.healthapp.web.rest;

import com.example.healthapp.service.ImportPacientiService;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST endpoint for importing patients and treatment allocations from a CSV file.
 * Accepts a multipart file upload plus an optional {@code dataSource} parameter
 * (REAL | SIMULAT, default SIMULAT).
 */
@RestController
@RequestMapping("/api/import")
public class ImportResource {

    private final ImportPacientiService importPacientiService;

    public ImportResource(ImportPacientiService importPacientiService) {
        this.importPacientiService = importPacientiService;
    }

    /**
     * {@code POST /api/import/pacienti-alocari} : import patients + allocations from CSV.
     *
     * @param file       the CSV file (multipart/form-data, field name "file")
     * @param dataSource optional tag written into the patient record (default "SIMULAT")
     * @return JSON with {@code imported} count and the effective {@code dataSource}
     */
    @PostMapping(value = "/pacienti-alocari", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> importPacienti(
        @RequestPart("file") MultipartFile file,
        @RequestParam(value = "dataSource", defaultValue = "SIMULAT") String dataSource
    ) throws Exception {
        int imported = importPacientiService.importCsv(file.getBytes(), dataSource);
        return ResponseEntity.ok(Map.of("imported", imported, "dataSource", dataSource));
    }
}
