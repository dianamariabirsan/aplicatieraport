package com.example.healthapp.service;

import com.example.healthapp.domain.Medic;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.mapper.MedicMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Medic}.
 */
@Service
@Transactional
public class MedicService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicService.class);

    private final MedicRepository medicRepository;

    private final MedicMapper medicMapper;

    public MedicService(MedicRepository medicRepository, MedicMapper medicMapper) {
        this.medicRepository = medicRepository;
        this.medicMapper = medicMapper;
    }

    /**
     * Save a medic.
     *
     * @param medicDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicDTO save(MedicDTO medicDTO) {
        LOG.debug("Request to save Medic : {}", medicDTO);
        Medic medic = medicMapper.toEntity(medicDTO);
        medic = medicRepository.save(medic);
        return medicMapper.toDto(medic);
    }

    /**
     * Update a medic.
     *
     * @param medicDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicDTO update(MedicDTO medicDTO) {
        LOG.debug("Request to update Medic : {}", medicDTO);
        Medic medic = medicMapper.toEntity(medicDTO);
        medic = medicRepository.save(medic);
        return medicMapper.toDto(medic);
    }

    /**
     * Partially update a medic.
     *
     * @param medicDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicDTO> partialUpdate(MedicDTO medicDTO) {
        LOG.debug("Request to partially update Medic : {}", medicDTO);

        return medicRepository
            .findById(medicDTO.getId())
            .map(existingMedic -> {
                medicMapper.partialUpdate(existingMedic, medicDTO);

                return existingMedic;
            })
            .map(medicRepository::save)
            .map(medicMapper::toDto);
    }

    /**
     * Get one medic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicDTO> findOne(Long id) {
        LOG.debug("Request to get Medic : {}", id);
        return medicRepository.findById(id).map(medicMapper::toDto);
    }

    /**
     * Delete the medic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Medic : {}", id);
        medicRepository.deleteById(id);
    }
}
