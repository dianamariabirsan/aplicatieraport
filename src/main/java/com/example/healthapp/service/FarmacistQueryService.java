package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.repository.FarmacistRepository;
import com.example.healthapp.service.criteria.FarmacistCriteria;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.mapper.FarmacistMapper;
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
 * Service for executing complex queries for {@link Farmacist} entities in the database.
 * The main input is a {@link FarmacistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FarmacistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FarmacistQueryService extends QueryService<Farmacist> {

    private static final Logger LOG = LoggerFactory.getLogger(FarmacistQueryService.class);

    private final FarmacistRepository farmacistRepository;

    private final FarmacistMapper farmacistMapper;

    public FarmacistQueryService(FarmacistRepository farmacistRepository, FarmacistMapper farmacistMapper) {
        this.farmacistRepository = farmacistRepository;
        this.farmacistMapper = farmacistMapper;
    }

    /**
     * Return a {@link Page} of {@link FarmacistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FarmacistDTO> findByCriteria(FarmacistCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Farmacist> specification = createSpecification(criteria);
        return farmacistRepository.findAll(specification, page).map(farmacistMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FarmacistCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Farmacist> specification = createSpecification(criteria);
        return farmacistRepository.count(specification);
    }

    /**
     * Function to convert {@link FarmacistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Farmacist> createSpecification(FarmacistCriteria criteria) {
        Specification<Farmacist> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Farmacist_.id),
                buildStringSpecification(criteria.getNume(), Farmacist_.nume),
                buildStringSpecification(criteria.getPrenume(), Farmacist_.prenume),
                buildStringSpecification(criteria.getFarmacie(), Farmacist_.farmacie),
                buildStringSpecification(criteria.getEmail(), Farmacist_.email),
                buildStringSpecification(criteria.getTelefon(), Farmacist_.telefon),
                buildSpecification(criteria.getAdministrariId(), root ->
                    root.join(Farmacist_.administraris, JoinType.LEFT).get(Administrare_.id)
                )
            );
        }
        return specification;
    }
}
