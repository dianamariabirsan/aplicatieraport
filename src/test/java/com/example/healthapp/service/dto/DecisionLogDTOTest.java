package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DecisionLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecisionLogDTO.class);
        DecisionLogDTO decisionLogDTO1 = new DecisionLogDTO();
        decisionLogDTO1.setId(1L);
        DecisionLogDTO decisionLogDTO2 = new DecisionLogDTO();
        assertThat(decisionLogDTO1).isNotEqualTo(decisionLogDTO2);
        decisionLogDTO2.setId(decisionLogDTO1.getId());
        assertThat(decisionLogDTO1).isEqualTo(decisionLogDTO2);
        decisionLogDTO2.setId(2L);
        assertThat(decisionLogDTO1).isNotEqualTo(decisionLogDTO2);
        decisionLogDTO1.setId(null);
        assertThat(decisionLogDTO1).isNotEqualTo(decisionLogDTO2);
    }
}
