package com.example.healthapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PacientCriteriaTest {

    @Test
    void newPacientCriteriaHasAllFiltersNullTest() {
        var pacientCriteria = new PacientCriteria();
        assertThat(pacientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pacientCriteriaFluentMethodsCreatesFiltersTest() {
        var pacientCriteria = new PacientCriteria();

        setAllFilters(pacientCriteria);

        assertThat(pacientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pacientCriteriaCopyCreatesNullFilterTest() {
        var pacientCriteria = new PacientCriteria();
        var copy = pacientCriteria.copy();

        assertThat(pacientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pacientCriteria)
        );
    }

    @Test
    void pacientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pacientCriteria = new PacientCriteria();
        setAllFilters(pacientCriteria);

        var copy = pacientCriteria.copy();

        assertThat(pacientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pacientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pacientCriteria = new PacientCriteria();

        assertThat(pacientCriteria).hasToString("PacientCriteria{}");
    }

    private static void setAllFilters(PacientCriteria pacientCriteria) {
        pacientCriteria.id();
        pacientCriteria.nume();
        pacientCriteria.prenume();
        pacientCriteria.sex();
        pacientCriteria.varsta();
        pacientCriteria.greutate();
        pacientCriteria.inaltime();
        pacientCriteria.circumferintaAbdominala();
        pacientCriteria.cnp();
        pacientCriteria.comorbiditati();
        pacientCriteria.gradSedentarism();
        pacientCriteria.istoricTratament();
        pacientCriteria.toleranta();
        pacientCriteria.email();
        pacientCriteria.telefon();
        pacientCriteria.alocariId();
        pacientCriteria.reactiiAdverseId();
        pacientCriteria.monitorizariId();
        pacientCriteria.medicId();
        pacientCriteria.farmacistId();
        pacientCriteria.distinct();
    }

    private static Condition<PacientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNume()) &&
                condition.apply(criteria.getPrenume()) &&
                condition.apply(criteria.getSex()) &&
                condition.apply(criteria.getVarsta()) &&
                condition.apply(criteria.getGreutate()) &&
                condition.apply(criteria.getInaltime()) &&
                condition.apply(criteria.getCircumferintaAbdominala()) &&
                condition.apply(criteria.getCnp()) &&
                condition.apply(criteria.getComorbiditati()) &&
                condition.apply(criteria.getGradSedentarism()) &&
                condition.apply(criteria.getIstoricTratament()) &&
                condition.apply(criteria.getToleranta()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelefon()) &&
                condition.apply(criteria.getAlocariId()) &&
                condition.apply(criteria.getReactiiAdverseId()) &&
                condition.apply(criteria.getMonitorizariId()) &&
                condition.apply(criteria.getMedicId()) &&
                condition.apply(criteria.getFarmacistId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PacientCriteria> copyFiltersAre(PacientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNume(), copy.getNume()) &&
                condition.apply(criteria.getPrenume(), copy.getPrenume()) &&
                condition.apply(criteria.getSex(), copy.getSex()) &&
                condition.apply(criteria.getVarsta(), copy.getVarsta()) &&
                condition.apply(criteria.getGreutate(), copy.getGreutate()) &&
                condition.apply(criteria.getInaltime(), copy.getInaltime()) &&
                condition.apply(criteria.getCircumferintaAbdominala(), copy.getCircumferintaAbdominala()) &&
                condition.apply(criteria.getCnp(), copy.getCnp()) &&
                condition.apply(criteria.getComorbiditati(), copy.getComorbiditati()) &&
                condition.apply(criteria.getGradSedentarism(), copy.getGradSedentarism()) &&
                condition.apply(criteria.getIstoricTratament(), copy.getIstoricTratament()) &&
                condition.apply(criteria.getToleranta(), copy.getToleranta()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelefon(), copy.getTelefon()) &&
                condition.apply(criteria.getAlocariId(), copy.getAlocariId()) &&
                condition.apply(criteria.getReactiiAdverseId(), copy.getReactiiAdverseId()) &&
                condition.apply(criteria.getMonitorizariId(), copy.getMonitorizariId()) &&
                condition.apply(criteria.getMedicId(), copy.getMedicId()) &&
                condition.apply(criteria.getFarmacistId(), copy.getFarmacistId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
