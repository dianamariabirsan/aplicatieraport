package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Feedback;
import com.example.healthapp.repository.FeedbackRepository;
import com.example.healthapp.service.criteria.FeedbackCriteria;
import com.example.healthapp.service.dto.FeedbackDTO;
import com.example.healthapp.service.mapper.FeedbackMapper;
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
 * Service for executing complex queries for {@link Feedback} entities in the database.
 * The main input is a {@link FeedbackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FeedbackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeedbackQueryService extends QueryService<Feedback> {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackQueryService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    public FeedbackQueryService(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }

    /**
     * Return a {@link Page} of {@link FeedbackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> findByCriteria(FeedbackCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification, page).map(feedbackMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeedbackCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.count(specification);
    }

    /**
     * Function to convert {@link FeedbackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Feedback> createSpecification(FeedbackCriteria criteria) {
        Specification<Feedback> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Feedback_.id),
                buildRangeSpecification(criteria.getScor(), Feedback_.scor),
                buildStringSpecification(criteria.getComentariu(), Feedback_.comentariu),
                buildRangeSpecification(criteria.getDataFeedback(), Feedback_.dataFeedback),
                buildSpecification(criteria.getAlocareId(), root -> root.join(Feedback_.alocare, JoinType.LEFT).get(AlocareTratament_.id))
            );
        }
        return specification;
    }
}
