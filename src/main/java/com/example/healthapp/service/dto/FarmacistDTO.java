package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Farmacist} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FarmacistDTO implements Serializable {

    private Long id;

    @NotNull
    private String nume;

    @NotNull
    private String prenume;

    @NotNull
    private String farmacie;

    @NotNull
    private String email;

    private String telefon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getFarmacie() {
        return farmacie;
    }

    public void setFarmacie(String farmacie) {
        this.farmacie = farmacie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FarmacistDTO)) {
            return false;
        }

        FarmacistDTO farmacistDTO = (FarmacistDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, farmacistDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FarmacistDTO{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", farmacie='" + getFarmacie() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            "}";
    }
}
