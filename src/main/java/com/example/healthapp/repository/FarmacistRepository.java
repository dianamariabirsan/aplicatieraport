package com.example.healthapp.repository;

import com.example.healthapp.domain.Farmacist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Farmacist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FarmacistRepository extends JpaRepository<Farmacist, Long>, JpaSpecificationExecutor<Farmacist> {}
