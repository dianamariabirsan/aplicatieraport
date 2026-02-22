package com.example.healthapp.service;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.service.dto.AdministrareDTO;
import com.example.healthapp.service.mapper.AdministrareMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Administrare}.
 */
@Service
@Transactional
public class AdministrareService {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrareService.class);

    private final AdministrareRepository administrareRepository;

    private final AdministrareMapper administrareMapper;

    public AdministrareService(AdministrareRepository administrareRepository, AdministrareMapper administrareMapper) {
        this.administrareRepository = administrareRepository;
        this.administrareMapper = administrareMapper;
    }

    /**
     * Save a administrare.
     *
     * @param administrareDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministrareDTO save(AdministrareDTO administrareDTO) {
        LOG.debug("Request to save Administrare : {}", administrareDTO);
        Administrare administrare = administrareMapper.toEntity(administrareDTO);
        administrare = administrareRepository.save(administrare);
        return administrareMapper.toDto(administrare);
    }

    /**
     * Update a administrare.
     *
     * @param administrareDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministrareDTO update(AdministrareDTO administrareDTO) {
        LOG.debug("Request to update Administrare : {}", administrareDTO);
        Administrare administrare = administrareMapper.toEntity(administrareDTO);
        administrare = administrareRepository.save(administrare);
        return administrareMapper.toDto(administrare);
    }

    /**
     * Partially update a administrare.
     *
     * @param administrareDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministrareDTO> partialUpdate(AdministrareDTO administrareDTO) {
        LOG.debug("Request to partially update Administrare : {}", administrareDTO);

        return administrareRepository
            .findById(administrareDTO.getId())
            .map(existingAdministrare -> {
                administrareMapper.partialUpdate(existingAdministrare, administrareDTO);

                return existingAdministrare;
            })
            .map(administrareRepository::save)
            .map(administrareMapper::toDto);
    }

    /**
     * Get all the administrares with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AdministrareDTO> findAllWithEagerRelationships(Pageable pageable) {
        return administrareRepository.findAllWithEagerRelationships(pageable).map(administrareMapper::toDto);
    }

    /**
     * Get one administrare by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministrareDTO> findOne(Long id) {
        LOG.debug("Request to get Administrare : {}", id);
        return administrareRepository.findOneWithEagerRelationships(id).map(administrareMapper::toDto);
    }

    /**
     * Delete the administrare by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Administrare : {}", id);
        administrareRepository.deleteById(id);
    }
}
