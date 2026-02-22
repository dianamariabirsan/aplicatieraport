package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicament} and its DTO {@link MedicamentDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicamentMapper extends EntityMapper<MedicamentDTO, Medicament> {
    @Mapping(target = "infoExtern", source = "infoExtern", qualifiedByName = "externalDrugInfoId")
    MedicamentDTO toDto(Medicament s);

    @Named("externalDrugInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExternalDrugInfoDTO toDtoExternalDrugInfoId(ExternalDrugInfo externalDrugInfo);
}
