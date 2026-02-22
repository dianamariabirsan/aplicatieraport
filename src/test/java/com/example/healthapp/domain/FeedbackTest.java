package com.example.healthapp.domain;

import static com.example.healthapp.domain.AlocareTratamentTestSamples.*;
import static com.example.healthapp.domain.FeedbackTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.healthapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void alocareTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        AlocareTratament alocareTratamentBack = getAlocareTratamentRandomSampleGenerator();

        feedback.setAlocare(alocareTratamentBack);
        assertThat(feedback.getAlocare()).isEqualTo(alocareTratamentBack);

        feedback.alocare(null);
        assertThat(feedback.getAlocare()).isNull();
    }
}
