package com.example.healthapp.web.rest;

import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.AlocareTratamentQueryService;
import com.example.healthapp.service.AlocareTratamentService;
import com.example.healthapp.service.criteria.AlocareTratamentCriteria;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.service.mapper.DecisionLogMapper;
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
 * REST controller for managing {@link com.example.healthapp.domain.AlocareTratament}.
 */
@RestController
@RequestMapping("/api/alocare-trataments")
public class AlocareTratamentResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlocareTratamentResource.class);

    private static final String ENTITY_NAME = "alocareTratament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlocareTratamentService alocareTratamentService;

    private final AlocareTratamentRepository alocareTratamentRepository;

    private final AlocareTratamentQueryService alocareTratamentQueryService;

    private final DecisionLogRepository decisionLogRepository;

    private final DecisionLogMapper decisionLogMapper;

    public AlocareTratamentResource(
        AlocareTratamentService alocareTratamentService,
        AlocareTratamentRepository alocareTratamentRepository,
        AlocareTratamentQueryService alocareTratamentQueryService,
        DecisionLogRepository decisionLogRepository,
        DecisionLogMapper decisionLogMapper
    ) {
        this.alocareTratamentService = alocareTratamentService;
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.alocareTratamentQueryService = alocareTratamentQueryService;
        this.decisionLogRepository = decisionLogRepository;
        this.decisionLogMapper = decisionLogMapper;
    }

    /**
     * {@code POST  /alocare-trataments} : Create a new alocareTratament.
     *
     * @param alocareTratamentDTO the alocareTratamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alocareTratamentDTO, or with status {@code 400 (Bad Request)} if the alocareTratament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlocareTratamentDTO> createAlocareTratament(@Valid @RequestBody AlocareTratamentDTO alocareTratamentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlocareTratament : {}", alocareTratamentDTO);
        if (alocareTratamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new alocareTratament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alocareTratamentDTO = alocareTratamentService.save(alocareTratamentDTO);
        return ResponseEntity.created(new URI("/api/alocare-trataments/" + alocareTratamentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alocareTratamentDTO.getId().toString()))
            .body(alocareTratamentDTO);
    }

    /**
     * {@code PUT  /alocare-trataments/:id} : Updates an existing alocareTratament.
     *
     * @param id the id of the alocareTratamentDTO to save.
     * @param alocareTratamentDTO the alocareTratamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alocareTratamentDTO,
     * or with status {@code 400 (Bad Request)} if the alocareTratamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alocareTratamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlocareTratamentDTO> updateAlocareTratament(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlocareTratamentDTO alocareTratamentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlocareTratament : {}, {}", id, alocareTratamentDTO);
        if (alocareTratamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alocareTratamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alocareTratamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alocareTratamentDTO = alocareTratamentService.update(alocareTratamentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alocareTratamentDTO.getId().toString()))
            .body(alocareTratamentDTO);
    }

    /**
     * {@code PATCH  /alocare-trataments/:id} : Partial updates given fields of an existing alocareTratament, field will ignore if it is null
     *
     * @param id the id of the alocareTratamentDTO to save.
     * @param alocareTratamentDTO the alocareTratamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alocareTratamentDTO,
     * or with status {@code 400 (Bad Request)} if the alocareTratamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alocareTratamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alocareTratamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlocareTratamentDTO> partialUpdateAlocareTratament(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlocareTratamentDTO alocareTratamentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlocareTratament partially : {}, {}", id, alocareTratamentDTO);
        if (alocareTratamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alocareTratamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alocareTratamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlocareTratamentDTO> result = alocareTratamentService.partialUpdate(alocareTratamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alocareTratamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alocare-trataments} : get all the alocareTrataments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alocareTrataments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlocareTratamentDTO>> getAllAlocareTrataments(
        AlocareTratamentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlocareTrataments by criteria: {}", criteria);

        Page<AlocareTratamentDTO> page = alocareTratamentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alocare-trataments/count} : count all the alocareTrataments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlocareTrataments(AlocareTratamentCriteria criteria) {
        LOG.debug("REST request to count AlocareTrataments by criteria: {}", criteria);
        return ResponseEntity.ok().body(alocareTratamentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alocare-trataments/:id} : get the "id" alocareTratament.
     *
     * @param id the id of the alocareTratamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alocareTratamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlocareTratamentDTO> getAlocareTratament(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlocareTratament : {}", id);
        Optional<AlocareTratamentDTO> alocareTratamentDTO = alocareTratamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alocareTratamentDTO);
    }

    /**
     * {@code DELETE  /alocare-trataments/:id} : delete the "id" alocareTratament.
     *
     * @param id the id of the alocareTratamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlocareTratament(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlocareTratament : {}", id);
        alocareTratamentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /alocare-trataments/:id/decision-logs} : get all decision logs for a given alocareTratament.
     *
     * @param id the id of the alocareTratament.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decision logs.
     */
    @GetMapping("/{id}/decision-logs")
    public ResponseEntity<List<DecisionLogDTO>> getDecisionLogsForAlocare(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DecisionLogs for AlocareTratament : {}", id);
        List<DecisionLogDTO> logs = decisionLogRepository
            .findAllByAlocareIdOrderByTimestampDesc(id)
            .stream()
            .map(decisionLogMapper::toDto)
            .toList();
        return ResponseEntity.ok(logs);
    }
}
