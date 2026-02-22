package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.StudiiLiteratura} entity. This class is used
 * in {@link com.example.healthapp.web.rest.StudiiLiteraturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /studii-literaturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudiiLiteraturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titlu;

    private StringFilter autori;

    private IntegerFilter anul;

    private StringFilter tipStudiu;

    private StringFilter substanta;

    private StringFilter concluzie;

    private StringFilter link;

    private LongFilter medicamentId;

    private Boolean distinct;

    public StudiiLiteraturaCriteria() {}

    public StudiiLiteraturaCriteria(StudiiLiteraturaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titlu = other.optionalTitlu().map(StringFilter::copy).orElse(null);
        this.autori = other.optionalAutori().map(StringFilter::copy).orElse(null);
        this.anul = other.optionalAnul().map(IntegerFilter::copy).orElse(null);
        this.tipStudiu = other.optionalTipStudiu().map(StringFilter::copy).orElse(null);
        this.substanta = other.optionalSubstanta().map(StringFilter::copy).orElse(null);
        this.concluzie = other.optionalConcluzie().map(StringFilter::copy).orElse(null);
        this.link = other.optionalLink().map(StringFilter::copy).orElse(null);
        this.medicamentId = other.optionalMedicamentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StudiiLiteraturaCriteria copy() {
        return new StudiiLiteraturaCriteria(this);
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

    public StringFilter getTitlu() {
        return titlu;
    }

    public Optional<StringFilter> optionalTitlu() {
        return Optional.ofNullable(titlu);
    }

    public StringFilter titlu() {
        if (titlu == null) {
            setTitlu(new StringFilter());
        }
        return titlu;
    }

    public void setTitlu(StringFilter titlu) {
        this.titlu = titlu;
    }

    public StringFilter getAutori() {
        return autori;
    }

    public Optional<StringFilter> optionalAutori() {
        return Optional.ofNullable(autori);
    }

    public StringFilter autori() {
        if (autori == null) {
            setAutori(new StringFilter());
        }
        return autori;
    }

    public void setAutori(StringFilter autori) {
        this.autori = autori;
    }

    public IntegerFilter getAnul() {
        return anul;
    }

    public Optional<IntegerFilter> optionalAnul() {
        return Optional.ofNullable(anul);
    }

    public IntegerFilter anul() {
        if (anul == null) {
            setAnul(new IntegerFilter());
        }
        return anul;
    }

    public void setAnul(IntegerFilter anul) {
        this.anul = anul;
    }

    public StringFilter getTipStudiu() {
        return tipStudiu;
    }

    public Optional<StringFilter> optionalTipStudiu() {
        return Optional.ofNullable(tipStudiu);
    }

    public StringFilter tipStudiu() {
        if (tipStudiu == null) {
            setTipStudiu(new StringFilter());
        }
        return tipStudiu;
    }

    public void setTipStudiu(StringFilter tipStudiu) {
        this.tipStudiu = tipStudiu;
    }

    public StringFilter getSubstanta() {
        return substanta;
    }

    public Optional<StringFilter> optionalSubstanta() {
        return Optional.ofNullable(substanta);
    }

    public StringFilter substanta() {
        if (substanta == null) {
            setSubstanta(new StringFilter());
        }
        return substanta;
    }

    public void setSubstanta(StringFilter substanta) {
        this.substanta = substanta;
    }

    public StringFilter getConcluzie() {
        return concluzie;
    }

    public Optional<StringFilter> optionalConcluzie() {
        return Optional.ofNullable(concluzie);
    }

    public StringFilter concluzie() {
        if (concluzie == null) {
            setConcluzie(new StringFilter());
        }
        return concluzie;
    }

    public void setConcluzie(StringFilter concluzie) {
        this.concluzie = concluzie;
    }

    public StringFilter getLink() {
        return link;
    }

    public Optional<StringFilter> optionalLink() {
        return Optional.ofNullable(link);
    }

    public StringFilter link() {
        if (link == null) {
            setLink(new StringFilter());
        }
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
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
        final StudiiLiteraturaCriteria that = (StudiiLiteraturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titlu, that.titlu) &&
            Objects.equals(autori, that.autori) &&
            Objects.equals(anul, that.anul) &&
            Objects.equals(tipStudiu, that.tipStudiu) &&
            Objects.equals(substanta, that.substanta) &&
            Objects.equals(concluzie, that.concluzie) &&
            Objects.equals(link, that.link) &&
            Objects.equals(medicamentId, that.medicamentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titlu, autori, anul, tipStudiu, substanta, concluzie, link, medicamentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudiiLiteraturaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitlu().map(f -> "titlu=" + f + ", ").orElse("") +
            optionalAutori().map(f -> "autori=" + f + ", ").orElse("") +
            optionalAnul().map(f -> "anul=" + f + ", ").orElse("") +
            optionalTipStudiu().map(f -> "tipStudiu=" + f + ", ").orElse("") +
            optionalSubstanta().map(f -> "substanta=" + f + ", ").orElse("") +
            optionalConcluzie().map(f -> "concluzie=" + f + ", ").orElse("") +
            optionalLink().map(f -> "link=" + f + ", ").orElse("") +
            optionalMedicamentId().map(f -> "medicamentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
