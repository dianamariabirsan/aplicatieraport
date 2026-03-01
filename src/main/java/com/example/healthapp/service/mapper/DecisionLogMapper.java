package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.dto.DecisionLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DecisionLog} and its DTO {@link DecisionLogDTO}.
 */
@Mapper(componentModel = "spring", uses = { AlocareTratamentMapper.class })
public interface DecisionLogMapper extends EntityMapper<DecisionLogDTO, DecisionLog> {
    @Mapping(target = "alocare", source = "alocare", qualifiedByName = "alocareTratamentId")
    DecisionLogDTO toDto(DecisionLog s);

    @Override
    @Mapping(target = "alocare", ignore = true)
    DecisionLog toEntity(DecisionLogDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "alocare", ignore = true)
    void partialUpdate(@MappingTarget DecisionLog entity, DecisionLogDTO dto);

    @Named("alocareTratamentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlocareTratamentDTO toDtoAlocareTratamentId(AlocareTratament alocareTratament);
}
