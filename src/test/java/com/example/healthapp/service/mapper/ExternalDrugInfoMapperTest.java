package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.ExternalDrugInfoAsserts.*;
import static com.example.healthapp.domain.ExternalDrugInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.domain.Medicament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExternalDrugInfoMapperTest {

    private ExternalDrugInfoMapper externalDrugInfoMapper;

    @BeforeEach
    void setUp() {
        externalDrugInfoMapper = new ExternalDrugInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExternalDrugInfoSample1();
        var actual = externalDrugInfoMapper.toEntity(externalDrugInfoMapper.toDto(expected));
        assertExternalDrugInfoAllPropertiesEquals(expected, actual);
    }

    @Test
    void shouldMapMedicamentSummaryWithName() {
        var entity = getExternalDrugInfoSample1();
        entity.setMedicament(new Medicament().id(7L).denumire("Paracetamol"));

        var dto = externalDrugInfoMapper.toDto(entity);

        assertThat(dto.getMedicament()).isNotNull();
        assertThat(dto.getMedicament().getId()).isEqualTo(7L);
        assertThat(dto.getMedicament().getDenumire()).isEqualTo("Paracetamol");
    }
}
