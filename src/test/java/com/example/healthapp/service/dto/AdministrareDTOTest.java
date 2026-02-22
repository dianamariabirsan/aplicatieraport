package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministrareDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministrareDTO.class);
        AdministrareDTO administrareDTO1 = new AdministrareDTO();
        administrareDTO1.setId(1L);
        AdministrareDTO administrareDTO2 = new AdministrareDTO();
        assertThat(administrareDTO1).isNotEqualTo(administrareDTO2);
        administrareDTO2.setId(administrareDTO1.getId());
        assertThat(administrareDTO1).isEqualTo(administrareDTO2);
        administrareDTO2.setId(2L);
        assertThat(administrareDTO1).isNotEqualTo(administrareDTO2);
        administrareDTO1.setId(null);
        assertThat(administrareDTO1).isNotEqualTo(administrareDTO2);
    }
}
