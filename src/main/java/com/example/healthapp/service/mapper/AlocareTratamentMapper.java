package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlocareTratament} and its DTO {@link AlocareTratamentDTO}.
 */
/**
 * Mapper for the entity {@link AlocareTratament} and its DTO {@link AlocareTratamentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlocareTratamentMapper extends EntityMapper<AlocareTratamentDTO, AlocareTratament> {}
//@Mapper(componentModel = "spring")
//public interface AlocareTratamentMapper extends EntityMapper<AlocareTratamentDTO, AlocareTratament> {
//    @Mapping(target = "medic", source = "medic", qualifiedByName = "medicNume")
//    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "medicamentDenumire")
//    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientId")
//    AlocareTratamentDTO toDto(AlocareTratament s);
//
//    @Named("medicNume")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "nume", source = "nume")
//    MedicDTO toDtoMedicNume(Medic medic);
//
//    @Named("medicamentDenumire")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "denumire", source = "denumire")
//    MedicamentDTO toDtoMedicamentDenumire(Medicament medicament);
//
//    @Named("pacientId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    PacientDTO toDtoPacientId(Pacient pacient);
//}
