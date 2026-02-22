package com.example.healthapp.web.rest;

import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.PacientQueryService;
import com.example.healthapp.service.PacientService;
import com.example.healthapp.service.criteria.PacientCriteria;
import com.example.healthapp.service.dto.PacientDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Pacient}.
 */
@RestController
@RequestMapping("/api/pacients")
public class PacientResource {

    private static final Logger LOG = LoggerFactory.getLogger(PacientResource.class);

    private static final String ENTITY_NAME = "pacient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PacientService pacientService;

    private final PacientRepository pacientRepository;

    private final PacientQueryService pacientQueryService;

    public PacientResource(PacientService pacientService, PacientRepository pacientRepository, PacientQueryService pacientQueryService) {
        this.pacientService = pacientService;
        this.pacientRepository = pacientRepository;
        this.pacientQueryService = pacientQueryService;
    }

    /**
     * {@code POST  /pacients} : Create a new pacient.
     *
     * @param pacientDTO the pacientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pacientDTO, or with status {@code 400 (Bad Request)} if the pacient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PacientDTO> createPacient(@Valid @RequestBody PacientDTO pacientDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pacient : {}", pacientDTO);
        if (pacientDTO.getId() != null) {
            throw new BadRequestAlertException("A new pacient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pacientDTO = pacientService.save(pacientDTO);
        return ResponseEntity.created(new URI("/api/pacients/" + pacientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pacientDTO.getId().toString()))
            .body(pacientDTO);
    }

    /**
     * {@code PUT  /pacients/:id} : Updates an existing pacient.
     *
     * @param id the id of the pacientDTO to save.
     * @param pacientDTO the pacientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacientDTO,
     * or with status {@code 400 (Bad Request)} if the pacientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pacientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PacientDTO> updatePacient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PacientDTO pacientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pacient : {}, {}", id, pacientDTO);
        if (pacientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pacientDTO = pacientService.update(pacientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacientDTO.getId().toString()))
            .body(pacientDTO);
    }

    /**
     * {@code PATCH  /pacients/:id} : Partial updates given fields of an existing pacient, field will ignore if it is null
     *
     * @param id the id of the pacientDTO to save.
     * @param pacientDTO the pacientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pacientDTO,
     * or with status {@code 400 (Bad Request)} if the pacientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pacientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pacientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PacientDTO> partialUpdatePacient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PacientDTO pacientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pacient partially : {}, {}", id, pacientDTO);
        if (pacientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pacientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pacientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PacientDTO> result = pacientService.partialUpdate(pacientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pacientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pacients} : get all the pacients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pacients in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PacientDTO>> getAllPacients(
        PacientCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Pacients by criteria: {}", criteria);

        Page<PacientDTO> page = pacientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pacients/count} : count all the pacients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPacients(PacientCriteria criteria) {
        LOG.debug("REST request to count Pacients by criteria: {}", criteria);
        return ResponseEntity.ok().body(pacientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pacients/:id} : get the "id" pacient.
     *
     * @param id the id of the pacientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pacientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacientDTO> getPacient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pacient : {}", id);
        Optional<PacientDTO> pacientDTO = pacientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pacientDTO);
    }

    /**
     * {@code DELETE  /pacients/:id} : delete the "id" pacient.
     *
     * @param id the id of the pacientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePacient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pacient : {}", id);
        pacientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
