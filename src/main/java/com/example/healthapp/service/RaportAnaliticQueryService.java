package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.repository.RaportAnaliticRepository;
import com.example.healthapp.service.criteria.RaportAnaliticCriteria;
import com.example.healthapp.service.dto.RaportAnaliticDTO;
import com.example.healthapp.service.mapper.RaportAnaliticMapper;
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
 * Service for executing complex queries for {@link RaportAnalitic} entities in the database.
 * The main input is a {@link RaportAnaliticCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RaportAnaliticDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RaportAnaliticQueryService extends QueryService<RaportAnalitic> {

    private static final Logger LOG = LoggerFactory.getLogger(RaportAnaliticQueryService.class);

    private final RaportAnaliticRepository raportAnaliticRepository;

    private final RaportAnaliticMapper raportAnaliticMapper;

    public RaportAnaliticQueryService(RaportAnaliticRepository raportAnaliticRepository, RaportAnaliticMapper raportAnaliticMapper) {
        this.raportAnaliticRepository = raportAnaliticRepository;
        this.raportAnaliticMapper = raportAnaliticMapper;
    }

    /**
     * Return a {@link Page} of {@link RaportAnaliticDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RaportAnaliticDTO> findByCriteria(RaportAnaliticCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RaportAnalitic> specification = createSpecification(criteria);
        return raportAnaliticRepository.findAll(specification, page).map(raportAnaliticMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RaportAnaliticCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RaportAnalitic> specification = createSpecification(criteria);
        return raportAnaliticRepository.count(specification);
    }

    /**
     * Function to convert {@link RaportAnaliticCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RaportAnalitic> createSpecification(RaportAnaliticCriteria criteria) {
        Specification<RaportAnalitic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), RaportAnalitic_.id),
                buildRangeSpecification(criteria.getPerioadaStart(), RaportAnalitic_.perioadaStart),
                buildRangeSpecification(criteria.getPerioadaEnd(), RaportAnalitic_.perioadaEnd),
                buildRangeSpecification(criteria.getEficientaMedie(), RaportAnalitic_.eficientaMedie),
                buildRangeSpecification(criteria.getRataReactiiAdverse(), RaportAnalitic_.rataReactiiAdverse),
                buildStringSpecification(criteria.getObservatii(), RaportAnalitic_.observatii),
                buildStringSpecification(criteria.getConcluzii(), RaportAnalitic_.concluzii),
                buildSpecification(criteria.getMedicamentId(), root ->
                    root.join(RaportAnalitic_.medicament, JoinType.LEFT).get(Medicament_.id)
                ),
                buildSpecification(criteria.getMedicId(), root -> root.join(RaportAnalitic_.medic, JoinType.LEFT).get(Medic_.id))
            );
        }
        return specification;
    }
}
