package com.example.healthapp.service;

import com.example.healthapp.domain.*; // for static metamodels
import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.service.criteria.ExternalDrugInfoCriteria;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.mapper.ExternalDrugInfoMapper;
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
 * Service for executing complex queries for {@link ExternalDrugInfo} entities in the database.
 * The main input is a {@link ExternalDrugInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ExternalDrugInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExternalDrugInfoQueryService extends QueryService<ExternalDrugInfo> {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalDrugInfoQueryService.class);

    private final ExternalDrugInfoRepository externalDrugInfoRepository;

    private final ExternalDrugInfoMapper externalDrugInfoMapper;

    public ExternalDrugInfoQueryService(
        ExternalDrugInfoRepository externalDrugInfoRepository,
        ExternalDrugInfoMapper externalDrugInfoMapper
    ) {
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.externalDrugInfoMapper = externalDrugInfoMapper;
    }

    /**
     * Return a {@link Page} of {@link ExternalDrugInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExternalDrugInfoDTO> findByCriteria(ExternalDrugInfoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExternalDrugInfo> specification = createSpecification(criteria);
        return externalDrugInfoRepository.findAll(specification, page).map(externalDrugInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExternalDrugInfoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ExternalDrugInfo> specification = createSpecification(criteria);
        return externalDrugInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link ExternalDrugInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExternalDrugInfo> createSpecification(ExternalDrugInfoCriteria criteria) {
        Specification<ExternalDrugInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ExternalDrugInfo_.id),
                buildStringSpecification(criteria.getSource(), ExternalDrugInfo_.source),
                buildStringSpecification(criteria.getProductSummary(), ExternalDrugInfo_.productSummary),
                buildRangeSpecification(criteria.getLastUpdated(), ExternalDrugInfo_.lastUpdated),
                buildStringSpecification(criteria.getSourceUrl(), ExternalDrugInfo_.sourceUrl),
                buildSpecification(criteria.getMedicamentId(), root ->
                    root.join(ExternalDrugInfo_.medicament, JoinType.LEFT).get(Medicament_.id)
                )
            );
        }
        return specification;
    }
}
