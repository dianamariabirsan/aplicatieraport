package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.AlocareTratamentAsserts.*;
import static com.example.healthapp.domain.AlocareTratamentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlocareTratamentMapperTest {

    private AlocareTratamentMapper alocareTratamentMapper;

    @BeforeEach
    void setUp() {
        alocareTratamentMapper = new AlocareTratamentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlocareTratamentSample1();
        var actual = alocareTratamentMapper.toEntity(alocareTratamentMapper.toDto(expected));
        assertAlocareTratamentAllPropertiesEquals(expected, actual);
    }
}
