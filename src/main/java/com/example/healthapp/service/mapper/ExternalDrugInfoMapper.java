package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExternalDrugInfo} and its DTO {@link ExternalDrugInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalDrugInfoMapper extends EntityMapper<ExternalDrugInfoDTO, ExternalDrugInfo> {}
