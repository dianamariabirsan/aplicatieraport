package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaportAnaliticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaportAnaliticDTO.class);
        RaportAnaliticDTO raportAnaliticDTO1 = new RaportAnaliticDTO();
        raportAnaliticDTO1.setId(1L);
        RaportAnaliticDTO raportAnaliticDTO2 = new RaportAnaliticDTO();
        assertThat(raportAnaliticDTO1).isNotEqualTo(raportAnaliticDTO2);
        raportAnaliticDTO2.setId(raportAnaliticDTO1.getId());
        assertThat(raportAnaliticDTO1).isEqualTo(raportAnaliticDTO2);
        raportAnaliticDTO2.setId(2L);
        assertThat(raportAnaliticDTO1).isNotEqualTo(raportAnaliticDTO2);
        raportAnaliticDTO1.setId(null);
        assertThat(raportAnaliticDTO1).isNotEqualTo(raportAnaliticDTO2);
    }
}
