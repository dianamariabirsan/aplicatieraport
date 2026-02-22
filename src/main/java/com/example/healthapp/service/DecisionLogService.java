package com.example.healthapp.service;

import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.service.mapper.DecisionLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.DecisionLog}.
 */
@Service
@Transactional
public class DecisionLogService {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionLogService.class);

    private final DecisionLogRepository decisionLogRepository;

    private final DecisionLogMapper decisionLogMapper;

    public DecisionLogService(DecisionLogRepository decisionLogRepository, DecisionLogMapper decisionLogMapper) {
        this.decisionLogRepository = decisionLogRepository;
        this.decisionLogMapper = decisionLogMapper;
    }

    /**
     * Save a decisionLog.
     *
     * @param decisionLogDTO the entity to save.
     * @return the persisted entity.
     */
    public DecisionLogDTO save(DecisionLogDTO decisionLogDTO) {
        LOG.debug("Request to save DecisionLog : {}", decisionLogDTO);
        DecisionLog decisionLog = decisionLogMapper.toEntity(decisionLogDTO);
        decisionLog = decisionLogRepository.save(decisionLog);
        return decisionLogMapper.toDto(decisionLog);
    }

    /**
     * Update a decisionLog.
     *
     * @param decisionLogDTO the entity to save.
     * @return the persisted entity.
     */
    public DecisionLogDTO update(DecisionLogDTO decisionLogDTO) {
        LOG.debug("Request to update DecisionLog : {}", decisionLogDTO);
        DecisionLog decisionLog = decisionLogMapper.toEntity(decisionLogDTO);
        decisionLog = decisionLogRepository.save(decisionLog);
        return decisionLogMapper.toDto(decisionLog);
    }

    /**
     * Partially update a decisionLog.
     *
     * @param decisionLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DecisionLogDTO> partialUpdate(DecisionLogDTO decisionLogDTO) {
        LOG.debug("Request to partially update DecisionLog : {}", decisionLogDTO);

        return decisionLogRepository
            .findById(decisionLogDTO.getId())
            .map(existingDecisionLog -> {
                decisionLogMapper.partialUpdate(existingDecisionLog, decisionLogDTO);

                return existingDecisionLog;
            })
            .map(decisionLogRepository::save)
            .map(decisionLogMapper::toDto);
    }

    /**
     * Get one decisionLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DecisionLogDTO> findOne(Long id) {
        LOG.debug("Request to get DecisionLog : {}", id);
        return decisionLogRepository.findById(id).map(decisionLogMapper::toDto);
    }

    /**
     * Delete the decisionLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DecisionLog : {}", id);
        decisionLogRepository.deleteById(id);
    }
}
