package com.example.healthapp.service.mapper;

import com.example.healthapp.domain.Monitorizare;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.service.dto.MonitorizareDTO;
import com.example.healthapp.service.dto.PacientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Monitorizare} and its DTO {@link MonitorizareDTO}.
 */
@Mapper(componentModel = "spring", uses = { PacientMapper.class })
public interface MonitorizareMapper extends EntityMapper<MonitorizareDTO, Monitorizare> {
    @Mapping(target = "pacient", source = "pacient", qualifiedByName = "pacientId")
    MonitorizareDTO toDto(Monitorizare s);

    @Named("pacientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacientDTO toDtoPacientId(Pacient pacient);
}
