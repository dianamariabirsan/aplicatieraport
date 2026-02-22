package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Feedback} entity. This class is used
 * in {@link com.example.healthapp.web.rest.FeedbackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /feedbacks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter scor;

    private StringFilter comentariu;

    private InstantFilter dataFeedback;

    private LongFilter alocareId;

    private Boolean distinct;

    public FeedbackCriteria() {}

    public FeedbackCriteria(FeedbackCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.scor = other.optionalScor().map(IntegerFilter::copy).orElse(null);
        this.comentariu = other.optionalComentariu().map(StringFilter::copy).orElse(null);
        this.dataFeedback = other.optionalDataFeedback().map(InstantFilter::copy).orElse(null);
        this.alocareId = other.optionalAlocareId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FeedbackCriteria copy() {
        return new FeedbackCriteria(this);
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

    public IntegerFilter getScor() {
        return scor;
    }

    public Optional<IntegerFilter> optionalScor() {
        return Optional.ofNullable(scor);
    }

    public IntegerFilter scor() {
        if (scor == null) {
            setScor(new IntegerFilter());
        }
        return scor;
    }

    public void setScor(IntegerFilter scor) {
        this.scor = scor;
    }

    public StringFilter getComentariu() {
        return comentariu;
    }

    public Optional<StringFilter> optionalComentariu() {
        return Optional.ofNullable(comentariu);
    }

    public StringFilter comentariu() {
        if (comentariu == null) {
            setComentariu(new StringFilter());
        }
        return comentariu;
    }

    public void setComentariu(StringFilter comentariu) {
        this.comentariu = comentariu;
    }

    public InstantFilter getDataFeedback() {
        return dataFeedback;
    }

    public Optional<InstantFilter> optionalDataFeedback() {
        return Optional.ofNullable(dataFeedback);
    }

    public InstantFilter dataFeedback() {
        if (dataFeedback == null) {
            setDataFeedback(new InstantFilter());
        }
        return dataFeedback;
    }

    public void setDataFeedback(InstantFilter dataFeedback) {
        this.dataFeedback = dataFeedback;
    }

    public LongFilter getAlocareId() {
        return alocareId;
    }

    public Optional<LongFilter> optionalAlocareId() {
        return Optional.ofNullable(alocareId);
    }

    public LongFilter alocareId() {
        if (alocareId == null) {
            setAlocareId(new LongFilter());
        }
        return alocareId;
    }

    public void setAlocareId(LongFilter alocareId) {
        this.alocareId = alocareId;
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
        final FeedbackCriteria that = (FeedbackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(scor, that.scor) &&
            Objects.equals(comentariu, that.comentariu) &&
            Objects.equals(dataFeedback, that.dataFeedback) &&
            Objects.equals(alocareId, that.alocareId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scor, comentariu, dataFeedback, alocareId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalScor().map(f -> "scor=" + f + ", ").orElse("") +
            optionalComentariu().map(f -> "comentariu=" + f + ", ").orElse("") +
            optionalDataFeedback().map(f -> "dataFeedback=" + f + ", ").orElse("") +
            optionalAlocareId().map(f -> "alocareId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
