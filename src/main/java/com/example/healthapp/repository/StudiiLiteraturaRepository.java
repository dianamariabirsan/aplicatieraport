package com.example.healthapp.repository;

import com.example.healthapp.domain.StudiiLiteratura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudiiLiteratura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudiiLiteraturaRepository extends JpaRepository<StudiiLiteratura, Long>, JpaSpecificationExecutor<StudiiLiteratura> {}
