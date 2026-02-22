package com.example.healthapp.service.criteria;

import com.example.healthapp.domain.enumeration.ActorType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.DecisionLog} entity. This class is used
 * in {@link com.example.healthapp.web.rest.DecisionLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decision-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionLogCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ActorType
     */
    public static class ActorTypeFilter extends Filter<ActorType> {

        public ActorTypeFilter() {}

        public ActorTypeFilter(ActorTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActorTypeFilter copy() {
            return new ActorTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter timestamp;

    private ActorTypeFilter actorType;

    private StringFilter recomandare;

    private DoubleFilter modelScore;

    private StringFilter reguliTriggered;

    private StringFilter externalChecks;

    private LongFilter alocareId;

    private Boolean distinct;

    public DecisionLogCriteria() {}

    public DecisionLogCriteria(DecisionLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.timestamp = other.optionalTimestamp().map(InstantFilter::copy).orElse(null);
        this.actorType = other.optionalActorType().map(ActorTypeFilter::copy).orElse(null);
        this.recomandare = other.optionalRecomandare().map(StringFilter::copy).orElse(null);
        this.modelScore = other.optionalModelScore().map(DoubleFilter::copy).orElse(null);
        this.reguliTriggered = other.optionalReguliTriggered().map(StringFilter::copy).orElse(null);
        this.externalChecks = other.optionalExternalChecks().map(StringFilter::copy).orElse(null);
        this.alocareId = other.optionalAlocareId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DecisionLogCriteria copy() {
        return new DecisionLogCriteria(this);
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

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public Optional<InstantFilter> optionalTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            setTimestamp(new InstantFilter());
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public ActorTypeFilter getActorType() {
        return actorType;
    }

    public Optional<ActorTypeFilter> optionalActorType() {
        return Optional.ofNullable(actorType);
    }

    public ActorTypeFilter actorType() {
        if (actorType == null) {
            setActorType(new ActorTypeFilter());
        }
        return actorType;
    }

    public void setActorType(ActorTypeFilter actorType) {
        this.actorType = actorType;
    }

    public StringFilter getRecomandare() {
        return recomandare;
    }

    public Optional<StringFilter> optionalRecomandare() {
        return Optional.ofNullable(recomandare);
    }

    public StringFilter recomandare() {
        if (recomandare == null) {
            setRecomandare(new StringFilter());
        }
        return recomandare;
    }

    public void setRecomandare(StringFilter recomandare) {
        this.recomandare = recomandare;
    }

    public DoubleFilter getModelScore() {
        return modelScore;
    }

    public Optional<DoubleFilter> optionalModelScore() {
        return Optional.ofNullable(modelScore);
    }

    public DoubleFilter modelScore() {
        if (modelScore == null) {
            setModelScore(new DoubleFilter());
        }
        return modelScore;
    }

    public void setModelScore(DoubleFilter modelScore) {
        this.modelScore = modelScore;
    }

    public StringFilter getReguliTriggered() {
        return reguliTriggered;
    }

    public Optional<StringFilter> optionalReguliTriggered() {
        return Optional.ofNullable(reguliTriggered);
    }

    public StringFilter reguliTriggered() {
        if (reguliTriggered == null) {
            setReguliTriggered(new StringFilter());
        }
        return reguliTriggered;
    }

    public void setReguliTriggered(StringFilter reguliTriggered) {
        this.reguliTriggered = reguliTriggered;
    }

    public StringFilter getExternalChecks() {
        return externalChecks;
    }

    public Optional<StringFilter> optionalExternalChecks() {
        return Optional.ofNullable(externalChecks);
    }

    public StringFilter externalChecks() {
        if (externalChecks == null) {
            setExternalChecks(new StringFilter());
        }
        return externalChecks;
    }

    public void setExternalChecks(StringFilter externalChecks) {
        this.externalChecks = externalChecks;
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
        final DecisionLogCriteria that = (DecisionLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(actorType, that.actorType) &&
            Objects.equals(recomandare, that.recomandare) &&
            Objects.equals(modelScore, that.modelScore) &&
            Objects.equals(reguliTriggered, that.reguliTriggered) &&
            Objects.equals(externalChecks, that.externalChecks) &&
            Objects.equals(alocareId, that.alocareId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, actorType, recomandare, modelScore, reguliTriggered, externalChecks, alocareId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTimestamp().map(f -> "timestamp=" + f + ", ").orElse("") +
            optionalActorType().map(f -> "actorType=" + f + ", ").orElse("") +
            optionalRecomandare().map(f -> "recomandare=" + f + ", ").orElse("") +
            optionalModelScore().map(f -> "modelScore=" + f + ", ").orElse("") +
            optionalReguliTriggered().map(f -> "reguliTriggered=" + f + ", ").orElse("") +
            optionalExternalChecks().map(f -> "externalChecks=" + f + ", ").orElse("") +
            optionalAlocareId().map(f -> "alocareId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
