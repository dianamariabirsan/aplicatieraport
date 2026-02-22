package com.example.healthapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * =====================
 * ENTITĂȚI DE BAZĂ
 * =====================
 */
@Entity
@Table(name = "medic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medic implements Serializable {

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
    @Column(name = "specializare", nullable = false)
    private String specializare;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "cabinet")
    private String cabinet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return this.nume;
    }

    public Medic nume(String nume) {
        this.setNume(nume);
        return this;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return this.prenume;
    }

    public Medic prenume(String prenume) {
        this.setPrenume(prenume);
        return this;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getSpecializare() {
        return this.specializare;
    }

    public Medic specializare(String specializare) {
        this.setSpecializare(specializare);
        return this;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }

    public String getEmail() {
        return this.email;
    }

    public Medic email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public Medic telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getCabinet() {
        return this.cabinet;
    }

    public Medic cabinet(String cabinet) {
        this.setCabinet(cabinet);
        return this;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medic)) {
            return false;
        }
        return getId() != null && getId().equals(((Medic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medic{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", specializare='" + getSpecializare() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", cabinet='" + getCabinet() + "'" +
            "}";
    }
}
