package com.example.healthapp.repository;

import com.example.healthapp.domain.ExternalDrugInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExternalDrugInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExternalDrugInfoRepository extends JpaRepository<ExternalDrugInfo, Long>, JpaSpecificationExecutor<ExternalDrugInfo> {}
