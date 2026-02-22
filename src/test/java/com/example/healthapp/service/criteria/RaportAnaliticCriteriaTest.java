package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RaportAnaliticCriteriaTest {

    @Test
    void newRaportAnaliticCriteriaHasAllFiltersNullTest() {
        var raportAnaliticCriteria = new RaportAnaliticCriteria();
        assertThat(raportAnaliticCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void raportAnaliticCriteriaFluentMethodsCreatesFiltersTest() {
        var raportAnaliticCriteria = new RaportAnaliticCriteria();

        setAllFilters(raportAnaliticCriteria);

        assertThat(raportAnaliticCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void raportAnaliticCriteriaCopyCreatesNullFilterTest() {
        var raportAnaliticCriteria = new RaportAnaliticCriteria();
        var copy = raportAnaliticCriteria.copy();

        assertThat(raportAnaliticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(raportAnaliticCriteria)
        );
    }

    @Test
    void raportAnaliticCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var raportAnaliticCriteria = new RaportAnaliticCriteria();
        setAllFilters(raportAnaliticCriteria);

        var copy = raportAnaliticCriteria.copy();

        assertThat(raportAnaliticCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(raportAnaliticCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var raportAnaliticCriteria = new RaportAnaliticCriteria();

        assertThat(raportAnaliticCriteria).hasToString("RaportAnaliticCriteria{}");
    }

    private static void setAllFilters(RaportAnaliticCriteria raportAnaliticCriteria) {
        raportAnaliticCriteria.id();
        raportAnaliticCriteria.perioadaStart();
        raportAnaliticCriteria.perioadaEnd();
        raportAnaliticCriteria.eficientaMedie();
        raportAnaliticCriteria.rataReactiiAdverse();
        raportAnaliticCriteria.observatii();
        raportAnaliticCriteria.concluzii();
        raportAnaliticCriteria.medicamentId();
        raportAnaliticCriteria.medicId();
        raportAnaliticCriteria.distinct();
    }

    private static Condition<RaportAnaliticCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPerioadaStart()) &&
                condition.apply(criteria.getPerioadaEnd()) &&
                condition.apply(criteria.getEficientaMedie()) &&
                condition.apply(criteria.getRataReactiiAdverse()) &&
                condition.apply(criteria.getObservatii()) &&
                condition.apply(criteria.getConcluzii()) &&
                condition.apply(criteria.getMedicamentId()) &&
                condition.apply(criteria.getMedicId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RaportAnaliticCriteria> copyFiltersAre(
        RaportAnaliticCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPerioadaStart(), copy.getPerioadaStart()) &&
                condition.apply(criteria.getPerioadaEnd(), copy.getPerioadaEnd()) &&
                condition.apply(criteria.getEficientaMedie(), copy.getEficientaMedie()) &&
                condition.apply(criteria.getRataReactiiAdverse(), copy.getRataReactiiAdverse()) &&
                condition.apply(criteria.getObservatii(), copy.getObservatii()) &&
                condition.apply(criteria.getConcluzii(), copy.getConcluzii()) &&
                condition.apply(criteria.getMedicamentId(), copy.getMedicamentId()) &&
                condition.apply(criteria.getMedicId(), copy.getMedicId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
