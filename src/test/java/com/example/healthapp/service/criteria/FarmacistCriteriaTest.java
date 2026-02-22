package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FarmacistCriteriaTest {

    @Test
    void newFarmacistCriteriaHasAllFiltersNullTest() {
        var farmacistCriteria = new FarmacistCriteria();
        assertThat(farmacistCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void farmacistCriteriaFluentMethodsCreatesFiltersTest() {
        var farmacistCriteria = new FarmacistCriteria();

        setAllFilters(farmacistCriteria);

        assertThat(farmacistCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void farmacistCriteriaCopyCreatesNullFilterTest() {
        var farmacistCriteria = new FarmacistCriteria();
        var copy = farmacistCriteria.copy();

        assertThat(farmacistCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(farmacistCriteria)
        );
    }

    @Test
    void farmacistCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var farmacistCriteria = new FarmacistCriteria();
        setAllFilters(farmacistCriteria);

        var copy = farmacistCriteria.copy();

        assertThat(farmacistCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(farmacistCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var farmacistCriteria = new FarmacistCriteria();

        assertThat(farmacistCriteria).hasToString("FarmacistCriteria{}");
    }

    private static void setAllFilters(FarmacistCriteria farmacistCriteria) {
        farmacistCriteria.id();
        farmacistCriteria.nume();
        farmacistCriteria.prenume();
        farmacistCriteria.farmacie();
        farmacistCriteria.email();
        farmacistCriteria.telefon();
        farmacistCriteria.administrariId();
        farmacistCriteria.distinct();
    }

    private static Condition<FarmacistCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNume()) &&
                condition.apply(criteria.getPrenume()) &&
                condition.apply(criteria.getFarmacie()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefon()) &&
                condition.apply(criteria.getAdministrariId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FarmacistCriteria> copyFiltersAre(FarmacistCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNume(), copy.getNume()) &&
                condition.apply(criteria.getPrenume(), copy.getPrenume()) &&
                condition.apply(criteria.getFarmacie(), copy.getFarmacie()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefon(), copy.getTelefon()) &&
                condition.apply(criteria.getAdministrariId(), copy.getAdministrariId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
