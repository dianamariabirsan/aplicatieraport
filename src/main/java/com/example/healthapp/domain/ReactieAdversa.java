package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReactieAdversa.
 */
@Entity
@Table(name = "reactie_adversa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactieAdversa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_raportare", nullable = false)
    private Instant dataRaportare;

    @Column(name = "severitate")
    private String severitate;

    @NotNull
    @Column(name = "descriere", nullable = false)
    private String descriere;

    @Column(name = "evolutie")
    private String evolutie;

    @Column(name = "raportat_de")
    private String raportatDe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "infoExtern", "studiis" }, allowSetters = true)
    private Medicament medicament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alocaris", "reactiiAdverses", "monitorizaris", "medic", "farmacist" }, allowSetters = true)
    private Pacient pacient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReactieAdversa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataRaportare() {
        return this.dataRaportare;
    }

    public ReactieAdversa dataRaportare(Instant dataRaportare) {
        this.setDataRaportare(dataRaportare);
        return this;
    }

    public void setDataRaportare(Instant dataRaportare) {
        this.dataRaportare = dataRaportare;
    }

    public String getSeveritate() {
        return this.severitate;
    }

    public ReactieAdversa severitate(String severitate) {
        this.setSeveritate(severitate);
        return this;
    }

    public void setSeveritate(String severitate) {
        this.severitate = severitate;
    }

    public String getDescriere() {
        return this.descriere;
    }

    public ReactieAdversa descriere(String descriere) {
        this.setDescriere(descriere);
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getEvolutie() {
        return this.evolutie;
    }

    public ReactieAdversa evolutie(String evolutie) {
        this.setEvolutie(evolutie);
        return this;
    }

    public void setEvolutie(String evolutie) {
        this.evolutie = evolutie;
    }

    public String getRaportatDe() {
        return this.raportatDe;
    }

    public ReactieAdversa raportatDe(String raportatDe) {
        this.setRaportatDe(raportatDe);
        return this;
    }

    public void setRaportatDe(String raportatDe) {
        this.raportatDe = raportatDe;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public ReactieAdversa medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Pacient getPacient() {
        return this.pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public ReactieAdversa pacient(Pacient pacient) {
        this.setPacient(pacient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReactieAdversa)) {
            return false;
        }
        return getId() != null && getId().equals(((ReactieAdversa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactieAdversa{" +
            "id=" + getId() +
            ", dataRaportare='" + getDataRaportare() + "'" +
            ", severitate='" + getSeveritate() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", evolutie='" + getEvolutie() + "'" +
            ", raportatDe='" + getRaportatDe() + "'" +
            "}";
    }
}
