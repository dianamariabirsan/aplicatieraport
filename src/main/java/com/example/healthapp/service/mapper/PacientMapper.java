package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pacient} and its DTO {@link PacientDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicMapper.class, FarmacistMapper.class })
public interface PacientMapper extends EntityMapper<PacientDTO, Pacient> {
    @Mapping(target = "medic", source = "medic", qualifiedByName = "medicNume")
    @Mapping(target = "farmacist", source = "farmacist", qualifiedByName = "farmacistNume")
    PacientDTO toDto(Pacient s);

    @Override
    @Mapping(target = "alocaris", ignore = true)
    @Mapping(target = "removeAlocari", ignore = true)
    @Mapping(target = "reactiiAdverses", ignore = true)
    @Mapping(target = "removeReactiiAdverse", ignore = true)
    @Mapping(target = "monitorizaris", ignore = true)
    @Mapping(target = "removeMonitorizari", ignore = true)
    Pacient toEntity(PacientDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "alocaris", ignore = true)
    @Mapping(target = "removeAlocari", ignore = true)
    @Mapping(target = "reactiiAdverses", ignore = true)
    @Mapping(target = "removeReactiiAdverse", ignore = true)
    @Mapping(target = "monitorizaris", ignore = true)
    @Mapping(target = "removeMonitorizari", ignore = true)
    void partialUpdate(@MappingTarget Pacient entity, PacientDTO dto);

    @Named("medicNume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    MedicDTO toDtoMedicNume(Medic medic);

    @Named("farmacistNume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    FarmacistDTO toDtoFarmacistNume(Farmacist farmacist);
}
