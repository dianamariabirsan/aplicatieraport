package com.example.healthapp.web.rest;

import com.example.healthapp.repository.FarmacistRepository;
import com.example.healthapp.service.FarmacistQueryService;
import com.example.healthapp.service.FarmacistService;
import com.example.healthapp.service.criteria.FarmacistCriteria;
import com.example.healthapp.service.dto.FarmacistDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.Farmacist}.
 */
@RestController
@RequestMapping("/api/farmacists")
public class FarmacistResource {

    private static final Logger LOG = LoggerFactory.getLogger(FarmacistResource.class);

    private static final String ENTITY_NAME = "farmacist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmacistService farmacistService;

    private final FarmacistRepository farmacistRepository;

    private final FarmacistQueryService farmacistQueryService;

    public FarmacistResource(
        FarmacistService farmacistService,
        FarmacistRepository farmacistRepository,
        FarmacistQueryService farmacistQueryService
    ) {
        this.farmacistService = farmacistService;
        this.farmacistRepository = farmacistRepository;
        this.farmacistQueryService = farmacistQueryService;
    }

    /**
     * {@code POST  /farmacists} : Create a new farmacist.
     *
     * @param farmacistDTO the farmacistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmacistDTO, or with status {@code 400 (Bad Request)} if the farmacist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FarmacistDTO> createFarmacist(@Valid @RequestBody FarmacistDTO farmacistDTO) throws URISyntaxException {
        LOG.debug("REST request to save Farmacist : {}", farmacistDTO);
        if (farmacistDTO.getId() != null) {
            throw new BadRequestAlertException("A new farmacist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        farmacistDTO = farmacistService.save(farmacistDTO);
        return ResponseEntity.created(new URI("/api/farmacists/" + farmacistDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, farmacistDTO.getId().toString()))
            .body(farmacistDTO);
    }

    /**
     * {@code PUT  /farmacists/:id} : Updates an existing farmacist.
     *
     * @param id the id of the farmacistDTO to save.
     * @param farmacistDTO the farmacistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmacistDTO,
     * or with status {@code 400 (Bad Request)} if the farmacistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmacistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FarmacistDTO> updateFarmacist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FarmacistDTO farmacistDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Farmacist : {}, {}", id, farmacistDTO);
        if (farmacistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmacistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmacistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        farmacistDTO = farmacistService.update(farmacistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, farmacistDTO.getId().toString()))
            .body(farmacistDTO);
    }

    /**
     * {@code PATCH  /farmacists/:id} : Partial updates given fields of an existing farmacist, field will ignore if it is null
     *
     * @param id the id of the farmacistDTO to save.
     * @param farmacistDTO the farmacistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmacistDTO,
     * or with status {@code 400 (Bad Request)} if the farmacistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the farmacistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmacistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FarmacistDTO> partialUpdateFarmacist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FarmacistDTO farmacistDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Farmacist partially : {}, {}", id, farmacistDTO);
        if (farmacistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmacistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmacistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FarmacistDTO> result = farmacistService.partialUpdate(farmacistDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, farmacistDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /farmacists} : get all the farmacists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmacists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FarmacistDTO>> getAllFarmacists(
        FarmacistCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Farmacists by criteria: {}", criteria);

        Page<FarmacistDTO> page = farmacistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmacists/count} : count all the farmacists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFarmacists(FarmacistCriteria criteria) {
        LOG.debug("REST request to count Farmacists by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmacistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmacists/:id} : get the "id" farmacist.
     *
     * @param id the id of the farmacistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmacistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FarmacistDTO> getFarmacist(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Farmacist : {}", id);
        Optional<FarmacistDTO> farmacistDTO = farmacistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmacistDTO);
    }

    /**
     * {@code DELETE  /farmacists/:id} : delete the "id" farmacist.
     *
     * @param id the id of the farmacistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmacist(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Farmacist : {}", id);
        farmacistService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
