package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReactieAdversa} and its DTO {@link ReactieAdversaDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, PacientMapper.class })
public interface ReactieAdversaMapper extends EntityMapper<ReactieAdversaDTO, ReactieAdversa> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentDenumire")
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientId")
    ReactieAdversaDTO toDto(ReactieAdversa s);

    @Named("medicamentDenumire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentDenumire(Medicament medicament);

    @Named("pacientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacientDTO toDtoPacientId(Pacient pacient);
}
