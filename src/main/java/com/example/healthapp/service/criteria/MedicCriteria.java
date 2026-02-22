package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Medic} entity. This class is used
 * in {@link com.example.healthapp.web.rest.MedicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nume;

    private StringFilter prenume;

    private StringFilter specializare;

    private StringFilter email;

    private StringFilter telefon;

    private StringFilter cabinet;

    private Boolean distinct;

    public MedicCriteria() {}

    public MedicCriteria(MedicCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nume = other.optionalNume().map(StringFilter::copy).orElse(null);
        this.prenume = other.optionalPrenume().map(StringFilter::copy).orElse(null);
        this.specializare = other.optionalSpecializare().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefon = other.optionalTelefon().map(StringFilter::copy).orElse(null);
        this.cabinet = other.optionalCabinet().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicCriteria copy() {
        return new MedicCriteria(this);
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

    public StringFilter getNume() {
        return nume;
    }

    public Optional<StringFilter> optionalNume() {
        return Optional.ofNullable(nume);
    }

    public StringFilter nume() {
        if (nume == null) {
            setNume(new StringFilter());
        }
        return nume;
    }

    public void setNume(StringFilter nume) {
        this.nume = nume;
    }

    public StringFilter getPrenume() {
        return prenume;
    }

    public Optional<StringFilter> optionalPrenume() {
        return Optional.ofNullable(prenume);
    }

    public StringFilter prenume() {
        if (prenume == null) {
            setPrenume(new StringFilter());
        }
        return prenume;
    }

    public void setPrenume(StringFilter prenume) {
        this.prenume = prenume;
    }

    public StringFilter getSpecializare() {
        return specializare;
    }

    public Optional<StringFilter> optionalSpecializare() {
        return Optional.ofNullable(specializare);
    }

    public StringFilter specializare() {
        if (specializare == null) {
            setSpecializare(new StringFilter());
        }
        return specializare;
    }

    public void setSpecializare(StringFilter specializare) {
        this.specializare = specializare;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefon() {
        return telefon;
    }

    public Optional<StringFilter> optionalTelefon() {
        return Optional.ofNullable(telefon);
    }

    public StringFilter telefon() {
        if (telefon == null) {
            setTelefon(new StringFilter());
        }
        return telefon;
    }

    public void setTelefon(StringFilter telefon) {
        this.telefon = telefon;
    }

    public StringFilter getCabinet() {
        return cabinet;
    }

    public Optional<StringFilter> optionalCabinet() {
        return Optional.ofNullable(cabinet);
    }

    public StringFilter cabinet() {
        if (cabinet == null) {
            setCabinet(new StringFilter());
        }
        return cabinet;
    }

    public void setCabinet(StringFilter cabinet) {
        this.cabinet = cabinet;
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
        final MedicCriteria that = (MedicCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nume, that.nume) &&
            Objects.equals(prenume, that.prenume) &&
            Objects.equals(specializare, that.specializare) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefon, that.telefon) &&
            Objects.equals(cabinet, that.cabinet) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nume, prenume, specializare, email, telefon, cabinet, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNume().map(f -> "nume=" + f + ", ").orElse("") +
            optionalPrenume().map(f -> "prenume=" + f + ", ").orElse("") +
            optionalSpecializare().map(f -> "specializare=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefon().map(f -> "telefon=" + f + ", ").orElse("") +
            optionalCabinet().map(f -> "cabinet=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
