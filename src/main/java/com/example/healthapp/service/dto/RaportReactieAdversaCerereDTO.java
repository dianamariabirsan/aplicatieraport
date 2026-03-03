package com.example.healthapp.service.dto;

import com.example.healthapp.domain.enumeration.SeveritateReactie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RaportReactieAdversaCerereDTO {

    @NotNull
    private Long pacientId;

    @NotBlank
    private String descriere;

    private SeveritateReactie severitate;
    private String evolutie;
    private String raportatDe; // "PACIENT" / "FARMACIST" etc (sau email)

    public Long getPacientId() {
        return pacientId;
    }

    public void setPacientId(Long pacientId) {
        this.pacientId = pacientId;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public SeveritateReactie getSeveritate() {
        return severitate;
    }

    public void setSeveritate(SeveritateReactie severitate) {
        this.severitate = severitate;
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
}
