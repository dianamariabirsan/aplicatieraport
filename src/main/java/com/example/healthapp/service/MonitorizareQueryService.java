package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Monitorizare;
import com.example.healthapp.repository.MonitorizareRepository;
import com.example.healthapp.service.criteria.MonitorizareCriteria;
import com.example.healthapp.service.dto.MonitorizareDTO;
import com.example.healthapp.service.mapper.MonitorizareMapper;
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
 * Service for executing complex queries for {@link Monitorizare} entities in the database.
 * The main input is a {@link MonitorizareCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MonitorizareDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonitorizareQueryService extends QueryService<Monitorizare> {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorizareQueryService.class);

    private final MonitorizareRepository monitorizareRepository;

    private final MonitorizareMapper monitorizareMapper;

    public MonitorizareQueryService(MonitorizareRepository monitorizareRepository, MonitorizareMapper monitorizareMapper) {
        this.monitorizareRepository = monitorizareRepository;
        this.monitorizareMapper = monitorizareMapper;
    }

    /**
     * Return a {@link Page} of {@link MonitorizareDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonitorizareDTO> findByCriteria(MonitorizareCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Monitorizare> specification = createSpecification(criteria);
        return monitorizareRepository.findAll(specification, page).map(monitorizareMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonitorizareCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Monitorizare> specification = createSpecification(criteria);
        return monitorizareRepository.count(specification);
    }

    /**
     * Function to convert {@link MonitorizareCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Monitorizare> createSpecification(MonitorizareCriteria criteria) {
        Specification<Monitorizare> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Monitorizare_.id),
                buildRangeSpecification(criteria.getDataInstant(), Monitorizare_.dataInstant),
                buildRangeSpecification(criteria.getTensiuneSist(), Monitorizare_.tensiuneSist),
                buildRangeSpecification(criteria.getTensiuneDiast(), Monitorizare_.tensiuneDiast),
                buildRangeSpecification(criteria.getPuls(), Monitorizare_.puls),
                buildRangeSpecification(criteria.getGlicemie(), Monitorizare_.glicemie),
                buildRangeSpecification(criteria.getScorEficacitate(), Monitorizare_.scorEficacitate),
                buildStringSpecification(criteria.getComentarii(), Monitorizare_.comentarii),
                buildSpecification(criteria.getPacientId(), root -> root.join(Monitorizare_.pacient, JoinType.LEFT).get(Pacient_.id))
            );
        }
        return specification;
    }
}
