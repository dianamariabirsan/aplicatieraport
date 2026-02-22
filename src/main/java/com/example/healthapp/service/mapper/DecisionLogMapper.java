package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.DecisionLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DecisionLog} and its DTO {@link DecisionLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface DecisionLogMapper extends EntityMapper<DecisionLogDTO, DecisionLog> {
    @Mapping(target = "alocare", source = "alocare", qualifiedByName = "alocareTratamentId")
    DecisionLogDTO toDto(DecisionLog s);

    @Named("alocareTratamentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlocareTratamentDTO toDtoAlocareTratamentId(AlocareTratament alocareTratament);
}
