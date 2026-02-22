package com.example.healthapp.repository;

import com.example.healthapp.domain.RaportAnalitic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RaportAnalitic entity.
 */
@Repository
public interface RaportAnaliticRepository extends JpaRepository<RaportAnalitic, Long>, JpaSpecificationExecutor<RaportAnalitic> {
    default Optional<RaportAnalitic> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RaportAnalitic> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RaportAnalitic> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select raportAnalitic from RaportAnalitic raportAnalitic left join fetch raportAnalitic.medicament left join fetch raportAnalitic.medic",
        countQuery = "select count(raportAnalitic) from RaportAnalitic raportAnalitic"
    )
    Page<RaportAnalitic> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select raportAnalitic from RaportAnalitic raportAnalitic left join fetch raportAnalitic.medicament left join fetch raportAnalitic.medic"
    )
    List<RaportAnalitic> findAllWithToOneRelationships();

    @Query(
        "select raportAnalitic from RaportAnalitic raportAnalitic left join fetch raportAnalitic.medicament left join fetch raportAnalitic.medic where raportAnalitic.id =:id"
    )
    Optional<RaportAnalitic> findOneWithToOneRelationships(@Param("id") Long id);
}
