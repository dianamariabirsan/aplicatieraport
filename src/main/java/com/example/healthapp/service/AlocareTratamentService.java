package com.example.healthapp.service;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.mapper.AlocareTratamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.AlocareTratament}.
 */
@Service
@Transactional
public class AlocareTratamentService {

    private static final Logger LOG = LoggerFactory.getLogger(AlocareTratamentService.class);

    private final AlocareTratamentRepository alocareTratamentRepository;

    private final AlocareTratamentMapper alocareTratamentMapper;

    public AlocareTratamentService(AlocareTratamentRepository alocareTratamentRepository, AlocareTratamentMapper alocareTratamentMapper) {
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.alocareTratamentMapper = alocareTratamentMapper;
    }

    /**
     * Save a alocareTratament.
     *
     * @param alocareTratamentDTO the entity to save.
     * @return the persisted entity.
     */
    public AlocareTratamentDTO save(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to save AlocareTratament : {}", alocareTratamentDTO);
        AlocareTratament alocareTratament = alocareTratamentMapper.toEntity(alocareTratamentDTO);
        alocareTratament = alocareTratamentRepository.save(alocareTratament);
        return alocareTratamentMapper.toDto(alocareTratament);
    }

    /**
     * Update a alocareTratament.
     *
     * @param alocareTratamentDTO the entity to save.
     * @return the persisted entity.
     */
    public AlocareTratamentDTO update(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to update AlocareTratament : {}", alocareTratamentDTO);
        AlocareTratament alocareTratament = alocareTratamentMapper.toEntity(alocareTratamentDTO);
        alocareTratament = alocareTratamentRepository.save(alocareTratament);
        return alocareTratamentMapper.toDto(alocareTratament);
    }

    /**
     * Partially update a alocareTratament.
     *
     * @param alocareTratamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlocareTratamentDTO> partialUpdate(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to partially update AlocareTratament : {}", alocareTratamentDTO);

        return alocareTratamentRepository
            .findById(alocareTratamentDTO.getId())
            .map(existingAlocareTratament -> {
                alocareTratamentMapper.partialUpdate(existingAlocareTratament, alocareTratamentDTO);

                return existingAlocareTratament;
            })
            .map(alocareTratamentRepository::save)
            .map(alocareTratamentMapper::toDto);
    }

    /**
     * Get all the alocareTrataments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlocareTratamentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alocareTratamentRepository.findAllWithEagerRelationships(pageable).map(alocareTratamentMapper::toDto);
    }

    /**
     * Get one alocareTratament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlocareTratamentDTO> findOne(Long id) {
        LOG.debug("Request to get AlocareTratament : {}", id);
        return alocareTratamentRepository.findOneWithEagerRelationships(id).map(alocareTratamentMapper::toDto);
    }

    /**
     * Delete the alocareTratament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlocareTratament : {}", id);
        alocareTratamentRepository.deleteById(id);
    }
}
