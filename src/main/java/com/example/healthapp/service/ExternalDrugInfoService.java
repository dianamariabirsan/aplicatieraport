package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.mapper.ExternalDrugInfoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.healthapp.domain.ExternalDrugInfo}.
 */
@Service
@Transactional
public class ExternalDrugInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalDrugInfoService.class);

    private final ExternalDrugInfoRepository externalDrugInfoRepository;

    private final ExternalDrugInfoMapper externalDrugInfoMapper;

    private final MedicamentRepository medicamentRepository;

    private final MedicamentService medicamentService;

    public ExternalDrugInfoService(
        ExternalDrugInfoRepository externalDrugInfoRepository,
        ExternalDrugInfoMapper externalDrugInfoMapper,
        MedicamentRepository medicamentRepository,
        MedicamentService medicamentService
    ) {
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.externalDrugInfoMapper = externalDrugInfoMapper;
        this.medicamentRepository = medicamentRepository;
        this.medicamentService = medicamentService;
    }

    /**
     * After saving ExternalDrugInfo, if the DTO carries a medicament reference,
     * load that Medicament, set its infoExtern FK to this ExternalDrugInfo and save it.
     * Then sync clinical fields from productSummary JSON.
     */
    private void linkAndSyncMedicament(ExternalDrugInfo savedExternal, ExternalDrugInfoDTO dto) {
        Long medicamentId = dto.getMedicament() != null ? dto.getMedicament().getId() : null;
        if (medicamentId != null) {
            medicamentRepository
                .findById(medicamentId)
                .ifPresent(medicament -> {
                    medicament.setInfoExtern(savedExternal);
                    medicamentService.populateMedicamentFromExternalInfo(medicament, savedExternal);
                    medicamentRepository.save(medicament);
                    LOG.debug(
                        "Linked and auto-populated Medicament id={} from ExternalDrugInfo id={}",
                        medicament.getId(),
                        savedExternal.getId()
                    );
                });
        } else {
            syncLinkedMedicament(savedExternal);
        }
    }

    /**
     * Sync the Medicament linked to this ExternalDrugInfo by populating its clinical fields
     * from the productSummary JSON. Called after every save/update of ExternalDrugInfo.
     */
    private void syncLinkedMedicament(ExternalDrugInfo externalDrugInfo) {
        if (externalDrugInfo.getId() == null) return;
        medicamentRepository
            .findOneByInfoExternId(externalDrugInfo.getId())
            .ifPresent(medicament -> {
                medicamentService.populateMedicamentFromExternalInfo(medicament, externalDrugInfo);
                medicamentRepository.save(medicament);
                LOG.debug("Auto-populated Medicament id={} from ExternalDrugInfo id={}", medicament.getId(), externalDrugInfo.getId());
            });
    }

    /**
     * Save a externalDrugInfo.
     *
     * @param externalDrugInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalDrugInfoDTO save(ExternalDrugInfoDTO externalDrugInfoDTO) {
        LOG.debug("Request to save ExternalDrugInfo : {}", externalDrugInfoDTO);
        ExternalDrugInfo externalDrugInfo = externalDrugInfoMapper.toEntity(externalDrugInfoDTO);
        externalDrugInfo = externalDrugInfoRepository.save(externalDrugInfo);
        linkAndSyncMedicament(externalDrugInfo, externalDrugInfoDTO);
        return externalDrugInfoMapper.toDto(externalDrugInfo);
    }

    /**
     * Update a externalDrugInfo.
     *
     * @param externalDrugInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public ExternalDrugInfoDTO update(ExternalDrugInfoDTO externalDrugInfoDTO) {
        LOG.debug("Request to update ExternalDrugInfo : {}", externalDrugInfoDTO);
        ExternalDrugInfo externalDrugInfo = externalDrugInfoMapper.toEntity(externalDrugInfoDTO);
        externalDrugInfo = externalDrugInfoRepository.save(externalDrugInfo);
        linkAndSyncMedicament(externalDrugInfo, externalDrugInfoDTO);
        return externalDrugInfoMapper.toDto(externalDrugInfo);
    }

    /**
     * Partially update a externalDrugInfo.
     *
     * @param externalDrugInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExternalDrugInfoDTO> partialUpdate(ExternalDrugInfoDTO externalDrugInfoDTO) {
        LOG.debug("Request to partially update ExternalDrugInfo : {}", externalDrugInfoDTO);

        return externalDrugInfoRepository
            .findById(externalDrugInfoDTO.getId())
            .map(existingExternalDrugInfo -> {
                externalDrugInfoMapper.partialUpdate(existingExternalDrugInfo, externalDrugInfoDTO);

                return existingExternalDrugInfo;
            })
            .map(externalDrugInfoRepository::save)
            .map(saved -> {
                linkAndSyncMedicament(saved, externalDrugInfoDTO);
                return saved;
            })
            .map(externalDrugInfoMapper::toDto);
    }

    /**
     *  Get all the externalDrugInfos where Medicament is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ExternalDrugInfoDTO> findAllWhereMedicamentIsNull() {
        LOG.debug("Request to get all externalDrugInfos where Medicament is null");
        return StreamSupport.stream(externalDrugInfoRepository.findAll().spliterator(), false)
            .filter(externalDrugInfo -> externalDrugInfo.getMedicament() == null)
            .map(externalDrugInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one externalDrugInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExternalDrugInfoDTO> findOne(Long id) {
        LOG.debug("Request to get ExternalDrugInfo : {}", id);
        return externalDrugInfoRepository.findById(id).map(externalDrugInfoMapper::toDto);
    }

    /**
     * Delete the externalDrugInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExternalDrugInfo : {}", id);
        externalDrugInfoRepository.deleteById(id);
    }
}
