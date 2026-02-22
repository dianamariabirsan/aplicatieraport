package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Administrare;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.service.criteria.AdministrareCriteria;
import com.example.healthapp.service.dto.AdministrareDTO;
import com.example.healthapp.service.mapper.AdministrareMapper;
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
 * Service for executing complex queries for {@link Administrare} entities in the database.
 * The main input is a {@link AdministrareCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AdministrareDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdministrareQueryService extends QueryService<Administrare> {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrareQueryService.class);

    private final AdministrareRepository administrareRepository;

    private final AdministrareMapper administrareMapper;

    public AdministrareQueryService(AdministrareRepository administrareRepository, AdministrareMapper administrareMapper) {
        this.administrareRepository = administrareRepository;
        this.administrareMapper = administrareMapper;
    }

    /**
     * Return a {@link Page} of {@link AdministrareDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdministrareDTO> findByCriteria(AdministrareCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Administrare> specification = createSpecification(criteria);
        return administrareRepository.findAll(specification, page).map(administrareMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdministrareCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Administrare> specification = createSpecification(criteria);
        return administrareRepository.count(specification);
    }

    /**
     * Function to convert {@link AdministrareCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Administrare> createSpecification(AdministrareCriteria criteria) {
        Specification<Administrare> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Administrare_.id),
                buildRangeSpecification(criteria.getDataAdministrare(), Administrare_.dataAdministrare),
                buildStringSpecification(criteria.getTipTratament(), Administrare_.tipTratament),
                buildRangeSpecification(criteria.getDoza(), Administrare_.doza),
                buildStringSpecification(criteria.getUnitate(), Administrare_.unitate),
                buildStringSpecification(criteria.getModAdministrare(), Administrare_.modAdministrare),
                buildStringSpecification(criteria.getObservatii(), Administrare_.observatii),
                buildSpecification(criteria.getPacientId(), root -> root.join(Administrare_.pacient, JoinType.LEFT).get(Pacient_.id)),
                buildSpecification(criteria.getFarmacistId(), root -> root.join(Administrare_.farmacist, JoinType.LEFT).get(Farmacist_.id))
            );
        }
        return specification;
    }
}
