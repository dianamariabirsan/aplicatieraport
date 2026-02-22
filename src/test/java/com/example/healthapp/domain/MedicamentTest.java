package com.example.healthapp.domain;

import static com.example.healthapp.domain.ExternalDrugInfoTestSamples.*;
import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static com.example.healthapp.domain.StudiiLiteraturaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicament.class);
        Medicament medicament1 = getMedicamentSample1();
        Medicament medicament2 = new Medicament();
        assertThat(medicament1).isNotEqualTo(medicament2);

        medicament2.setId(medicament1.getId());
        assertThat(medicament1).isEqualTo(medicament2);

        medicament2 = getMedicamentSample2();
        assertThat(medicament1).isNotEqualTo(medicament2);
    }

    @Test
    void infoExternTest() {
        Medicament medicament = getMedicamentRandomSampleGenerator();
        ExternalDrugInfo externalDrugInfoBack = getExternalDrugInfoRandomSampleGenerator();

        medicament.setInfoExtern(externalDrugInfoBack);
        assertThat(medicament.getInfoExtern()).isEqualTo(externalDrugInfoBack);

        medicament.infoExtern(null);
        assertThat(medicament.getInfoExtern()).isNull();
    }

    @Test
    void studiiTest() {
        Medicament medicament = getMedicamentRandomSampleGenerator();
        StudiiLiteratura studiiLiteraturaBack = getStudiiLiteraturaRandomSampleGenerator();

        medicament.addStudii(studiiLiteraturaBack);
        assertThat(medicament.getStudiis()).containsOnly(studiiLiteraturaBack);
        assertThat(studiiLiteraturaBack.getMedicament()).isEqualTo(medicament);

        medicament.removeStudii(studiiLiteraturaBack);
        assertThat(medicament.getStudiis()).doesNotContain(studiiLiteraturaBack);
        assertThat(studiiLiteraturaBack.getMedicament()).isNull();

        medicament.studiis(new HashSet<>(Set.of(studiiLiteraturaBack)));
        assertThat(medicament.getStudiis()).containsOnly(studiiLiteraturaBack);
        assertThat(studiiLiteraturaBack.getMedicament()).isEqualTo(medicament);

        medicament.setStudiis(new HashSet<>());
        assertThat(medicament.getStudiis()).doesNotContain(studiiLiteraturaBack);
        assertThat(studiiLiteraturaBack.getMedicament()).isNull();
    }
}
