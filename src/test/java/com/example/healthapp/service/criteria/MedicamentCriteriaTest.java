package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicamentCriteriaTest {

    @Test
    void newMedicamentCriteriaHasAllFiltersNullTest() {
        var medicamentCriteria = new MedicamentCriteria();
        assertThat(medicamentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void medicamentCriteriaFluentMethodsCreatesFiltersTest() {
        var medicamentCriteria = new MedicamentCriteria();

        setAllFilters(medicamentCriteria);

        assertThat(medicamentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void medicamentCriteriaCopyCreatesNullFilterTest() {
        var medicamentCriteria = new MedicamentCriteria();
        var copy = medicamentCriteria.copy();

        assertThat(medicamentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(medicamentCriteria)
        );
    }

    @Test
    void medicamentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicamentCriteria = new MedicamentCriteria();
        setAllFilters(medicamentCriteria);

        var copy = medicamentCriteria.copy();

        assertThat(medicamentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(medicamentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicamentCriteria = new MedicamentCriteria();

        assertThat(medicamentCriteria).hasToString("MedicamentCriteria{}");
    }

    private static void setAllFilters(MedicamentCriteria medicamentCriteria) {
        medicamentCriteria.id();
        medicamentCriteria.denumire();
        medicamentCriteria.substanta();
        medicamentCriteria.indicatii();
        medicamentCriteria.contraindicatii();
        medicamentCriteria.interactiuni();
        medicamentCriteria.avertizari();
        medicamentCriteria.dozaRecomandata();
        medicamentCriteria.formaFarmaceutica();
        medicamentCriteria.infoExternId();
        medicamentCriteria.studiiId();
        medicamentCriteria.distinct();
    }

    private static Condition<MedicamentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDenumire()) &&
                condition.apply(criteria.getSubstanta()) &&
                condition.apply(criteria.getIndicatii()) &&
                condition.apply(criteria.getContraindicatii()) &&
                condition.apply(criteria.getInteractiuni()) &&
                condition.apply(criteria.getAvertizari()) &&
                condition.apply(criteria.getDozaRecomandata()) &&
                condition.apply(criteria.getFormaFarmaceutica()) &&
                condition.apply(criteria.getInfoExternId()) &&
                condition.apply(criteria.getStudiiId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicamentCriteria> copyFiltersAre(MedicamentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDenumire(), copy.getDenumire()) &&
                condition.apply(criteria.getSubstanta(), copy.getSubstanta()) &&
                condition.apply(criteria.getIndicatii(), copy.getIndicatii()) &&
                condition.apply(criteria.getContraindicatii(), copy.getContraindicatii()) &&
                condition.apply(criteria.getInteractiuni(), copy.getInteractiuni()) &&
                condition.apply(criteria.getAvertizari(), copy.getAvertizari()) &&
                condition.apply(criteria.getDozaRecomandata(), copy.getDozaRecomandata()) &&
                condition.apply(criteria.getFormaFarmaceutica(), copy.getFormaFarmaceutica()) &&
                condition.apply(criteria.getInfoExternId(), copy.getInfoExternId()) &&
                condition.apply(criteria.getStudiiId(), copy.getStudiiId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
