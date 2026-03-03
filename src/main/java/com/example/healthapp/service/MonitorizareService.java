package com.example.healthapp.service;

import com.example.healthapp.domain.Monitorizare;
import com.example.healthapp.repository.MonitorizareRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.dto.MonitorizareDTO;
import com.example.healthapp.service.mapper.MonitorizareMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Monitorizare}.
 */
@Service
@Transactional
public class MonitorizareService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorizareService.class);

    private final MonitorizareRepository monitorizareRepository;

    private final MonitorizareMapper monitorizareMapper;

    private final PacientRepository pacientRepository;

    public MonitorizareService(
        MonitorizareRepository monitorizareRepository,
        MonitorizareMapper monitorizareMapper,
        PacientRepository pacientRepository
    ) {
        this.monitorizareRepository = monitorizareRepository;
        this.monitorizareMapper = monitorizareMapper;
        this.pacientRepository = pacientRepository;
    }

    private void resolveRelationships(Monitorizare entity, MonitorizareDTO dto) {
        if (dto.getPacient() != null && dto.getPacient().getId() != null) {
            pacientRepository
                .findById(dto.getPacient().getId())
                .ifPresentOrElse(entity::setPacient, () ->
                    LOG.warn("resolveRelationships: Pacient id={} not found", dto.getPacient().getId())
                );
        }
    }

    /**
     * Save a monitorizare.
     *
     * @param monitorizareDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitorizareDTO save(MonitorizareDTO monitorizareDTO) {
        LOG.debug("Request to save Monitorizare : {}", monitorizareDTO);
        Monitorizare monitorizare = monitorizareMapper.toEntity(monitorizareDTO);
        resolveRelationships(monitorizare, monitorizareDTO);
        monitorizare = monitorizareRepository.save(monitorizare);
        return monitorizareMapper.toDto(monitorizare);
    }

    /**
     * Update a monitorizare.
     *
     * @param monitorizareDTO the entity to save.
     * @return the persisted entity.
     */
    public MonitorizareDTO update(MonitorizareDTO monitorizareDTO) {
        LOG.debug("Request to update Monitorizare : {}", monitorizareDTO);
        Monitorizare monitorizare = monitorizareMapper.toEntity(monitorizareDTO);
        resolveRelationships(monitorizare, monitorizareDTO);
        monitorizare = monitorizareRepository.save(monitorizare);
        return monitorizareMapper.toDto(monitorizare);
    }

    /**
     * Partially update a monitorizare.
     *
     * @param monitorizareDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonitorizareDTO> partialUpdate(MonitorizareDTO monitorizareDTO) {
        LOG.debug("Request to partially update Monitorizare : {}", monitorizareDTO);

        return monitorizareRepository
            .findById(monitorizareDTO.getId())
            .map(existingMonitorizare -> {
                monitorizareMapper.partialUpdate(existingMonitorizare, monitorizareDTO);

                return existingMonitorizare;
            })
            .map(monitorizareRepository::save)
            .map(monitorizareMapper::toDto);
    }

    /**
     * Get one monitorizare by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonitorizareDTO> findOne(Long id) {
        LOG.debug("Request to get Monitorizare : {}", id);
        return monitorizareRepository.findOneWithEagerRelationships(id).map(monitorizareMapper::toDto);
    }

    /**
     * Delete the monitorizare by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Monitorizare : {}", id);
        monitorizareRepository.deleteById(id);
    }
}
