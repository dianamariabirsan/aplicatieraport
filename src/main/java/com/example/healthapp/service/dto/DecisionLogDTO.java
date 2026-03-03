package com.example.healthapp.service.dto;

import com.example.healthapp.domain.enumeration.ActorType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.DecisionLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant timestamp;

    @NotNull
    private ActorType actorType;

    private String recomandare;

    private Double modelScore;

    private String reguliTriggered;

    private String externalChecks;

    private String finalDecision;

    private ActorType decisionSource;

    private String overrideReason;

    private AlocareTratamentDTO alocare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public Double getModelScore() {
        return modelScore;
    }

    public void setModelScore(Double modelScore) {
        this.modelScore = modelScore;
    }

    public String getReguliTriggered() {
        return reguliTriggered;
    }

    public void setReguliTriggered(String reguliTriggered) {
        this.reguliTriggered = reguliTriggered;
    }

    public String getExternalChecks() {
        return externalChecks;
    }

    public void setExternalChecks(String externalChecks) {
        this.externalChecks = externalChecks;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    public ActorType getDecisionSource() {
        return decisionSource;
    }

    public void setDecisionSource(ActorType decisionSource) {
        this.decisionSource = decisionSource;
    }

    public String getOverrideReason() {
        return overrideReason;
    }

    public void setOverrideReason(String overrideReason) {
        this.overrideReason = overrideReason;
    }

    public AlocareTratamentDTO getAlocare() {
        return alocare;
    }

    public void setAlocare(AlocareTratamentDTO alocare) {
        this.alocare = alocare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionLogDTO)) {
            return false;
        }

        DecisionLogDTO decisionLogDTO = (DecisionLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, decisionLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionLogDTO{" +
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
            ", alocare=" + getAlocare() +
            "}";
    }
}
