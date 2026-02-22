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
 * A Farmacist.
 */
@Entity
@Table(name = "farmacist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Farmacist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nume", nullable = false)
    private String nume;

    @NotNull
    @Column(name = "prenume", nullable = false)
    private String prenume;

    @NotNull
    @Column(name = "farmacie", nullable = false)
    private String farmacie;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefon")
    private String telefon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "farmacist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pacient", "farmacist" }, allowSetters = true)
    private Set<Administrare> administraris = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Farmacist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return this.nume;
    }

    public Farmacist nume(String nume) {
        this.setNume(nume);
        return this;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public Farmacist prenume(String prenume) {
        this.setPrenume(prenume);
        return this;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getFarmacie() {
        return this.farmacie;
    }

    public Farmacist farmacie(String farmacie) {
        this.setFarmacie(farmacie);
        return this;
    }

    public void setFarmacie(String farmacie) {
        this.farmacie = farmacie;
    }

    public String getEmail() {
        return this.email;
    }

    public Farmacist email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public Farmacist telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Set<Administrare> getAdministraris() {
        return this.administraris;
    }

    public void setAdministraris(Set<Administrare> administrares) {
        if (this.administraris != null) {
            this.administraris.forEach(i -> i.setFarmacist(null));
        }
        if (administrares != null) {
            administrares.forEach(i -> i.setFarmacist(this));
        }
        this.administraris = administrares;
    }

    public Farmacist administraris(Set<Administrare> administrares) {
        this.setAdministraris(administrares);
        return this;
    }

    public Farmacist addAdministrari(Administrare administrare) {
        this.administraris.add(administrare);
        administrare.setFarmacist(this);
        return this;
    }

    public Farmacist removeAdministrari(Administrare administrare) {
        this.administraris.remove(administrare);
        administrare.setFarmacist(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Farmacist)) {
            return false;
        }
        return getId() != null && getId().equals(((Farmacist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Farmacist{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", farmacie='" + getFarmacie() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            "}";
    }
}
