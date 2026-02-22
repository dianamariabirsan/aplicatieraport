package com.example.healthapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.example.healthapp.domain.Pacient} entity. This class is used
 * in {@link com.example.healthapp.web.rest.PacientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nume;

    private StringFilter prenume;

    private StringFilter sex;

    private IntegerFilter varsta;

    private DoubleFilter greutate;

    private DoubleFilter inaltime;

    private DoubleFilter circumferintaAbdominala;

    private StringFilter cnp;

    private StringFilter comorbiditati;

    private StringFilter gradSedentarism;

    private StringFilter istoricTratament;

    private StringFilter toleranta;

    private StringFilter email;

    private StringFilter telefon;

    private LongFilter alocariId;

    private LongFilter reactiiAdverseId;

    private LongFilter monitorizariId;

    private LongFilter medicId;

    private LongFilter farmacistId;

    private Boolean distinct;

    public PacientCriteria() {}

    public PacientCriteria(PacientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nume = other.optionalNume().map(StringFilter::copy).orElse(null);
        this.prenume = other.optionalPrenume().map(StringFilter::copy).orElse(null);
        this.sex = other.optionalSex().map(StringFilter::copy).orElse(null);
        this.varsta = other.optionalVarsta().map(IntegerFilter::copy).orElse(null);
        this.greutate = other.optionalGreutate().map(DoubleFilter::copy).orElse(null);
        this.inaltime = other.optionalInaltime().map(DoubleFilter::copy).orElse(null);
        this.circumferintaAbdominala = other.optionalCircumferintaAbdominala().map(DoubleFilter::copy).orElse(null);
        this.cnp = other.optionalCnp().map(StringFilter::copy).orElse(null);
        this.comorbiditati = other.optionalComorbiditati().map(StringFilter::copy).orElse(null);
        this.gradSedentarism = other.optionalGradSedentarism().map(StringFilter::copy).orElse(null);
        this.istoricTratament = other.optionalIstoricTratament().map(StringFilter::copy).orElse(null);
        this.toleranta = other.optionalToleranta().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefon = other.optionalTelefon().map(StringFilter::copy).orElse(null);
        this.alocariId = other.optionalAlocariId().map(LongFilter::copy).orElse(null);
        this.reactiiAdverseId = other.optionalReactiiAdverseId().map(LongFilter::copy).orElse(null);
        this.monitorizariId = other.optionalMonitorizariId().map(LongFilter::copy).orElse(null);
        this.medicId = other.optionalMedicId().map(LongFilter::copy).orElse(null);
        this.farmacistId = other.optionalFarmacistId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PacientCriteria copy() {
        return new PacientCriteria(this);
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

    public StringFilter getSex() {
        return sex;
    }

    public Optional<StringFilter> optionalSex() {
        return Optional.ofNullable(sex);
    }

    public StringFilter sex() {
        if (sex == null) {
            setSex(new StringFilter());
        }
        return sex;
    }

    public void setSex(StringFilter sex) {
        this.sex = sex;
    }

    public IntegerFilter getVarsta() {
        return varsta;
    }

    public Optional<IntegerFilter> optionalVarsta() {
        return Optional.ofNullable(varsta);
    }

    public IntegerFilter varsta() {
        if (varsta == null) {
            setVarsta(new IntegerFilter());
        }
        return varsta;
    }

    public void setVarsta(IntegerFilter varsta) {
        this.varsta = varsta;
    }

    public DoubleFilter getGreutate() {
        return greutate;
    }

    public Optional<DoubleFilter> optionalGreutate() {
        return Optional.ofNullable(greutate);
    }

    public DoubleFilter greutate() {
        if (greutate == null) {
            setGreutate(new DoubleFilter());
        }
        return greutate;
    }

    public void setGreutate(DoubleFilter greutate) {
        this.greutate = greutate;
    }

    public DoubleFilter getInaltime() {
        return inaltime;
    }

    public Optional<DoubleFilter> optionalInaltime() {
        return Optional.ofNullable(inaltime);
    }

    public DoubleFilter inaltime() {
        if (inaltime == null) {
            setInaltime(new DoubleFilter());
        }
        return inaltime;
    }

    public void setInaltime(DoubleFilter inaltime) {
        this.inaltime = inaltime;
    }

    public DoubleFilter getCircumferintaAbdominala() {
        return circumferintaAbdominala;
    }

    public Optional<DoubleFilter> optionalCircumferintaAbdominala() {
        return Optional.ofNullable(circumferintaAbdominala);
    }

    public DoubleFilter circumferintaAbdominala() {
        if (circumferintaAbdominala == null) {
            setCircumferintaAbdominala(new DoubleFilter());
        }
        return circumferintaAbdominala;
    }

    public void setCircumferintaAbdominala(DoubleFilter circumferintaAbdominala) {
        this.circumferintaAbdominala = circumferintaAbdominala;
    }

    public StringFilter getCnp() {
        return cnp;
    }

    public Optional<StringFilter> optionalCnp() {
        return Optional.ofNullable(cnp);
    }

    public StringFilter cnp() {
        if (cnp == null) {
            setCnp(new StringFilter());
        }
        return cnp;
    }

    public void setCnp(StringFilter cnp) {
        this.cnp = cnp;
    }

    public StringFilter getComorbiditati() {
        return comorbiditati;
    }

    public Optional<StringFilter> optionalComorbiditati() {
        return Optional.ofNullable(comorbiditati);
    }

    public StringFilter comorbiditati() {
        if (comorbiditati == null) {
            setComorbiditati(new StringFilter());
        }
        return comorbiditati;
    }

    public void setComorbiditati(StringFilter comorbiditati) {
        this.comorbiditati = comorbiditati;
    }

    public StringFilter getGradSedentarism() {
        return gradSedentarism;
    }

    public Optional<StringFilter> optionalGradSedentarism() {
        return Optional.ofNullable(gradSedentarism);
    }

    public StringFilter gradSedentarism() {
        if (gradSedentarism == null) {
            setGradSedentarism(new StringFilter());
        }
        return gradSedentarism;
    }

    public void setGradSedentarism(StringFilter gradSedentarism) {
        this.gradSedentarism = gradSedentarism;
    }

    public StringFilter getIstoricTratament() {
        return istoricTratament;
    }

    public Optional<StringFilter> optionalIstoricTratament() {
        return Optional.ofNullable(istoricTratament);
    }

    public StringFilter istoricTratament() {
        if (istoricTratament == null) {
            setIstoricTratament(new StringFilter());
        }
        return istoricTratament;
    }

    public void setIstoricTratament(StringFilter istoricTratament) {
        this.istoricTratament = istoricTratament;
    }

    public StringFilter getToleranta() {
        return toleranta;
    }

    public Optional<StringFilter> optionalToleranta() {
        return Optional.ofNullable(toleranta);
    }

    public StringFilter toleranta() {
        if (toleranta == null) {
            setToleranta(new StringFilter());
        }
        return toleranta;
    }

    public void setToleranta(StringFilter toleranta) {
        this.toleranta = toleranta;
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

    public LongFilter getAlocariId() {
        return alocariId;
    }

    public Optional<LongFilter> optionalAlocariId() {
        return Optional.ofNullable(alocariId);
    }

    public LongFilter alocariId() {
        if (alocariId == null) {
            setAlocariId(new LongFilter());
        }
        return alocariId;
    }

    public void setAlocariId(LongFilter alocariId) {
        this.alocariId = alocariId;
    }

    public LongFilter getReactiiAdverseId() {
        return reactiiAdverseId;
    }

    public Optional<LongFilter> optionalReactiiAdverseId() {
        return Optional.ofNullable(reactiiAdverseId);
    }

    public LongFilter reactiiAdverseId() {
        if (reactiiAdverseId == null) {
            setReactiiAdverseId(new LongFilter());
        }
        return reactiiAdverseId;
    }

    public void setReactiiAdverseId(LongFilter reactiiAdverseId) {
        this.reactiiAdverseId = reactiiAdverseId;
    }

    public LongFilter getMonitorizariId() {
        return monitorizariId;
    }

    public Optional<LongFilter> optionalMonitorizariId() {
        return Optional.ofNullable(monitorizariId);
    }

    public LongFilter monitorizariId() {
        if (monitorizariId == null) {
            setMonitorizariId(new LongFilter());
        }
        return monitorizariId;
    }

    public void setMonitorizariId(LongFilter monitorizariId) {
        this.monitorizariId = monitorizariId;
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
        final PacientCriteria that = (PacientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nume, that.nume) &&
            Objects.equals(prenume, that.prenume) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(varsta, that.varsta) &&
            Objects.equals(greutate, that.greutate) &&
            Objects.equals(inaltime, that.inaltime) &&
            Objects.equals(circumferintaAbdominala, that.circumferintaAbdominala) &&
            Objects.equals(cnp, that.cnp) &&
            Objects.equals(comorbiditati, that.comorbiditati) &&
            Objects.equals(gradSedentarism, that.gradSedentarism) &&
            Objects.equals(istoricTratament, that.istoricTratament) &&
            Objects.equals(toleranta, that.toleranta) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefon, that.telefon) &&
            Objects.equals(alocariId, that.alocariId) &&
            Objects.equals(reactiiAdverseId, that.reactiiAdverseId) &&
            Objects.equals(monitorizariId, that.monitorizariId) &&
            Objects.equals(medicId, that.medicId) &&
            Objects.equals(farmacistId, that.farmacistId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nume,
            prenume,
            sex,
            varsta,
            greutate,
            inaltime,
            circumferintaAbdominala,
            cnp,
            comorbiditati,
            gradSedentarism,
            istoricTratament,
            toleranta,
            email,
            telefon,
            alocariId,
            reactiiAdverseId,
            monitorizariId,
            medicId,
            farmacistId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNume().map(f -> "nume=" + f + ", ").orElse("") +
            optionalPrenume().map(f -> "prenume=" + f + ", ").orElse("") +
            optionalSex().map(f -> "sex=" + f + ", ").orElse("") +
            optionalVarsta().map(f -> "varsta=" + f + ", ").orElse("") +
            optionalGreutate().map(f -> "greutate=" + f + ", ").orElse("") +
            optionalInaltime().map(f -> "inaltime=" + f + ", ").orElse("") +
            optionalCircumferintaAbdominala().map(f -> "circumferintaAbdominala=" + f + ", ").orElse("") +
            optionalCnp().map(f -> "cnp=" + f + ", ").orElse("") +
            optionalComorbiditati().map(f -> "comorbiditati=" + f + ", ").orElse("") +
            optionalGradSedentarism().map(f -> "gradSedentarism=" + f + ", ").orElse("") +
            optionalIstoricTratament().map(f -> "istoricTratament=" + f + ", ").orElse("") +
            optionalToleranta().map(f -> "toleranta=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefon().map(f -> "telefon=" + f + ", ").orElse("") +
            optionalAlocariId().map(f -> "alocariId=" + f + ", ").orElse("") +
            optionalReactiiAdverseId().map(f -> "reactiiAdverseId=" + f + ", ").orElse("") +
            optionalMonitorizariId().map(f -> "monitorizariId=" + f + ", ").orElse("") +
            optionalMedicId().map(f -> "medicId=" + f + ", ").orElse("") +
            optionalFarmacistId().map(f -> "farmacistId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
