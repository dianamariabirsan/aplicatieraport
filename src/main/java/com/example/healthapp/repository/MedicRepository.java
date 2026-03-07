package com.example.healthapp.repository;

import com.example.healthapp.domain.Medic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicRepository extends JpaRepository<Medic, Long>, JpaSpecificationExecutor<Medic> {
    Optional<Medic> findOneByEmailIgnoreCase(String email);
}
