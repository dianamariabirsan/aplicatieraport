package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalDrugInfoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalDrugInfoDTO.class);
        ExternalDrugInfoDTO externalDrugInfoDTO1 = new ExternalDrugInfoDTO();
        externalDrugInfoDTO1.setId(1L);
        ExternalDrugInfoDTO externalDrugInfoDTO2 = new ExternalDrugInfoDTO();
        assertThat(externalDrugInfoDTO1).isNotEqualTo(externalDrugInfoDTO2);
        externalDrugInfoDTO2.setId(externalDrugInfoDTO1.getId());
        assertThat(externalDrugInfoDTO1).isEqualTo(externalDrugInfoDTO2);
        externalDrugInfoDTO2.setId(2L);
        assertThat(externalDrugInfoDTO1).isNotEqualTo(externalDrugInfoDTO2);
        externalDrugInfoDTO1.setId(null);
        assertThat(externalDrugInfoDTO1).isNotEqualTo(externalDrugInfoDTO2);
    }
}
