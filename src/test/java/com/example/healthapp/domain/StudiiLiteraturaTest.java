package com.example.healthapp.domain;

import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static com.example.healthapp.domain.StudiiLiteraturaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudiiLiteraturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudiiLiteratura.class);
        StudiiLiteratura studiiLiteratura1 = getStudiiLiteraturaSample1();
        StudiiLiteratura studiiLiteratura2 = new StudiiLiteratura();
        assertThat(studiiLiteratura1).isNotEqualTo(studiiLiteratura2);

        studiiLiteratura2.setId(studiiLiteratura1.getId());
        assertThat(studiiLiteratura1).isEqualTo(studiiLiteratura2);

        studiiLiteratura2 = getStudiiLiteraturaSample2();
        assertThat(studiiLiteratura1).isNotEqualTo(studiiLiteratura2);
    }

    @Test
    void medicamentTest() {
        StudiiLiteratura studiiLiteratura = getStudiiLiteraturaRandomSampleGenerator();
        Medicament medicamentBack = getMedicamentRandomSampleGenerator();

        studiiLiteratura.setMedicament(medicamentBack);
        assertThat(studiiLiteratura.getMedicament()).isEqualTo(medicamentBack);

        studiiLiteratura.medicament(null);
        assertThat(studiiLiteratura.getMedicament()).isNull();
    }
}
