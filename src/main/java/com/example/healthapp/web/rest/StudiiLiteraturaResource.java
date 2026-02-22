package com.example.healthapp.web.rest;

import com.example.healthapp.repository.StudiiLiteraturaRepository;
import com.example.healthapp.service.StudiiLiteraturaQueryService;
import com.example.healthapp.service.StudiiLiteraturaService;
import com.example.healthapp.service.criteria.StudiiLiteraturaCriteria;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.StudiiLiteratura}.
 */
@RestController
@RequestMapping("/api/studii-literaturas")
public class StudiiLiteraturaResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudiiLiteraturaResource.class);

    private static final String ENTITY_NAME = "studiiLiteratura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudiiLiteraturaService studiiLiteraturaService;

    private final StudiiLiteraturaRepository studiiLiteraturaRepository;

    private final StudiiLiteraturaQueryService studiiLiteraturaQueryService;

    public StudiiLiteraturaResource(
        StudiiLiteraturaService studiiLiteraturaService,
        StudiiLiteraturaRepository studiiLiteraturaRepository,
        StudiiLiteraturaQueryService studiiLiteraturaQueryService
    ) {
        this.studiiLiteraturaService = studiiLiteraturaService;
        this.studiiLiteraturaRepository = studiiLiteraturaRepository;
        this.studiiLiteraturaQueryService = studiiLiteraturaQueryService;
    }

    /**
     * {@code POST  /studii-literaturas} : Create a new studiiLiteratura.
     *
     * @param studiiLiteraturaDTO the studiiLiteraturaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studiiLiteraturaDTO, or with status {@code 400 (Bad Request)} if the studiiLiteratura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudiiLiteraturaDTO> createStudiiLiteratura(@Valid @RequestBody StudiiLiteraturaDTO studiiLiteraturaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StudiiLiteratura : {}", studiiLiteraturaDTO);
        if (studiiLiteraturaDTO.getId() != null) {
            throw new BadRequestAlertException("A new studiiLiteratura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studiiLiteraturaDTO = studiiLiteraturaService.save(studiiLiteraturaDTO);
        return ResponseEntity.created(new URI("/api/studii-literaturas/" + studiiLiteraturaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studiiLiteraturaDTO.getId().toString()))
            .body(studiiLiteraturaDTO);
    }

    /**
     * {@code PUT  /studii-literaturas/:id} : Updates an existing studiiLiteratura.
     *
     * @param id the id of the studiiLiteraturaDTO to save.
     * @param studiiLiteraturaDTO the studiiLiteraturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studiiLiteraturaDTO,
     * or with status {@code 400 (Bad Request)} if the studiiLiteraturaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studiiLiteraturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudiiLiteraturaDTO> updateStudiiLiteratura(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudiiLiteraturaDTO studiiLiteraturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudiiLiteratura : {}, {}", id, studiiLiteraturaDTO);
        if (studiiLiteraturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studiiLiteraturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studiiLiteraturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studiiLiteraturaDTO = studiiLiteraturaService.update(studiiLiteraturaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studiiLiteraturaDTO.getId().toString()))
            .body(studiiLiteraturaDTO);
    }

    /**
     * {@code PATCH  /studii-literaturas/:id} : Partial updates given fields of an existing studiiLiteratura, field will ignore if it is null
     *
     * @param id the id of the studiiLiteraturaDTO to save.
     * @param studiiLiteraturaDTO the studiiLiteraturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studiiLiteraturaDTO,
     * or with status {@code 400 (Bad Request)} if the studiiLiteraturaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studiiLiteraturaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studiiLiteraturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudiiLiteraturaDTO> partialUpdateStudiiLiteratura(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudiiLiteraturaDTO studiiLiteraturaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudiiLiteratura partially : {}, {}", id, studiiLiteraturaDTO);
        if (studiiLiteraturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studiiLiteraturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studiiLiteraturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudiiLiteraturaDTO> result = studiiLiteraturaService.partialUpdate(studiiLiteraturaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studiiLiteraturaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /studii-literaturas} : get all the studiiLiteraturas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studiiLiteraturas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudiiLiteraturaDTO>> getAllStudiiLiteraturas(
        StudiiLiteraturaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get StudiiLiteraturas by criteria: {}", criteria);

        Page<StudiiLiteraturaDTO> page = studiiLiteraturaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /studii-literaturas/count} : count all the studiiLiteraturas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStudiiLiteraturas(StudiiLiteraturaCriteria criteria) {
        LOG.debug("REST request to count StudiiLiteraturas by criteria: {}", criteria);
        return ResponseEntity.ok().body(studiiLiteraturaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /studii-literaturas/:id} : get the "id" studiiLiteratura.
     *
     * @param id the id of the studiiLiteraturaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studiiLiteraturaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudiiLiteraturaDTO> getStudiiLiteratura(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudiiLiteratura : {}", id);
        Optional<StudiiLiteraturaDTO> studiiLiteraturaDTO = studiiLiteraturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studiiLiteraturaDTO);
    }

    /**
     * {@code DELETE  /studii-literaturas/:id} : delete the "id" studiiLiteratura.
     *
     * @param id the id of the studiiLiteraturaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudiiLiteratura(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudiiLiteratura : {}", id);
        studiiLiteraturaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
