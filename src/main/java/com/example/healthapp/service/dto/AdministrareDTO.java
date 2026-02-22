package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Administrare} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdministrareDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dataAdministrare;

    @NotNull
    private String tipTratament;

    private Double doza;

    private String unitate;

    private String modAdministrare;

    private String observatii;

    private PacientDTO pacient;

    private FarmacistDTO farmacist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataAdministrare() {
        return dataAdministrare;
    }

    public void setDataAdministrare(Instant dataAdministrare) {
        this.dataAdministrare = dataAdministrare;
    }

    public String getTipTratament() {
        return tipTratament;
    }

    public void setTipTratament(String tipTratament) {
        this.tipTratament = tipTratament;
    }

    public Double getDoza() {
        return doza;
    }

    public void setDoza(Double doza) {
        this.doza = doza;
    }

    public String getUnitate() {
        return unitate;
    }

    public void setUnitate(String unitate) {
        this.unitate = unitate;
    }

    public String getModAdministrare() {
        return modAdministrare;
    }

    public void setModAdministrare(String modAdministrare) {
        this.modAdministrare = modAdministrare;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public PacientDTO getPacient() {
        return pacient;
    }

    public void setPacient(PacientDTO pacient) {
        this.pacient = pacient;
    }

    public FarmacistDTO getFarmacist() {
        return farmacist;
    }

    public void setFarmacist(FarmacistDTO farmacist) {
        this.farmacist = farmacist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdministrareDTO)) {
            return false;
        }

        AdministrareDTO administrareDTO = (AdministrareDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, administrareDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministrareDTO{" +
            "id=" + getId() +
            ", dataAdministrare='" + getDataAdministrare() + "'" +
            ", tipTratament='" + getTipTratament() + "'" +
            ", doza=" + getDoza() +
            ", unitate='" + getUnitate() + "'" +
            ", modAdministrare='" + getModAdministrare() + "'" +
            ", observatii='" + getObservatii() + "'" +
            ", pacient=" + getPacient() +
            ", farmacist=" + getFarmacist() +
            "}";
    }
}
