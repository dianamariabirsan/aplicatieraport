package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AlocareTratament} and its DTO {@link AlocareTratamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicMapper.class, MedicamentMapper.class, PacientMapper.class })
public interface AlocareTratamentMapper extends EntityMapper<AlocareTratamentDTO, AlocareTratament> {
    @Override
    @Mapping(target = "deciziis", ignore = true)
    @Mapping(target = "removeDecizii", ignore = true)
    @Mapping(target = "feedbackuris", ignore = true)
    @Mapping(target = "removeFeedbackuri", ignore = true)
    AlocareTratament toEntity(AlocareTratamentDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "deciziis", ignore = true)
    @Mapping(target = "removeDecizii", ignore = true)
    @Mapping(target = "feedbackuris", ignore = true)
    @Mapping(target = "removeFeedbackuri", ignore = true)
    void partialUpdate(@MappingTarget AlocareTratament entity, AlocareTratamentDTO dto);
}
