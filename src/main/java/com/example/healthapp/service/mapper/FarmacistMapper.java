package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.service.dto.FarmacistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Farmacist} and its DTO {@link FarmacistDTO}.
 */
@Mapper(componentModel = "spring")
public interface FarmacistMapper extends EntityMapper<FarmacistDTO, Farmacist> {
    @Override
    @Mapping(target = "administraris", ignore = true)
    @Mapping(target = "removeAdministrari", ignore = true)
    Farmacist toEntity(FarmacistDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "administraris", ignore = true)
    @Mapping(target = "removeAdministrari", ignore = true)
    void partialUpdate(@MappingTarget Farmacist entity, FarmacistDTO dto);
}
