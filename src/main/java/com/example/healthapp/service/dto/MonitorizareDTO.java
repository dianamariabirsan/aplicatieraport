package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Monitorizare} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitorizareDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dataInstant;

    @DecimalMin(value = "60.0")
    @DecimalMax(value = "300.0")
    private Double tensiuneSist;

    @DecimalMin(value = "30.0")
    @DecimalMax(value = "200.0")
    private Double tensiuneDiast;

    @Min(20)
    @Max(250)
    private Integer puls;

    @DecimalMin(value = "2.0")
    @DecimalMax(value = "1000.0")
    private Double glicemie;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double scorEficacitate;

    private String comentarii;

    private PacientDTO pacient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataInstant() {
        return dataInstant;
    }

    public void setDataInstant(Instant dataInstant) {
        this.dataInstant = dataInstant;
    }

    public Double getTensiuneSist() {
        return tensiuneSist;
    }

    public void setTensiuneSist(Double tensiuneSist) {
        this.tensiuneSist = tensiuneSist;
    }

    public Double getTensiuneDiast() {
        return tensiuneDiast;
    }

    public void setTensiuneDiast(Double tensiuneDiast) {
        this.tensiuneDiast = tensiuneDiast;
    }

    public Integer getPuls() {
        return puls;
    }

    public void setPuls(Integer puls) {
        this.puls = puls;
    }

    public Double getGlicemie() {
        return glicemie;
    }

    public void setGlicemie(Double glicemie) {
        this.glicemie = glicemie;
    }

    public Double getScorEficacitate() {
        return scorEficacitate;
    }

    public void setScorEficacitate(Double scorEficacitate) {
        this.scorEficacitate = scorEficacitate;
    }

    public String getComentarii() {
        return comentarii;
    }

    public void setComentarii(String comentarii) {
        this.comentarii = comentarii;
    }

    public PacientDTO getPacient() {
        return pacient;
    }

    public void setPacient(PacientDTO pacient) {
        this.pacient = pacient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitorizareDTO)) {
            return false;
        }

        MonitorizareDTO monitorizareDTO = (MonitorizareDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monitorizareDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitorizareDTO{" +
            "id=" + getId() +
            ", dataInstant='" + getDataInstant() + "'" +
            ", tensiuneSist=" + getTensiuneSist() +
            ", tensiuneDiast=" + getTensiuneDiast() +
            ", puls=" + getPuls() +
            ", glicemie=" + getGlicemie() +
            ", scorEficacitate=" + getScorEficacitate() +
            ", comentarii='" + getComentarii() + "'" +
            ", pacient=" + getPacient() +
            "}";
    }
}
