package com.example.healthapp.repository;

import com.example.healthapp.domain.Monitorizare;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Monitorizare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonitorizareRepository extends JpaRepository<Monitorizare, Long>, JpaSpecificationExecutor<Monitorizare> {}
