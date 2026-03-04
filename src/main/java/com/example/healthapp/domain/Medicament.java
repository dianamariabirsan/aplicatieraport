package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medicament.
 */
@Entity
@Table(name = "medicament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "denumire", nullable = false)
    private String denumire;

    @NotNull
    @Column(name = "substanta", nullable = false)
    private String substanta;

    @Column(name = "indicatii", length = 4000)
    private String indicatii;

    @Column(name = "contraindicatii", length = 4000)
    private String contraindicatii;

    @Column(name = "interactiuni", length = 4000)
    private String interactiuni;

    @Column(name = "avertizari", length = 4000)
    private String avertizari;

    @Column(name = "doza_recomandata")
    private String dozaRecomandata;

    @Column(name = "forma_farmaceutica")
    private String formaFarmaceutica;

    @JsonIgnoreProperties(value = { "medicament" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ExternalDrugInfo infoExtern;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "medicament")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament" }, allowSetters = true)
    private Set<StudiiLiteratura> studiis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public Medicament denumire(String denumire) {
        this.setDenumire(denumire);
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getSubstanta() {
        return this.substanta;
    }

    public Medicament substanta(String substanta) {
        this.setSubstanta(substanta);
        return this;
    }

    public void setSubstanta(String substanta) {
        this.substanta = substanta;
    }

    public String getIndicatii() {
        return this.indicatii;
    }

    public Medicament indicatii(String indicatii) {
        this.setIndicatii(indicatii);
        return this;
    }

    public void setIndicatii(String indicatii) {
        this.indicatii = indicatii;
    }

    public String getContraindicatii() {
        return this.contraindicatii;
    }

    public Medicament contraindicatii(String contraindicatii) {
        this.setContraindicatii(contraindicatii);
        return this;
    }

    public void setContraindicatii(String contraindicatii) {
        this.contraindicatii = contraindicatii;
    }

    public String getInteractiuni() {
        return this.interactiuni;
    }

    public Medicament interactiuni(String interactiuni) {
        this.setInteractiuni(interactiuni);
        return this;
    }

    public void setInteractiuni(String interactiuni) {
        this.interactiuni = interactiuni;
    }

    public String getAvertizari() {
        return this.avertizari;
    }

    public Medicament avertizari(String avertizari) {
        this.setAvertizari(avertizari);
        return this;
    }

    public void setAvertizari(String avertizari) {
        this.avertizari = avertizari;
    }

    public String getDozaRecomandata() {
        return this.dozaRecomandata;
    }

    public Medicament dozaRecomandata(String dozaRecomandata) {
        this.setDozaRecomandata(dozaRecomandata);
        return this;
    }

    public void setDozaRecomandata(String dozaRecomandata) {
        this.dozaRecomandata = dozaRecomandata;
    }

    public String getFormaFarmaceutica() {
        return this.formaFarmaceutica;
    }

    public Medicament formaFarmaceutica(String formaFarmaceutica) {
        this.setFormaFarmaceutica(formaFarmaceutica);
        return this;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public ExternalDrugInfo getInfoExtern() {
        return this.infoExtern;
    }

    public void setInfoExtern(ExternalDrugInfo externalDrugInfo) {
        this.infoExtern = externalDrugInfo;
    }

    public Medicament infoExtern(ExternalDrugInfo externalDrugInfo) {
        this.setInfoExtern(externalDrugInfo);
        return this;
    }

    public Set<StudiiLiteratura> getStudiis() {
        return this.studiis;
    }

    public void setStudiis(Set<StudiiLiteratura> studiiLiteraturas) {
        if (this.studiis != null) {
            this.studiis.forEach(i -> i.setMedicament(null));
        }
        if (studiiLiteraturas != null) {
            studiiLiteraturas.forEach(i -> i.setMedicament(this));
        }
        this.studiis = studiiLiteraturas;
    }

    public Medicament studiis(Set<StudiiLiteratura> studiiLiteraturas) {
        this.setStudiis(studiiLiteraturas);
        return this;
    }

    public Medicament addStudii(StudiiLiteratura studiiLiteratura) {
        this.studiis.add(studiiLiteratura);
        studiiLiteratura.setMedicament(this);
        return this;
    }

    public Medicament removeStudii(StudiiLiteratura studiiLiteratura) {
        this.studiis.remove(studiiLiteratura);
        studiiLiteratura.setMedicament(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicament)) {
            return false;
        }
        return getId() != null && getId().equals(((Medicament) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicament{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", substanta='" + getSubstanta() + "'" +
            ", indicatii='" + getIndicatii() + "'" +
            ", contraindicatii='" + getContraindicatii() + "'" +
            ", interactiuni='" + getInteractiuni() + "'" +
            ", avertizari='" + getAvertizari() + "'" +
            ", dozaRecomandata='" + getDozaRecomandata() + "'" +
            ", formaFarmaceutica='" + getFormaFarmaceutica() + "'" +
            "}";
    }
}
