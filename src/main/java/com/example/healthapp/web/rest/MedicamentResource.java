package com.example.healthapp.web.rest;

import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.MedicamentQueryService;
import com.example.healthapp.service.MedicamentService;
import com.example.healthapp.service.criteria.MedicamentCriteria;
import com.example.healthapp.service.dto.MedicamentDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Medicament}.
 */
@RestController
@RequestMapping("/api/medicaments")
public class MedicamentResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentResource.class);

    private static final String ENTITY_NAME = "medicament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentService medicamentService;

    private final MedicamentRepository medicamentRepository;

    private final MedicamentQueryService medicamentQueryService;

    public MedicamentResource(
        MedicamentService medicamentService,
        MedicamentRepository medicamentRepository,
        MedicamentQueryService medicamentQueryService
    ) {
        this.medicamentService = medicamentService;
        this.medicamentRepository = medicamentRepository;
        this.medicamentQueryService = medicamentQueryService;
    }

    /**
     * {@code POST  /medicaments} : Create a new medicament.
     *
     * @param medicamentDTO the medicamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamentDTO, or with status {@code 400 (Bad Request)} if the medicament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicamentDTO> createMedicament(@Valid @RequestBody MedicamentDTO medicamentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Medicament : {}", medicamentDTO);
        if (medicamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicamentDTO = medicamentService.save(medicamentDTO);
        return ResponseEntity.created(new URI("/api/medicaments/" + medicamentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicamentDTO.getId().toString()))
            .body(medicamentDTO);
    }

    /**
     * {@code PUT  /medicaments/:id} : Updates an existing medicament.
     *
     * @param id the id of the medicamentDTO to save.
     * @param medicamentDTO the medicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentDTO> updateMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicamentDTO medicamentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Medicament : {}, {}", id, medicamentDTO);
        if (medicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicamentDTO = medicamentService.update(medicamentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentDTO.getId().toString()))
            .body(medicamentDTO);
    }

    /**
     * {@code PATCH  /medicaments/:id} : Partial updates given fields of an existing medicament, field will ignore if it is null
     *
     * @param id the id of the medicamentDTO to save.
     * @param medicamentDTO the medicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicamentDTO> partialUpdateMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicamentDTO medicamentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Medicament partially : {}, {}", id, medicamentDTO);
        if (medicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicamentDTO> result = medicamentService.partialUpdate(medicamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicaments} : get all the medicaments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicaments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicamentDTO>> getAllMedicaments(
        MedicamentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Medicaments by criteria: {}", criteria);

        Page<MedicamentDTO> page = medicamentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicaments/count} : count all the medicaments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMedicaments(MedicamentCriteria criteria) {
        LOG.debug("REST request to count Medicaments by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicamentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medicaments/:id} : get the "id" medicament.
     *
     * @param id the id of the medicamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentDTO> getMedicament(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medicament : {}", id);
        Optional<MedicamentDTO> medicamentDTO = medicamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicamentDTO);
    }

    /**
     * {@code DELETE  /medicaments/:id} : delete the "id" medicament.
     *
     * @param id the id of the medicamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medicament : {}", id);
        medicamentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
