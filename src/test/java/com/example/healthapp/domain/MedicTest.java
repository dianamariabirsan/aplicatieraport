package com.example.healthapp.domain;

import static com.example.healthapp.domain.MedicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medic.class);
        Medic medic1 = getMedicSample1();
        Medic medic2 = new Medic();
        assertThat(medic1).isNotEqualTo(medic2);

        medic2.setId(medic1.getId());
        assertThat(medic1).isEqualTo(medic2);

        medic2 = getMedicSample2();
        assertThat(medic1).isNotEqualTo(medic2);
    }
}
