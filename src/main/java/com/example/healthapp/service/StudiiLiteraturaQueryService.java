package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.StudiiLiteratura;
import com.example.healthapp.repository.StudiiLiteraturaRepository;
import com.example.healthapp.service.criteria.StudiiLiteraturaCriteria;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
import com.example.healthapp.service.mapper.StudiiLiteraturaMapper;
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
 * Service for executing complex queries for {@link StudiiLiteratura} entities in the database.
 * The main input is a {@link StudiiLiteraturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link StudiiLiteraturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudiiLiteraturaQueryService extends QueryService<StudiiLiteratura> {

    private static final Logger LOG = LoggerFactory.getLogger(StudiiLiteraturaQueryService.class);

    private final StudiiLiteraturaRepository studiiLiteraturaRepository;

    private final StudiiLiteraturaMapper studiiLiteraturaMapper;

    public StudiiLiteraturaQueryService(
        StudiiLiteraturaRepository studiiLiteraturaRepository,
        StudiiLiteraturaMapper studiiLiteraturaMapper
    ) {
        this.studiiLiteraturaRepository = studiiLiteraturaRepository;
        this.studiiLiteraturaMapper = studiiLiteraturaMapper;
    }

    /**
     * Return a {@link Page} of {@link StudiiLiteraturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudiiLiteraturaDTO> findByCriteria(StudiiLiteraturaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudiiLiteratura> specification = createSpecification(criteria);
        return studiiLiteraturaRepository.findAll(specification, page).map(studiiLiteraturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudiiLiteraturaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StudiiLiteratura> specification = createSpecification(criteria);
        return studiiLiteraturaRepository.count(specification);
    }

    /**
     * Function to convert {@link StudiiLiteraturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudiiLiteratura> createSpecification(StudiiLiteraturaCriteria criteria) {
        Specification<StudiiLiteratura> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), StudiiLiteratura_.id),
                buildStringSpecification(criteria.getTitlu(), StudiiLiteratura_.titlu),
                buildStringSpecification(criteria.getAutori(), StudiiLiteratura_.autori),
                buildRangeSpecification(criteria.getAnul(), StudiiLiteratura_.anul),
                buildStringSpecification(criteria.getTipStudiu(), StudiiLiteratura_.tipStudiu),
                buildStringSpecification(criteria.getSubstanta(), StudiiLiteratura_.substanta),
                buildStringSpecification(criteria.getConcluzie(), StudiiLiteratura_.concluzie),
                buildStringSpecification(criteria.getLink(), StudiiLiteratura_.link),
                buildSpecification(criteria.getMedicamentId(), root ->
                    root.join(StudiiLiteratura_.medicament, JoinType.LEFT).get(Medicament_.id)
                )
            );
        }
        return specification;
    }
}
