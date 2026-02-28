package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExternalDrugInfo} and its DTO {@link ExternalDrugInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalDrugInfoMapper extends EntityMapper<ExternalDrugInfoDTO, ExternalDrugInfo> {
    @Override
    @Mapping(target = "medicament", ignore = true)
    ExternalDrugInfo toEntity(ExternalDrugInfoDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicament", ignore = true)
    void partialUpdate(@MappingTarget ExternalDrugInfo entity, ExternalDrugInfoDTO dto);
}
