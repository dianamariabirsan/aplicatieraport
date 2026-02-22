package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.StudiiLiteraturaAsserts.*;
import static com.example.healthapp.domain.StudiiLiteraturaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudiiLiteraturaMapperTest {

    private StudiiLiteraturaMapper studiiLiteraturaMapper;

    @BeforeEach
    void setUp() {
        studiiLiteraturaMapper = new StudiiLiteraturaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudiiLiteraturaSample1();
        var actual = studiiLiteraturaMapper.toEntity(studiiLiteraturaMapper.toDto(expected));
        assertStudiiLiteraturaAllPropertiesEquals(expected, actual);
    }
}
