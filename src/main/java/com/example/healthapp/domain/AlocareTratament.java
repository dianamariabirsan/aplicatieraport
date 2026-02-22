package com.example.healthapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlocareTratament.
 */
@Entity
@Table(name = "alocare_tratament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlocareTratament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_decizie", nullable = false)
    private Instant dataDecizie;

    @NotNull
    @Column(name = "tratament_propus", nullable = false)
    private String tratamentPropus;

    @Column(name = "motiv_decizie")
    private String motivDecizie;

    @Column(name = "scor_decizie")
    private Double scorDecizie;

    @Column(name = "decizie_validata")
    private Boolean decizieValidata;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alocare")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alocare" }, allowSetters = true)
    private Set<DecisionLog> deciziis = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alocare")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alocare" }, allowSetters = true)
    private Set<Feedback> feedbackuris = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Medic medic;

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

    public AlocareTratament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataDecizie() {
        return this.dataDecizie;
    }

    public AlocareTratament dataDecizie(Instant dataDecizie) {
        this.setDataDecizie(dataDecizie);
        return this;
    }

    public void setDataDecizie(Instant dataDecizie) {
        this.dataDecizie = dataDecizie;
    }

    public String getTratamentPropus() {
        return this.tratamentPropus;
    }

    public AlocareTratament tratamentPropus(String tratamentPropus) {
        this.setTratamentPropus(tratamentPropus);
        return this;
    }

    public void setTratamentPropus(String tratamentPropus) {
        this.tratamentPropus = tratamentPropus;
    }

    public String getMotivDecizie() {
        return this.motivDecizie;
    }

    public AlocareTratament motivDecizie(String motivDecizie) {
        this.setMotivDecizie(motivDecizie);
        return this;
    }

    public void setMotivDecizie(String motivDecizie) {
        this.motivDecizie = motivDecizie;
    }

    public Double getScorDecizie() {
        return this.scorDecizie;
    }

    public AlocareTratament scorDecizie(Double scorDecizie) {
        this.setScorDecizie(scorDecizie);
        return this;
    }

    public void setScorDecizie(Double scorDecizie) {
        this.scorDecizie = scorDecizie;
    }

    public Boolean getDecizieValidata() {
        return this.decizieValidata;
    }

    public AlocareTratament decizieValidata(Boolean decizieValidata) {
        this.setDecizieValidata(decizieValidata);
        return this;
    }

    public void setDecizieValidata(Boolean decizieValidata) {
        this.decizieValidata = decizieValidata;
    }

    public Set<DecisionLog> getDeciziis() {
        return this.deciziis;
    }

    public void setDeciziis(Set<DecisionLog> decisionLogs) {
        if (this.deciziis != null) {
            this.deciziis.forEach(i -> i.setAlocare(null));
        }
        if (decisionLogs != null) {
            decisionLogs.forEach(i -> i.setAlocare(this));
        }
        this.deciziis = decisionLogs;
    }

    public AlocareTratament deciziis(Set<DecisionLog> decisionLogs) {
        this.setDeciziis(decisionLogs);
        return this;
    }

    public AlocareTratament addDecizii(DecisionLog decisionLog) {
        this.deciziis.add(decisionLog);
        decisionLog.setAlocare(this);
        return this;
    }

    public AlocareTratament removeDecizii(DecisionLog decisionLog) {
        this.deciziis.remove(decisionLog);
        decisionLog.setAlocare(null);
        return this;
    }

    public Set<Feedback> getFeedbackuris() {
        return this.feedbackuris;
    }

    public void setFeedbackuris(Set<Feedback> feedbacks) {
        if (this.feedbackuris != null) {
            this.feedbackuris.forEach(i -> i.setAlocare(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setAlocare(this));
        }
        this.feedbackuris = feedbacks;
    }

    public AlocareTratament feedbackuris(Set<Feedback> feedbacks) {
        this.setFeedbackuris(feedbacks);
        return this;
    }

    public AlocareTratament addFeedbackuri(Feedback feedback) {
        this.feedbackuris.add(feedback);
        feedback.setAlocare(this);
        return this;
    }

    public AlocareTratament removeFeedbackuri(Feedback feedback) {
        this.feedbackuris.remove(feedback);
        feedback.setAlocare(null);
        return this;
    }

    public Medic getMedic() {
        return this.medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public AlocareTratament medic(Medic medic) {
        this.setMedic(medic);
        return this;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public AlocareTratament medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Pacient getPacient() {
        return this.pacient;
    }

    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public AlocareTratament pacient(Pacient pacient) {
        this.setPacient(pacient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlocareTratament)) {
            return false;
        }
        return getId() != null && getId().equals(((AlocareTratament) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlocareTratament{" +
            "id=" + getId() +
            ", dataDecizie='" + getDataDecizie() + "'" +
            ", tratamentPropus='" + getTratamentPropus() + "'" +
            ", motivDecizie='" + getMotivDecizie() + "'" +
            ", scorDecizie=" + getScorDecizie() +
            ", decizieValidata='" + getDecizieValidata() + "'" +
            "}";
    }
}
