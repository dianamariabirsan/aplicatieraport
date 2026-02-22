package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudiiLiteraturaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudiiLiteraturaDTO.class);
        StudiiLiteraturaDTO studiiLiteraturaDTO1 = new StudiiLiteraturaDTO();
        studiiLiteraturaDTO1.setId(1L);
        StudiiLiteraturaDTO studiiLiteraturaDTO2 = new StudiiLiteraturaDTO();
        assertThat(studiiLiteraturaDTO1).isNotEqualTo(studiiLiteraturaDTO2);
        studiiLiteraturaDTO2.setId(studiiLiteraturaDTO1.getId());
        assertThat(studiiLiteraturaDTO1).isEqualTo(studiiLiteraturaDTO2);
        studiiLiteraturaDTO2.setId(2L);
        assertThat(studiiLiteraturaDTO1).isNotEqualTo(studiiLiteraturaDTO2);
        studiiLiteraturaDTO1.setId(null);
        assertThat(studiiLiteraturaDTO1).isNotEqualTo(studiiLiteraturaDTO2);
    }
}
