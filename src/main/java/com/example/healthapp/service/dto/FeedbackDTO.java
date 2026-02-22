package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Feedback} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer scor;

    @Size(max = 500)
    private String comentariu;

    private Instant dataFeedback;

    private AlocareTratamentDTO alocare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScor() {
        return scor;
    }

    public void setScor(Integer scor) {
        this.scor = scor;
    }

    public String getComentariu() {
        return comentariu;
    }

    public void setComentariu(String comentariu) {
        this.comentariu = comentariu;
    }

    public Instant getDataFeedback() {
        return dataFeedback;
    }

    public void setDataFeedback(Instant dataFeedback) {
        this.dataFeedback = dataFeedback;
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
        if (!(o instanceof FeedbackDTO)) {
            return false;
        }

        FeedbackDTO feedbackDTO = (FeedbackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedbackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackDTO{" +
            "id=" + getId() +
            ", scor=" + getScor() +
            ", comentariu='" + getComentariu() + "'" +
            ", dataFeedback='" + getDataFeedback() + "'" +
            ", alocare=" + getAlocare() +
            "}";
    }
}
