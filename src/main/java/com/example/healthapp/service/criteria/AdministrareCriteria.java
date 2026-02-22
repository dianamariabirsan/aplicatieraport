package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Administrare} entity. This class is used
 * in {@link com.example.healthapp.web.rest.AdministrareResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /administrares?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdministrareCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataAdministrare;

    private StringFilter tipTratament;

    private DoubleFilter doza;

    private StringFilter unitate;

    private StringFilter modAdministrare;

    private StringFilter observatii;

    private LongFilter pacientId;

    private LongFilter farmacistId;

    private Boolean distinct;

    public AdministrareCriteria() {}

    public AdministrareCriteria(AdministrareCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataAdministrare = other.optionalDataAdministrare().map(InstantFilter::copy).orElse(null);
        this.tipTratament = other.optionalTipTratament().map(StringFilter::copy).orElse(null);
        this.doza = other.optionalDoza().map(DoubleFilter::copy).orElse(null);
        this.unitate = other.optionalUnitate().map(StringFilter::copy).orElse(null);
        this.modAdministrare = other.optionalModAdministrare().map(StringFilter::copy).orElse(null);
        this.observatii = other.optionalObservatii().map(StringFilter::copy).orElse(null);
        this.pacientId = other.optionalPacientId().map(LongFilter::copy).orElse(null);
        this.farmacistId = other.optionalFarmacistId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AdministrareCriteria copy() {
        return new AdministrareCriteria(this);
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

    public InstantFilter getDataAdministrare() {
        return dataAdministrare;
    }

    public Optional<InstantFilter> optionalDataAdministrare() {
        return Optional.ofNullable(dataAdministrare);
    }

    public InstantFilter dataAdministrare() {
        if (dataAdministrare == null) {
            setDataAdministrare(new InstantFilter());
        }
        return dataAdministrare;
    }

    public void setDataAdministrare(InstantFilter dataAdministrare) {
        this.dataAdministrare = dataAdministrare;
    }

    public StringFilter getTipTratament() {
        return tipTratament;
    }

    public Optional<StringFilter> optionalTipTratament() {
        return Optional.ofNullable(tipTratament);
    }

    public StringFilter tipTratament() {
        if (tipTratament == null) {
            setTipTratament(new StringFilter());
        }
        return tipTratament;
    }

    public void setTipTratament(StringFilter tipTratament) {
        this.tipTratament = tipTratament;
    }

    public DoubleFilter getDoza() {
        return doza;
    }

    public Optional<DoubleFilter> optionalDoza() {
        return Optional.ofNullable(doza);
    }

    public DoubleFilter doza() {
        if (doza == null) {
            setDoza(new DoubleFilter());
        }
        return doza;
    }

    public void setDoza(DoubleFilter doza) {
        this.doza = doza;
    }

    public StringFilter getUnitate() {
        return unitate;
    }

    public Optional<StringFilter> optionalUnitate() {
        return Optional.ofNullable(unitate);
    }

    public StringFilter unitate() {
        if (unitate == null) {
            setUnitate(new StringFilter());
        }
        return unitate;
    }

    public void setUnitate(StringFilter unitate) {
        this.unitate = unitate;
    }

    public StringFilter getModAdministrare() {
        return modAdministrare;
    }

    public Optional<StringFilter> optionalModAdministrare() {
        return Optional.ofNullable(modAdministrare);
    }

    public StringFilter modAdministrare() {
        if (modAdministrare == null) {
            setModAdministrare(new StringFilter());
        }
        return modAdministrare;
    }

    public void setModAdministrare(StringFilter modAdministrare) {
        this.modAdministrare = modAdministrare;
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

    public LongFilter getFarmacistId() {
        return farmacistId;
    }

    public Optional<LongFilter> optionalFarmacistId() {
        return Optional.ofNullable(farmacistId);
    }

    public LongFilter farmacistId() {
        if (farmacistId == null) {
            setFarmacistId(new LongFilter());
        }
        return farmacistId;
    }

    public void setFarmacistId(LongFilter farmacistId) {
        this.farmacistId = farmacistId;
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
        final AdministrareCriteria that = (AdministrareCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataAdministrare, that.dataAdministrare) &&
            Objects.equals(tipTratament, that.tipTratament) &&
            Objects.equals(doza, that.doza) &&
            Objects.equals(unitate, that.unitate) &&
            Objects.equals(modAdministrare, that.modAdministrare) &&
            Objects.equals(observatii, that.observatii) &&
            Objects.equals(pacientId, that.pacientId) &&
            Objects.equals(farmacistId, that.farmacistId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dataAdministrare,
            tipTratament,
            doza,
            unitate,
            modAdministrare,
            observatii,
            pacientId,
            farmacistId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministrareCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataAdministrare().map(f -> "dataAdministrare=" + f + ", ").orElse("") +
            optionalTipTratament().map(f -> "tipTratament=" + f + ", ").orElse("") +
            optionalDoza().map(f -> "doza=" + f + ", ").orElse("") +
            optionalUnitate().map(f -> "unitate=" + f + ", ").orElse("") +
            optionalModAdministrare().map(f -> "modAdministrare=" + f + ", ").orElse("") +
            optionalObservatii().map(f -> "observatii=" + f + ", ").orElse("") +
            optionalPacientId().map(f -> "pacientId=" + f + ", ").orElse("") +
            optionalFarmacistId().map(f -> "farmacistId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
