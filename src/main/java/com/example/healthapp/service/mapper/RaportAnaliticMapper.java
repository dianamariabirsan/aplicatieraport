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
@Mapper(componentModel = "spring")
public interface RaportAnaliticMapper extends EntityMapper<RaportAnaliticDTO, RaportAnalitic> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentDenumire")
    @Mapping(target = "medic", source = "medic", qualifiedByName = "medicNume")
    RaportAnaliticDTO toDto(RaportAnalitic s);

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
