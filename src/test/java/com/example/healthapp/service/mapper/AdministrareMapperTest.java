package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.AdministrareAsserts.*;
import static com.example.healthapp.domain.AdministrareTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdministrareMapperTest {

    private AdministrareMapper administrareMapper;

    @BeforeEach
    void setUp() {
        administrareMapper = new AdministrareMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdministrareSample1();
        var actual = administrareMapper.toEntity(administrareMapper.toDto(expected));
        assertAdministrareAllPropertiesEquals(expected, actual);
    }
}
