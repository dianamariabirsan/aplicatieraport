package com.example.healthapp.domain;

import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static com.example.healthapp.domain.PacientTestSamples.*;
import static com.example.healthapp.domain.ReactieAdversaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactieAdversaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReactieAdversa.class);
        ReactieAdversa reactieAdversa1 = getReactieAdversaSample1();
        ReactieAdversa reactieAdversa2 = new ReactieAdversa();
        assertThat(reactieAdversa1).isNotEqualTo(reactieAdversa2);

        reactieAdversa2.setId(reactieAdversa1.getId());
        assertThat(reactieAdversa1).isEqualTo(reactieAdversa2);

        reactieAdversa2 = getReactieAdversaSample2();
        assertThat(reactieAdversa1).isNotEqualTo(reactieAdversa2);
    }

    @Test
    void medicamentTest() {
        ReactieAdversa reactieAdversa = getReactieAdversaRandomSampleGenerator();
        Medicament medicamentBack = getMedicamentRandomSampleGenerator();

        reactieAdversa.setMedicament(medicamentBack);
        assertThat(reactieAdversa.getMedicament()).isEqualTo(medicamentBack);

        reactieAdversa.medicament(null);
        assertThat(reactieAdversa.getMedicament()).isNull();
    }

    @Test
    void pacientTest() {
        ReactieAdversa reactieAdversa = getReactieAdversaRandomSampleGenerator();
        Pacient pacientBack = getPacientRandomSampleGenerator();

        reactieAdversa.setPacient(pacientBack);
        assertThat(reactieAdversa.getPacient()).isEqualTo(pacientBack);

        reactieAdversa.pacient(null);
        assertThat(reactieAdversa.getPacient()).isNull();
    }
}
