package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudiiLiteratura.
 */
@Entity
@Table(name = "studii_literatura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudiiLiteratura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titlu", nullable = false)
    private String titlu;

    @Column(name = "autori")
    private String autori;

    @Column(name = "anul")
    private Integer anul;

    @Column(name = "tip_studiu")
    private String tipStudiu;

    @Column(name = "substanta")
    private String substanta;

    @Column(name = "concluzie")
    private String concluzie;

    @Column(name = "link")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "infoExtern", "studiis" }, allowSetters = true)
    private Medicament medicament;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudiiLiteratura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitlu() {
        return this.titlu;
    }

    public StudiiLiteratura titlu(String titlu) {
        this.setTitlu(titlu);
        return this;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutori() {
        return this.autori;
    }

    public StudiiLiteratura autori(String autori) {
        this.setAutori(autori);
        return this;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public Integer getAnul() {
        return this.anul;
    }

    public StudiiLiteratura anul(Integer anul) {
        this.setAnul(anul);
        return this;
    }

    public void setAnul(Integer anul) {
        this.anul = anul;
    }

    public String getTipStudiu() {
        return this.tipStudiu;
    }

    public StudiiLiteratura tipStudiu(String tipStudiu) {
        this.setTipStudiu(tipStudiu);
        return this;
    }

    public void setTipStudiu(String tipStudiu) {
        this.tipStudiu = tipStudiu;
    }

    public String getSubstanta() {
        return this.substanta;
    }

    public StudiiLiteratura substanta(String substanta) {
        this.setSubstanta(substanta);
        return this;
    }

    public void setSubstanta(String substanta) {
        this.substanta = substanta;
    }

    public String getConcluzie() {
        return this.concluzie;
    }

    public StudiiLiteratura concluzie(String concluzie) {
        this.setConcluzie(concluzie);
        return this;
    }

    public void setConcluzie(String concluzie) {
        this.concluzie = concluzie;
    }

    public String getLink() {
        return this.link;
    }

    public StudiiLiteratura link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public StudiiLiteratura medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudiiLiteratura)) {
            return false;
        }
        return getId() != null && getId().equals(((StudiiLiteratura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudiiLiteratura{" +
            "id=" + getId() +
            ", titlu='" + getTitlu() + "'" +
            ", autori='" + getAutori() + "'" +
            ", anul=" + getAnul() +
            ", tipStudiu='" + getTipStudiu() + "'" +
            ", substanta='" + getSubstanta() + "'" +
            ", concluzie='" + getConcluzie() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
