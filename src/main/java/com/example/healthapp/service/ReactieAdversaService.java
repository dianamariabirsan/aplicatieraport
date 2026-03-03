package com.example.healthapp.service;

import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
import com.example.healthapp.service.mapper.ReactieAdversaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.ReactieAdversa}.
 */
@Service
@Transactional
public class ReactieAdversaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReactieAdversaService.class);

    private final ReactieAdversaRepository reactieAdversaRepository;

    private final ReactieAdversaMapper reactieAdversaMapper;

    private final PacientRepository pacientRepository;

    private final MedicamentRepository medicamentRepository;

    public ReactieAdversaService(
        ReactieAdversaRepository reactieAdversaRepository,
        ReactieAdversaMapper reactieAdversaMapper,
        PacientRepository pacientRepository,
        MedicamentRepository medicamentRepository
    ) {
        this.reactieAdversaRepository = reactieAdversaRepository;
        this.reactieAdversaMapper = reactieAdversaMapper;
        this.pacientRepository = pacientRepository;
        this.medicamentRepository = medicamentRepository;
    }

    private void resolveRelationships(ReactieAdversa entity, ReactieAdversaDTO dto) {
        if (dto.getPacient() != null && dto.getPacient().getId() != null) {
            pacientRepository
                .findById(dto.getPacient().getId())
                .ifPresentOrElse(entity::setPacient, () ->
                    LOG.warn("resolveRelationships: Pacient id={} not found", dto.getPacient().getId())
                );
        }
        if (dto.getMedicament() != null && dto.getMedicament().getId() != null) {
            medicamentRepository
                .findById(dto.getMedicament().getId())
                .ifPresentOrElse(entity::setMedicament, () ->
                    LOG.warn("resolveRelationships: Medicament id={} not found", dto.getMedicament().getId())
                );
        }
    }

    /**
     * Save a reactieAdversa.
     *
     * @param reactieAdversaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReactieAdversaDTO save(ReactieAdversaDTO reactieAdversaDTO) {
        LOG.debug("Request to save ReactieAdversa : {}", reactieAdversaDTO);
        ReactieAdversa reactieAdversa = reactieAdversaMapper.toEntity(reactieAdversaDTO);
        resolveRelationships(reactieAdversa, reactieAdversaDTO);
        reactieAdversa = reactieAdversaRepository.save(reactieAdversa);
        return reactieAdversaMapper.toDto(reactieAdversa);
    }

    /**
     * Update a reactieAdversa.
     *
     * @param reactieAdversaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReactieAdversaDTO update(ReactieAdversaDTO reactieAdversaDTO) {
        LOG.debug("Request to update ReactieAdversa : {}", reactieAdversaDTO);
        ReactieAdversa reactieAdversa = reactieAdversaMapper.toEntity(reactieAdversaDTO);
        resolveRelationships(reactieAdversa, reactieAdversaDTO);
        reactieAdversa = reactieAdversaRepository.save(reactieAdversa);
        return reactieAdversaMapper.toDto(reactieAdversa);
    }

    /**
     * Partially update a reactieAdversa.
     *
     * @param reactieAdversaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReactieAdversaDTO> partialUpdate(ReactieAdversaDTO reactieAdversaDTO) {
        LOG.debug("Request to partially update ReactieAdversa : {}", reactieAdversaDTO);

        return reactieAdversaRepository
            .findById(reactieAdversaDTO.getId())
            .map(existingReactieAdversa -> {
                reactieAdversaMapper.partialUpdate(existingReactieAdversa, reactieAdversaDTO);

                return existingReactieAdversa;
            })
            .map(reactieAdversaRepository::save)
            .map(reactieAdversaMapper::toDto);
    }

    /**
     * Get all the reactieAdversas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReactieAdversaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reactieAdversaRepository.findAllWithEagerRelationships(pageable).map(reactieAdversaMapper::toDto);
    }

    /**
     * Get one reactieAdversa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReactieAdversaDTO> findOne(Long id) {
        LOG.debug("Request to get ReactieAdversa : {}", id);
        return reactieAdversaRepository.findOneWithEagerRelationships(id).map(reactieAdversaMapper::toDto);
    }

    /**
     * Delete the reactieAdversa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReactieAdversa : {}", id);
        reactieAdversaRepository.deleteById(id);
    }
}
