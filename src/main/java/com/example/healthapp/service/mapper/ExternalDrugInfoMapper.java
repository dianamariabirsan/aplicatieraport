package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link ExternalDrugInfo} and its DTO {@link ExternalDrugInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalDrugInfoMapper extends EntityMapper<ExternalDrugInfoDTO, ExternalDrugInfo> {
    @Override
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentSummary")
    ExternalDrugInfoDTO toDto(ExternalDrugInfo s);

    @Override
    @Mapping(target = "medicament", ignore = true)
    ExternalDrugInfo toEntity(ExternalDrugInfoDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicament", ignore = true)
    void partialUpdate(@MappingTarget ExternalDrugInfo entity, ExternalDrugInfoDTO dto);

    @Named("medicamentSummary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentSummary(Medicament medicament);
}
