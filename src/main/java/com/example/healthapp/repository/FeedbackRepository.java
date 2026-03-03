package com.example.healthapp.repository;

import com.example.healthapp.domain.Feedback;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
    default Optional<Feedback> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Feedback> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Feedback> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select feedback from Feedback feedback left join fetch feedback.alocare",
        countQuery = "select count(feedback) from Feedback feedback"
    )
    Page<Feedback> findAllWithToOneRelationships(Pageable pageable);

    @Query("select feedback from Feedback feedback left join fetch feedback.alocare")
    List<Feedback> findAllWithToOneRelationships();

    @Query("select feedback from Feedback feedback left join fetch feedback.alocare where feedback.id =:id")
    Optional<Feedback> findOneWithToOneRelationships(@Param("id") Long id);
}
