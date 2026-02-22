package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FeedbackCriteriaTest {

    @Test
    void newFeedbackCriteriaHasAllFiltersNullTest() {
        var feedbackCriteria = new FeedbackCriteria();
        assertThat(feedbackCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void feedbackCriteriaFluentMethodsCreatesFiltersTest() {
        var feedbackCriteria = new FeedbackCriteria();

        setAllFilters(feedbackCriteria);

        assertThat(feedbackCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void feedbackCriteriaCopyCreatesNullFilterTest() {
        var feedbackCriteria = new FeedbackCriteria();
        var copy = feedbackCriteria.copy();

        assertThat(feedbackCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(feedbackCriteria)
        );
    }

    @Test
    void feedbackCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var feedbackCriteria = new FeedbackCriteria();
        setAllFilters(feedbackCriteria);

        var copy = feedbackCriteria.copy();

        assertThat(feedbackCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(feedbackCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var feedbackCriteria = new FeedbackCriteria();

        assertThat(feedbackCriteria).hasToString("FeedbackCriteria{}");
    }

    private static void setAllFilters(FeedbackCriteria feedbackCriteria) {
        feedbackCriteria.id();
        feedbackCriteria.scor();
        feedbackCriteria.comentariu();
        feedbackCriteria.dataFeedback();
        feedbackCriteria.alocareId();
        feedbackCriteria.distinct();
    }

    private static Condition<FeedbackCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getScor()) &&
                condition.apply(criteria.getComentariu()) &&
                condition.apply(criteria.getDataFeedback()) &&
                condition.apply(criteria.getAlocareId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FeedbackCriteria> copyFiltersAre(FeedbackCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getScor(), copy.getScor()) &&
                condition.apply(criteria.getComentariu(), copy.getComentariu()) &&
                condition.apply(criteria.getDataFeedback(), copy.getDataFeedback()) &&
                condition.apply(criteria.getAlocareId(), copy.getAlocareId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
