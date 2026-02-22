package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExternalDrugInfoCriteriaTest {

    @Test
    void newExternalDrugInfoCriteriaHasAllFiltersNullTest() {
        var externalDrugInfoCriteria = new ExternalDrugInfoCriteria();
        assertThat(externalDrugInfoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void externalDrugInfoCriteriaFluentMethodsCreatesFiltersTest() {
        var externalDrugInfoCriteria = new ExternalDrugInfoCriteria();

        setAllFilters(externalDrugInfoCriteria);

        assertThat(externalDrugInfoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void externalDrugInfoCriteriaCopyCreatesNullFilterTest() {
        var externalDrugInfoCriteria = new ExternalDrugInfoCriteria();
        var copy = externalDrugInfoCriteria.copy();

        assertThat(externalDrugInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(externalDrugInfoCriteria)
        );
    }

    @Test
    void externalDrugInfoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var externalDrugInfoCriteria = new ExternalDrugInfoCriteria();
        setAllFilters(externalDrugInfoCriteria);

        var copy = externalDrugInfoCriteria.copy();

        assertThat(externalDrugInfoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(externalDrugInfoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var externalDrugInfoCriteria = new ExternalDrugInfoCriteria();

        assertThat(externalDrugInfoCriteria).hasToString("ExternalDrugInfoCriteria{}");
    }

    private static void setAllFilters(ExternalDrugInfoCriteria externalDrugInfoCriteria) {
        externalDrugInfoCriteria.id();
        externalDrugInfoCriteria.source();
        externalDrugInfoCriteria.productSummary();
        externalDrugInfoCriteria.lastUpdated();
        externalDrugInfoCriteria.sourceUrl();
        externalDrugInfoCriteria.medicamentId();
        externalDrugInfoCriteria.distinct();
    }

    private static Condition<ExternalDrugInfoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getProductSummary()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getSourceUrl()) &&
                condition.apply(criteria.getMedicamentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExternalDrugInfoCriteria> copyFiltersAre(
        ExternalDrugInfoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getProductSummary(), copy.getProductSummary()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getSourceUrl(), copy.getSourceUrl()) &&
                condition.apply(criteria.getMedicamentId(), copy.getMedicamentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
