package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlocareTratamentCriteriaTest {

    @Test
    void newAlocareTratamentCriteriaHasAllFiltersNullTest() {
        var alocareTratamentCriteria = new AlocareTratamentCriteria();
        assertThat(alocareTratamentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void alocareTratamentCriteriaFluentMethodsCreatesFiltersTest() {
        var alocareTratamentCriteria = new AlocareTratamentCriteria();

        setAllFilters(alocareTratamentCriteria);

        assertThat(alocareTratamentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void alocareTratamentCriteriaCopyCreatesNullFilterTest() {
        var alocareTratamentCriteria = new AlocareTratamentCriteria();
        var copy = alocareTratamentCriteria.copy();

        assertThat(alocareTratamentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(alocareTratamentCriteria)
        );
    }

    @Test
    void alocareTratamentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var alocareTratamentCriteria = new AlocareTratamentCriteria();
        setAllFilters(alocareTratamentCriteria);

        var copy = alocareTratamentCriteria.copy();

        assertThat(alocareTratamentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(alocareTratamentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var alocareTratamentCriteria = new AlocareTratamentCriteria();

        assertThat(alocareTratamentCriteria).hasToString("AlocareTratamentCriteria{}");
    }

    private static void setAllFilters(AlocareTratamentCriteria alocareTratamentCriteria) {
        alocareTratamentCriteria.id();
        alocareTratamentCriteria.dataDecizie();
        alocareTratamentCriteria.tratamentPropus();
        alocareTratamentCriteria.motivDecizie();
        alocareTratamentCriteria.scorDecizie();
        alocareTratamentCriteria.decizieValidata();
        alocareTratamentCriteria.deciziiId();
        alocareTratamentCriteria.feedbackuriId();
        alocareTratamentCriteria.medicId();
        alocareTratamentCriteria.medicamentId();
        alocareTratamentCriteria.pacientId();
        alocareTratamentCriteria.distinct();
    }

    private static Condition<AlocareTratamentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataDecizie()) &&
                condition.apply(criteria.getTratamentPropus()) &&
                condition.apply(criteria.getMotivDecizie()) &&
                condition.apply(criteria.getScorDecizie()) &&
                condition.apply(criteria.getDecizieValidata()) &&
                condition.apply(criteria.getDeciziiId()) &&
                condition.apply(criteria.getFeedbackuriId()) &&
                condition.apply(criteria.getMedicId()) &&
                condition.apply(criteria.getMedicamentId()) &&
                condition.apply(criteria.getPacientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlocareTratamentCriteria> copyFiltersAre(
        AlocareTratamentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataDecizie(), copy.getDataDecizie()) &&
                condition.apply(criteria.getTratamentPropus(), copy.getTratamentPropus()) &&
                condition.apply(criteria.getMotivDecizie(), copy.getMotivDecizie()) &&
                condition.apply(criteria.getScorDecizie(), copy.getScorDecizie()) &&
                condition.apply(criteria.getDecizieValidata(), copy.getDecizieValidata()) &&
                condition.apply(criteria.getDeciziiId(), copy.getDeciziiId()) &&
                condition.apply(criteria.getFeedbackuriId(), copy.getFeedbackuriId()) &&
                condition.apply(criteria.getMedicId(), copy.getMedicId()) &&
                condition.apply(criteria.getMedicamentId(), copy.getMedicamentId()) &&
                condition.apply(criteria.getPacientId(), copy.getPacientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
