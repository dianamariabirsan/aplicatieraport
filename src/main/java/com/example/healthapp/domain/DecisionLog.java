package com.example.healthapp.domain;

import com.example.healthapp.domain.enumeration.ActorType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DecisionLog.
 */
@Entity
@Table(name = "decision_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", nullable = false)
    private ActorType actorType;

    @Lob
    @Column(name = "recomandare")
    private String recomandare;

    @Column(name = "model_score")
    private Double modelScore;

    @Lob
    @Column(name = "reguli_triggered")
    private String reguliTriggered;

    @Lob
    @Column(name = "external_checks")
    private String externalChecks;

    @Lob
    @Column(name = "final_decision")
    private String finalDecision;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_source")
    private ActorType decisionSource;

    @Lob
    @Column(name = "override_reason")
    private String overrideReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "deciziis", "feedbackuris", "medic", "medicament", "pacient" }, allowSetters = true)
    private AlocareTratament alocare;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DecisionLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public DecisionLog timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ActorType getActorType() {
        return this.actorType;
    }

    public DecisionLog actorType(ActorType actorType) {
        this.setActorType(actorType);
        return this;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getRecomandare() {
        return this.recomandare;
    }

    public DecisionLog recomandare(String recomandare) {
        this.setRecomandare(recomandare);
        return this;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public Double getModelScore() {
        return this.modelScore;
    }

    public DecisionLog modelScore(Double modelScore) {
        this.setModelScore(modelScore);
        return this;
    }

    public void setModelScore(Double modelScore) {
        this.modelScore = modelScore;
    }

    public String getReguliTriggered() {
        return this.reguliTriggered;
    }

    public DecisionLog reguliTriggered(String reguliTriggered) {
        this.setReguliTriggered(reguliTriggered);
        return this;
    }

    public void setReguliTriggered(String reguliTriggered) {
        this.reguliTriggered = reguliTriggered;
    }

    public String getExternalChecks() {
        return this.externalChecks;
    }

    public DecisionLog externalChecks(String externalChecks) {
        this.setExternalChecks(externalChecks);
        return this;
    }

    public void setExternalChecks(String externalChecks) {
        this.externalChecks = externalChecks;
    }

    public String getFinalDecision() {
        return this.finalDecision;
    }

    public DecisionLog finalDecision(String finalDecision) {
        this.setFinalDecision(finalDecision);
        return this;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public ActorType getDecisionSource() {
        return this.decisionSource;
    }

    public DecisionLog decisionSource(ActorType decisionSource) {
        this.setDecisionSource(decisionSource);
        return this;
    }

    public void setDecisionSource(ActorType decisionSource) {
        this.decisionSource = decisionSource;
    }

    public String getOverrideReason() {
        return this.overrideReason;
    }

    public DecisionLog overrideReason(String overrideReason) {
        this.setOverrideReason(overrideReason);
        return this;
    }

    public void setOverrideReason(String overrideReason) {
        this.overrideReason = overrideReason;
    }

    public AlocareTratament getAlocare() {
        return this.alocare;
    }

    public void setAlocare(AlocareTratament alocareTratament) {
        this.alocare = alocareTratament;
    }

    public DecisionLog alocare(AlocareTratament alocareTratament) {
        this.setAlocare(alocareTratament);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionLog)) {
            return false;
        }
        return getId() != null && getId().equals(((DecisionLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionLog{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", actorType='" + getActorType() + "'" +
            ", recomandare='" + getRecomandare() + "'" +
            ", modelScore=" + getModelScore() +
            ", reguliTriggered='" + getReguliTriggered() + "'" +
            ", externalChecks='" + getExternalChecks() + "'" +
            ", finalDecision='" + getFinalDecision() + "'" +
            ", decisionSource='" + getDecisionSource() + "'" +
            ", overrideReason='" + getOverrideReason() + "'" +
            "}";
    }
}
