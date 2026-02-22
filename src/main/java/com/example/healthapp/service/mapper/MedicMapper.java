package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medic;
import com.example.healthapp.service.dto.MedicDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medic} and its DTO {@link MedicDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicMapper extends EntityMapper<MedicDTO, Medic> {}
