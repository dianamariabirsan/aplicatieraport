package com.example.healthapp.web.rest;

import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.service.AdministrareQueryService;
import com.example.healthapp.service.AdministrareService;
import com.example.healthapp.service.criteria.AdministrareCriteria;
import com.example.healthapp.service.dto.AdministrareDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Administrare}.
 */
@RestController
@RequestMapping("/api/administrares")
public class AdministrareResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrareResource.class);

    private static final String ENTITY_NAME = "administrare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministrareService administrareService;

    private final AdministrareRepository administrareRepository;

    private final AdministrareQueryService administrareQueryService;

    public AdministrareResource(
        AdministrareService administrareService,
        AdministrareRepository administrareRepository,
        AdministrareQueryService administrareQueryService
    ) {
        this.administrareService = administrareService;
        this.administrareRepository = administrareRepository;
        this.administrareQueryService = administrareQueryService;
    }

    /**
     * {@code POST  /administrares} : Create a new administrare.
     *
     * @param administrareDTO the administrareDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administrareDTO, or with status {@code 400 (Bad Request)} if the administrare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdministrareDTO> createAdministrare(@Valid @RequestBody AdministrareDTO administrareDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Administrare : {}", administrareDTO);
        if (administrareDTO.getId() != null) {
            throw new BadRequestAlertException("A new administrare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        administrareDTO = administrareService.save(administrareDTO);
        return ResponseEntity.created(new URI("/api/administrares/" + administrareDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, administrareDTO.getId().toString()))
            .body(administrareDTO);
    }

    /**
     * {@code PUT  /administrares/:id} : Updates an existing administrare.
     *
     * @param id the id of the administrareDTO to save.
     * @param administrareDTO the administrareDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrareDTO,
     * or with status {@code 400 (Bad Request)} if the administrareDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administrareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdministrareDTO> updateAdministrare(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdministrareDTO administrareDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Administrare : {}, {}", id, administrareDTO);
        if (administrareDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrareDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        administrareDTO = administrareService.update(administrareDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrareDTO.getId().toString()))
            .body(administrareDTO);
    }

    /**
     * {@code PATCH  /administrares/:id} : Partial updates given fields of an existing administrare, field will ignore if it is null
     *
     * @param id the id of the administrareDTO to save.
     * @param administrareDTO the administrareDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrareDTO,
     * or with status {@code 400 (Bad Request)} if the administrareDTO is not valid,
     * or with status {@code 404 (Not Found)} if the administrareDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the administrareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdministrareDTO> partialUpdateAdministrare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdministrareDTO administrareDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Administrare partially : {}, {}", id, administrareDTO);
        if (administrareDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrareDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdministrareDTO> result = administrareService.partialUpdate(administrareDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrareDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /administrares} : get all the administrares.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administrares in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdministrareDTO>> getAllAdministrares(
        AdministrareCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Administrares by criteria: {}", criteria);

        Page<AdministrareDTO> page = administrareQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /administrares/count} : count all the administrares.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAdministrares(AdministrareCriteria criteria) {
        LOG.debug("REST request to count Administrares by criteria: {}", criteria);
        return ResponseEntity.ok().body(administrareQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /administrares/:id} : get the "id" administrare.
     *
     * @param id the id of the administrareDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administrareDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdministrareDTO> getAdministrare(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Administrare : {}", id);
        Optional<AdministrareDTO> administrareDTO = administrareService.findOne(id);
        return ResponseUtil.wrapOrNotFound(administrareDTO);
    }

    /**
     * {@code DELETE  /administrares/:id} : delete the "id" administrare.
     *
     * @param id the id of the administrareDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrare(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Administrare : {}", id);
        administrareService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
