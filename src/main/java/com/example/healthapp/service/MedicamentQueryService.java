package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.criteria.MedicamentCriteria;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.mapper.MedicamentMapper;
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
 * Service for executing complex queries for {@link Medicament} entities in the database.
 * The main input is a {@link MedicamentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicamentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicamentQueryService extends QueryService<Medicament> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentQueryService.class);

    private final MedicamentRepository medicamentRepository;

    private final MedicamentMapper medicamentMapper;

    public MedicamentQueryService(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicamentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> findByCriteria(MedicamentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicament> specification = createSpecification(criteria);
        return medicamentRepository.findAll(specification, page).map(medicamentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicamentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medicament> specification = createSpecification(criteria);
        return medicamentRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicamentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medicament> createSpecification(MedicamentCriteria criteria) {
        Specification<Medicament> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Medicament_.id),
                buildStringSpecification(criteria.getDenumire(), Medicament_.denumire),
                buildStringSpecification(criteria.getSubstanta(), Medicament_.substanta),
                buildStringSpecification(criteria.getIndicatii(), Medicament_.indicatii),
                buildStringSpecification(criteria.getContraindicatii(), Medicament_.contraindicatii),
                buildStringSpecification(criteria.getInteractiuni(), Medicament_.interactiuni),
                buildStringSpecification(criteria.getDozaRecomandata(), Medicament_.dozaRecomandata),
                buildStringSpecification(criteria.getFormaFarmaceutica(), Medicament_.formaFarmaceutica),
                buildSpecification(criteria.getInfoExternId(), root ->
                    root.join(Medicament_.infoExtern, JoinType.LEFT).get(ExternalDrugInfo_.id)
                ),
                buildSpecification(criteria.getStudiiId(), root -> root.join(Medicament_.studiis, JoinType.LEFT).get(StudiiLiteratura_.id))
            );
        }
        return specification;
    }
}
