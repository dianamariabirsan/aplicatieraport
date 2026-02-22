package com.example.healthapp.service;

import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.repository.RaportAnaliticRepository;
import com.example.healthapp.service.dto.RaportAnaliticDTO;
import com.example.healthapp.service.mapper.RaportAnaliticMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.RaportAnalitic}.
 */
@Service
@Transactional
public class RaportAnaliticService {

    private static final Logger LOG = LoggerFactory.getLogger(RaportAnaliticService.class);

    private final RaportAnaliticRepository raportAnaliticRepository;

    private final RaportAnaliticMapper raportAnaliticMapper;

    public RaportAnaliticService(RaportAnaliticRepository raportAnaliticRepository, RaportAnaliticMapper raportAnaliticMapper) {
        this.raportAnaliticRepository = raportAnaliticRepository;
        this.raportAnaliticMapper = raportAnaliticMapper;
    }

    /**
     * Save a raportAnalitic.
     *
     * @param raportAnaliticDTO the entity to save.
     * @return the persisted entity.
     */
    public RaportAnaliticDTO save(RaportAnaliticDTO raportAnaliticDTO) {
        LOG.debug("Request to save RaportAnalitic : {}", raportAnaliticDTO);
        RaportAnalitic raportAnalitic = raportAnaliticMapper.toEntity(raportAnaliticDTO);
        raportAnalitic = raportAnaliticRepository.save(raportAnalitic);
        return raportAnaliticMapper.toDto(raportAnalitic);
    }

    /**
     * Update a raportAnalitic.
     *
     * @param raportAnaliticDTO the entity to save.
     * @return the persisted entity.
     */
    public RaportAnaliticDTO update(RaportAnaliticDTO raportAnaliticDTO) {
        LOG.debug("Request to update RaportAnalitic : {}", raportAnaliticDTO);
        RaportAnalitic raportAnalitic = raportAnaliticMapper.toEntity(raportAnaliticDTO);
        raportAnalitic = raportAnaliticRepository.save(raportAnalitic);
        return raportAnaliticMapper.toDto(raportAnalitic);
    }

    /**
     * Partially update a raportAnalitic.
     *
     * @param raportAnaliticDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RaportAnaliticDTO> partialUpdate(RaportAnaliticDTO raportAnaliticDTO) {
        LOG.debug("Request to partially update RaportAnalitic : {}", raportAnaliticDTO);

        return raportAnaliticRepository
            .findById(raportAnaliticDTO.getId())
            .map(existingRaportAnalitic -> {
                raportAnaliticMapper.partialUpdate(existingRaportAnalitic, raportAnaliticDTO);

                return existingRaportAnalitic;
            })
            .map(raportAnaliticRepository::save)
            .map(raportAnaliticMapper::toDto);
    }

    /**
     * Get all the raportAnalitics with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RaportAnaliticDTO> findAllWithEagerRelationships(Pageable pageable) {
        return raportAnaliticRepository.findAllWithEagerRelationships(pageable).map(raportAnaliticMapper::toDto);
    }

    /**
     * Get one raportAnalitic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RaportAnaliticDTO> findOne(Long id) {
        LOG.debug("Request to get RaportAnalitic : {}", id);
        return raportAnaliticRepository.findOneWithEagerRelationships(id).map(raportAnaliticMapper::toDto);
    }

    /**
     * Delete the raportAnalitic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RaportAnalitic : {}", id);
        raportAnaliticRepository.deleteById(id);
    }
}
