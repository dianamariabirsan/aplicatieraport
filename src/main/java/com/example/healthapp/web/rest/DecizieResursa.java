package com.example.healthapp.web.rest;

import com.example.healthapp.service.MotorDecizionalService;
import com.example.healthapp.service.dto.EvaluareDecizieCerereDTO;
import com.example.healthapp.service.dto.EvaluareDecizieRezultatDTO;
import com.example.healthapp.service.dto.RecomandareAbCerereDTO;
import com.example.healthapp.service.dto.RecomandareAbRezultatDTO;
import com.example.healthapp.service.dto.ValidarePropunereCerereDTO;
import com.example.healthapp.service.dto.ValidarePropunereRezultatDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Resource pentru motorul decizional clinic.
 *
 * <ul>
 *   <li>POST /api/decizie/valideaza-propunere — medicul propune, sistemul validează</li>
 *   <li>POST /api/decizie/recomanda-ab — sistemul recomandă între A și B</li>
 *   <li>POST /api/decizie/evalueaza — endpoint unificat (backward-compatible)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/decizie")
public class DecizieResursa {

    private final MotorDecizionalService motorDecizionalService;

    public DecizieResursa(MotorDecizionalService motorDecizionalService) {
        this.motorDecizionalService = motorDecizionalService;
    }

    /**
     * POST /api/decizie/valideaza-propunere
     *
     * <p>Medicul propune un tratament pentru pacient; sistemul verifică contraindicațiile,
     * interacțiunile și istoricul de reacții adverse, și returnează VALID/INVALID cu motivare.
     * Persistă un DecisionLog(actorType=MEDIC) pentru auditabilitate.
     *
     * @param cerere datele propunerii (pacientId, medicId, medicamentPropusId)
     * @return rezultatul validării cu reguli declanșate și avertismente
     */
    @PostMapping("/valideaza-propunere")
    public ResponseEntity<ValidarePropunereRezultatDTO> valideazaPropunere(
        @Valid @RequestBody ValidarePropunereCerereDTO cerere
    ) {
        return ResponseEntity.ok(motorDecizionalService.valideazaPropunere(cerere));
    }

    /**
     * POST /api/decizie/recomanda-ab
     *
     * <p>Sistemul calculează scoruri pentru medicamentul A și B, aplică regulile clinice
     * și meta-decisorul, și returnează recomandarea cu explicabilitate completă.
     * Persistă AlocareTratament + DecisionLog(actorType=SISTEM_AI).
     *
     * @param cerere datele comparației (pacientId, medicId, medicamentAId, medicamentBId)
     * @return scorurile A/B, regulile declanșate, evidența studiilor clinice, recomandarea finală
     */
    @PostMapping("/recomanda-ab")
    public ResponseEntity<RecomandareAbRezultatDTO> recomandaAb(
        @Valid @RequestBody RecomandareAbCerereDTO cerere
    ) {
        return ResponseEntity.ok(motorDecizionalService.recomandaAb(cerere));
    }

    /**
     * POST /api/decizie/evalueaza
     *
     * <p>Endpoint unificat: combină validarea propunerii cu recomandarea A vs B.
     * Compatibil cu clientul Angular existent.
     *
     * @param cerere datele evaluării (pacientId, tratamentA, tratamentB)
     * @return evaluarea completă cu scoruri, reguli, evidență studii, recomandare
     */
    @PostMapping("/evalueaza")
    public ResponseEntity<EvaluareDecizieRezultatDTO> evalueaza(
        @Valid @RequestBody EvaluareDecizieCerereDTO cerere
    ) {
        return ResponseEntity.ok(motorDecizionalService.evalueaza(cerere));
    }
}
