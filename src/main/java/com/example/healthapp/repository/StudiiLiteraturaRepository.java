package com.example.healthapp.repository;

import com.example.healthapp.domain.StudiiLiteratura;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudiiLiteratura entity.
 */
@Repository
public interface StudiiLiteraturaRepository extends JpaRepository<StudiiLiteratura, Long>, JpaSpecificationExecutor<StudiiLiteratura> {
    default Optional<StudiiLiteratura> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StudiiLiteratura> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StudiiLiteratura> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select studiiLiteratura from StudiiLiteratura studiiLiteratura left join fetch studiiLiteratura.medicament",
        countQuery = "select count(studiiLiteratura) from StudiiLiteratura studiiLiteratura"
    )
    Page<StudiiLiteratura> findAllWithToOneRelationships(Pageable pageable);

    @Query("select studiiLiteratura from StudiiLiteratura studiiLiteratura left join fetch studiiLiteratura.medicament")
    List<StudiiLiteratura> findAllWithToOneRelationships();

    @Query(
        "select studiiLiteratura from StudiiLiteratura studiiLiteratura left join fetch studiiLiteratura.medicament where studiiLiteratura.id =:id"
    )
    Optional<StudiiLiteratura> findOneWithToOneRelationships(@Param("id") Long id);
}
