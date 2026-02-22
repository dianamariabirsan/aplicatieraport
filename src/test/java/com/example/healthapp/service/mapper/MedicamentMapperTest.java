package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.MedicamentAsserts.*;
import static com.example.healthapp.domain.MedicamentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicamentMapperTest {

    private MedicamentMapper medicamentMapper;

    @BeforeEach
    void setUp() {
        medicamentMapper = new MedicamentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicamentSample1();
        var actual = medicamentMapper.toEntity(medicamentMapper.toDto(expected));
        assertMedicamentAllPropertiesEquals(expected, actual);
    }
}
