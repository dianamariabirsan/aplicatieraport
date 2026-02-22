package com.example.healthapp.web.rest;

import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.service.ExternalDrugInfoQueryService;
import com.example.healthapp.service.ExternalDrugInfoService;
import com.example.healthapp.service.criteria.ExternalDrugInfoCriteria;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
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
 * REST controller for managing {@link com.example.healthapp.domain.ExternalDrugInfo}.
 */
@RestController
@RequestMapping("/api/external-drug-infos")
public class ExternalDrugInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalDrugInfoResource.class);

    private static final String ENTITY_NAME = "externalDrugInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExternalDrugInfoService externalDrugInfoService;

    private final ExternalDrugInfoRepository externalDrugInfoRepository;

    private final ExternalDrugInfoQueryService externalDrugInfoQueryService;

    public ExternalDrugInfoResource(
        ExternalDrugInfoService externalDrugInfoService,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        ExternalDrugInfoQueryService externalDrugInfoQueryService
    ) {
        this.externalDrugInfoService = externalDrugInfoService;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.externalDrugInfoQueryService = externalDrugInfoQueryService;
    }

    /**
     * {@code POST  /external-drug-infos} : Create a new externalDrugInfo.
     *
     * @param externalDrugInfoDTO the externalDrugInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new externalDrugInfoDTO, or with status {@code 400 (Bad Request)} if the externalDrugInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExternalDrugInfoDTO> createExternalDrugInfo(@Valid @RequestBody ExternalDrugInfoDTO externalDrugInfoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExternalDrugInfo : {}", externalDrugInfoDTO);
        if (externalDrugInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new externalDrugInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        externalDrugInfoDTO = externalDrugInfoService.save(externalDrugInfoDTO);
        return ResponseEntity.created(new URI("/api/external-drug-infos/" + externalDrugInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, externalDrugInfoDTO.getId().toString()))
            .body(externalDrugInfoDTO);
    }

    /**
     * {@code PUT  /external-drug-infos/:id} : Updates an existing externalDrugInfo.
     *
     * @param id the id of the externalDrugInfoDTO to save.
     * @param externalDrugInfoDTO the externalDrugInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalDrugInfoDTO,
     * or with status {@code 400 (Bad Request)} if the externalDrugInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the externalDrugInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExternalDrugInfoDTO> updateExternalDrugInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExternalDrugInfoDTO externalDrugInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExternalDrugInfo : {}, {}", id, externalDrugInfoDTO);
        if (externalDrugInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalDrugInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalDrugInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        externalDrugInfoDTO = externalDrugInfoService.update(externalDrugInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, externalDrugInfoDTO.getId().toString()))
            .body(externalDrugInfoDTO);
    }

    /**
     * {@code PATCH  /external-drug-infos/:id} : Partial updates given fields of an existing externalDrugInfo, field will ignore if it is null
     *
     * @param id the id of the externalDrugInfoDTO to save.
     * @param externalDrugInfoDTO the externalDrugInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated externalDrugInfoDTO,
     * or with status {@code 400 (Bad Request)} if the externalDrugInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the externalDrugInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the externalDrugInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExternalDrugInfoDTO> partialUpdateExternalDrugInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExternalDrugInfoDTO externalDrugInfoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExternalDrugInfo partially : {}, {}", id, externalDrugInfoDTO);
        if (externalDrugInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, externalDrugInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!externalDrugInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExternalDrugInfoDTO> result = externalDrugInfoService.partialUpdate(externalDrugInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, externalDrugInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /external-drug-infos} : get all the externalDrugInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of externalDrugInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExternalDrugInfoDTO>> getAllExternalDrugInfos(
        ExternalDrugInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExternalDrugInfos by criteria: {}", criteria);

        Page<ExternalDrugInfoDTO> page = externalDrugInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /external-drug-infos/count} : count all the externalDrugInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExternalDrugInfos(ExternalDrugInfoCriteria criteria) {
        LOG.debug("REST request to count ExternalDrugInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(externalDrugInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /external-drug-infos/:id} : get the "id" externalDrugInfo.
     *
     * @param id the id of the externalDrugInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the externalDrugInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExternalDrugInfoDTO> getExternalDrugInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExternalDrugInfo : {}", id);
        Optional<ExternalDrugInfoDTO> externalDrugInfoDTO = externalDrugInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(externalDrugInfoDTO);
    }

    /**
     * {@code DELETE  /external-drug-infos/:id} : delete the "id" externalDrugInfo.
     *
     * @param id the id of the externalDrugInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExternalDrugInfo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExternalDrugInfo : {}", id);
        externalDrugInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
