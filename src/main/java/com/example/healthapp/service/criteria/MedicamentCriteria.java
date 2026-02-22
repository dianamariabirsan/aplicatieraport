package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Medicament} entity. This class is used
 * in {@link com.example.healthapp.web.rest.MedicamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medicaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicamentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter denumire;

    private StringFilter substanta;

    private StringFilter indicatii;

    private StringFilter contraindicatii;

    private StringFilter interactiuni;

    private StringFilter dozaRecomandata;

    private StringFilter formaFarmaceutica;

    private LongFilter infoExternId;

    private LongFilter studiiId;

    private Boolean distinct;

    public MedicamentCriteria() {}

    public MedicamentCriteria(MedicamentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.denumire = other.optionalDenumire().map(StringFilter::copy).orElse(null);
        this.substanta = other.optionalSubstanta().map(StringFilter::copy).orElse(null);
        this.indicatii = other.optionalIndicatii().map(StringFilter::copy).orElse(null);
        this.contraindicatii = other.optionalContraindicatii().map(StringFilter::copy).orElse(null);
        this.interactiuni = other.optionalInteractiuni().map(StringFilter::copy).orElse(null);
        this.dozaRecomandata = other.optionalDozaRecomandata().map(StringFilter::copy).orElse(null);
        this.formaFarmaceutica = other.optionalFormaFarmaceutica().map(StringFilter::copy).orElse(null);
        this.infoExternId = other.optionalInfoExternId().map(LongFilter::copy).orElse(null);
        this.studiiId = other.optionalStudiiId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicamentCriteria copy() {
        return new MedicamentCriteria(this);
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

    public StringFilter getDenumire() {
        return denumire;
    }

    public Optional<StringFilter> optionalDenumire() {
        return Optional.ofNullable(denumire);
    }

    public StringFilter denumire() {
        if (denumire == null) {
            setDenumire(new StringFilter());
        }
        return denumire;
    }

    public void setDenumire(StringFilter denumire) {
        this.denumire = denumire;
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

    public StringFilter getIndicatii() {
        return indicatii;
    }

    public Optional<StringFilter> optionalIndicatii() {
        return Optional.ofNullable(indicatii);
    }

    public StringFilter indicatii() {
        if (indicatii == null) {
            setIndicatii(new StringFilter());
        }
        return indicatii;
    }

    public void setIndicatii(StringFilter indicatii) {
        this.indicatii = indicatii;
    }

    public StringFilter getContraindicatii() {
        return contraindicatii;
    }

    public Optional<StringFilter> optionalContraindicatii() {
        return Optional.ofNullable(contraindicatii);
    }

    public StringFilter contraindicatii() {
        if (contraindicatii == null) {
            setContraindicatii(new StringFilter());
        }
        return contraindicatii;
    }

    public void setContraindicatii(StringFilter contraindicatii) {
        this.contraindicatii = contraindicatii;
    }

    public StringFilter getInteractiuni() {
        return interactiuni;
    }

    public Optional<StringFilter> optionalInteractiuni() {
        return Optional.ofNullable(interactiuni);
    }

    public StringFilter interactiuni() {
        if (interactiuni == null) {
            setInteractiuni(new StringFilter());
        }
        return interactiuni;
    }

    public void setInteractiuni(StringFilter interactiuni) {
        this.interactiuni = interactiuni;
    }

    public StringFilter getDozaRecomandata() {
        return dozaRecomandata;
    }

    public Optional<StringFilter> optionalDozaRecomandata() {
        return Optional.ofNullable(dozaRecomandata);
    }

    public StringFilter dozaRecomandata() {
        if (dozaRecomandata == null) {
            setDozaRecomandata(new StringFilter());
        }
        return dozaRecomandata;
    }

    public void setDozaRecomandata(StringFilter dozaRecomandata) {
        this.dozaRecomandata = dozaRecomandata;
    }

    public StringFilter getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public Optional<StringFilter> optionalFormaFarmaceutica() {
        return Optional.ofNullable(formaFarmaceutica);
    }

    public StringFilter formaFarmaceutica() {
        if (formaFarmaceutica == null) {
            setFormaFarmaceutica(new StringFilter());
        }
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(StringFilter formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public LongFilter getInfoExternId() {
        return infoExternId;
    }

    public Optional<LongFilter> optionalInfoExternId() {
        return Optional.ofNullable(infoExternId);
    }

    public LongFilter infoExternId() {
        if (infoExternId == null) {
            setInfoExternId(new LongFilter());
        }
        return infoExternId;
    }

    public void setInfoExternId(LongFilter infoExternId) {
        this.infoExternId = infoExternId;
    }

    public LongFilter getStudiiId() {
        return studiiId;
    }

    public Optional<LongFilter> optionalStudiiId() {
        return Optional.ofNullable(studiiId);
    }

    public LongFilter studiiId() {
        if (studiiId == null) {
            setStudiiId(new LongFilter());
        }
        return studiiId;
    }

    public void setStudiiId(LongFilter studiiId) {
        this.studiiId = studiiId;
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
        final MedicamentCriteria that = (MedicamentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(denumire, that.denumire) &&
            Objects.equals(substanta, that.substanta) &&
            Objects.equals(indicatii, that.indicatii) &&
            Objects.equals(contraindicatii, that.contraindicatii) &&
            Objects.equals(interactiuni, that.interactiuni) &&
            Objects.equals(dozaRecomandata, that.dozaRecomandata) &&
            Objects.equals(formaFarmaceutica, that.formaFarmaceutica) &&
            Objects.equals(infoExternId, that.infoExternId) &&
            Objects.equals(studiiId, that.studiiId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            denumire,
            substanta,
            indicatii,
            contraindicatii,
            interactiuni,
            dozaRecomandata,
            formaFarmaceutica,
            infoExternId,
            studiiId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDenumire().map(f -> "denumire=" + f + ", ").orElse("") +
            optionalSubstanta().map(f -> "substanta=" + f + ", ").orElse("") +
            optionalIndicatii().map(f -> "indicatii=" + f + ", ").orElse("") +
            optionalContraindicatii().map(f -> "contraindicatii=" + f + ", ").orElse("") +
            optionalInteractiuni().map(f -> "interactiuni=" + f + ", ").orElse("") +
            optionalDozaRecomandata().map(f -> "dozaRecomandata=" + f + ", ").orElse("") +
            optionalFormaFarmaceutica().map(f -> "formaFarmaceutica=" + f + ", ").orElse("") +
            optionalInfoExternId().map(f -> "infoExternId=" + f + ", ").orElse("") +
            optionalStudiiId().map(f -> "studiiId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
