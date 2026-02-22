package com.example.healthapp.domain;

import static com.example.healthapp.domain.AlocareTratamentTestSamples.*;
import static com.example.healthapp.domain.DecisionLogTestSamples.*;
import static com.example.healthapp.domain.FeedbackTestSamples.*;
import static com.example.healthapp.domain.MedicTestSamples.*;
import static com.example.healthapp.domain.MedicamentTestSamples.*;
import static com.example.healthapp.domain.PacientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlocareTratamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlocareTratament.class);
        AlocareTratament alocareTratament1 = getAlocareTratamentSample1();
        AlocareTratament alocareTratament2 = new AlocareTratament();
        assertThat(alocareTratament1).isNotEqualTo(alocareTratament2);

        alocareTratament2.setId(alocareTratament1.getId());
        assertThat(alocareTratament1).isEqualTo(alocareTratament2);

        alocareTratament2 = getAlocareTratamentSample2();
        assertThat(alocareTratament1).isNotEqualTo(alocareTratament2);
    }

    @Test
    void deciziiTest() {
        AlocareTratament alocareTratament = getAlocareTratamentRandomSampleGenerator();
        DecisionLog decisionLogBack = getDecisionLogRandomSampleGenerator();

        alocareTratament.addDecizii(decisionLogBack);
        assertThat(alocareTratament.getDeciziis()).containsOnly(decisionLogBack);
        assertThat(decisionLogBack.getAlocare()).isEqualTo(alocareTratament);

        alocareTratament.removeDecizii(decisionLogBack);
        assertThat(alocareTratament.getDeciziis()).doesNotContain(decisionLogBack);
        assertThat(decisionLogBack.getAlocare()).isNull();

        alocareTratament.deciziis(new HashSet<>(Set.of(decisionLogBack)));
        assertThat(alocareTratament.getDeciziis()).containsOnly(decisionLogBack);
        assertThat(decisionLogBack.getAlocare()).isEqualTo(alocareTratament);

        alocareTratament.setDeciziis(new HashSet<>());
        assertThat(alocareTratament.getDeciziis()).doesNotContain(decisionLogBack);
        assertThat(decisionLogBack.getAlocare()).isNull();
    }

    @Test
    void feedbackuriTest() {
        AlocareTratament alocareTratament = getAlocareTratamentRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        alocareTratament.addFeedbackuri(feedbackBack);
        assertThat(alocareTratament.getFeedbackuris()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getAlocare()).isEqualTo(alocareTratament);

        alocareTratament.removeFeedbackuri(feedbackBack);
        assertThat(alocareTratament.getFeedbackuris()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getAlocare()).isNull();

        alocareTratament.feedbackuris(new HashSet<>(Set.of(feedbackBack)));
        assertThat(alocareTratament.getFeedbackuris()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getAlocare()).isEqualTo(alocareTratament);

        alocareTratament.setFeedbackuris(new HashSet<>());
        assertThat(alocareTratament.getFeedbackuris()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getAlocare()).isNull();
    }

    @Test
    void medicTest() {
        AlocareTratament alocareTratament = getAlocareTratamentRandomSampleGenerator();
        Medic medicBack = getMedicRandomSampleGenerator();

        alocareTratament.setMedic(medicBack);
        assertThat(alocareTratament.getMedic()).isEqualTo(medicBack);

        alocareTratament.medic(null);
        assertThat(alocareTratament.getMedic()).isNull();
    }

    @Test
    void medicamentTest() {
        AlocareTratament alocareTratament = getAlocareTratamentRandomSampleGenerator();
        Medicament medicamentBack = getMedicamentRandomSampleGenerator();

        alocareTratament.setMedicament(medicamentBack);
        assertThat(alocareTratament.getMedicament()).isEqualTo(medicamentBack);

        alocareTratament.medicament(null);
        assertThat(alocareTratament.getMedicament()).isNull();
    }

    @Test
    void pacientTest() {
        AlocareTratament alocareTratament = getAlocareTratamentRandomSampleGenerator();
        Pacient pacientBack = getPacientRandomSampleGenerator();

        alocareTratament.setPacient(pacientBack);
        assertThat(alocareTratament.getPacient()).isEqualTo(pacientBack);

        alocareTratament.pacient(null);
        assertThat(alocareTratament.getPacient()).isNull();
    }
}
