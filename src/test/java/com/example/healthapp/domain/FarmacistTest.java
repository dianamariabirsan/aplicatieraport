package com.example.healthapp.domain;

import static com.example.healthapp.domain.AdministrareTestSamples.*;
import static com.example.healthapp.domain.FarmacistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FarmacistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Farmacist.class);
        Farmacist farmacist1 = getFarmacistSample1();
        Farmacist farmacist2 = new Farmacist();
        assertThat(farmacist1).isNotEqualTo(farmacist2);

        farmacist2.setId(farmacist1.getId());
        assertThat(farmacist1).isEqualTo(farmacist2);

        farmacist2 = getFarmacistSample2();
        assertThat(farmacist1).isNotEqualTo(farmacist2);
    }

    @Test
    void administrariTest() {
        Farmacist farmacist = getFarmacistRandomSampleGenerator();
        Administrare administrareBack = getAdministrareRandomSampleGenerator();

        farmacist.addAdministrari(administrareBack);
        assertThat(farmacist.getAdministraris()).containsOnly(administrareBack);
        assertThat(administrareBack.getFarmacist()).isEqualTo(farmacist);

        farmacist.removeAdministrari(administrareBack);
        assertThat(farmacist.getAdministraris()).doesNotContain(administrareBack);
        assertThat(administrareBack.getFarmacist()).isNull();

        farmacist.administraris(new HashSet<>(Set.of(administrareBack)));
        assertThat(farmacist.getAdministraris()).containsOnly(administrareBack);
        assertThat(administrareBack.getFarmacist()).isEqualTo(farmacist);

        farmacist.setAdministraris(new HashSet<>());
        assertThat(farmacist.getAdministraris()).doesNotContain(administrareBack);
        assertThat(administrareBack.getFarmacist()).isNull();
    }
}
