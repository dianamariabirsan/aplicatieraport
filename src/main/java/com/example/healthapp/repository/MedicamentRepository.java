package com.example.healthapp.repository;

import com.example.healthapp.domain.Medicament;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long>, JpaSpecificationExecutor<Medicament> {
    Optional<Medicament> findOneByDenumireIgnoreCase(String denumire);
}
