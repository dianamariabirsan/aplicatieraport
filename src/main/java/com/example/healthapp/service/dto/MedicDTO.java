package com.example.healthapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Medic} entity.
 */
@Schema(description = "=====================\nENTITĂȚI DE BAZĂ\n=====================")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicDTO implements Serializable {

    private Long id;

    @NotNull
    private String nume;

    @NotNull
    private String prenume;

    @NotNull
    private String specializare;

    @NotNull
    private String email;

    private String telefon;

    private String cabinet;

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

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
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

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicDTO)) {
            return false;
        }

        MedicDTO medicDTO = (MedicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicDTO{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", specializare='" + getSpecializare() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", cabinet='" + getCabinet() + "'" +
            "}";
    }
}
