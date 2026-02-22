package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.StudiiLiteratura;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudiiLiteratura} and its DTO {@link StudiiLiteraturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudiiLiteraturaMapper extends EntityMapper<StudiiLiteraturaDTO, StudiiLiteratura> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentId")
    StudiiLiteraturaDTO toDto(StudiiLiteratura s);

    @Named("medicamentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicamentDTO toDtoMedicamentId(Medicament medicament);
}
