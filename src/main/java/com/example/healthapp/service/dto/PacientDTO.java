package com.example.healthapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.healthapp.domain.Pacient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacientDTO implements Serializable {

    private Long id;

    @NotNull
    private String nume;

    @NotNull
    private String prenume;

    @NotNull
    private String sex;

    @NotNull
    private Integer varsta;

    private Double greutate;

    private Double inaltime;

    private Double circumferintaAbdominala;

    @Size(min = 13, max = 13)
    private String cnp;

    private String comorbiditati;

    private String gradSedentarism;

    private String istoricTratament;

    private String toleranta;

    private String email;

    private String telefon;

    private MedicDTO medic;

    private FarmacistDTO farmacist;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    public Double getGreutate() {
        return greutate;
    }

    public void setGreutate(Double greutate) {
        this.greutate = greutate;
    }

    public Double getInaltime() {
        return inaltime;
    }

    public void setInaltime(Double inaltime) {
        this.inaltime = inaltime;
    }

    public Double getCircumferintaAbdominala() {
        return circumferintaAbdominala;
    }

    public void setCircumferintaAbdominala(Double circumferintaAbdominala) {
        this.circumferintaAbdominala = circumferintaAbdominala;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getComorbiditati() {
        return comorbiditati;
    }

    public void setComorbiditati(String comorbiditati) {
        this.comorbiditati = comorbiditati;
    }

    public String getGradSedentarism() {
        return gradSedentarism;
    }

    public void setGradSedentarism(String gradSedentarism) {
        this.gradSedentarism = gradSedentarism;
    }

    public String getIstoricTratament() {
        return istoricTratament;
    }

    public void setIstoricTratament(String istoricTratament) {
        this.istoricTratament = istoricTratament;
    }

    public String getToleranta() {
        return toleranta;
    }

    public void setToleranta(String toleranta) {
        this.toleranta = toleranta;
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

    public MedicDTO getMedic() {
        return medic;
    }

    public void setMedic(MedicDTO medic) {
        this.medic = medic;
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
        if (!(o instanceof PacientDTO)) {
            return false;
        }

        PacientDTO pacientDTO = (PacientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacientDTO{" +
            "id=" + getId() +
            ", nume='" + getNume() + "'" +
            ", prenume='" + getPrenume() + "'" +
            ", sex='" + getSex() + "'" +
            ", varsta=" + getVarsta() +
            ", greutate=" + getGreutate() +
            ", inaltime=" + getInaltime() +
            ", circumferintaAbdominala=" + getCircumferintaAbdominala() +
            ", cnp='" + getCnp() + "'" +
            ", comorbiditati='" + getComorbiditati() + "'" +
            ", gradSedentarism='" + getGradSedentarism() + "'" +
            ", istoricTratament='" + getIstoricTratament() + "'" +
            ", toleranta='" + getToleranta() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", medic=" + getMedic() +
            ", farmacist=" + getFarmacist() +
            "}";
    }
}
