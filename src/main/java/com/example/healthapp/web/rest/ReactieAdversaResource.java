package com.example.healthapp.web.rest;

import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.ReactieAdversaQueryService;
import com.example.healthapp.service.ReactieAdversaService;
import com.example.healthapp.service.criteria.ReactieAdversaCriteria;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.ReactieAdversa}.
 */
@RestController
@RequestMapping("/api/reactie-adversas")
public class ReactieAdversaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReactieAdversaResource.class);

    private static final String ENTITY_NAME = "reactieAdversa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReactieAdversaService reactieAdversaService;

    private final ReactieAdversaRepository reactieAdversaRepository;

    private final ReactieAdversaQueryService reactieAdversaQueryService;

    public ReactieAdversaResource(
        ReactieAdversaService reactieAdversaService,
        ReactieAdversaRepository reactieAdversaRepository,
        ReactieAdversaQueryService reactieAdversaQueryService
    ) {
        this.reactieAdversaService = reactieAdversaService;
        this.reactieAdversaRepository = reactieAdversaRepository;
        this.reactieAdversaQueryService = reactieAdversaQueryService;
    }

    /**
     * {@code POST  /reactie-adversas} : Create a new reactieAdversa.
     *
     * @param reactieAdversaDTO the reactieAdversaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reactieAdversaDTO, or with status {@code 400 (Bad Request)} if the reactieAdversa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReactieAdversaDTO> createReactieAdversa(@Valid @RequestBody ReactieAdversaDTO reactieAdversaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReactieAdversa : {}", reactieAdversaDTO);
        if (reactieAdversaDTO.getId() != null) {
            throw new BadRequestAlertException("A new reactieAdversa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reactieAdversaDTO = reactieAdversaService.save(reactieAdversaDTO);
        return ResponseEntity.created(new URI("/api/reactie-adversas/" + reactieAdversaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reactieAdversaDTO.getId().toString()))
            .body(reactieAdversaDTO);
    }

    /**
     * {@code PUT  /reactie-adversas/:id} : Updates an existing reactieAdversa.
     *
     * @param id the id of the reactieAdversaDTO to save.
     * @param reactieAdversaDTO the reactieAdversaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactieAdversaDTO,
     * or with status {@code 400 (Bad Request)} if the reactieAdversaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reactieAdversaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReactieAdversaDTO> updateReactieAdversa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReactieAdversaDTO reactieAdversaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReactieAdversa : {}, {}", id, reactieAdversaDTO);
        if (reactieAdversaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactieAdversaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactieAdversaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reactieAdversaDTO = reactieAdversaService.update(reactieAdversaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactieAdversaDTO.getId().toString()))
            .body(reactieAdversaDTO);
    }

    /**
     * {@code PATCH  /reactie-adversas/:id} : Partial updates given fields of an existing reactieAdversa, field will ignore if it is null
     *
     * @param id the id of the reactieAdversaDTO to save.
     * @param reactieAdversaDTO the reactieAdversaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactieAdversaDTO,
     * or with status {@code 400 (Bad Request)} if the reactieAdversaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reactieAdversaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reactieAdversaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReactieAdversaDTO> partialUpdateReactieAdversa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReactieAdversaDTO reactieAdversaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReactieAdversa partially : {}, {}", id, reactieAdversaDTO);
        if (reactieAdversaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reactieAdversaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reactieAdversaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReactieAdversaDTO> result = reactieAdversaService.partialUpdate(reactieAdversaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reactieAdversaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reactie-adversas} : get all the reactieAdversas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reactieAdversas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReactieAdversaDTO>> getAllReactieAdversas(
        ReactieAdversaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReactieAdversas by criteria: {}", criteria);

        Page<ReactieAdversaDTO> page = reactieAdversaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reactie-adversas/count} : count all the reactieAdversas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReactieAdversas(ReactieAdversaCriteria criteria) {
        LOG.debug("REST request to count ReactieAdversas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reactieAdversaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reactie-adversas/:id} : get the "id" reactieAdversa.
     *
     * @param id the id of the reactieAdversaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reactieAdversaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReactieAdversaDTO> getReactieAdversa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReactieAdversa : {}", id);
        Optional<ReactieAdversaDTO> reactieAdversaDTO = reactieAdversaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reactieAdversaDTO);
    }

    /**
     * {@code DELETE  /reactie-adversas/:id} : delete the "id" reactieAdversa.
     *
     * @param id the id of the reactieAdversaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReactieAdversa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReactieAdversa : {}", id);
        reactieAdversaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
