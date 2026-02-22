package com.example.healthapp.domain;

import static com.example.healthapp.domain.MonitorizareTestSamples.*;
import static com.example.healthapp.domain.PacientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonitorizareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Monitorizare.class);
        Monitorizare monitorizare1 = getMonitorizareSample1();
        Monitorizare monitorizare2 = new Monitorizare();
        assertThat(monitorizare1).isNotEqualTo(monitorizare2);

        monitorizare2.setId(monitorizare1.getId());
        assertThat(monitorizare1).isEqualTo(monitorizare2);

        monitorizare2 = getMonitorizareSample2();
        assertThat(monitorizare1).isNotEqualTo(monitorizare2);
    }

    @Test
    void pacientTest() {
        Monitorizare monitorizare = getMonitorizareRandomSampleGenerator();
        Pacient pacientBack = getPacientRandomSampleGenerator();

        monitorizare.setPacient(pacientBack);
        assertThat(monitorizare.getPacient()).isEqualTo(pacientBack);

        monitorizare.pacient(null);
        assertThat(monitorizare.getPacient()).isNull();
    }
}
