package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.service.dto.FarmacistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Farmacist} and its DTO {@link FarmacistDTO}.
 */
@Mapper(componentModel = "spring")
public interface FarmacistMapper extends EntityMapper<FarmacistDTO, Farmacist> {}
