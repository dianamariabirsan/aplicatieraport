package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Medicament} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicamentDTO implements Serializable {

    private Long id;

    @NotNull
    private String denumire;

    @NotNull
    private String substanta;

    private String indicatii;

    private String contraindicatii;

    private String interactiuni;

    private String dozaRecomandata;

    private String formaFarmaceutica;

    private ExternalDrugInfoDTO infoExtern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getSubstanta() {
        return substanta;
    }

    public void setSubstanta(String substanta) {
        this.substanta = substanta;
    }

    public String getIndicatii() {
        return indicatii;
    }

    public void setIndicatii(String indicatii) {
        this.indicatii = indicatii;
    }

    public String getContraindicatii() {
        return contraindicatii;
    }

    public void setContraindicatii(String contraindicatii) {
        this.contraindicatii = contraindicatii;
    }

    public String getInteractiuni() {
        return interactiuni;
    }

    public void setInteractiuni(String interactiuni) {
        this.interactiuni = interactiuni;
    }

    public String getDozaRecomandata() {
        return dozaRecomandata;
    }

    public void setDozaRecomandata(String dozaRecomandata) {
        this.dozaRecomandata = dozaRecomandata;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public ExternalDrugInfoDTO getInfoExtern() {
        return infoExtern;
    }

    public void setInfoExtern(ExternalDrugInfoDTO infoExtern) {
        this.infoExtern = infoExtern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicamentDTO)) {
            return false;
        }

        MedicamentDTO medicamentDTO = (MedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentDTO{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", substanta='" + getSubstanta() + "'" +
            ", indicatii='" + getIndicatii() + "'" +
            ", contraindicatii='" + getContraindicatii() + "'" +
            ", interactiuni='" + getInteractiuni() + "'" +
            ", dozaRecomandata='" + getDozaRecomandata() + "'" +
            ", formaFarmaceutica='" + getFormaFarmaceutica() + "'" +
            ", infoExtern=" + getInfoExtern() +
            "}";
    }
}
