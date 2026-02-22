package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FarmacistDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FarmacistDTO.class);
        FarmacistDTO farmacistDTO1 = new FarmacistDTO();
        farmacistDTO1.setId(1L);
        FarmacistDTO farmacistDTO2 = new FarmacistDTO();
        assertThat(farmacistDTO1).isNotEqualTo(farmacistDTO2);
        farmacistDTO2.setId(farmacistDTO1.getId());
        assertThat(farmacistDTO1).isEqualTo(farmacistDTO2);
        farmacistDTO2.setId(2L);
        assertThat(farmacistDTO1).isNotEqualTo(farmacistDTO2);
        farmacistDTO1.setId(null);
        assertThat(farmacistDTO1).isNotEqualTo(farmacistDTO2);
    }
}
