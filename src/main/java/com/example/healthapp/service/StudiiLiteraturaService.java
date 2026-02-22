package com.example.healthapp.service;

import com.example.healthapp.domain.StudiiLiteratura;
import com.example.healthapp.repository.StudiiLiteraturaRepository;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
import com.example.healthapp.service.mapper.StudiiLiteraturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.StudiiLiteratura}.
 */
@Service
@Transactional
public class StudiiLiteraturaService {

    private static final Logger LOG = LoggerFactory.getLogger(StudiiLiteraturaService.class);

    private final StudiiLiteraturaRepository studiiLiteraturaRepository;

    private final StudiiLiteraturaMapper studiiLiteraturaMapper;

    public StudiiLiteraturaService(StudiiLiteraturaRepository studiiLiteraturaRepository, StudiiLiteraturaMapper studiiLiteraturaMapper) {
        this.studiiLiteraturaRepository = studiiLiteraturaRepository;
        this.studiiLiteraturaMapper = studiiLiteraturaMapper;
    }

    /**
     * Save a studiiLiteratura.
     *
     * @param studiiLiteraturaDTO the entity to save.
     * @return the persisted entity.
     */
    public StudiiLiteraturaDTO save(StudiiLiteraturaDTO studiiLiteraturaDTO) {
        LOG.debug("Request to save StudiiLiteratura : {}", studiiLiteraturaDTO);
        StudiiLiteratura studiiLiteratura = studiiLiteraturaMapper.toEntity(studiiLiteraturaDTO);
        studiiLiteratura = studiiLiteraturaRepository.save(studiiLiteratura);
        return studiiLiteraturaMapper.toDto(studiiLiteratura);
    }

    /**
     * Update a studiiLiteratura.
     *
     * @param studiiLiteraturaDTO the entity to save.
     * @return the persisted entity.
     */
    public StudiiLiteraturaDTO update(StudiiLiteraturaDTO studiiLiteraturaDTO) {
        LOG.debug("Request to update StudiiLiteratura : {}", studiiLiteraturaDTO);
        StudiiLiteratura studiiLiteratura = studiiLiteraturaMapper.toEntity(studiiLiteraturaDTO);
        studiiLiteratura = studiiLiteraturaRepository.save(studiiLiteratura);
        return studiiLiteraturaMapper.toDto(studiiLiteratura);
    }

    /**
     * Partially update a studiiLiteratura.
     *
     * @param studiiLiteraturaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudiiLiteraturaDTO> partialUpdate(StudiiLiteraturaDTO studiiLiteraturaDTO) {
        LOG.debug("Request to partially update StudiiLiteratura : {}", studiiLiteraturaDTO);

        return studiiLiteraturaRepository
            .findById(studiiLiteraturaDTO.getId())
            .map(existingStudiiLiteratura -> {
                studiiLiteraturaMapper.partialUpdate(existingStudiiLiteratura, studiiLiteraturaDTO);

                return existingStudiiLiteratura;
            })
            .map(studiiLiteraturaRepository::save)
            .map(studiiLiteraturaMapper::toDto);
    }

    /**
     * Get one studiiLiteratura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudiiLiteraturaDTO> findOne(Long id) {
        LOG.debug("Request to get StudiiLiteratura : {}", id);
        return studiiLiteraturaRepository.findById(id).map(studiiLiteraturaMapper::toDto);
    }

    /**
     * Delete the studiiLiteratura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StudiiLiteratura : {}", id);
        studiiLiteraturaRepository.deleteById(id);
    }
}
