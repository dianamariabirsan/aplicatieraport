package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Monitorizare.
 */
@Entity
@Table(name = "monitorizare")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Monitorizare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_instant", nullable = false)
    private Instant dataInstant;

    @Column(name = "tensiune_sist")
    private Double tensiuneSist;

    @Column(name = "tensiune_diast")
    private Double tensiuneDiast;

    @Column(name = "puls")
    private Integer puls;

    @Column(name = "glicemie")
    private Double glicemie;

    @Column(name = "scor_eficacitate")
    private Double scorEficacitate;

    @Column(name = "comentarii")
    private String comentarii;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "alocaris", "reactiiAdverses", "monitorizaris", "medic", "farmacist" }, allowSetters = true)
    private Pacient pacient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Monitorizare id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataInstant() {
        return this.dataInstant;
    }

    public Monitorizare dataInstant(Instant dataInstant) {
        this.setDataInstant(dataInstant);
        return this;
    }

    public void setDataInstant(Instant dataInstant) {
        this.dataInstant = dataInstant;
    }

    public Double getTensiuneSist() {
        return this.tensiuneSist;
    }

    public Monitorizare tensiuneSist(Double tensiuneSist) {
        this.setTensiuneSist(tensiuneSist);
        return this;
    }

    public void setTensiuneSist(Double tensiuneSist) {
        this.tensiuneSist = tensiuneSist;
    }

    public Double getTensiuneDiast() {
        return this.tensiuneDiast;
    }

    public Monitorizare tensiuneDiast(Double tensiuneDiast) {
        this.setTensiuneDiast(tensiuneDiast);
        return this;
    }

    public void setTensiuneDiast(Double tensiuneDiast) {
        this.tensiuneDiast = tensiuneDiast;
    }

    public Integer getPuls() {
        return this.puls;
    }

    public Monitorizare puls(Integer puls) {
        this.setPuls(puls);
        return this;
    }

    public void setPuls(Integer puls) {
        this.puls = puls;
    }

    public Double getGlicemie() {
        return this.glicemie;
    }

    public Monitorizare glicemie(Double glicemie) {
        this.setGlicemie(glicemie);
        return this;
    }

    public void setGlicemie(Double glicemie) {
        this.glicemie = glicemie;
    }

    public Double getScorEficacitate() {
        return this.scorEficacitate;
    }

    public Monitorizare scorEficacitate(Double scorEficacitate) {
        this.setScorEficacitate(scorEficacitate);
        return this;
    }

    public void setScorEficacitate(Double scorEficacitate) {
        this.scorEficacitate = scorEficacitate;
    }

    public String getComentarii() {
        return this.comentarii;
    }

    public Monitorizare comentarii(String comentarii) {
        this.setComentarii(comentarii);
        return this;
    }

    public void setComentarii(String comentarii) {
        this.comentarii = comentarii;
    }

    public Pacient getPacient() {
        return this.pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public Monitorizare pacient(Pacient pacient) {
        this.setPacient(pacient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Monitorizare)) {
            return false;
        }
        return getId() != null && getId().equals(((Monitorizare) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Monitorizare{" +
            "id=" + getId() +
            ", dataInstant='" + getDataInstant() + "'" +
            ", tensiuneSist=" + getTensiuneSist() +
            ", tensiuneDiast=" + getTensiuneDiast() +
            ", puls=" + getPuls() +
            ", glicemie=" + getGlicemie() +
            ", scorEficacitate=" + getScorEficacitate() +
            ", comentarii='" + getComentarii() + "'" +
            "}";
    }
}
