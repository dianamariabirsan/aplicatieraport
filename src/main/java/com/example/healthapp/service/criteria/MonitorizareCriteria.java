package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Monitorizare} entity. This class is used
 * in {@link com.example.healthapp.web.rest.MonitorizareResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /monitorizares?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitorizareCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataInstant;

    private DoubleFilter tensiuneSist;

    private DoubleFilter tensiuneDiast;

    private IntegerFilter puls;

    private DoubleFilter glicemie;

    private DoubleFilter scorEficacitate;

    private StringFilter comentarii;

    private LongFilter pacientId;

    private Boolean distinct;

    public MonitorizareCriteria() {}

    public MonitorizareCriteria(MonitorizareCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataInstant = other.optionalDataInstant().map(InstantFilter::copy).orElse(null);
        this.tensiuneSist = other.optionalTensiuneSist().map(DoubleFilter::copy).orElse(null);
        this.tensiuneDiast = other.optionalTensiuneDiast().map(DoubleFilter::copy).orElse(null);
        this.puls = other.optionalPuls().map(IntegerFilter::copy).orElse(null);
        this.glicemie = other.optionalGlicemie().map(DoubleFilter::copy).orElse(null);
        this.scorEficacitate = other.optionalScorEficacitate().map(DoubleFilter::copy).orElse(null);
        this.comentarii = other.optionalComentarii().map(StringFilter::copy).orElse(null);
        this.pacientId = other.optionalPacientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MonitorizareCriteria copy() {
        return new MonitorizareCriteria(this);
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

    public InstantFilter getDataInstant() {
        return dataInstant;
    }

    public Optional<InstantFilter> optionalDataInstant() {
        return Optional.ofNullable(dataInstant);
    }

    public InstantFilter dataInstant() {
        if (dataInstant == null) {
            setDataInstant(new InstantFilter());
        }
        return dataInstant;
    }

    public void setDataInstant(InstantFilter dataInstant) {
        this.dataInstant = dataInstant;
    }

    public DoubleFilter getTensiuneSist() {
        return tensiuneSist;
    }

    public Optional<DoubleFilter> optionalTensiuneSist() {
        return Optional.ofNullable(tensiuneSist);
    }

    public DoubleFilter tensiuneSist() {
        if (tensiuneSist == null) {
            setTensiuneSist(new DoubleFilter());
        }
        return tensiuneSist;
    }

    public void setTensiuneSist(DoubleFilter tensiuneSist) {
        this.tensiuneSist = tensiuneSist;
    }

    public DoubleFilter getTensiuneDiast() {
        return tensiuneDiast;
    }

    public Optional<DoubleFilter> optionalTensiuneDiast() {
        return Optional.ofNullable(tensiuneDiast);
    }

    public DoubleFilter tensiuneDiast() {
        if (tensiuneDiast == null) {
            setTensiuneDiast(new DoubleFilter());
        }
        return tensiuneDiast;
    }

    public void setTensiuneDiast(DoubleFilter tensiuneDiast) {
        this.tensiuneDiast = tensiuneDiast;
    }

    public IntegerFilter getPuls() {
        return puls;
    }

    public Optional<IntegerFilter> optionalPuls() {
        return Optional.ofNullable(puls);
    }

    public IntegerFilter puls() {
        if (puls == null) {
            setPuls(new IntegerFilter());
        }
        return puls;
    }

    public void setPuls(IntegerFilter puls) {
        this.puls = puls;
    }

    public DoubleFilter getGlicemie() {
        return glicemie;
    }

    public Optional<DoubleFilter> optionalGlicemie() {
        return Optional.ofNullable(glicemie);
    }

    public DoubleFilter glicemie() {
        if (glicemie == null) {
            setGlicemie(new DoubleFilter());
        }
        return glicemie;
    }

    public void setGlicemie(DoubleFilter glicemie) {
        this.glicemie = glicemie;
    }

    public DoubleFilter getScorEficacitate() {
        return scorEficacitate;
    }

    public Optional<DoubleFilter> optionalScorEficacitate() {
        return Optional.ofNullable(scorEficacitate);
    }

    public DoubleFilter scorEficacitate() {
        if (scorEficacitate == null) {
            setScorEficacitate(new DoubleFilter());
        }
        return scorEficacitate;
    }

    public void setScorEficacitate(DoubleFilter scorEficacitate) {
        this.scorEficacitate = scorEficacitate;
    }

    public StringFilter getComentarii() {
        return comentarii;
    }

    public Optional<StringFilter> optionalComentarii() {
        return Optional.ofNullable(comentarii);
    }

    public StringFilter comentarii() {
        if (comentarii == null) {
            setComentarii(new StringFilter());
        }
        return comentarii;
    }

    public void setComentarii(StringFilter comentarii) {
        this.comentarii = comentarii;
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
        final MonitorizareCriteria that = (MonitorizareCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataInstant, that.dataInstant) &&
            Objects.equals(tensiuneSist, that.tensiuneSist) &&
            Objects.equals(tensiuneDiast, that.tensiuneDiast) &&
            Objects.equals(puls, that.puls) &&
            Objects.equals(glicemie, that.glicemie) &&
            Objects.equals(scorEficacitate, that.scorEficacitate) &&
            Objects.equals(comentarii, that.comentarii) &&
            Objects.equals(pacientId, that.pacientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataInstant, tensiuneSist, tensiuneDiast, puls, glicemie, scorEficacitate, comentarii, pacientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitorizareCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataInstant().map(f -> "dataInstant=" + f + ", ").orElse("") +
            optionalTensiuneSist().map(f -> "tensiuneSist=" + f + ", ").orElse("") +
            optionalTensiuneDiast().map(f -> "tensiuneDiast=" + f + ", ").orElse("") +
            optionalPuls().map(f -> "puls=" + f + ", ").orElse("") +
            optionalGlicemie().map(f -> "glicemie=" + f + ", ").orElse("") +
            optionalScorEficacitate().map(f -> "scorEficacitate=" + f + ", ").orElse("") +
            optionalComentarii().map(f -> "comentarii=" + f + ", ").orElse("") +
            optionalPacientId().map(f -> "pacientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
