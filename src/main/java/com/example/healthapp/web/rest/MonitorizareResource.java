package com.example.healthapp.web.rest;

import com.example.healthapp.repository.MonitorizareRepository;
import com.example.healthapp.service.MonitorizareQueryService;
import com.example.healthapp.service.MonitorizareService;
import com.example.healthapp.service.criteria.MonitorizareCriteria;
import com.example.healthapp.service.dto.MonitorizareDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Monitorizare}.
 */
@RestController
@RequestMapping("/api/monitorizares")
public class MonitorizareResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorizareResource.class);

    private static final String ENTITY_NAME = "monitorizare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitorizareService monitorizareService;

    private final MonitorizareRepository monitorizareRepository;

    private final MonitorizareQueryService monitorizareQueryService;

    public MonitorizareResource(
        MonitorizareService monitorizareService,
        MonitorizareRepository monitorizareRepository,
        MonitorizareQueryService monitorizareQueryService
    ) {
        this.monitorizareService = monitorizareService;
        this.monitorizareRepository = monitorizareRepository;
        this.monitorizareQueryService = monitorizareQueryService;
    }

    /**
     * {@code POST  /monitorizares} : Create a new monitorizare.
     *
     * @param monitorizareDTO the monitorizareDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitorizareDTO, or with status {@code 400 (Bad Request)} if the monitorizare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitorizareDTO> createMonitorizare(@Valid @RequestBody MonitorizareDTO monitorizareDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Monitorizare : {}", monitorizareDTO);
        if (monitorizareDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitorizare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitorizareDTO = monitorizareService.save(monitorizareDTO);
        return ResponseEntity.created(new URI("/api/monitorizares/" + monitorizareDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitorizareDTO.getId().toString()))
            .body(monitorizareDTO);
    }

    /**
     * {@code PUT  /monitorizares/:id} : Updates an existing monitorizare.
     *
     * @param id the id of the monitorizareDTO to save.
     * @param monitorizareDTO the monitorizareDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitorizareDTO,
     * or with status {@code 400 (Bad Request)} if the monitorizareDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitorizareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitorizareDTO> updateMonitorizare(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitorizareDTO monitorizareDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Monitorizare : {}, {}", id, monitorizareDTO);
        if (monitorizareDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitorizareDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitorizareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitorizareDTO = monitorizareService.update(monitorizareDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitorizareDTO.getId().toString()))
            .body(monitorizareDTO);
    }

    /**
     * {@code PATCH  /monitorizares/:id} : Partial updates given fields of an existing monitorizare, field will ignore if it is null
     *
     * @param id the id of the monitorizareDTO to save.
     * @param monitorizareDTO the monitorizareDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitorizareDTO,
     * or with status {@code 400 (Bad Request)} if the monitorizareDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitorizareDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitorizareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitorizareDTO> partialUpdateMonitorizare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitorizareDTO monitorizareDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Monitorizare partially : {}, {}", id, monitorizareDTO);
        if (monitorizareDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitorizareDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitorizareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitorizareDTO> result = monitorizareService.partialUpdate(monitorizareDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitorizareDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitorizares} : get all the monitorizares.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitorizares in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitorizareDTO>> getAllMonitorizares(
        MonitorizareCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Monitorizares by criteria: {}", criteria);

        Page<MonitorizareDTO> page = monitorizareQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitorizares/count} : count all the monitorizares.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitorizares(MonitorizareCriteria criteria) {
        LOG.debug("REST request to count Monitorizares by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitorizareQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitorizares/:id} : get the "id" monitorizare.
     *
     * @param id the id of the monitorizareDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitorizareDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitorizareDTO> getMonitorizare(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Monitorizare : {}", id);
        Optional<MonitorizareDTO> monitorizareDTO = monitorizareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitorizareDTO);
    }

    /**
     * {@code DELETE  /monitorizares/:id} : delete the "id" monitorizare.
     *
     * @param id the id of the monitorizareDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitorizare(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Monitorizare : {}", id);
        monitorizareService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
