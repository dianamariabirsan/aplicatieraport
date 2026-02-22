package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.PacientAsserts.*;
import static com.example.healthapp.domain.PacientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PacientMapperTest {

    private PacientMapper pacientMapper;

    @BeforeEach
    void setUp() {
        pacientMapper = new PacientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPacientSample1();
        var actual = pacientMapper.toEntity(pacientMapper.toDto(expected));
        assertPacientAllPropertiesEquals(expected, actual);
    }
}
