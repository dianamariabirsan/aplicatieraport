package com.example.healthapp.web.rest;

import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.DecisionLogQueryService;
import com.example.healthapp.service.DecisionLogService;
import com.example.healthapp.service.criteria.DecisionLogCriteria;
import com.example.healthapp.service.dto.DecisionLogDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.healthapp.domain.DecisionLog}.
 * DecisionLog is read-only — entries are generated exclusively by the decision engine.
 */
@RestController
@RequestMapping("/api/decision-logs")
public class DecisionLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionLogResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecisionLogService decisionLogService;

    private final DecisionLogRepository decisionLogRepository;

    private final DecisionLogQueryService decisionLogQueryService;

    public DecisionLogResource(
        DecisionLogService decisionLogService,
        DecisionLogRepository decisionLogRepository,
        DecisionLogQueryService decisionLogQueryService
    ) {
        this.decisionLogService = decisionLogService;
        this.decisionLogRepository = decisionLogRepository;
        this.decisionLogQueryService = decisionLogQueryService;
    }

    /**
     * {@code POST  /decision-logs} : Not allowed — DecisionLog is generated automatically.
     */
    @PostMapping("")
    public ResponseEntity<Void> createDecisionLog() {
        return ResponseEntity.status(405).build();
    }

    /**
     * {@code PUT  /decision-logs/:id} : Not allowed — DecisionLog is read-only.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDecisionLog(@PathVariable("id") Long id) {
        return ResponseEntity.status(405).build();
    }

    /**
     * {@code PATCH  /decision-logs/:id} : Not allowed — DecisionLog is read-only.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Void> partialUpdateDecisionLog(@PathVariable("id") Long id) {
        return ResponseEntity.status(405).build();
    }

    /**
     * {@code DELETE  /decision-logs/:id} : Not allowed — DecisionLog is read-only.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecisionLog(@PathVariable("id") Long id) {
        return ResponseEntity.status(405).build();
    }

    /**
     * {@code GET  /decision-logs} : get all the decisionLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisionLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DecisionLogDTO>> getAllDecisionLogs(
        DecisionLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DecisionLogs by criteria: {}", criteria);

        Page<DecisionLogDTO> page = decisionLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decision-logs/count} : count all the decisionLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDecisionLogs(DecisionLogCriteria criteria) {
        LOG.debug("REST request to count DecisionLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(decisionLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decision-logs/:id} : get the "id" decisionLog.
     *
     * @param id the id of the decisionLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decisionLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DecisionLogDTO> getDecisionLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DecisionLog : {}", id);
        Optional<DecisionLogDTO> decisionLogDTO = decisionLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decisionLogDTO);
    }

    /**
     * {@code GET  /decision-logs/by-alocare/:alocareId} : get all decision logs for a given alocareTratament.
     *
     * @param alocareId the id of the alocareTratament.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisionLogs in body.
     */
    @GetMapping("/by-alocare/{alocareId}")
    public List<DecisionLogDTO> getByAlocare(@PathVariable("alocareId") Long alocareId) {
        LOG.debug("REST request to get DecisionLogs by alocareId : {}", alocareId);
        return decisionLogService.findAllByAlocareId(alocareId);
    }
}
