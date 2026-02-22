package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlocareTratamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlocareTratamentDTO.class);
        AlocareTratamentDTO alocareTratamentDTO1 = new AlocareTratamentDTO();
        alocareTratamentDTO1.setId(1L);
        AlocareTratamentDTO alocareTratamentDTO2 = new AlocareTratamentDTO();
        assertThat(alocareTratamentDTO1).isNotEqualTo(alocareTratamentDTO2);
        alocareTratamentDTO2.setId(alocareTratamentDTO1.getId());
        assertThat(alocareTratamentDTO1).isEqualTo(alocareTratamentDTO2);
        alocareTratamentDTO2.setId(2L);
        assertThat(alocareTratamentDTO1).isNotEqualTo(alocareTratamentDTO2);
        alocareTratamentDTO1.setId(null);
        assertThat(alocareTratamentDTO1).isNotEqualTo(alocareTratamentDTO2);
    }
}
