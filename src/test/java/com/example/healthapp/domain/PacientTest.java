package com.example.healthapp.domain;

import static com.example.healthapp.domain.AlocareTratamentTestSamples.*;
import static com.example.healthapp.domain.FarmacistTestSamples.*;
import static com.example.healthapp.domain.MedicTestSamples.*;
import static com.example.healthapp.domain.MonitorizareTestSamples.*;
import static com.example.healthapp.domain.PacientTestSamples.*;
import static com.example.healthapp.domain.ReactieAdversaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PacientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pacient.class);
        Pacient pacient1 = getPacientSample1();
        Pacient pacient2 = new Pacient();
        assertThat(pacient1).isNotEqualTo(pacient2);

        pacient2.setId(pacient1.getId());
        assertThat(pacient1).isEqualTo(pacient2);

        pacient2 = getPacientSample2();
        assertThat(pacient1).isNotEqualTo(pacient2);
    }

    @Test
    void alocariTest() {
        Pacient pacient = getPacientRandomSampleGenerator();
        AlocareTratament alocareTratamentBack = getAlocareTratamentRandomSampleGenerator();

        pacient.addAlocari(alocareTratamentBack);
        assertThat(pacient.getAlocaris()).containsOnly(alocareTratamentBack);
        assertThat(alocareTratamentBack.getPacient()).isEqualTo(pacient);

        pacient.removeAlocari(alocareTratamentBack);
        assertThat(pacient.getAlocaris()).doesNotContain(alocareTratamentBack);
        assertThat(alocareTratamentBack.getPacient()).isNull();

        pacient.alocaris(new HashSet<>(Set.of(alocareTratamentBack)));
        assertThat(pacient.getAlocaris()).containsOnly(alocareTratamentBack);
        assertThat(alocareTratamentBack.getPacient()).isEqualTo(pacient);

        pacient.setAlocaris(new HashSet<>());
        assertThat(pacient.getAlocaris()).doesNotContain(alocareTratamentBack);
        assertThat(alocareTratamentBack.getPacient()).isNull();
    }

    @Test
    void reactiiAdverseTest() {
        Pacient pacient = getPacientRandomSampleGenerator();
        ReactieAdversa reactieAdversaBack = getReactieAdversaRandomSampleGenerator();

        pacient.addReactiiAdverse(reactieAdversaBack);
        assertThat(pacient.getReactiiAdverses()).containsOnly(reactieAdversaBack);
        assertThat(reactieAdversaBack.getPacient()).isEqualTo(pacient);

        pacient.removeReactiiAdverse(reactieAdversaBack);
        assertThat(pacient.getReactiiAdverses()).doesNotContain(reactieAdversaBack);
        assertThat(reactieAdversaBack.getPacient()).isNull();

        pacient.reactiiAdverses(new HashSet<>(Set.of(reactieAdversaBack)));
        assertThat(pacient.getReactiiAdverses()).containsOnly(reactieAdversaBack);
        assertThat(reactieAdversaBack.getPacient()).isEqualTo(pacient);

        pacient.setReactiiAdverses(new HashSet<>());
        assertThat(pacient.getReactiiAdverses()).doesNotContain(reactieAdversaBack);
        assertThat(reactieAdversaBack.getPacient()).isNull();
    }

    @Test
    void monitorizariTest() {
        Pacient pacient = getPacientRandomSampleGenerator();
        Monitorizare monitorizareBack = getMonitorizareRandomSampleGenerator();

        pacient.addMonitorizari(monitorizareBack);
        assertThat(pacient.getMonitorizaris()).containsOnly(monitorizareBack);
        assertThat(monitorizareBack.getPacient()).isEqualTo(pacient);

        pacient.removeMonitorizari(monitorizareBack);
        assertThat(pacient.getMonitorizaris()).doesNotContain(monitorizareBack);
        assertThat(monitorizareBack.getPacient()).isNull();

        pacient.monitorizaris(new HashSet<>(Set.of(monitorizareBack)));
        assertThat(pacient.getMonitorizaris()).containsOnly(monitorizareBack);
        assertThat(monitorizareBack.getPacient()).isEqualTo(pacient);

        pacient.setMonitorizaris(new HashSet<>());
        assertThat(pacient.getMonitorizaris()).doesNotContain(monitorizareBack);
        assertThat(monitorizareBack.getPacient()).isNull();
    }

    @Test
    void medicTest() {
        Pacient pacient = getPacientRandomSampleGenerator();
        Medic medicBack = getMedicRandomSampleGenerator();

        pacient.setMedic(medicBack);
        assertThat(pacient.getMedic()).isEqualTo(medicBack);

        pacient.medic(null);
        assertThat(pacient.getMedic()).isNull();
    }

    @Test
    void farmacistTest() {
        Pacient pacient = getPacientRandomSampleGenerator();
        Farmacist farmacistBack = getFarmacistRandomSampleGenerator();

        pacient.setFarmacist(farmacistBack);
        assertThat(pacient.getFarmacist()).isEqualTo(farmacistBack);

        pacient.farmacist(null);
        assertThat(pacient.getFarmacist()).isNull();
    }
}
