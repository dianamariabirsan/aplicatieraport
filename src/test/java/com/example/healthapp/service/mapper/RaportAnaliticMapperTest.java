package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.RaportAnaliticAsserts.*;
import static com.example.healthapp.domain.RaportAnaliticTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RaportAnaliticMapperTest {

    private RaportAnaliticMapper raportAnaliticMapper;

    @BeforeEach
    void setUp() {
        raportAnaliticMapper = new RaportAnaliticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRaportAnaliticSample1();
        var actual = raportAnaliticMapper.toEntity(raportAnaliticMapper.toDto(expected));
        assertRaportAnaliticAllPropertiesEquals(expected, actual);
    }
}
