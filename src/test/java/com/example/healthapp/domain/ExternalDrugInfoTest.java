package com.example.healthapp.domain;

import static com.example.healthapp.domain.ExternalDrugInfoTestSamples.*;
import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalDrugInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalDrugInfo.class);
        ExternalDrugInfo externalDrugInfo1 = getExternalDrugInfoSample1();
        ExternalDrugInfo externalDrugInfo2 = new ExternalDrugInfo();
        assertThat(externalDrugInfo1).isNotEqualTo(externalDrugInfo2);

        externalDrugInfo2.setId(externalDrugInfo1.getId());
        assertThat(externalDrugInfo1).isEqualTo(externalDrugInfo2);

        externalDrugInfo2 = getExternalDrugInfoSample2();
        assertThat(externalDrugInfo1).isNotEqualTo(externalDrugInfo2);
    }

    @Test
    void medicamentTest() {
        ExternalDrugInfo externalDrugInfo = getExternalDrugInfoRandomSampleGenerator();
        Medicament medicamentBack = getMedicamentRandomSampleGenerator();

        externalDrugInfo.setMedicament(medicamentBack);
        assertThat(externalDrugInfo.getMedicament()).isEqualTo(medicamentBack);
        assertThat(medicamentBack.getInfoExtern()).isEqualTo(externalDrugInfo);

        externalDrugInfo.medicament(null);
        assertThat(externalDrugInfo.getMedicament()).isNull();
        assertThat(medicamentBack.getInfoExtern()).isNull();
    }
}
