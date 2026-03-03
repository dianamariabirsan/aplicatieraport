package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.criteria.ReactieAdversaCriteria;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
import com.example.healthapp.service.mapper.ReactieAdversaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ReactieAdversa} entities in the database.
 * The main input is a {@link ReactieAdversaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReactieAdversaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReactieAdversaQueryService extends QueryService<ReactieAdversa> {

    private static final Logger LOG = LoggerFactory.getLogger(ReactieAdversaQueryService.class);

    private final ReactieAdversaRepository reactieAdversaRepository;

    private final ReactieAdversaMapper reactieAdversaMapper;

    public ReactieAdversaQueryService(ReactieAdversaRepository reactieAdversaRepository, ReactieAdversaMapper reactieAdversaMapper) {
        this.reactieAdversaRepository = reactieAdversaRepository;
        this.reactieAdversaMapper = reactieAdversaMapper;
    }

    /**
     * Return a {@link Page} of {@link ReactieAdversaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReactieAdversaDTO> findByCriteria(ReactieAdversaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReactieAdversa> specification = createSpecification(criteria);
        return reactieAdversaRepository.findAll(specification, page).map(reactieAdversaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReactieAdversaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReactieAdversa> specification = createSpecification(criteria);
        return reactieAdversaRepository.count(specification);
    }

    /**
     * Function to convert {@link ReactieAdversaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReactieAdversa> createSpecification(ReactieAdversaCriteria criteria) {
        Specification<ReactieAdversa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReactieAdversa_.id),
                buildRangeSpecification(criteria.getDataRaportare(), ReactieAdversa_.dataRaportare),
                buildSpecification(criteria.getSeveritate(), ReactieAdversa_.severitate),
                buildStringSpecification(criteria.getDescriere(), ReactieAdversa_.descriere),
                buildStringSpecification(criteria.getEvolutie(), ReactieAdversa_.evolutie),
                buildStringSpecification(criteria.getRaportatDe(), ReactieAdversa_.raportatDe),
                buildSpecification(criteria.getMedicamentId(), root ->
                    root.join(ReactieAdversa_.medicament, JoinType.LEFT).get(Medicament_.id)
                ),
                buildSpecification(criteria.getPacientId(), root -> root.join(ReactieAdversa_.pacient, JoinType.LEFT).get(Pacient_.id))
            );
        }
        return specification;
    }
}
