package com.example.healthapp.repository;

import com.example.healthapp.domain.Medic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicRepository extends JpaRepository<Medic, Long>, JpaSpecificationExecutor<Medic> {}
