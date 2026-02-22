package com.example.healthapp.service;

import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.repository.FarmacistRepository;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.mapper.FarmacistMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Farmacist}.
 */
@Service
@Transactional
public class FarmacistService {

    private static final Logger LOG = LoggerFactory.getLogger(FarmacistService.class);

    private final FarmacistRepository farmacistRepository;

    private final FarmacistMapper farmacistMapper;

    public FarmacistService(FarmacistRepository farmacistRepository, FarmacistMapper farmacistMapper) {
        this.farmacistRepository = farmacistRepository;
        this.farmacistMapper = farmacistMapper;
    }

    /**
     * Save a farmacist.
     *
     * @param farmacistDTO the entity to save.
     * @return the persisted entity.
     */
    public FarmacistDTO save(FarmacistDTO farmacistDTO) {
        LOG.debug("Request to save Farmacist : {}", farmacistDTO);
        Farmacist farmacist = farmacistMapper.toEntity(farmacistDTO);
        farmacist = farmacistRepository.save(farmacist);
        return farmacistMapper.toDto(farmacist);
    }

    /**
     * Update a farmacist.
     *
     * @param farmacistDTO the entity to save.
     * @return the persisted entity.
     */
    public FarmacistDTO update(FarmacistDTO farmacistDTO) {
        LOG.debug("Request to update Farmacist : {}", farmacistDTO);
        Farmacist farmacist = farmacistMapper.toEntity(farmacistDTO);
        farmacist = farmacistRepository.save(farmacist);
        return farmacistMapper.toDto(farmacist);
    }

    /**
     * Partially update a farmacist.
     *
     * @param farmacistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FarmacistDTO> partialUpdate(FarmacistDTO farmacistDTO) {
        LOG.debug("Request to partially update Farmacist : {}", farmacistDTO);

        return farmacistRepository
            .findById(farmacistDTO.getId())
            .map(existingFarmacist -> {
                farmacistMapper.partialUpdate(existingFarmacist, farmacistDTO);

                return existingFarmacist;
            })
            .map(farmacistRepository::save)
            .map(farmacistMapper::toDto);
    }

    /**
     * Get one farmacist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FarmacistDTO> findOne(Long id) {
        LOG.debug("Request to get Farmacist : {}", id);
        return farmacistRepository.findById(id).map(farmacistMapper::toDto);
    }

    /**
     * Delete the farmacist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Farmacist : {}", id);
        farmacistRepository.deleteById(id);
    }
}
