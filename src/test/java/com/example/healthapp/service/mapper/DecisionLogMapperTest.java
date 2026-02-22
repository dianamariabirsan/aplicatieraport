package com.example.healthapp.service.mapper;

import static com.example.healthapp.domain.DecisionLogAsserts.*;
import static com.example.healthapp.domain.DecisionLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionLogMapperTest {

    private DecisionLogMapper decisionLogMapper;

    @BeforeEach
    void setUp() {
        decisionLogMapper = new DecisionLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDecisionLogSample1();
        var actual = decisionLogMapper.toEntity(decisionLogMapper.toDto(expected));
        assertDecisionLogAllPropertiesEquals(expected, actual);
    }
}
