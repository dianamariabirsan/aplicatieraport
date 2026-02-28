package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.AdministrareDTO;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrare} and its DTO {@link AdministrareDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacientMapper.class, FarmacistMapper.class })
public interface AdministrareMapper extends EntityMapper<AdministrareDTO, Administrare> {
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientNume")
    @Mapping(target = "farmacist", source = "farmacist", qualifiedByName = "farmacistId")
    AdministrareDTO toDto(Administrare s);

    @Named("pacientNume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    PacientDTO toDtoPacientNume(Pacient pacient);

    @Named("farmacistId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FarmacistDTO toDtoFarmacistId(Farmacist farmacist);
}
