package com.example.healthapp.web.rest;

import com.example.healthapp.service.EvidentaStudiiCliniceService;
import com.example.healthapp.service.InfoExterneMedicamentSyncService;
import com.example.healthapp.service.dto.EvidentaStudiuClinicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Resource pentru sursele externe: SMPC/prospect și ClinicalTrials.gov.
 *
 * <ul>
 *   <li>GET /api/extern/smpc/refresh?medicament=... — sincronizează prospect/SMPC</li>
 *   <li>GET /api/extern/studii-clinice?medicament=... — obține evidența din ClinicalTrials.gov</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/extern")
public class InfoExterneResursa {

    private final InfoExterneMedicamentSyncService syncService;
    private final EvidentaStudiiCliniceService evidentaStudiiCliniceService;

    public InfoExterneResursa(
        InfoExterneMedicamentSyncService syncService,
        EvidentaStudiiCliniceService evidentaStudiiCliniceService
    ) {
        this.syncService = syncService;
        this.evidentaStudiiCliniceService = evidentaStudiiCliniceService;
    }

    /**
     * GET /api/extern/smpc/refresh?medicament=Mounjaro
     *
     * <p>Descarcă PDF-ul oficial (SmPC/prospect) pentru medicamentul specificat,
     * extrage secțiunile relevante (contraindicații, interacțiuni, reacții adverse)
     * și persistă rezumatul în ExternalDrugInfo (evidence cache).
     *
     * @param medicament denumirea medicamentului
     * @return confirmarea sincronizării
     */
    @GetMapping("/smpc/refresh")
    public ResponseEntity<String> reimprospatareSmpc(@RequestParam String medicament) {
        try {
            syncService.sincronizeazaDupaDenumire(medicament);
            return ResponseEntity.ok("Sincronizare finalizată pentru: " + medicament);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Eroare la sincronizare: " + e.getMessage());
        }
    }

    /**
     * GET /api/extern/studii-clinice?medicament=Mounjaro
     *
     * <p>Obține evidența statistică de nivel 1 din ClinicalTrials.gov:
     * status, faza, înrolare, date, sponsor.
     *
     * @param medicament denumirea medicamentului
     * @return EvidentaStudiuClinicDTO cu datele primului studiu relevant
     */
    @GetMapping("/studii-clinice")
    public ResponseEntity<EvidentaStudiuClinicDTO> studiiClinice(@RequestParam String medicament) {
        EvidentaStudiuClinicDTO evidenta = evidentaStudiiCliniceService
            .obtineEvidentaPrimulStudiu(medicament);
        return ResponseEntity.ok(evidenta);
    }
}
