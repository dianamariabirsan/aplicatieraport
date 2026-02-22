package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RaportAnalitic.
 */
@Entity
@Table(name = "raport_analitic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RaportAnalitic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "perioada_start")
    private Instant perioadaStart;

    @Column(name = "perioada_end")
    private Instant perioadaEnd;

    @Column(name = "eficienta_medie")
    private Double eficientaMedie;

    @Column(name = "rata_reactii_adverse")
    private Double rataReactiiAdverse;

    @Column(name = "observatii")
    private String observatii;

    @Column(name = "concluzii")
    private String concluzii;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "infoExtern", "studiis" }, allowSetters = true)
    private Medicament medicament;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medic medic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RaportAnalitic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPerioadaStart() {
        return this.perioadaStart;
    }

    public RaportAnalitic perioadaStart(Instant perioadaStart) {
        this.setPerioadaStart(perioadaStart);
        return this;
    }

    public void setPerioadaStart(Instant perioadaStart) {
        this.perioadaStart = perioadaStart;
    }

    public Instant getPerioadaEnd() {
        return this.perioadaEnd;
    }

    public RaportAnalitic perioadaEnd(Instant perioadaEnd) {
        this.setPerioadaEnd(perioadaEnd);
        return this;
    }

    public void setPerioadaEnd(Instant perioadaEnd) {
        this.perioadaEnd = perioadaEnd;
    }

    public Double getEficientaMedie() {
        return this.eficientaMedie;
    }

    public RaportAnalitic eficientaMedie(Double eficientaMedie) {
        this.setEficientaMedie(eficientaMedie);
        return this;
    }

    public void setEficientaMedie(Double eficientaMedie) {
        this.eficientaMedie = eficientaMedie;
    }

    public Double getRataReactiiAdverse() {
        return this.rataReactiiAdverse;
    }

    public RaportAnalitic rataReactiiAdverse(Double rataReactiiAdverse) {
        this.setRataReactiiAdverse(rataReactiiAdverse);
        return this;
    }

    public void setRataReactiiAdverse(Double rataReactiiAdverse) {
        this.rataReactiiAdverse = rataReactiiAdverse;
    }

    public String getObservatii() {
        return this.observatii;
    }

    public RaportAnalitic observatii(String observatii) {
        this.setObservatii(observatii);
        return this;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public String getConcluzii() {
        return this.concluzii;
    }

    public RaportAnalitic concluzii(String concluzii) {
        this.setConcluzii(concluzii);
        return this;
    }

    public void setConcluzii(String concluzii) {
        this.concluzii = concluzii;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public RaportAnalitic medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Medic getMedic() {
        return this.medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public RaportAnalitic medic(Medic medic) {
        this.setMedic(medic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RaportAnalitic)) {
            return false;
        }
        return getId() != null && getId().equals(((RaportAnalitic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RaportAnalitic{" +
            "id=" + getId() +
            ", perioadaStart='" + getPerioadaStart() + "'" +
            ", perioadaEnd='" + getPerioadaEnd() + "'" +
            ", eficientaMedie=" + getEficientaMedie() +
            ", rataReactiiAdverse=" + getRataReactiiAdverse() +
            ", observatii='" + getObservatii() + "'" +
            ", concluzii='" + getConcluzii() + "'" +
            "}";
    }
}
