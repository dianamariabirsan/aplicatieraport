package com.example.healthapp.service;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.PacientRepository;
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
 * Scorul și motivul sunt calculate automat de DecisionEngineService — NU se preiau din UI.
 */
@Service
@Transactional
public class AlocareTratamentService {

    private static final Logger LOG = LoggerFactory.getLogger(AlocareTratamentService.class);

    private final AlocareTratamentRepository alocareTratamentRepository;
    private final AlocareTratamentMapper alocareTratamentMapper;
    private final DecisionEngineService decisionEngineService;
    private final PacientRepository pacientRepository;
    private final MedicamentRepository medicamentRepository;
    private final MedicRepository medicRepository;

    public AlocareTratamentService(
        AlocareTratamentRepository alocareTratamentRepository,
        AlocareTratamentMapper alocareTratamentMapper,
        DecisionEngineService decisionEngineService,
        PacientRepository pacientRepository,
        MedicamentRepository medicamentRepository,
        MedicRepository medicRepository
    ) {
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.alocareTratamentMapper = alocareTratamentMapper;
        this.decisionEngineService = decisionEngineService;
        this.pacientRepository = pacientRepository;
        this.medicamentRepository = medicamentRepository;
        this.medicRepository = medicRepository;
    }

    /**
     * Resolve relationships ignored by the mapper (pacient, medicament, medic).
     * The mapper uses @Mapping(target="pacient", ignore=true) etc. to avoid accidental
     * overwrites, so we fetch them here from their IDs in the DTO.
     */
    private void resolveRelationships(AlocareTratament entity, AlocareTratamentDTO dto) {
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
        if (dto.getMedic() != null && dto.getMedic().getId() != null) {
            medicRepository
                .findById(dto.getMedic().getId())
                .ifPresentOrElse(entity::setMedic, () -> LOG.warn("resolveRelationships: Medic id={} not found", dto.getMedic().getId()));
        }
    }

    private AlocareTratament runDecisionEngine(AlocareTratament entity) {
        try {
            DecisionEngineService.DecisionResult result = decisionEngineService.evaluate(entity);

            entity.setScorDecizie(result.score());
            entity.setMotivDecizie(result.recomandare());
            entity = alocareTratamentRepository.save(entity);

            try {
                decisionEngineService.persistAudit(entity, result, ActorType.SISTEM_AI);
            } catch (Exception auditEx) {
                // Auditul nu blochează salvarea alocării — se loghează eroarea pentru diagnosticare
                LOG.error(
                    "persistAudit failed for alocare id={} (pacientId={}, medicamentId={})",
                    entity.getId(),
                    entity.getPacient() != null ? entity.getPacient().getId() : null,
                    entity.getMedicament() != null ? entity.getMedicament().getId() : null,
                    auditEx
                );
            }

            return entity;
        } catch (Exception e) {
            LOG.error(
                "runDecisionEngine failed for alocare id={} (pacientId={}, medicamentId={})",
                entity.getId(),
                entity.getPacient() != null ? entity.getPacient().getId() : null,
                entity.getMedicament() != null ? entity.getMedicament().getId() : null,
                e
            );
            throw e;
        }
    }

    public AlocareTratamentDTO save(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to save AlocareTratament : {}", alocareTratamentDTO);

        if (alocareTratamentDTO.getPacient() == null || alocareTratamentDTO.getPacient().getId() == null) {
            throw new IllegalArgumentException("Pacientul este obligatoriu pentru alocarea tratamentului.");
        }

        if (alocareTratamentDTO.getMedicament() == null || alocareTratamentDTO.getMedicament().getId() == null) {
            throw new IllegalArgumentException("Medicamentul este obligatoriu pentru alocarea tratamentului.");
        }

        AlocareTratament alocareTratament = alocareTratamentMapper.toEntity(alocareTratamentDTO);
        resolveRelationships(alocareTratament, alocareTratamentDTO);
        // First save to get an ID (needed for DecisionLog FK)
        alocareTratament = alocareTratamentRepository.save(alocareTratament);
        // Run decision engine and persist audit
        alocareTratament = runDecisionEngine(alocareTratament);
        return alocareTratamentMapper.toDto(alocareTratament);
    }

    public AlocareTratamentDTO update(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to update AlocareTratament : {}", alocareTratamentDTO);
        AlocareTratament alocareTratament = alocareTratamentMapper.toEntity(alocareTratamentDTO);
        resolveRelationships(alocareTratament, alocareTratamentDTO);
        alocareTratament = alocareTratamentRepository.save(alocareTratament);
        alocareTratament = runDecisionEngine(alocareTratament);
        return alocareTratamentMapper.toDto(alocareTratament);
    }

    public Optional<AlocareTratamentDTO> partialUpdate(AlocareTratamentDTO alocareTratamentDTO) {
        LOG.debug("Request to partially update AlocareTratament : {}", alocareTratamentDTO);

        return alocareTratamentRepository
            .findById(alocareTratamentDTO.getId())
            .map(existingAlocareTratament -> {
                alocareTratamentMapper.partialUpdate(existingAlocareTratament, alocareTratamentDTO);
                resolveRelationships(existingAlocareTratament, alocareTratamentDTO);
                return existingAlocareTratament;
            })
            .map(alocareTratamentRepository::save)
            .map(this::runDecisionEngine)
            .map(alocareTratamentMapper::toDto);
    }

    public Page<AlocareTratamentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alocareTratamentRepository.findAllWithEagerRelationships(pageable).map(alocareTratamentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AlocareTratamentDTO> findOne(Long id) {
        LOG.debug("Request to get AlocareTratament : {}", id);
        return alocareTratamentRepository.findOneWithEagerRelationships(id).map(alocareTratamentMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete AlocareTratament : {}", id);
        alocareTratamentRepository.deleteById(id);
    }

    @Transactional
    public Optional<AlocareTratamentDTO> reevaluate(Long id) {
        LOG.debug("Request to reevaluate AlocareTratament : {}", id);
        return alocareTratamentRepository
            .findOneWithEagerRelationships(id)
            .map(alocare -> {
                DecisionEngineService.DecisionResult result = decisionEngineService.evaluate(alocare);
                alocare.setScorDecizie(result.score());
                alocare.setMotivDecizie(result.recomandare());
                alocare = alocareTratamentRepository.save(alocare);
                decisionEngineService.persistAudit(alocare, result, ActorType.SISTEM_AI);
                return alocareTratamentMapper.toDto(alocare);
            });
    }
}
