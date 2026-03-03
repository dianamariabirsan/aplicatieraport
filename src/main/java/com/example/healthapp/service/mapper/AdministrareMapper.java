package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.AdministrareDTO;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrare} and its DTO {@link AdministrareDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacientMapper.class, FarmacistMapper.class, MedicamentMapper.class })
public interface AdministrareMapper extends EntityMapper<AdministrareDTO, Administrare> {
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientNume")
    @Mapping(target = "farmacist", source = "farmacist", qualifiedByName = "farmacistId")
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentAdminstrare")
    AdministrareDTO toDto(Administrare s);

    @Override
    @Mapping(target = "pacient", ignore = true)
    @Mapping(target = "farmacist", ignore = true)
    @Mapping(target = "medicament", ignore = true)
    Administrare toEntity(AdministrareDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pacient", ignore = true)
    @Mapping(target = "farmacist", ignore = true)
    @Mapping(target = "medicament", ignore = true)
    void partialUpdate(@MappingTarget Administrare entity, AdministrareDTO dto);

    @Named("pacientNume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    PacientDTO toDtoPacientNume(Pacient pacient);

    @Named("farmacistId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    @Mapping(target = "prenume", source = "prenume")
    FarmacistDTO toDtoFarmacistId(Farmacist farmacist);

    @Named("medicamentAdminstrare")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentAdminstrare(Medicament medicament);
}
