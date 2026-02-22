package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DecisionLogCriteriaTest {

    @Test
    void newDecisionLogCriteriaHasAllFiltersNullTest() {
        var decisionLogCriteria = new DecisionLogCriteria();
        assertThat(decisionLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void decisionLogCriteriaFluentMethodsCreatesFiltersTest() {
        var decisionLogCriteria = new DecisionLogCriteria();

        setAllFilters(decisionLogCriteria);

        assertThat(decisionLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void decisionLogCriteriaCopyCreatesNullFilterTest() {
        var decisionLogCriteria = new DecisionLogCriteria();
        var copy = decisionLogCriteria.copy();

        assertThat(decisionLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(decisionLogCriteria)
        );
    }

    @Test
    void decisionLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var decisionLogCriteria = new DecisionLogCriteria();
        setAllFilters(decisionLogCriteria);

        var copy = decisionLogCriteria.copy();

        assertThat(decisionLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(decisionLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var decisionLogCriteria = new DecisionLogCriteria();

        assertThat(decisionLogCriteria).hasToString("DecisionLogCriteria{}");
    }

    private static void setAllFilters(DecisionLogCriteria decisionLogCriteria) {
        decisionLogCriteria.id();
        decisionLogCriteria.timestamp();
        decisionLogCriteria.actorType();
        decisionLogCriteria.recomandare();
        decisionLogCriteria.modelScore();
        decisionLogCriteria.reguliTriggered();
        decisionLogCriteria.externalChecks();
        decisionLogCriteria.alocareId();
        decisionLogCriteria.distinct();
    }

    private static Condition<DecisionLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTimestamp()) &&
                condition.apply(criteria.getActorType()) &&
                condition.apply(criteria.getRecomandare()) &&
                condition.apply(criteria.getModelScore()) &&
                condition.apply(criteria.getReguliTriggered()) &&
                condition.apply(criteria.getExternalChecks()) &&
                condition.apply(criteria.getAlocareId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DecisionLogCriteria> copyFiltersAre(DecisionLogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTimestamp(), copy.getTimestamp()) &&
                condition.apply(criteria.getActorType(), copy.getActorType()) &&
                condition.apply(criteria.getRecomandare(), copy.getRecomandare()) &&
                condition.apply(criteria.getModelScore(), copy.getModelScore()) &&
                condition.apply(criteria.getReguliTriggered(), copy.getReguliTriggered()) &&
                condition.apply(criteria.getExternalChecks(), copy.getExternalChecks()) &&
                condition.apply(criteria.getAlocareId(), copy.getAlocareId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
