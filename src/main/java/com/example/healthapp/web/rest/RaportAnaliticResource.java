package com.example.healthapp.web.rest;

import com.example.healthapp.repository.RaportAnaliticRepository;
import com.example.healthapp.service.RaportAnaliticQueryService;
import com.example.healthapp.service.RaportAnaliticService;
import com.example.healthapp.service.criteria.RaportAnaliticCriteria;
import com.example.healthapp.service.dto.RaportAnaliticDTO;
import com.example.healthapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.example.healthapp.domain.RaportAnalitic}.
 */
@RestController
@RequestMapping("/api/raport-analitics")
public class RaportAnaliticResource {

    private static final Logger LOG = LoggerFactory.getLogger(RaportAnaliticResource.class);

    private static final String ENTITY_NAME = "raportAnalitic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaportAnaliticService raportAnaliticService;

    private final RaportAnaliticRepository raportAnaliticRepository;

    private final RaportAnaliticQueryService raportAnaliticQueryService;

    public RaportAnaliticResource(
        RaportAnaliticService raportAnaliticService,
        RaportAnaliticRepository raportAnaliticRepository,
        RaportAnaliticQueryService raportAnaliticQueryService
    ) {
        this.raportAnaliticService = raportAnaliticService;
        this.raportAnaliticRepository = raportAnaliticRepository;
        this.raportAnaliticQueryService = raportAnaliticQueryService;
    }

    /**
     * {@code POST  /raport-analitics} : Create a new raportAnalitic.
     *
     * @param raportAnaliticDTO the raportAnaliticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raportAnaliticDTO, or with status {@code 400 (Bad Request)} if the raportAnalitic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RaportAnaliticDTO> createRaportAnalitic(@RequestBody RaportAnaliticDTO raportAnaliticDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RaportAnalitic : {}", raportAnaliticDTO);
        if (raportAnaliticDTO.getId() != null) {
            throw new BadRequestAlertException("A new raportAnalitic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        raportAnaliticDTO = raportAnaliticService.save(raportAnaliticDTO);
        return ResponseEntity.created(new URI("/api/raport-analitics/" + raportAnaliticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, raportAnaliticDTO.getId().toString()))
            .body(raportAnaliticDTO);
    }

    /**
     * {@code PUT  /raport-analitics/:id} : Updates an existing raportAnalitic.
     *
     * @param id the id of the raportAnaliticDTO to save.
     * @param raportAnaliticDTO the raportAnaliticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raportAnaliticDTO,
     * or with status {@code 400 (Bad Request)} if the raportAnaliticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raportAnaliticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RaportAnaliticDTO> updateRaportAnalitic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RaportAnaliticDTO raportAnaliticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RaportAnalitic : {}, {}", id, raportAnaliticDTO);
        if (raportAnaliticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raportAnaliticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raportAnaliticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        raportAnaliticDTO = raportAnaliticService.update(raportAnaliticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raportAnaliticDTO.getId().toString()))
            .body(raportAnaliticDTO);
    }

    /**
     * {@code PATCH  /raport-analitics/:id} : Partial updates given fields of an existing raportAnalitic, field will ignore if it is null
     *
     * @param id the id of the raportAnaliticDTO to save.
     * @param raportAnaliticDTO the raportAnaliticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raportAnaliticDTO,
     * or with status {@code 400 (Bad Request)} if the raportAnaliticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the raportAnaliticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the raportAnaliticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RaportAnaliticDTO> partialUpdateRaportAnalitic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RaportAnaliticDTO raportAnaliticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RaportAnalitic partially : {}, {}", id, raportAnaliticDTO);
        if (raportAnaliticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raportAnaliticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raportAnaliticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RaportAnaliticDTO> result = raportAnaliticService.partialUpdate(raportAnaliticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raportAnaliticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /raport-analitics} : get all the raportAnalitics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raportAnalitics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RaportAnaliticDTO>> getAllRaportAnalitics(
        RaportAnaliticCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RaportAnalitics by criteria: {}", criteria);

        Page<RaportAnaliticDTO> page = raportAnaliticQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raport-analitics/count} : count all the raportAnalitics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRaportAnalitics(RaportAnaliticCriteria criteria) {
        LOG.debug("REST request to count RaportAnalitics by criteria: {}", criteria);
        return ResponseEntity.ok().body(raportAnaliticQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /raport-analitics/:id} : get the "id" raportAnalitic.
     *
     * @param id the id of the raportAnaliticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raportAnaliticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RaportAnaliticDTO> getRaportAnalitic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RaportAnalitic : {}", id);
        Optional<RaportAnaliticDTO> raportAnaliticDTO = raportAnaliticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raportAnaliticDTO);
    }

    /**
     * {@code DELETE  /raport-analitics/:id} : delete the "id" raportAnalitic.
     *
     * @param id the id of the raportAnaliticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRaportAnalitic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RaportAnalitic : {}", id);
        raportAnaliticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
