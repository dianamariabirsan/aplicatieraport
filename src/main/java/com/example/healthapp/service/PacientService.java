package com.example.healthapp.service;

import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.dto.PacientDTO;
import com.example.healthapp.service.mapper.PacientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Pacient}.
 */
@Service
@Transactional
public class PacientService {

    private static final Logger LOG = LoggerFactory.getLogger(PacientService.class);

    private final PacientRepository pacientRepository;

    private final PacientMapper pacientMapper;

    public PacientService(PacientRepository pacientRepository, PacientMapper pacientMapper) {
        this.pacientRepository = pacientRepository;
        this.pacientMapper = pacientMapper;
    }

    /**
     * Save a pacient.
     *
     * @param pacientDTO the entity to save.
     * @return the persisted entity.
     */
    public PacientDTO save(PacientDTO pacientDTO) {
        LOG.debug("Request to save Pacient : {}", pacientDTO);
        Pacient pacient = pacientMapper.toEntity(pacientDTO);
        pacient = pacientRepository.save(pacient);
        return pacientMapper.toDto(pacient);
    }

    /**
     * Update a pacient.
     *
     * @param pacientDTO the entity to save.
     * @return the persisted entity.
     */
    public PacientDTO update(PacientDTO pacientDTO) {
        LOG.debug("Request to update Pacient : {}", pacientDTO);
        Pacient pacient = pacientMapper.toEntity(pacientDTO);
        pacient = pacientRepository.save(pacient);
        return pacientMapper.toDto(pacient);
    }

    /**
     * Partially update a pacient.
     *
     * @param pacientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PacientDTO> partialUpdate(PacientDTO pacientDTO) {
        LOG.debug("Request to partially update Pacient : {}", pacientDTO);

        return pacientRepository
            .findById(pacientDTO.getId())
            .map(existingPacient -> {
                pacientMapper.partialUpdate(existingPacient, pacientDTO);

                return existingPacient;
            })
            .map(pacientRepository::save)
            .map(pacientMapper::toDto);
    }

    /**
     * Get all the pacients with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PacientDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacientRepository.findAllWithEagerRelationships(pageable).map(pacientMapper::toDto);
    }

    /**
     * Get one pacient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PacientDTO> findOne(Long id) {
        LOG.debug("Request to get Pacient : {}", id);
        return pacientRepository.findOneWithEagerRelationships(id).map(pacientMapper::toDto);
    }

    /**
     * Delete the pacient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pacient : {}", id);
        pacientRepository.deleteById(id);
    }
}
