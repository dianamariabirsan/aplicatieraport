package com.example.healthapp.service;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.mapper.MedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.Medicament}.
 */
@Service
@Transactional
public class MedicamentService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepository;

    private final MedicamentMapper medicamentMapper;

    public MedicamentService(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
    }

    /**
     * Save a medicament.
     *
     * @param medicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicamentDTO save(MedicamentDTO medicamentDTO) {
        LOG.debug("Request to save Medicament : {}", medicamentDTO);
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        medicament = medicamentRepository.save(medicament);
        return medicamentMapper.toDto(medicament);
    }

    /**
     * Update a medicament.
     *
     * @param medicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicamentDTO update(MedicamentDTO medicamentDTO) {
        LOG.debug("Request to update Medicament : {}", medicamentDTO);
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        medicament = medicamentRepository.save(medicament);
        return medicamentMapper.toDto(medicament);
    }

    /**
     * Partially update a medicament.
     *
     * @param medicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicamentDTO> partialUpdate(MedicamentDTO medicamentDTO) {
        LOG.debug("Request to partially update Medicament : {}", medicamentDTO);

        return medicamentRepository
            .findById(medicamentDTO.getId())
            .map(existingMedicament -> {
                medicamentMapper.partialUpdate(existingMedicament, medicamentDTO);

                return existingMedicament;
            })
            .map(medicamentRepository::save)
            .map(medicamentMapper::toDto);
    }

    /**
     * Get one medicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicamentDTO> findOne(Long id) {
        LOG.debug("Request to get Medicament : {}", id);
        return medicamentRepository.findById(id).map(medicamentMapper::toDto);
    }

    /**
     * Delete the medicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Medicament : {}", id);
        medicamentRepository.deleteById(id);
    }
}
