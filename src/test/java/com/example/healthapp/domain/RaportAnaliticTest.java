package com.example.healthapp.domain;

import static com.example.healthapp.domain.MedicTestSamples.*;
import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static com.example.healthapp.domain.RaportAnaliticTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaportAnaliticTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaportAnalitic.class);
        RaportAnalitic raportAnalitic1 = getRaportAnaliticSample1();
        RaportAnalitic raportAnalitic2 = new RaportAnalitic();
        assertThat(raportAnalitic1).isNotEqualTo(raportAnalitic2);

        raportAnalitic2.setId(raportAnalitic1.getId());
        assertThat(raportAnalitic1).isEqualTo(raportAnalitic2);

        raportAnalitic2 = getRaportAnaliticSample2();
        assertThat(raportAnalitic1).isNotEqualTo(raportAnalitic2);
    }

    @Test
    void medicamentTest() {
        RaportAnalitic raportAnalitic = getRaportAnaliticRandomSampleGenerator();
        Medicament medicamentBack = getMedicamentRandomSampleGenerator();

        raportAnalitic.setMedicament(medicamentBack);
        assertThat(raportAnalitic.getMedicament()).isEqualTo(medicamentBack);

        raportAnalitic.medicament(null);
        assertThat(raportAnalitic.getMedicament()).isNull();
    }

    @Test
    void medicTest() {
        RaportAnalitic raportAnalitic = getRaportAnaliticRandomSampleGenerator();
        Medic medicBack = getMedicRandomSampleGenerator();

        raportAnalitic.setMedic(medicBack);
        assertThat(raportAnalitic.getMedic()).isEqualTo(medicBack);

        raportAnalitic.medic(null);
        assertThat(raportAnalitic.getMedic()).isNull();
    }
}
