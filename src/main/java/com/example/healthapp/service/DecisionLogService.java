package com.example.healthapp.service;

import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.service.mapper.DecisionLogMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read-only service for {@link com.example.healthapp.domain.DecisionLog}.
 * DecisionLog entries are created exclusively by {@link DecisionEngineService} — never via API.
 */
@Service
@Transactional(readOnly = true)
public class DecisionLogService {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionLogService.class);

    private final DecisionLogRepository decisionLogRepository;
    private final DecisionLogMapper decisionLogMapper;

    public DecisionLogService(DecisionLogRepository decisionLogRepository, DecisionLogMapper decisionLogMapper) {
        this.decisionLogRepository = decisionLogRepository;
        this.decisionLogMapper = decisionLogMapper;
    }

    /**
     * Get one decisionLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DecisionLogDTO> findOne(Long id) {
        LOG.debug("Request to get DecisionLog : {}", id);
        return decisionLogRepository.findById(id).map(decisionLogMapper::toDto);
    }

    /**
     * Get all decision logs for a given alocare, ordered by timestamp descending.
     *
     * @param alocareId the id of the alocareTratament.
     * @return the list of decision logs.
     */
    public List<DecisionLogDTO> findAllByAlocareId(Long alocareId) {
        LOG.debug("Request to get DecisionLogs by alocareId : {}", alocareId);
        return decisionLogRepository.findAllByAlocareIdOrderByTimestampDesc(alocareId).stream().map(decisionLogMapper::toDto).toList();
    }
}
