package com.example.healthapp.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RaportReactieAdversaCerereDTO {

    @NotNull
    private Long pacientId;

    @NotBlank
    private String descriere;

    private String severitate;
    private String evolutie;
    private String raportatDe; // "PACIENT" / "FARMACIST" etc (sau email)

    public Long getPacientId() { return pacientId; }
    public void setPacientId(Long pacientId) { this.pacientId = pacientId; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public String getSeveritate() { return severitate; }
    public void setSeveritate(String severitate) { this.severitate = severitate; }

    public String getEvolutie() { return evolutie; }
    public void setEvolutie(String evolutie) { this.evolutie = evolutie; }

    public String getRaportatDe() { return raportatDe; }
    public void setRaportatDe(String raportatDe) { this.raportatDe = raportatDe; }
}
