package com.example.healthapp.repository;

import com.example.healthapp.domain.Monitorizare;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Monitorizare entity.
 */
@Repository
public interface MonitorizareRepository extends JpaRepository<Monitorizare, Long>, JpaSpecificationExecutor<Monitorizare> {
    default Optional<Monitorizare> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Monitorizare> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Monitorizare> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select monitorizare from Monitorizare monitorizare left join fetch monitorizare.pacient",
        countQuery = "select count(monitorizare) from Monitorizare monitorizare"
    )
    Page<Monitorizare> findAllWithToOneRelationships(Pageable pageable);

    @Query("select monitorizare from Monitorizare monitorizare left join fetch monitorizare.pacient")
    List<Monitorizare> findAllWithToOneRelationships();

    @Query("select monitorizare from Monitorizare monitorizare left join fetch monitorizare.pacient where monitorizare.id =:id")
    Optional<Monitorizare> findOneWithToOneRelationships(@Param("id") Long id);
}
