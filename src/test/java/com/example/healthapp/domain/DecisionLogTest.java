package com.example.healthapp.domain;

import static com.example.healthapp.domain.AlocareTratamentTestSamples.*;
import static com.example.healthapp.domain.DecisionLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecisionLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecisionLog.class);
        DecisionLog decisionLog1 = getDecisionLogSample1();
        DecisionLog decisionLog2 = new DecisionLog();
        assertThat(decisionLog1).isNotEqualTo(decisionLog2);

        decisionLog2.setId(decisionLog1.getId());
        assertThat(decisionLog1).isEqualTo(decisionLog2);

        decisionLog2 = getDecisionLogSample2();
        assertThat(decisionLog1).isNotEqualTo(decisionLog2);
    }

    @Test
    void alocareTest() {
        DecisionLog decisionLog = getDecisionLogRandomSampleGenerator();
        AlocareTratament alocareTratamentBack = getAlocareTratamentRandomSampleGenerator();

        decisionLog.setAlocare(alocareTratamentBack);
        assertThat(decisionLog.getAlocare()).isEqualTo(alocareTratamentBack);

        decisionLog.alocare(null);
        assertThat(decisionLog.getAlocare()).isNull();
    }
}
