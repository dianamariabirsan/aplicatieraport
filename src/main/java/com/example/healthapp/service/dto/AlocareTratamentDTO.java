package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.AlocareTratament} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlocareTratamentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dataDecizie;

    @NotNull
    private String tratamentPropus;

    private String motivDecizie;

    private Double scorDecizie;

    private Boolean decizieValidata;

    private MedicDTO medic;

    private MedicamentDTO medicament;

    private PacientDTO pacient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataDecizie() {
        return dataDecizie;
    }

    public void setDataDecizie(Instant dataDecizie) {
        this.dataDecizie = dataDecizie;
    }

    public String getTratamentPropus() {
        return tratamentPropus;
    }

    public void setTratamentPropus(String tratamentPropus) {
        this.tratamentPropus = tratamentPropus;
    }

    public String getMotivDecizie() {
        return motivDecizie;
    }

    public void setMotivDecizie(String motivDecizie) {
        this.motivDecizie = motivDecizie;
    }

    public Double getScorDecizie() {
        return scorDecizie;
    }

    public void setScorDecizie(Double scorDecizie) {
        this.scorDecizie = scorDecizie;
    }

    public Boolean getDecizieValidata() {
        return decizieValidata;
    }

    public void setDecizieValidata(Boolean decizieValidata) {
        this.decizieValidata = decizieValidata;
    }

    public MedicDTO getMedic() {
        return medic;
    }

    public void setMedic(MedicDTO medic) {
        this.medic = medic;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
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
        if (!(o instanceof AlocareTratamentDTO)) {
            return false;
        }

        AlocareTratamentDTO alocareTratamentDTO = (AlocareTratamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, alocareTratamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlocareTratamentDTO{" +
            "id=" + getId() +
            ", dataDecizie='" + getDataDecizie() + "'" +
            ", tratamentPropus='" + getTratamentPropus() + "'" +
            ", motivDecizie='" + getMotivDecizie() + "'" +
            ", scorDecizie=" + getScorDecizie() +
            ", decizieValidata='" + getDecizieValidata() + "'" +
            ", medic=" + getMedic() +
            ", medicament=" + getMedicament() +
            ", pacient=" + getPacient() +
            "}";
    }
}
