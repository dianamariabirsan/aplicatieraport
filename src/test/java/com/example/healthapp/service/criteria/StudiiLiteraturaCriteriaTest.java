package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudiiLiteraturaCriteriaTest {

    @Test
    void newStudiiLiteraturaCriteriaHasAllFiltersNullTest() {
        var studiiLiteraturaCriteria = new StudiiLiteraturaCriteria();
        assertThat(studiiLiteraturaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studiiLiteraturaCriteriaFluentMethodsCreatesFiltersTest() {
        var studiiLiteraturaCriteria = new StudiiLiteraturaCriteria();

        setAllFilters(studiiLiteraturaCriteria);

        assertThat(studiiLiteraturaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studiiLiteraturaCriteriaCopyCreatesNullFilterTest() {
        var studiiLiteraturaCriteria = new StudiiLiteraturaCriteria();
        var copy = studiiLiteraturaCriteria.copy();

        assertThat(studiiLiteraturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studiiLiteraturaCriteria)
        );
    }

    @Test
    void studiiLiteraturaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studiiLiteraturaCriteria = new StudiiLiteraturaCriteria();
        setAllFilters(studiiLiteraturaCriteria);

        var copy = studiiLiteraturaCriteria.copy();

        assertThat(studiiLiteraturaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studiiLiteraturaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studiiLiteraturaCriteria = new StudiiLiteraturaCriteria();

        assertThat(studiiLiteraturaCriteria).hasToString("StudiiLiteraturaCriteria{}");
    }

    private static void setAllFilters(StudiiLiteraturaCriteria studiiLiteraturaCriteria) {
        studiiLiteraturaCriteria.id();
        studiiLiteraturaCriteria.titlu();
        studiiLiteraturaCriteria.autori();
        studiiLiteraturaCriteria.anul();
        studiiLiteraturaCriteria.tipStudiu();
        studiiLiteraturaCriteria.substanta();
        studiiLiteraturaCriteria.concluzie();
        studiiLiteraturaCriteria.link();
        studiiLiteraturaCriteria.medicamentId();
        studiiLiteraturaCriteria.distinct();
    }

    private static Condition<StudiiLiteraturaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitlu()) &&
                condition.apply(criteria.getAutori()) &&
                condition.apply(criteria.getAnul()) &&
                condition.apply(criteria.getTipStudiu()) &&
                condition.apply(criteria.getSubstanta()) &&
                condition.apply(criteria.getConcluzie()) &&
                condition.apply(criteria.getLink()) &&
                condition.apply(criteria.getMedicamentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudiiLiteraturaCriteria> copyFiltersAre(
        StudiiLiteraturaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitlu(), copy.getTitlu()) &&
                condition.apply(criteria.getAutori(), copy.getAutori()) &&
                condition.apply(criteria.getAnul(), copy.getAnul()) &&
                condition.apply(criteria.getTipStudiu(), copy.getTipStudiu()) &&
                condition.apply(criteria.getSubstanta(), copy.getSubstanta()) &&
                condition.apply(criteria.getConcluzie(), copy.getConcluzie()) &&
                condition.apply(criteria.getLink(), copy.getLink()) &&
                condition.apply(criteria.getMedicamentId(), copy.getMedicamentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
