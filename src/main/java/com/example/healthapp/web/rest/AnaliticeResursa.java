package com.example.healthapp.web.rest;

import com.example.healthapp.service.AnaliticeService;
import com.example.healthapp.service.dto.AnaliticeMedicamentDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Resource pentru analitice real-world.
 *
 * <ul>
 *   <li>GET /api/analitice/medicament/{id} — analitice pentru un medicament specific</li>
 *   <li>GET /api/analitice/medicament — analitice pentru toate medicamentele</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/analitice")
public class AnaliticeResursa {

    private final AnaliticeService analiticeService;

    public AnaliticeResursa(AnaliticeService analiticeService) {
        this.analiticeService = analiticeService;
    }

    /**
     * GET /api/analitice/medicament/{id}
     *
     * <p>Returnează analiticele real-world pentru un medicament:
     * rata reacțiilor adverse, eficiența medie (din Feedback),
     * numărul total de alocări și reacțiile severe.
     *
     * @param id ID-ul medicamentului
     * @return AnaliticeMedicamentDTO cu statisticile calculate
     */
    @GetMapping("/medicament/{id}")
    public ResponseEntity<AnaliticeMedicamentDTO> analiticeMedicament(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(analiticeService.calculeazaAnaliticePentruMedicament(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/analitice/medicament
     *
     * <p>Returnează analiticele pentru toate medicamentele din baza de date.
     *
     * @return lista de AnaliticeMedicamentDTO
     */
    @GetMapping("/medicament")
    public ResponseEntity<List<AnaliticeMedicamentDTO>> toateAnaliticele() {
        return ResponseEntity.ok(analiticeService.calculeazaToateAnaliticele());
    }
}
