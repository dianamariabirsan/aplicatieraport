package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.ReactieAdversa} entity. This class is used
 * in {@link com.example.healthapp.web.rest.ReactieAdversaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reactie-adversas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactieAdversaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataRaportare;

    private StringFilter severitate;

    private StringFilter descriere;

    private StringFilter evolutie;

    private StringFilter raportatDe;

    private LongFilter medicamentId;

    private LongFilter pacientId;

    private Boolean distinct;

    public ReactieAdversaCriteria() {}

    public ReactieAdversaCriteria(ReactieAdversaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataRaportare = other.optionalDataRaportare().map(InstantFilter::copy).orElse(null);
        this.severitate = other.optionalSeveritate().map(StringFilter::copy).orElse(null);
        this.descriere = other.optionalDescriere().map(StringFilter::copy).orElse(null);
        this.evolutie = other.optionalEvolutie().map(StringFilter::copy).orElse(null);
        this.raportatDe = other.optionalRaportatDe().map(StringFilter::copy).orElse(null);
        this.medicamentId = other.optionalMedicamentId().map(LongFilter::copy).orElse(null);
        this.pacientId = other.optionalPacientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReactieAdversaCriteria copy() {
        return new ReactieAdversaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDataRaportare() {
        return dataRaportare;
    }

    public Optional<InstantFilter> optionalDataRaportare() {
        return Optional.ofNullable(dataRaportare);
    }

    public InstantFilter dataRaportare() {
        if (dataRaportare == null) {
            setDataRaportare(new InstantFilter());
        }
        return dataRaportare;
    }

    public void setDataRaportare(InstantFilter dataRaportare) {
        this.dataRaportare = dataRaportare;
    }

    public StringFilter getSeveritate() {
        return severitate;
    }

    public Optional<StringFilter> optionalSeveritate() {
        return Optional.ofNullable(severitate);
    }

    public StringFilter severitate() {
        if (severitate == null) {
            setSeveritate(new StringFilter());
        }
        return severitate;
    }

    public void setSeveritate(StringFilter severitate) {
        this.severitate = severitate;
    }

    public StringFilter getDescriere() {
        return descriere;
    }

    public Optional<StringFilter> optionalDescriere() {
        return Optional.ofNullable(descriere);
    }

    public StringFilter descriere() {
        if (descriere == null) {
            setDescriere(new StringFilter());
        }
        return descriere;
    }

    public void setDescriere(StringFilter descriere) {
        this.descriere = descriere;
    }

    public StringFilter getEvolutie() {
        return evolutie;
    }

    public Optional<StringFilter> optionalEvolutie() {
        return Optional.ofNullable(evolutie);
    }

    public StringFilter evolutie() {
        if (evolutie == null) {
            setEvolutie(new StringFilter());
        }
        return evolutie;
    }

    public void setEvolutie(StringFilter evolutie) {
        this.evolutie = evolutie;
    }

    public StringFilter getRaportatDe() {
        return raportatDe;
    }

    public Optional<StringFilter> optionalRaportatDe() {
        return Optional.ofNullable(raportatDe);
    }

    public StringFilter raportatDe() {
        if (raportatDe == null) {
            setRaportatDe(new StringFilter());
        }
        return raportatDe;
    }

    public void setRaportatDe(StringFilter raportatDe) {
        this.raportatDe = raportatDe;
    }

    public LongFilter getMedicamentId() {
        return medicamentId;
    }

    public Optional<LongFilter> optionalMedicamentId() {
        return Optional.ofNullable(medicamentId);
    }

    public LongFilter medicamentId() {
        if (medicamentId == null) {
            setMedicamentId(new LongFilter());
        }
        return medicamentId;
    }

    public void setMedicamentId(LongFilter medicamentId) {
        this.medicamentId = medicamentId;
    }

    public LongFilter getPacientId() {
        return pacientId;
    }

    public Optional<LongFilter> optionalPacientId() {
        return Optional.ofNullable(pacientId);
    }

    public LongFilter pacientId() {
        if (pacientId == null) {
            setPacientId(new LongFilter());
        }
        return pacientId;
    }

    public void setPacientId(LongFilter pacientId) {
        this.pacientId = pacientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReactieAdversaCriteria that = (ReactieAdversaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataRaportare, that.dataRaportare) &&
            Objects.equals(severitate, that.severitate) &&
            Objects.equals(descriere, that.descriere) &&
            Objects.equals(evolutie, that.evolutie) &&
            Objects.equals(raportatDe, that.raportatDe) &&
            Objects.equals(medicamentId, that.medicamentId) &&
            Objects.equals(pacientId, that.pacientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataRaportare, severitate, descriere, evolutie, raportatDe, medicamentId, pacientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactieAdversaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataRaportare().map(f -> "dataRaportare=" + f + ", ").orElse("") +
            optionalSeveritate().map(f -> "severitate=" + f + ", ").orElse("") +
            optionalDescriere().map(f -> "descriere=" + f + ", ").orElse("") +
            optionalEvolutie().map(f -> "evolutie=" + f + ", ").orElse("") +
            optionalRaportatDe().map(f -> "raportatDe=" + f + ", ").orElse("") +
            optionalMedicamentId().map(f -> "medicamentId=" + f + ", ").orElse("") +
            optionalPacientId().map(f -> "pacientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
