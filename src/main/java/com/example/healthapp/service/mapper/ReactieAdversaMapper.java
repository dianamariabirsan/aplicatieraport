package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReactieAdversa} and its DTO {@link ReactieAdversaDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, PacientMapper.class })
public interface ReactieAdversaMapper extends EntityMapper<ReactieAdversaDTO, ReactieAdversa> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentDenumire")
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientNumePrenume")
    ReactieAdversaDTO toDto(ReactieAdversa s);

    @Override
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "pacient", ignore = true)
    ReactieAdversa toEntity(ReactieAdversaDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "pacient", ignore = true)
    void partialUpdate(@MappingTarget ReactieAdversa entity, ReactieAdversaDTO dto);

    @Named("medicamentDenumire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentDenumire(Medicament medicament);

    @Named("pacientNumePrenume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    PacientDTO toDtoPacientNumePrenume(Pacient pacient);
}
