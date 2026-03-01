package com.example.healthapp.repository;

import com.example.healthapp.domain.DecisionLog;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DecisionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecisionLogRepository extends JpaRepository<DecisionLog, Long>, JpaSpecificationExecutor<DecisionLog> {
    List<DecisionLog> findAllByAlocareIdOrderByTimestampDesc(Long alocareId);
}