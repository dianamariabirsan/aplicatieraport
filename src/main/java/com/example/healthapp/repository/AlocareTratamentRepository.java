package com.example.healthapp.repository;

import com.example.healthapp.domain.AlocareTratament;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface AlocareTratamentRepository
    extends JpaRepository<AlocareTratament, Long>, JpaSpecificationExecutor<AlocareTratament> {

    Optional<AlocareTratament> findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(Long pacientId);

    Optional<AlocareTratament> findTopByPacientIdOrderByDataDecizieDesc(Long pacientId);

    Optional<AlocareTratament> findOneWithEagerRelationships(Long id);

    Page<AlocareTratament> findAllWithEagerRelationships(Pageable pageable);

    long countByMedicamentId(Long medicamentId);
}
