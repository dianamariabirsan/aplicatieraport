package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.FarmacistAsserts.*;
import static com.example.healthapp.domain.FarmacistTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FarmacistMapperTest {

    private FarmacistMapper farmacistMapper;

    @BeforeEach
    void setUp() {
        farmacistMapper = new FarmacistMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFarmacistSample1();
        var actual = farmacistMapper.toEntity(farmacistMapper.toDto(expected));
        assertFarmacistAllPropertiesEquals(expected, actual);
    }
}
