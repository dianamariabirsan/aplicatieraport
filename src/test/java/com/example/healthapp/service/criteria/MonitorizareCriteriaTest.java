package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitorizareCriteriaTest {

    @Test
    void newMonitorizareCriteriaHasAllFiltersNullTest() {
        var monitorizareCriteria = new MonitorizareCriteria();
        assertThat(monitorizareCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitorizareCriteriaFluentMethodsCreatesFiltersTest() {
        var monitorizareCriteria = new MonitorizareCriteria();

        setAllFilters(monitorizareCriteria);

        assertThat(monitorizareCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitorizareCriteriaCopyCreatesNullFilterTest() {
        var monitorizareCriteria = new MonitorizareCriteria();
        var copy = monitorizareCriteria.copy();

        assertThat(monitorizareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitorizareCriteria)
        );
    }

    @Test
    void monitorizareCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitorizareCriteria = new MonitorizareCriteria();
        setAllFilters(monitorizareCriteria);

        var copy = monitorizareCriteria.copy();

        assertThat(monitorizareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitorizareCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitorizareCriteria = new MonitorizareCriteria();

        assertThat(monitorizareCriteria).hasToString("MonitorizareCriteria{}");
    }

    private static void setAllFilters(MonitorizareCriteria monitorizareCriteria) {
        monitorizareCriteria.id();
        monitorizareCriteria.dataInstant();
        monitorizareCriteria.tensiuneSist();
        monitorizareCriteria.tensiuneDiast();
        monitorizareCriteria.puls();
        monitorizareCriteria.glicemie();
        monitorizareCriteria.scorEficacitate();
        monitorizareCriteria.comentarii();
        monitorizareCriteria.pacientId();
        monitorizareCriteria.distinct();
    }

    private static Condition<MonitorizareCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataInstant()) &&
                condition.apply(criteria.getTensiuneSist()) &&
                condition.apply(criteria.getTensiuneDiast()) &&
                condition.apply(criteria.getPuls()) &&
                condition.apply(criteria.getGlicemie()) &&
                condition.apply(criteria.getScorEficacitate()) &&
                condition.apply(criteria.getComentarii()) &&
                condition.apply(criteria.getPacientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitorizareCriteria> copyFiltersAre(
        MonitorizareCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataInstant(), copy.getDataInstant()) &&
                condition.apply(criteria.getTensiuneSist(), copy.getTensiuneSist()) &&
                condition.apply(criteria.getTensiuneDiast(), copy.getTensiuneDiast()) &&
                condition.apply(criteria.getPuls(), copy.getPuls()) &&
                condition.apply(criteria.getGlicemie(), copy.getGlicemie()) &&
                condition.apply(criteria.getScorEficacitate(), copy.getScorEficacitate()) &&
                condition.apply(criteria.getComentarii(), copy.getComentarii()) &&
                condition.apply(criteria.getPacientId(), copy.getPacientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
