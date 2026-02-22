package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactieAdversaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReactieAdversaDTO.class);
        ReactieAdversaDTO reactieAdversaDTO1 = new ReactieAdversaDTO();
        reactieAdversaDTO1.setId(1L);
        ReactieAdversaDTO reactieAdversaDTO2 = new ReactieAdversaDTO();
        assertThat(reactieAdversaDTO1).isNotEqualTo(reactieAdversaDTO2);
        reactieAdversaDTO2.setId(reactieAdversaDTO1.getId());
        assertThat(reactieAdversaDTO1).isEqualTo(reactieAdversaDTO2);
        reactieAdversaDTO2.setId(2L);
        assertThat(reactieAdversaDTO1).isNotEqualTo(reactieAdversaDTO2);
        reactieAdversaDTO1.setId(null);
        assertThat(reactieAdversaDTO1).isNotEqualTo(reactieAdversaDTO2);
    }
}
