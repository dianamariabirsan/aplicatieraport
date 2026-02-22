package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AdministrareCriteriaTest {

    @Test
    void newAdministrareCriteriaHasAllFiltersNullTest() {
        var administrareCriteria = new AdministrareCriteria();
        assertThat(administrareCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void administrareCriteriaFluentMethodsCreatesFiltersTest() {
        var administrareCriteria = new AdministrareCriteria();

        setAllFilters(administrareCriteria);

        assertThat(administrareCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void administrareCriteriaCopyCreatesNullFilterTest() {
        var administrareCriteria = new AdministrareCriteria();
        var copy = administrareCriteria.copy();

        assertThat(administrareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(administrareCriteria)
        );
    }

    @Test
    void administrareCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var administrareCriteria = new AdministrareCriteria();
        setAllFilters(administrareCriteria);

        var copy = administrareCriteria.copy();

        assertThat(administrareCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(administrareCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var administrareCriteria = new AdministrareCriteria();

        assertThat(administrareCriteria).hasToString("AdministrareCriteria{}");
    }

    private static void setAllFilters(AdministrareCriteria administrareCriteria) {
        administrareCriteria.id();
        administrareCriteria.dataAdministrare();
        administrareCriteria.tipTratament();
        administrareCriteria.doza();
        administrareCriteria.unitate();
        administrareCriteria.modAdministrare();
        administrareCriteria.observatii();
        administrareCriteria.pacientId();
        administrareCriteria.farmacistId();
        administrareCriteria.distinct();
    }

    private static Condition<AdministrareCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataAdministrare()) &&
                condition.apply(criteria.getTipTratament()) &&
                condition.apply(criteria.getDoza()) &&
                condition.apply(criteria.getUnitate()) &&
                condition.apply(criteria.getModAdministrare()) &&
                condition.apply(criteria.getObservatii()) &&
                condition.apply(criteria.getPacientId()) &&
                condition.apply(criteria.getFarmacistId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AdministrareCriteria> copyFiltersAre(
        AdministrareCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataAdministrare(), copy.getDataAdministrare()) &&
                condition.apply(criteria.getTipTratament(), copy.getTipTratament()) &&
                condition.apply(criteria.getDoza(), copy.getDoza()) &&
                condition.apply(criteria.getUnitate(), copy.getUnitate()) &&
                condition.apply(criteria.getModAdministrare(), copy.getModAdministrare()) &&
                condition.apply(criteria.getObservatii(), copy.getObservatii()) &&
                condition.apply(criteria.getPacientId(), copy.getPacientId()) &&
                condition.apply(criteria.getFarmacistId(), copy.getFarmacistId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
