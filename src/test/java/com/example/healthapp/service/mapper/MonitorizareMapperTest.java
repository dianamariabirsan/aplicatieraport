package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.MonitorizareAsserts.*;
import static com.example.healthapp.domain.MonitorizareTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonitorizareMapperTest {

    private MonitorizareMapper monitorizareMapper;

    @BeforeEach
    void setUp() {
        monitorizareMapper = new MonitorizareMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMonitorizareSample1();
        var actual = monitorizareMapper.toEntity(monitorizareMapper.toDto(expected));
        assertMonitorizareAllPropertiesEquals(expected, actual);
    }
}
