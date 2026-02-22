package com.example.healthapp.repository;

import com.example.healthapp.domain.Administrare;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Administrare entity.
 */
@Repository
public interface AdministrareRepository extends JpaRepository<Administrare, Long>, JpaSpecificationExecutor<Administrare> {
    default Optional<Administrare> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Administrare> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Administrare> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select administrare from Administrare administrare left join fetch administrare.pacient",
        countQuery = "select count(administrare) from Administrare administrare"
    )
    Page<Administrare> findAllWithToOneRelationships(Pageable pageable);

    @Query("select administrare from Administrare administrare left join fetch administrare.pacient")
    List<Administrare> findAllWithToOneRelationships();

    @Query("select administrare from Administrare administrare left join fetch administrare.pacient where administrare.id =:id")
    Optional<Administrare> findOneWithToOneRelationships(@Param("id") Long id);
}
