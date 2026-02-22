package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Medic;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.service.criteria.MedicCriteria;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.mapper.MedicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Medic} entities in the database.
 * The main input is a {@link MedicCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicQueryService extends QueryService<Medic> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicQueryService.class);

    private final MedicRepository medicRepository;

    private final MedicMapper medicMapper;

    public MedicQueryService(MedicRepository medicRepository, MedicMapper medicMapper) {
        this.medicRepository = medicRepository;
        this.medicMapper = medicMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicDTO> findByCriteria(MedicCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medic> specification = createSpecification(criteria);
        return medicRepository.findAll(specification, page).map(medicMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medic> specification = createSpecification(criteria);
        return medicRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medic> createSpecification(MedicCriteria criteria) {
        Specification<Medic> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Medic_.id),
                buildStringSpecification(criteria.getNume(), Medic_.nume),
                buildStringSpecification(criteria.getPrenume(), Medic_.prenume),
                buildStringSpecification(criteria.getSpecializare(), Medic_.specializare),
                buildStringSpecification(criteria.getEmail(), Medic_.email),
                buildStringSpecification(criteria.getTelefon(), Medic_.telefon),
                buildStringSpecification(criteria.getCabinet(), Medic_.cabinet)
            );
        }
        return specification;
    }
}
