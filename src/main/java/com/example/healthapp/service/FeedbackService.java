package com.example.healthapp.service;

import com.example.healthapp.domain.Feedback;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.FeedbackRepository;
import com.example.healthapp.service.dto.FeedbackDTO;
import com.example.healthapp.service.mapper.FeedbackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Feedback}.
 */
@Service
@Transactional
public class FeedbackService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final AlocareTratamentRepository alocareTratamentRepository;

    public FeedbackService(
        FeedbackRepository feedbackRepository,
        FeedbackMapper feedbackMapper,
        AlocareTratamentRepository alocareTratamentRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.alocareTratamentRepository = alocareTratamentRepository;
    }

    private void resolveRelationships(Feedback entity, FeedbackDTO dto) {
        if (dto.getAlocare() != null && dto.getAlocare().getId() != null) {
            alocareTratamentRepository
                .findById(dto.getAlocare().getId())
                .ifPresentOrElse(entity::setAlocare, () ->
                    LOG.warn("resolveRelationships: AlocareTratament id={} not found", dto.getAlocare().getId())
                );
        }
    }

    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        resolveRelationships(feedback, feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    /**
     * Update a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    public FeedbackDTO update(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to update Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        resolveRelationships(feedback, feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    /**
     * Partially update a feedback.
     *
     * @param feedbackDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                feedbackMapper.partialUpdate(existingFeedback, feedbackDTO);

                return existingFeedback;
            })
            .map(feedbackRepository::save)
            .map(feedbackMapper::toDto);
    }

    /**
     * Get one feedback by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeedbackDTO> findOne(Long id) {
        LOG.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findOneWithEagerRelationships(id).map(feedbackMapper::toDto);
    }

    /**
     * Delete the feedback by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
    }
}
