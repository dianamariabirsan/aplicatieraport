package com.example.healthapp.repository;

import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.domain.enumeration.SeveritateReactie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReactieAdversa entity.
 */
@Repository
public interface ReactieAdversaRepository extends JpaRepository<ReactieAdversa, Long>, JpaSpecificationExecutor<ReactieAdversa> {
    default Optional<ReactieAdversa> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ReactieAdversa> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ReactieAdversa> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select reactieAdversa from ReactieAdversa reactieAdversa left join fetch reactieAdversa.medicament left join fetch reactieAdversa.pacient",
        countQuery = "select count(reactieAdversa) from ReactieAdversa reactieAdversa"
    )
    Page<ReactieAdversa> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select reactieAdversa from ReactieAdversa reactieAdversa left join fetch reactieAdversa.medicament left join fetch reactieAdversa.pacient"
    )
    List<ReactieAdversa> findAllWithToOneRelationships();

    @Query(
        "select reactieAdversa from ReactieAdversa reactieAdversa left join fetch reactieAdversa.medicament left join fetch reactieAdversa.pacient where reactieAdversa.id =:id"
    )
    Optional<ReactieAdversa> findOneWithToOneRelationships(@Param("id") Long id);

    /**
     * Returns all severe adverse reactions for a patient, with medicament eagerly loaded.
     * Used by DecisionEngineService (P2 priority rule).
     */
    @Query("select r from ReactieAdversa r left join fetch r.medicament where r.pacient.id = :pacientId and r.severitate = :severitate")
    List<ReactieAdversa> findByPacientIdAndSeveritate(
        @Param("pacientId") Long pacientId,
        @Param("severitate") SeveritateReactie severitate
    );
}
