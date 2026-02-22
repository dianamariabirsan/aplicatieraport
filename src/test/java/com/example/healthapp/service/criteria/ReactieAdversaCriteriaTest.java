package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReactieAdversaCriteriaTest {

    @Test
    void newReactieAdversaCriteriaHasAllFiltersNullTest() {
        var reactieAdversaCriteria = new ReactieAdversaCriteria();
        assertThat(reactieAdversaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reactieAdversaCriteriaFluentMethodsCreatesFiltersTest() {
        var reactieAdversaCriteria = new ReactieAdversaCriteria();

        setAllFilters(reactieAdversaCriteria);

        assertThat(reactieAdversaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reactieAdversaCriteriaCopyCreatesNullFilterTest() {
        var reactieAdversaCriteria = new ReactieAdversaCriteria();
        var copy = reactieAdversaCriteria.copy();

        assertThat(reactieAdversaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reactieAdversaCriteria)
        );
    }

    @Test
    void reactieAdversaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reactieAdversaCriteria = new ReactieAdversaCriteria();
        setAllFilters(reactieAdversaCriteria);

        var copy = reactieAdversaCriteria.copy();

        assertThat(reactieAdversaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reactieAdversaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reactieAdversaCriteria = new ReactieAdversaCriteria();

        assertThat(reactieAdversaCriteria).hasToString("ReactieAdversaCriteria{}");
    }

    private static void setAllFilters(ReactieAdversaCriteria reactieAdversaCriteria) {
        reactieAdversaCriteria.id();
        reactieAdversaCriteria.dataRaportare();
        reactieAdversaCriteria.severitate();
        reactieAdversaCriteria.descriere();
        reactieAdversaCriteria.evolutie();
        reactieAdversaCriteria.raportatDe();
        reactieAdversaCriteria.medicamentId();
        reactieAdversaCriteria.pacientId();
        reactieAdversaCriteria.distinct();
    }

    private static Condition<ReactieAdversaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataRaportare()) &&
                condition.apply(criteria.getSeveritate()) &&
                condition.apply(criteria.getDescriere()) &&
                condition.apply(criteria.getEvolutie()) &&
                condition.apply(criteria.getRaportatDe()) &&
                condition.apply(criteria.getMedicamentId()) &&
                condition.apply(criteria.getPacientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReactieAdversaCriteria> copyFiltersAre(
        ReactieAdversaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataRaportare(), copy.getDataRaportare()) &&
                condition.apply(criteria.getSeveritate(), copy.getSeveritate()) &&
                condition.apply(criteria.getDescriere(), copy.getDescriere()) &&
                condition.apply(criteria.getEvolutie(), copy.getEvolutie()) &&
                condition.apply(criteria.getRaportatDe(), copy.getRaportatDe()) &&
                condition.apply(criteria.getMedicamentId(), copy.getMedicamentId()) &&
                condition.apply(criteria.getPacientId(), copy.getPacientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
