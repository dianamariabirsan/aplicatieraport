package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.ExternalDrugInfoAsserts.*;
import static com.example.healthapp.domain.ExternalDrugInfoTestSamples.*;

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
}
