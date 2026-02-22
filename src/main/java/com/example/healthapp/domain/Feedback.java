package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "scor", nullable = false)
    private Integer scor;

    @Size(max = 500)
    @Column(name = "comentariu", length = 500)
    private String comentariu;

    @Column(name = "data_feedback")
    private Instant dataFeedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "deciziis", "feedbackuris", "medic", "medicament", "pacient" }, allowSetters = true)
    private AlocareTratament alocare;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScor() {
        return this.scor;
    }

    public Feedback scor(Integer scor) {
        this.setScor(scor);
        return this;
    }

    public void setScor(Integer scor) {
        this.scor = scor;
    }

    public String getComentariu() {
        return this.comentariu;
    }

    public Feedback comentariu(String comentariu) {
        this.setComentariu(comentariu);
        return this;
    }

    public void setComentariu(String comentariu) {
        this.comentariu = comentariu;
    }

    public Instant getDataFeedback() {
        return this.dataFeedback;
    }

    public Feedback dataFeedback(Instant dataFeedback) {
        this.setDataFeedback(dataFeedback);
        return this;
    }

    public void setDataFeedback(Instant dataFeedback) {
        this.dataFeedback = dataFeedback;
    }

    public AlocareTratament getAlocare() {
        return this.alocare;
    }

    public void setAlocare(AlocareTratament alocareTratament) {
        this.alocare = alocareTratament;
    }

    public Feedback alocare(AlocareTratament alocareTratament) {
        this.setAlocare(alocareTratament);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback)) {
            return false;
        }
        return getId() != null && getId().equals(((Feedback) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", scor=" + getScor() +
            ", comentariu='" + getComentariu() + "'" +
            ", dataFeedback='" + getDataFeedback() + "'" +
            "}";
    }
}
