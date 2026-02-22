package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.service.criteria.AlocareTratamentCriteria;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.mapper.AlocareTratamentMapper;
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
 * Service for executing complex queries for {@link AlocareTratament} entities in the database.
 * The main input is a {@link AlocareTratamentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlocareTratamentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlocareTratamentQueryService extends QueryService<AlocareTratament> {

    private static final Logger LOG = LoggerFactory.getLogger(AlocareTratamentQueryService.class);

    private final AlocareTratamentRepository alocareTratamentRepository;

    private final AlocareTratamentMapper alocareTratamentMapper;

    public AlocareTratamentQueryService(
        AlocareTratamentRepository alocareTratamentRepository,
        AlocareTratamentMapper alocareTratamentMapper
    ) {
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.alocareTratamentMapper = alocareTratamentMapper;
    }

    /**
     * Return a {@link Page} of {@link AlocareTratamentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlocareTratamentDTO> findByCriteria(AlocareTratamentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlocareTratament> specification = createSpecification(criteria);
        return alocareTratamentRepository.findAll(specification, page).map(alocareTratamentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlocareTratamentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlocareTratament> specification = createSpecification(criteria);
        return alocareTratamentRepository.count(specification);
    }

    /**
     * Function to convert {@link AlocareTratamentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlocareTratament> createSpecification(AlocareTratamentCriteria criteria) {
        Specification<AlocareTratament> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AlocareTratament_.id),
                buildRangeSpecification(criteria.getDataDecizie(), AlocareTratament_.dataDecizie),
                buildStringSpecification(criteria.getTratamentPropus(), AlocareTratament_.tratamentPropus),
                buildStringSpecification(criteria.getMotivDecizie(), AlocareTratament_.motivDecizie),
                buildRangeSpecification(criteria.getScorDecizie(), AlocareTratament_.scorDecizie),
                buildSpecification(criteria.getDecizieValidata(), AlocareTratament_.decizieValidata),
                buildSpecification(criteria.getDeciziiId(), root ->
                    root.join(AlocareTratament_.deciziis, JoinType.LEFT).get(DecisionLog_.id)
                ),
                buildSpecification(criteria.getFeedbackuriId(), root ->
                    root.join(AlocareTratament_.feedbackuris, JoinType.LEFT).get(Feedback_.id)
                ),
                buildSpecification(criteria.getMedicId(), root -> root.join(AlocareTratament_.medic, JoinType.LEFT).get(Medic_.id)),
                buildSpecification(criteria.getMedicamentId(), root ->
                    root.join(AlocareTratament_.medicament, JoinType.LEFT).get(Medicament_.id)
                ),
                buildSpecification(criteria.getPacientId(), root -> root.join(AlocareTratament_.pacient, JoinType.LEFT).get(Pacient_.id))
            );
        }
        return specification;
    }
}
