package com.example.healthapp.service;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.mapper.MedicamentMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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

    private final ExternalDrugInfoRepository externalDrugInfoRepository;

    private final ObjectMapper objectMapper;

    public MedicamentService(
        MedicamentRepository medicamentRepository,
        MedicamentMapper medicamentMapper,
        ExternalDrugInfoRepository externalDrugInfoRepository,
        ObjectMapper objectMapper
    ) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
        this.externalDrugInfoRepository = externalDrugInfoRepository;
        this.objectMapper = objectMapper;
    }

    private void resolveRelationships(Medicament entity, MedicamentDTO dto) {
        if (dto.getInfoExtern() != null && dto.getInfoExtern().getId() != null) {
            externalDrugInfoRepository
                .findById(dto.getInfoExtern().getId())
                .ifPresentOrElse(entity::setInfoExtern, () ->
                    LOG.warn("resolveRelationships: ExternalDrugInfo id={} not found", dto.getInfoExtern().getId())
                );
        }
    }

    /**
     * Populate Medicament clinical fields from the productSummary JSON stored in ExternalDrugInfo.
     * Expected JSON format: {"contraindicatii": [...], "interactiuni": [...], "avertizari": [...], "indicatii": [...]}
     * Package-private to allow direct access from {@link ExternalDrugInfoService} in the same package.
     *
     * @param medicament the Medicament entity to populate
     * @param externalDrugInfo the ExternalDrugInfo containing the productSummary JSON
     */
    public void populateMedicamentFromExternalInfo(Medicament medicament, ExternalDrugInfo externalDrugInfo) {
        if (medicament == null || externalDrugInfo == null) {
            return;
        }

        String productSummary = externalDrugInfo.getProductSummary();
        if (productSummary == null || productSummary.isBlank()) {
            return;
        }

        try {
            JsonNode root = objectMapper.readTree(productSummary);

            medicament.setIndicatii(joinSection(root, "indicatii"));
            medicament.setContraindicatii(joinSection(root, "contraindicatii"));
            medicament.setInteractiuni(joinSection(root, "interactiuni"));
            medicament.setAvertizari(joinSection(root, "avertizari"));
            medicament.setDozaRecomandata(joinSection(root, "dozaRecomandata"));
        } catch (Exception e) {
            LOG.warn(
                "populateMedicamentFromExternalInfo: invalid productSummary JSON for ExternalDrugInfo id={}",
                externalDrugInfo.getId(),
                e
            );
        }
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
        resolveRelationships(medicament, medicamentDTO);
        populateMedicamentFromExternalInfo(medicament, medicament.getInfoExtern());
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
        resolveRelationships(medicament, medicamentDTO);
        populateMedicamentFromExternalInfo(medicament, medicament.getInfoExtern());
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
                resolveRelationships(existingMedicament, medicamentDTO);
                populateMedicamentFromExternalInfo(existingMedicament, existingMedicament.getInfoExtern());
                return existingMedicament;
            })
            .map(medicamentRepository::save)
            .map(medicamentMapper::toDto);
    }

    private String joinSection(JsonNode root, String fieldName) {
        JsonNode node = root.path(fieldName);
        if (node.isMissingNode() || node.isNull()) {
            return null;
        }

        if (node.isArray()) {
            List<String> values = new ArrayList<>();
            node.forEach(item -> {
                if (!item.isNull()) {
                    String text = item.asText("").trim();
                    if (!text.isBlank()) {
                        values.add(text);
                    }
                }
            });
            return values.isEmpty() ? null : String.join("\n", values);
        }

        String value = node.asText("").trim();
        return value.isBlank() ? null : value;
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
