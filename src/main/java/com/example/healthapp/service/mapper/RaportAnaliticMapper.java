package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.RaportAnaliticDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RaportAnalitic} and its DTO {@link RaportAnaliticDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, MedicMapper.class })
public interface RaportAnaliticMapper extends EntityMapper<RaportAnaliticDTO, RaportAnalitic> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentDenumire")
    @Mapping(target = "medic", source = "medic", qualifiedByName = "medicNume")
    RaportAnaliticDTO toDto(RaportAnalitic s);

    @Override
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "medic", ignore = true)
    RaportAnalitic toEntity(RaportAnaliticDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicament", ignore = true)
    @Mapping(target = "medic", ignore = true)
    void partialUpdate(@MappingTarget RaportAnalitic entity, RaportAnaliticDTO dto);

    @Named("medicamentDenumire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denumire", source = "denumire")
    MedicamentDTO toDtoMedicamentDenumire(Medicament medicament);

    @Named("medicNume")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nume", source = "nume")
    MedicDTO toDtoMedicNume(Medic medic);
}
