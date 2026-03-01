package com.example.healthapp.repository;

import com.example.healthapp.domain.AlocareTratament;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlocareTratamentRepository extends JpaRepository<AlocareTratament, Long>, JpaSpecificationExecutor<AlocareTratament> {
    Optional<AlocareTratament> findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(Long pacientId);

    Optional<AlocareTratament> findTopByPacientIdOrderByDataDecizieDesc(Long pacientId);

    default Optional<AlocareTratament> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default Page<AlocareTratament> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    default List<AlocareTratament> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    @Query(
        value = "select alocareTratament from AlocareTratament alocareTratament " +
        "left join fetch alocareTratament.medic " +
        "left join fetch alocareTratament.medicament",
        countQuery = "select count(alocareTratament) from AlocareTratament alocareTratament"
    )
    Page<AlocareTratament> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select alocareTratament from AlocareTratament alocareTratament " +
        "left join fetch alocareTratament.medic " +
        "left join fetch alocareTratament.medicament"
    )
    List<AlocareTratament> findAllWithToOneRelationships();

    @Query(
        "select alocareTratament from AlocareTratament alocareTratament " +
        "left join fetch alocareTratament.medic " +
        "left join fetch alocareTratament.medicament " +
        "where alocareTratament.id = :id"
    )
    Optional<AlocareTratament> findOneWithToOneRelationships(@Param("id") Long id);
}
//@Repository
//public interface AlocareTratamentRepository
//    extends JpaRepository<AlocareTratament, Long>, JpaSpecificationExecutor<AlocareTratament> {
//    Optional<AlocareTratament> findTopByPacientIdOrderByDataDecizieDesc(Long pacientId);
//
//    Optional<AlocareTratament> findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(Long pacientId);
//    Optional<AlocareTratament>
//    findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(Long pacientId);
//
//    Optional<AlocareTratament>
//    findTopByPacientIdOrderByDataDecizieDesc(Long pacientId);
//
//    Optional<AlocareTratament> findOneWithEagerRelationships(Long id);
//
//    Page<AlocareTratament> findAllWithEagerRelationships(Pageable pageable);
//}
/**
 * Spring Data JPA repository for the AlocareTratament entity.
 */
//@Repository
//public interface AlocareTratamentRepository extends JpaRepository<AlocareTratament, Long>, JpaSpecificationExecutor<AlocareTratament> {
//    default Optional<AlocareTratament> findOneWithEagerRelationships(Long id) {
//        return this.findOneWithToOneRelationships(id);
//    }
//
//    default List<AlocareTratament> findAllWithEagerRelationships() {
//        return this.findAllWithToOneRelationships();
//    }
//
//    default Page<AlocareTratament> findAllWithEagerRelationships(Pageable pageable) {
//        return this.findAllWithToOneRelationships(pageable);
//    }
//
//    @Query(
//        value = "select alocareTratament from AlocareTratament alocareTratament left join fetch alocareTratament.medic left join fetch alocareTratament.medicament",
//        countQuery = "select count(alocareTratament) from AlocareTratament alocareTratament"
//    )
//    Page<AlocareTratament> findAllWithToOneRelationships(Pageable pageable);
//
//    @Query(
//        "select alocareTratament from AlocareTratament alocareTratament left join fetch alocareTratament.medic left join fetch alocareTratament.medicament"
//    )
//    List<AlocareTratament> findAllWithToOneRelationships();
//
//    @Query(
//        "select alocareTratament from AlocareTratament alocareTratament left join fetch alocareTratament.medic left join fetch alocareTratament.medicament where alocareTratament.id =:id"
//    )
//    Optional<AlocareTratament> findOneWithToOneRelationships(@Param("id") Long id);
//
//    Optional<AlocareTratament> findTopByPacientIdOrderByDataDecizieDesc(Long pacientId);
//
//
//}
