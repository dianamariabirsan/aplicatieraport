package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.StudiiLiteratura} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudiiLiteraturaDTO implements Serializable {

    private Long id;

    @NotNull
    private String titlu;

    private String autori;

    private Integer anul;

    private String tipStudiu;

    private String substanta;

    private String concluzie;

    private String link;

    private MedicamentDTO medicament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public Integer getAnul() {
        return anul;
    }

    public void setAnul(Integer anul) {
        this.anul = anul;
    }

    public String getTipStudiu() {
        return tipStudiu;
    }

    public void setTipStudiu(String tipStudiu) {
        this.tipStudiu = tipStudiu;
    }

    public String getSubstanta() {
        return substanta;
    }

    public void setSubstanta(String substanta) {
        this.substanta = substanta;
    }

    public String getConcluzie() {
        return concluzie;
    }

    public void setConcluzie(String concluzie) {
        this.concluzie = concluzie;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudiiLiteraturaDTO)) {
            return false;
        }

        StudiiLiteraturaDTO studiiLiteraturaDTO = (StudiiLiteraturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studiiLiteraturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudiiLiteraturaDTO{" +
            "id=" + getId() +
            ", titlu='" + getTitlu() + "'" +
            ", autori='" + getAutori() + "'" +
            ", anul=" + getAnul() +
            ", tipStudiu='" + getTipStudiu() + "'" +
            ", substanta='" + getSubstanta() + "'" +
            ", concluzie='" + getConcluzie() + "'" +
            ", link='" + getLink() + "'" +
            ", medicament=" + getMedicament() +
            "}";
    }
}
