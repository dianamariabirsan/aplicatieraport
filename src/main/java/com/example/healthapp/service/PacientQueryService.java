package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.criteria.PacientCriteria;
import com.example.healthapp.service.dto.PacientDTO;
import com.example.healthapp.service.mapper.PacientMapper;
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
 * Service for executing complex queries for {@link Pacient} entities in the database.
 * The main input is a {@link PacientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PacientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PacientQueryService extends QueryService<Pacient> {

    private static final Logger LOG = LoggerFactory.getLogger(PacientQueryService.class);

    private final PacientRepository pacientRepository;

    private final PacientMapper pacientMapper;

    public PacientQueryService(PacientRepository pacientRepository, PacientMapper pacientMapper) {
        this.pacientRepository = pacientRepository;
        this.pacientMapper = pacientMapper;
    }

    /**
     * Return a {@link Page} of {@link PacientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PacientDTO> findByCriteria(PacientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pacient> specification = createSpecification(criteria);
        return pacientRepository.findAll(specification, page).map(pacientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PacientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pacient> specification = createSpecification(criteria);
        return pacientRepository.count(specification);
    }

    /**
     * Function to convert {@link PacientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pacient> createSpecification(PacientCriteria criteria) {
        Specification<Pacient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Pacient_.id),
                buildStringSpecification(criteria.getNume(), Pacient_.nume),
                buildStringSpecification(criteria.getPrenume(), Pacient_.prenume),
                buildStringSpecification(criteria.getSex(), Pacient_.sex),
                buildRangeSpecification(criteria.getVarsta(), Pacient_.varsta),
                buildRangeSpecification(criteria.getGreutate(), Pacient_.greutate),
                buildRangeSpecification(criteria.getInaltime(), Pacient_.inaltime),
                buildRangeSpecification(criteria.getCircumferintaAbdominala(), Pacient_.circumferintaAbdominala),
                buildStringSpecification(criteria.getCnp(), Pacient_.cnp),
                buildStringSpecification(criteria.getComorbiditati(), Pacient_.comorbiditati),
                buildStringSpecification(criteria.getGradSedentarism(), Pacient_.gradSedentarism),
                buildStringSpecification(criteria.getIstoricTratament(), Pacient_.istoricTratament),
                buildStringSpecification(criteria.getToleranta(), Pacient_.toleranta),
                buildStringSpecification(criteria.getEmail(), Pacient_.email),
                buildStringSpecification(criteria.getTelefon(), Pacient_.telefon),
                buildSpecification(criteria.getAlocariId(), root -> root.join(Pacient_.alocaris, JoinType.LEFT).get(AlocareTratament_.id)),
                buildSpecification(criteria.getReactiiAdverseId(), root ->
                    root.join(Pacient_.reactiiAdverses, JoinType.LEFT).get(ReactieAdversa_.id)
                ),
                buildSpecification(criteria.getMonitorizariId(), root ->
                    root.join(Pacient_.monitorizaris, JoinType.LEFT).get(Monitorizare_.id)
                ),
                buildSpecification(criteria.getMedicId(), root -> root.join(Pacient_.medic, JoinType.LEFT).get(Medic_.id)),
                buildSpecification(criteria.getFarmacistId(), root -> root.join(Pacient_.farmacist, JoinType.LEFT).get(Farmacist_.id))
            );
        }
        return specification;
    }
}
