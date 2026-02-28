package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicament} and its DTO {@link MedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ExternalDrugInfoMapper.class })
public interface MedicamentMapper extends EntityMapper<MedicamentDTO, Medicament> {
    @Mapping(target = "infoExtern", source = "infoExtern", qualifiedByName = "externalDrugInfoId")
    MedicamentDTO toDto(Medicament s);

    @Override
    @Mapping(target = "studiis", ignore = true)
    @Mapping(target = "removeStudii", ignore = true)
    Medicament toEntity(MedicamentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "studiis", ignore = true)
    @Mapping(target = "removeStudii", ignore = true)
    void partialUpdate(@MappingTarget Medicament entity, MedicamentDTO dto);

    @Named("externalDrugInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalDrugInfoDTO toDtoExternalDrugInfoId(ExternalDrugInfo externalDrugInfo);
}
