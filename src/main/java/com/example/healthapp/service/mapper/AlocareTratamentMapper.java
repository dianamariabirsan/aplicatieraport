package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlocareTratament} and its DTO {@link AlocareTratamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicMapper.class, MedicamentMapper.class, PacientMapper.class })
public interface AlocareTratamentMapper extends EntityMapper<AlocareTratamentDTO, AlocareTratament> {
    @Mapping(target = "medic", source = "medic", qualifiedByName = "medicAlocare")
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentAlocare")
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientAlocare")
    AlocareTratamentDTO toDto(AlocareTratament s);

    @Override
    @Mapping(target = "deciziis", ignore = true)
    @Mapping(target = "removeDecizii", ignore = true)
    @Mapping(target = "feedbackuris", ignore = true)
    @Mapping(target = "removeFeedbackuri", ignore = true)
    @Mapping(target = "medic", ignore = true)
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "pacient", ignore = true)
    AlocareTratament toEntity(AlocareTratamentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "deciziis", ignore = true)
    @Mapping(target = "removeDecizii", ignore = true)
    @Mapping(target = "feedbackuris", ignore = true)
    @Mapping(target = "removeFeedbackuri", ignore = true)
    @Mapping(target = "medic", ignore = true)
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "pacient", ignore = true)
    void partialUpdate(@MappingTarget AlocareTratament entity, AlocareTratamentDTO dto);

    @Named("medicAlocare")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    @Mapping(target = "specializare", source = "specializare")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "telefon", source = "telefon")
    @Mapping(target = "cabinet", source = "cabinet")
    MedicDTO toDtoMedicAlocare(Medic medic);

    @Named("medicamentAlocare")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    @Mapping(target = "substanta", source = "substanta")
    @Mapping(target = "indicatii", source = "indicatii")
    @Mapping(target = "contraindicatii", source = "contraindicatii")
    @Mapping(target = "interactiuni", source = "interactiuni")
    @Mapping(target = "dozaRecomandata", source = "dozaRecomandata")
    @Mapping(target = "formaFarmaceutica", source = "formaFarmaceutica")
    MedicamentDTO toDtoMedicamentAlocare(Medicament medicament);

    @Named("pacientAlocare")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "varsta", source = "varsta")
    @Mapping(target = "greutate", source = "greutate")
    @Mapping(target = "inaltime", source = "inaltime")
    @Mapping(target = "circumferintaAbdominala", source = "circumferintaAbdominala")
    @Mapping(target = "cnp", source = "cnp")
    @Mapping(target = "comorbiditati", source = "comorbiditati")
    @Mapping(target = "gradSedentarism", source = "gradSedentarism")
    @Mapping(target = "istoricTratament", source = "istoricTratament")
    @Mapping(target = "toleranta", source = "toleranta")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "telefon", source = "telefon")
    PacientDTO toDtoPacientAlocare(Pacient pacient);
}
