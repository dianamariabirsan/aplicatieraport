package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.StudiiLiteratura;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudiiLiteratura} and its DTO {@link StudiiLiteraturaDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class })
public interface StudiiLiteraturaMapper extends EntityMapper<StudiiLiteraturaDTO, StudiiLiteratura> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentId")
    StudiiLiteraturaDTO toDto(StudiiLiteratura s);

    @Override
    @Mapping(target = "medicament", ignore = true)
    StudiiLiteratura toEntity(StudiiLiteraturaDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicament", ignore = true)
    void partialUpdate(@MappingTarget StudiiLiteratura entity, StudiiLiteraturaDTO dto);

    @Named("medicamentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentId(Medicament medicament);
}
