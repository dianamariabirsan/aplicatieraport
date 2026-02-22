package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.criteria.DecisionLogCriteria;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.service.mapper.DecisionLogMapper;
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
 * Service for executing complex queries for {@link DecisionLog} entities in the database.
 * The main input is a {@link DecisionLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DecisionLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecisionLogQueryService extends QueryService<DecisionLog> {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionLogQueryService.class);

    private final DecisionLogRepository decisionLogRepository;

    private final DecisionLogMapper decisionLogMapper;

    public DecisionLogQueryService(DecisionLogRepository decisionLogRepository, DecisionLogMapper decisionLogMapper) {
        this.decisionLogRepository = decisionLogRepository;
        this.decisionLogMapper = decisionLogMapper;
    }

    /**
     * Return a {@link Page} of {@link DecisionLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionLogDTO> findByCriteria(DecisionLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DecisionLog> specification = createSpecification(criteria);
        return decisionLogRepository.findAll(specification, page).map(decisionLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecisionLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DecisionLog> specification = createSpecification(criteria);
        return decisionLogRepository.count(specification);
    }

    /**
     * Function to convert {@link DecisionLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DecisionLog> createSpecification(DecisionLogCriteria criteria) {
        Specification<DecisionLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), DecisionLog_.id),
                buildRangeSpecification(criteria.getTimestamp(), DecisionLog_.timestamp),
                buildSpecification(criteria.getActorType(), DecisionLog_.actorType),
                buildStringSpecification(criteria.getRecomandare(), DecisionLog_.recomandare),
                buildRangeSpecification(criteria.getModelScore(), DecisionLog_.modelScore),
                buildStringSpecification(criteria.getReguliTriggered(), DecisionLog_.reguliTriggered),
                buildStringSpecification(criteria.getExternalChecks(), DecisionLog_.externalChecks),
                buildSpecification(criteria.getAlocareId(), root -> root.join(DecisionLog_.alocare, JoinType.LEFT).get(AlocareTratament_.id)
                )
            );
        }
        return specification;
    }
}
