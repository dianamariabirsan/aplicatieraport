package com.example.healthapp.web.rest;

import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.service.MedicQueryService;
import com.example.healthapp.service.MedicService;
import com.example.healthapp.service.criteria.MedicCriteria;
import com.example.healthapp.service.dto.MedicDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Medic}.
 */
@RestController
@RequestMapping("/api/medics")
public class MedicResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicResource.class);

    private static final String ENTITY_NAME = "medic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicService medicService;

    private final MedicRepository medicRepository;

    private final MedicQueryService medicQueryService;

    public MedicResource(MedicService medicService, MedicRepository medicRepository, MedicQueryService medicQueryService) {
        this.medicService = medicService;
        this.medicRepository = medicRepository;
        this.medicQueryService = medicQueryService;
    }

    /**
     * {@code POST  /medics} : Create a new medic.
     *
     * @param medicDTO the medicDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicDTO, or with status {@code 400 (Bad Request)} if the medic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicDTO> createMedic(@Valid @RequestBody MedicDTO medicDTO) throws URISyntaxException {
        LOG.debug("REST request to save Medic : {}", medicDTO);
        if (medicDTO.getId() != null) {
            throw new BadRequestAlertException("A new medic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicDTO = medicService.save(medicDTO);
        return ResponseEntity.created(new URI("/api/medics/" + medicDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicDTO.getId().toString()))
            .body(medicDTO);
    }

    /**
     * {@code PUT  /medics/:id} : Updates an existing medic.
     *
     * @param id the id of the medicDTO to save.
     * @param medicDTO the medicDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicDTO,
     * or with status {@code 400 (Bad Request)} if the medicDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicDTO> updateMedic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicDTO medicDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Medic : {}, {}", id, medicDTO);
        if (medicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicDTO = medicService.update(medicDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicDTO.getId().toString()))
            .body(medicDTO);
    }

    /**
     * {@code PATCH  /medics/:id} : Partial updates given fields of an existing medic, field will ignore if it is null
     *
     * @param id the id of the medicDTO to save.
     * @param medicDTO the medicDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicDTO,
     * or with status {@code 400 (Bad Request)} if the medicDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicDTO> partialUpdateMedic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicDTO medicDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Medic partially : {}, {}", id, medicDTO);
        if (medicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicDTO> result = medicService.partialUpdate(medicDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medics} : get all the medics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicDTO>> getAllMedics(
        MedicCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Medics by criteria: {}", criteria);

        Page<MedicDTO> page = medicQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medics/count} : count all the medics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedics(MedicCriteria criteria) {
        LOG.debug("REST request to count Medics by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medics/:id} : get the "id" medic.
     *
     * @param id the id of the medicDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> getMedic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medic : {}", id);
        Optional<MedicDTO> medicDTO = medicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicDTO);
    }

    /**
     * {@code DELETE  /medics/:id} : delete the "id" medic.
     *
     * @param id the id of the medicDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medic : {}", id);
        medicService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
