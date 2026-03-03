package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Feedback;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.FeedbackDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring", uses = { AlocareTratamentMapper.class })
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "alocare", source = "alocare", qualifiedByName = "alocareTratamentId")
    FeedbackDTO toDto(Feedback s);

    @Override
    @Mapping(target = "alocare", ignore = true)
    Feedback toEntity(FeedbackDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "alocare", ignore = true)
    void partialUpdate(@MappingTarget Feedback entity, FeedbackDTO dto);

    @Named("alocareTratamentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "tratamentPropus", source = "tratamentPropus")
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "alocarePacientLabel")
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "alocareMedicamentLabel")
    AlocareTratamentDTO toDtoAlocareTratamentId(AlocareTratament alocareTratament);

    @Named("alocarePacientLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    PacientDTO toDtoAlocarePacientLabel(Pacient pacient);

    @Named("alocareMedicamentLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoAlocareMedicamentLabel(Medicament medicament);
}
