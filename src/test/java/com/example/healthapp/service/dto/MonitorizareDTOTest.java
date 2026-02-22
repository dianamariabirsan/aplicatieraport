package com.example.healthapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitorizareDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonitorizareDTO.class);
        MonitorizareDTO monitorizareDTO1 = new MonitorizareDTO();
        monitorizareDTO1.setId(1L);
        MonitorizareDTO monitorizareDTO2 = new MonitorizareDTO();
        assertThat(monitorizareDTO1).isNotEqualTo(monitorizareDTO2);
        monitorizareDTO2.setId(monitorizareDTO1.getId());
        assertThat(monitorizareDTO1).isEqualTo(monitorizareDTO2);
        monitorizareDTO2.setId(2L);
        assertThat(monitorizareDTO1).isNotEqualTo(monitorizareDTO2);
        monitorizareDTO1.setId(null);
        assertThat(monitorizareDTO1).isNotEqualTo(monitorizareDTO2);
    }
}
