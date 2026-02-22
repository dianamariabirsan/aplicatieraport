package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.RaportAnalitic} entity. This class is used
 * in {@link com.example.healthapp.web.rest.RaportAnaliticResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /raport-analitics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RaportAnaliticCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter perioadaStart;

    private InstantFilter perioadaEnd;

    private DoubleFilter eficientaMedie;

    private DoubleFilter rataReactiiAdverse;

    private StringFilter observatii;

    private StringFilter concluzii;

    private LongFilter medicamentId;

    private LongFilter medicId;

    private Boolean distinct;

    public RaportAnaliticCriteria() {}

    public RaportAnaliticCriteria(RaportAnaliticCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.perioadaStart = other.optionalPerioadaStart().map(InstantFilter::copy).orElse(null);
        this.perioadaEnd = other.optionalPerioadaEnd().map(InstantFilter::copy).orElse(null);
        this.eficientaMedie = other.optionalEficientaMedie().map(DoubleFilter::copy).orElse(null);
        this.rataReactiiAdverse = other.optionalRataReactiiAdverse().map(DoubleFilter::copy).orElse(null);
        this.observatii = other.optionalObservatii().map(StringFilter::copy).orElse(null);
        this.concluzii = other.optionalConcluzii().map(StringFilter::copy).orElse(null);
        this.medicamentId = other.optionalMedicamentId().map(LongFilter::copy).orElse(null);
        this.medicId = other.optionalMedicId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RaportAnaliticCriteria copy() {
        return new RaportAnaliticCriteria(this);
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

    public InstantFilter getPerioadaStart() {
        return perioadaStart;
    }

    public Optional<InstantFilter> optionalPerioadaStart() {
        return Optional.ofNullable(perioadaStart);
    }

    public InstantFilter perioadaStart() {
        if (perioadaStart == null) {
            setPerioadaStart(new InstantFilter());
        }
        return perioadaStart;
    }

    public void setPerioadaStart(InstantFilter perioadaStart) {
        this.perioadaStart = perioadaStart;
    }

    public InstantFilter getPerioadaEnd() {
        return perioadaEnd;
    }

    public Optional<InstantFilter> optionalPerioadaEnd() {
        return Optional.ofNullable(perioadaEnd);
    }

    public InstantFilter perioadaEnd() {
        if (perioadaEnd == null) {
            setPerioadaEnd(new InstantFilter());
        }
        return perioadaEnd;
    }

    public void setPerioadaEnd(InstantFilter perioadaEnd) {
        this.perioadaEnd = perioadaEnd;
    }

    public DoubleFilter getEficientaMedie() {
        return eficientaMedie;
    }

    public Optional<DoubleFilter> optionalEficientaMedie() {
        return Optional.ofNullable(eficientaMedie);
    }

    public DoubleFilter eficientaMedie() {
        if (eficientaMedie == null) {
            setEficientaMedie(new DoubleFilter());
        }
        return eficientaMedie;
    }

    public void setEficientaMedie(DoubleFilter eficientaMedie) {
        this.eficientaMedie = eficientaMedie;
    }

    public DoubleFilter getRataReactiiAdverse() {
        return rataReactiiAdverse;
    }

    public Optional<DoubleFilter> optionalRataReactiiAdverse() {
        return Optional.ofNullable(rataReactiiAdverse);
    }

    public DoubleFilter rataReactiiAdverse() {
        if (rataReactiiAdverse == null) {
            setRataReactiiAdverse(new DoubleFilter());
        }
        return rataReactiiAdverse;
    }

    public void setRataReactiiAdverse(DoubleFilter rataReactiiAdverse) {
        this.rataReactiiAdverse = rataReactiiAdverse;
    }

    public StringFilter getObservatii() {
        return observatii;
    }

    public Optional<StringFilter> optionalObservatii() {
        return Optional.ofNullable(observatii);
    }

    public StringFilter observatii() {
        if (observatii == null) {
            setObservatii(new StringFilter());
        }
        return observatii;
    }

    public void setObservatii(StringFilter observatii) {
        this.observatii = observatii;
    }

    public StringFilter getConcluzii() {
        return concluzii;
    }

    public Optional<StringFilter> optionalConcluzii() {
        return Optional.ofNullable(concluzii);
    }

    public StringFilter concluzii() {
        if (concluzii == null) {
            setConcluzii(new StringFilter());
        }
        return concluzii;
    }

    public void setConcluzii(StringFilter concluzii) {
        this.concluzii = concluzii;
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

    public LongFilter getMedicId() {
        return medicId;
    }

    public Optional<LongFilter> optionalMedicId() {
        return Optional.ofNullable(medicId);
    }

    public LongFilter medicId() {
        if (medicId == null) {
            setMedicId(new LongFilter());
        }
        return medicId;
    }

    public void setMedicId(LongFilter medicId) {
        this.medicId = medicId;
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
        final RaportAnaliticCriteria that = (RaportAnaliticCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(perioadaStart, that.perioadaStart) &&
            Objects.equals(perioadaEnd, that.perioadaEnd) &&
            Objects.equals(eficientaMedie, that.eficientaMedie) &&
            Objects.equals(rataReactiiAdverse, that.rataReactiiAdverse) &&
            Objects.equals(observatii, that.observatii) &&
            Objects.equals(concluzii, that.concluzii) &&
            Objects.equals(medicamentId, that.medicamentId) &&
            Objects.equals(medicId, that.medicId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            perioadaStart,
            perioadaEnd,
            eficientaMedie,
            rataReactiiAdverse,
            observatii,
            concluzii,
            medicamentId,
            medicId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RaportAnaliticCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPerioadaStart().map(f -> "perioadaStart=" + f + ", ").orElse("") +
            optionalPerioadaEnd().map(f -> "perioadaEnd=" + f + ", ").orElse("") +
            optionalEficientaMedie().map(f -> "eficientaMedie=" + f + ", ").orElse("") +
            optionalRataReactiiAdverse().map(f -> "rataReactiiAdverse=" + f + ", ").orElse("") +
            optionalObservatii().map(f -> "observatii=" + f + ", ").orElse("") +
            optionalConcluzii().map(f -> "concluzii=" + f + ", ").orElse("") +
            optionalMedicamentId().map(f -> "medicamentId=" + f + ", ").orElse("") +
            optionalMedicId().map(f -> "medicId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
