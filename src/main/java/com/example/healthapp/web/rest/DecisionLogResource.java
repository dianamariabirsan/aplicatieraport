package com.example.healthapp.web.rest;

import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.DecisionLogQueryService;
import com.example.healthapp.service.DecisionLogService;
import com.example.healthapp.service.criteria.DecisionLogCriteria;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.healthapp.domain.DecisionLog}.
 */
@RestController
@RequestMapping("/api/decision-logs")
public class DecisionLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionLogResource.class);

    private static final String ENTITY_NAME = "decisionLog";

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
     * {@code POST  /decision-logs} : Create a new decisionLog.
     *
     * @param decisionLogDTO the decisionLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decisionLogDTO, or with status {@code 400 (Bad Request)} if the decisionLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DecisionLogDTO> createDecisionLog(@Valid @RequestBody DecisionLogDTO decisionLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save DecisionLog : {}", decisionLogDTO);
        if (decisionLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new decisionLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        decisionLogDTO = decisionLogService.save(decisionLogDTO);
        return ResponseEntity.created(new URI("/api/decision-logs/" + decisionLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, decisionLogDTO.getId().toString()))
            .body(decisionLogDTO);
    }

    /**
     * {@code PUT  /decision-logs/:id} : Updates an existing decisionLog.
     *
     * @param id the id of the decisionLogDTO to save.
     * @param decisionLogDTO the decisionLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionLogDTO,
     * or with status {@code 400 (Bad Request)} if the decisionLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decisionLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DecisionLogDTO> updateDecisionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DecisionLogDTO decisionLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DecisionLog : {}, {}", id, decisionLogDTO);
        if (decisionLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        decisionLogDTO = decisionLogService.update(decisionLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionLogDTO.getId().toString()))
            .body(decisionLogDTO);
    }

    /**
     * {@code PATCH  /decision-logs/:id} : Partial updates given fields of an existing decisionLog, field will ignore if it is null
     *
     * @param id the id of the decisionLogDTO to save.
     * @param decisionLogDTO the decisionLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionLogDTO,
     * or with status {@code 400 (Bad Request)} if the decisionLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the decisionLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the decisionLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DecisionLogDTO> partialUpdateDecisionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DecisionLogDTO decisionLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DecisionLog partially : {}, {}", id, decisionLogDTO);
        if (decisionLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DecisionLogDTO> result = decisionLogService.partialUpdate(decisionLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionLogDTO.getId().toString())
        );
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
     * {@code DELETE  /decision-logs/:id} : delete the "id" decisionLog.
     *
     * @param id the id of the decisionLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecisionLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DecisionLog : {}", id);
        decisionLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
