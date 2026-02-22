package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.ReactieAdversaAsserts.*;
import static com.example.healthapp.domain.ReactieAdversaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReactieAdversaMapperTest {

    private ReactieAdversaMapper reactieAdversaMapper;

    @BeforeEach
    void setUp() {
        reactieAdversaMapper = new ReactieAdversaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReactieAdversaSample1();
        var actual = reactieAdversaMapper.toEntity(reactieAdversaMapper.toDto(expected));
        assertReactieAdversaAllPropertiesEquals(expected, actual);
    }
}
