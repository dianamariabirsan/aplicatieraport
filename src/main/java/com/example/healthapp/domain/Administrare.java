package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Administrare.
 */
@Entity
@Table(name = "administrare")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Administrare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_administrare", nullable = false)
    private Instant dataAdministrare;

    @NotNull
    @Column(name = "tip_tratament", nullable = false)
    private String tipTratament;

    @Column(name = "doza")
    private Double doza;

    @Column(name = "unitate")
    private String unitate;

    @Column(name = "mod_administrare")
    private String modAdministrare;

    @Column(name = "observatii")
    private String observatii;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alocaris", "reactiiAdverses", "monitorizaris", "medic", "farmacist" }, allowSetters = true)
    private Pacient pacient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "administraris" }, allowSetters = true)
    private Farmacist farmacist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Administrare id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataAdministrare() {
        return this.dataAdministrare;
    }

    public Administrare dataAdministrare(Instant dataAdministrare) {
        this.setDataAdministrare(dataAdministrare);
        return this;
    }

    public void setDataAdministrare(Instant dataAdministrare) {
        this.dataAdministrare = dataAdministrare;
    }

    public String getTipTratament() {
        return this.tipTratament;
    }

    public Administrare tipTratament(String tipTratament) {
        this.setTipTratament(tipTratament);
        return this;
    }

    public void setTipTratament(String tipTratament) {
        this.tipTratament = tipTratament;
    }

    public Double getDoza() {
        return this.doza;
    }

    public Administrare doza(Double doza) {
        this.setDoza(doza);
        return this;
    }

    public void setDoza(Double doza) {
        this.doza = doza;
    }

    public String getUnitate() {
        return this.unitate;
    }

    public Administrare unitate(String unitate) {
        this.setUnitate(unitate);
        return this;
    }

    public void setUnitate(String unitate) {
        this.unitate = unitate;
    }

    public String getModAdministrare() {
        return this.modAdministrare;
    }

    public Administrare modAdministrare(String modAdministrare) {
        this.setModAdministrare(modAdministrare);
        return this;
    }

    public void setModAdministrare(String modAdministrare) {
        this.modAdministrare = modAdministrare;
    }

    public String getObservatii() {
        return this.observatii;
    }

    public Administrare observatii(String observatii) {
        this.setObservatii(observatii);
        return this;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Pacient getPacient() {
        return this.pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public Administrare pacient(Pacient pacient) {
        this.setPacient(pacient);
        return this;
    }

    public Farmacist getFarmacist() {
        return this.farmacist;
    }

    public void setFarmacist(Farmacist farmacist) {
        this.farmacist = farmacist;
    }

    public Administrare farmacist(Farmacist farmacist) {
        this.setFarmacist(farmacist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Administrare)) {
            return false;
        }
        return getId() != null && getId().equals(((Administrare) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Administrare{" +
            "id=" + getId() +
            ", dataAdministrare='" + getDataAdministrare() + "'" +
            ", tipTratament='" + getTipTratament() + "'" +
            ", doza=" + getDoza() +
            ", unitate='" + getUnitate() + "'" +
            ", modAdministrare='" + getModAdministrare() + "'" +
            ", observatii='" + getObservatii() + "'" +
            "}";
    }
}
