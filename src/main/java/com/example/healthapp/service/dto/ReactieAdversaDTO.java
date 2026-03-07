package com.example.healthapp.service.dto;

import com.example.healthapp.domain.enumeration.SeveritateReactie;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.ReactieAdversa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactieAdversaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dataRaportare;

    private SeveritateReactie severitate;

    @NotNull
    private String descriere;

    private String evolutie;

    private String raportatDe;

    private MedicamentDTO medicament;

    @NotNull
    private PacientDTO pacient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataRaportare() {
        return dataRaportare;
    }

    public void setDataRaportare(Instant dataRaportare) {
        this.dataRaportare = dataRaportare;
    }

    public SeveritateReactie getSeveritate() {
        return severitate;
    }

    public void setSeveritate(SeveritateReactie severitate) {
        this.severitate = severitate;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getEvolutie() {
        return evolutie;
    }

    public void setEvolutie(String evolutie) {
        this.evolutie = evolutie;
    }

    public String getRaportatDe() {
        return raportatDe;
    }

    public void setRaportatDe(String raportatDe) {
        this.raportatDe = raportatDe;
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
        if (!(o instanceof ReactieAdversaDTO)) {
            return false;
        }

        ReactieAdversaDTO reactieAdversaDTO = (ReactieAdversaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reactieAdversaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactieAdversaDTO{" +
            "id=" + getId() +
            ", dataRaportare='" + getDataRaportare() + "'" +
            ", severitate='" + getSeveritate() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", evolutie='" + getEvolutie() + "'" +
            ", raportatDe='" + getRaportatDe() + "'" +
            ", medicament=" + getMedicament() +
            ", pacient=" + getPacient() +
            "}";
    }
}
