package com.example.healthapp.domain;

import static com.example.healthapp.domain.AdministrareTestSamples.*;
import static com.example.healthapp.domain.FarmacistTestSamples.*;
import static com.example.healthapp.domain.PacientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministrareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Administrare.class);
        Administrare administrare1 = getAdministrareSample1();
        Administrare administrare2 = new Administrare();
        assertThat(administrare1).isNotEqualTo(administrare2);

        administrare2.setId(administrare1.getId());
        assertThat(administrare1).isEqualTo(administrare2);

        administrare2 = getAdministrareSample2();
        assertThat(administrare1).isNotEqualTo(administrare2);
    }

    @Test
    void pacientTest() {
        Administrare administrare = getAdministrareRandomSampleGenerator();
        Pacient pacientBack = getPacientRandomSampleGenerator();

        administrare.setPacient(pacientBack);
        assertThat(administrare.getPacient()).isEqualTo(pacientBack);

        administrare.pacient(null);
        assertThat(administrare.getPacient()).isNull();
    }

    @Test
    void farmacistTest() {
        Administrare administrare = getAdministrareRandomSampleGenerator();
        Farmacist farmacistBack = getFarmacistRandomSampleGenerator();

        administrare.setFarmacist(farmacistBack);
        assertThat(administrare.getFarmacist()).isEqualTo(farmacistBack);

        administrare.farmacist(null);
        assertThat(administrare.getFarmacist()).isNull();
    }
}
