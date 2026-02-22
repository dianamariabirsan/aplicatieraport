package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicCriteriaTest {

    @Test
    void newMedicCriteriaHasAllFiltersNullTest() {
        var medicCriteria = new MedicCriteria();
        assertThat(medicCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicCriteriaFluentMethodsCreatesFiltersTest() {
        var medicCriteria = new MedicCriteria();

        setAllFilters(medicCriteria);

        assertThat(medicCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicCriteriaCopyCreatesNullFilterTest() {
        var medicCriteria = new MedicCriteria();
        var copy = medicCriteria.copy();

        assertThat(medicCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicCriteria)
        );
    }

    @Test
    void medicCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicCriteria = new MedicCriteria();
        setAllFilters(medicCriteria);

        var copy = medicCriteria.copy();

        assertThat(medicCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicCriteria = new MedicCriteria();

        assertThat(medicCriteria).hasToString("MedicCriteria{}");
    }

    private static void setAllFilters(MedicCriteria medicCriteria) {
        medicCriteria.id();
        medicCriteria.nume();
        medicCriteria.prenume();
        medicCriteria.specializare();
        medicCriteria.email();
        medicCriteria.telefon();
        medicCriteria.cabinet();
        medicCriteria.distinct();
    }

    private static Condition<MedicCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNume()) &&
                condition.apply(criteria.getPrenume()) &&
                condition.apply(criteria.getSpecializare()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefon()) &&
                condition.apply(criteria.getCabinet()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicCriteria> copyFiltersAre(MedicCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNume(), copy.getNume()) &&
                condition.apply(criteria.getPrenume(), copy.getPrenume()) &&
                condition.apply(criteria.getSpecializare(), copy.getSpecializare()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefon(), copy.getTelefon()) &&
                condition.apply(criteria.getCabinet(), copy.getCabinet()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
