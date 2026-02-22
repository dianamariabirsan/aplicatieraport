package com.example.healthapp.repository;

import com.example.healthapp.domain.Pacient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pacient entity.
 */
@Repository
public interface PacientRepository extends JpaRepository<Pacient, Long>, JpaSpecificationExecutor<Pacient> {
    default Optional<Pacient> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pacient> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pacient> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pacient from Pacient pacient left join fetch pacient.medic left join fetch pacient.farmacist",
        countQuery = "select count(pacient) from Pacient pacient"
    )
    Page<Pacient> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pacient from Pacient pacient left join fetch pacient.medic left join fetch pacient.farmacist")
    List<Pacient> findAllWithToOneRelationships();

    @Query("select pacient from Pacient pacient left join fetch pacient.medic left join fetch pacient.farmacist where pacient.id =:id")
    Optional<Pacient> findOneWithToOneRelationships(@Param("id") Long id);
}
