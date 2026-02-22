package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.AlocareTratament} entity. This class is used
 * in {@link com.example.healthapp.web.rest.AlocareTratamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alocare-trataments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlocareTratamentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataDecizie;

    private StringFilter tratamentPropus;

    private StringFilter motivDecizie;

    private DoubleFilter scorDecizie;

    private BooleanFilter decizieValidata;

    private LongFilter deciziiId;

    private LongFilter feedbackuriId;

    private LongFilter medicId;

    private LongFilter medicamentId;

    private LongFilter pacientId;

    private Boolean distinct;

    public AlocareTratamentCriteria() {}

    public AlocareTratamentCriteria(AlocareTratamentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataDecizie = other.optionalDataDecizie().map(InstantFilter::copy).orElse(null);
        this.tratamentPropus = other.optionalTratamentPropus().map(StringFilter::copy).orElse(null);
        this.motivDecizie = other.optionalMotivDecizie().map(StringFilter::copy).orElse(null);
        this.scorDecizie = other.optionalScorDecizie().map(DoubleFilter::copy).orElse(null);
        this.decizieValidata = other.optionalDecizieValidata().map(BooleanFilter::copy).orElse(null);
        this.deciziiId = other.optionalDeciziiId().map(LongFilter::copy).orElse(null);
        this.feedbackuriId = other.optionalFeedbackuriId().map(LongFilter::copy).orElse(null);
        this.medicId = other.optionalMedicId().map(LongFilter::copy).orElse(null);
        this.medicamentId = other.optionalMedicamentId().map(LongFilter::copy).orElse(null);
        this.pacientId = other.optionalPacientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlocareTratamentCriteria copy() {
        return new AlocareTratamentCriteria(this);
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

    public InstantFilter getDataDecizie() {
        return dataDecizie;
    }

    public Optional<InstantFilter> optionalDataDecizie() {
        return Optional.ofNullable(dataDecizie);
    }

    public InstantFilter dataDecizie() {
        if (dataDecizie == null) {
            setDataDecizie(new InstantFilter());
        }
        return dataDecizie;
    }

    public void setDataDecizie(InstantFilter dataDecizie) {
        this.dataDecizie = dataDecizie;
    }

    public StringFilter getTratamentPropus() {
        return tratamentPropus;
    }

    public Optional<StringFilter> optionalTratamentPropus() {
        return Optional.ofNullable(tratamentPropus);
    }

    public StringFilter tratamentPropus() {
        if (tratamentPropus == null) {
            setTratamentPropus(new StringFilter());
        }
        return tratamentPropus;
    }

    public void setTratamentPropus(StringFilter tratamentPropus) {
        this.tratamentPropus = tratamentPropus;
    }

    public StringFilter getMotivDecizie() {
        return motivDecizie;
    }

    public Optional<StringFilter> optionalMotivDecizie() {
        return Optional.ofNullable(motivDecizie);
    }

    public StringFilter motivDecizie() {
        if (motivDecizie == null) {
            setMotivDecizie(new StringFilter());
        }
        return motivDecizie;
    }

    public void setMotivDecizie(StringFilter motivDecizie) {
        this.motivDecizie = motivDecizie;
    }

    public DoubleFilter getScorDecizie() {
        return scorDecizie;
    }

    public Optional<DoubleFilter> optionalScorDecizie() {
        return Optional.ofNullable(scorDecizie);
    }

    public DoubleFilter scorDecizie() {
        if (scorDecizie == null) {
            setScorDecizie(new DoubleFilter());
        }
        return scorDecizie;
    }

    public void setScorDecizie(DoubleFilter scorDecizie) {
        this.scorDecizie = scorDecizie;
    }

    public BooleanFilter getDecizieValidata() {
        return decizieValidata;
    }

    public Optional<BooleanFilter> optionalDecizieValidata() {
        return Optional.ofNullable(decizieValidata);
    }

    public BooleanFilter decizieValidata() {
        if (decizieValidata == null) {
            setDecizieValidata(new BooleanFilter());
        }
        return decizieValidata;
    }

    public void setDecizieValidata(BooleanFilter decizieValidata) {
        this.decizieValidata = decizieValidata;
    }

    public LongFilter getDeciziiId() {
        return deciziiId;
    }

    public Optional<LongFilter> optionalDeciziiId() {
        return Optional.ofNullable(deciziiId);
    }

    public LongFilter deciziiId() {
        if (deciziiId == null) {
            setDeciziiId(new LongFilter());
        }
        return deciziiId;
    }

    public void setDeciziiId(LongFilter deciziiId) {
        this.deciziiId = deciziiId;
    }

    public LongFilter getFeedbackuriId() {
        return feedbackuriId;
    }

    public Optional<LongFilter> optionalFeedbackuriId() {
        return Optional.ofNullable(feedbackuriId);
    }

    public LongFilter feedbackuriId() {
        if (feedbackuriId == null) {
            setFeedbackuriId(new LongFilter());
        }
        return feedbackuriId;
    }

    public void setFeedbackuriId(LongFilter feedbackuriId) {
        this.feedbackuriId = feedbackuriId;
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
        final AlocareTratamentCriteria that = (AlocareTratamentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataDecizie, that.dataDecizie) &&
            Objects.equals(tratamentPropus, that.tratamentPropus) &&
            Objects.equals(motivDecizie, that.motivDecizie) &&
            Objects.equals(scorDecizie, that.scorDecizie) &&
            Objects.equals(decizieValidata, that.decizieValidata) &&
            Objects.equals(deciziiId, that.deciziiId) &&
            Objects.equals(feedbackuriId, that.feedbackuriId) &&
            Objects.equals(medicId, that.medicId) &&
            Objects.equals(medicamentId, that.medicamentId) &&
            Objects.equals(pacientId, that.pacientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataDecizie,
            tratamentPropus,
            motivDecizie,
            scorDecizie,
            decizieValidata,
            deciziiId,
            feedbackuriId,
            medicId,
            medicamentId,
            pacientId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlocareTratamentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataDecizie().map(f -> "dataDecizie=" + f + ", ").orElse("") +
            optionalTratamentPropus().map(f -> "tratamentPropus=" + f + ", ").orElse("") +
            optionalMotivDecizie().map(f -> "motivDecizie=" + f + ", ").orElse("") +
            optionalScorDecizie().map(f -> "scorDecizie=" + f + ", ").orElse("") +
            optionalDecizieValidata().map(f -> "decizieValidata=" + f + ", ").orElse("") +
            optionalDeciziiId().map(f -> "deciziiId=" + f + ", ").orElse("") +
            optionalFeedbackuriId().map(f -> "feedbackuriId=" + f + ", ").orElse("") +
            optionalMedicId().map(f -> "medicId=" + f + ", ").orElse("") +
            optionalMedicamentId().map(f -> "medicamentId=" + f + ", ").orElse("") +
            optionalPacientId().map(f -> "pacientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
