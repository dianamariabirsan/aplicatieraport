package com.example.healthapp.service.dto;

import jakarta.validation.constraints.NotNull;

public class EvaluareDecizieCerereDTO {

    @NotNull
    private Long pacientId;

    private Long medicId;

    // opțional: forțezi comparația A/B prin denumire
    private String tratamentA = "Mounjaro";
    private String tratamentB = "Wegovy";

    // opțional: comparație A/B prin ID
    private Long medicamentAId;
    private Long medicamentBId;

    private Double doza;
    private String frecventa;

    public Long getPacientId() {
        return pacientId;
    }

    public void setPacientId(Long pacientId) {
        this.pacientId = pacientId;
    }

    public Long getMedicId() {
        return medicId;
    }

    public void setMedicId(Long medicId) {
        this.medicId = medicId;
    }

    public String getTratamentA() {
        return tratamentA;
    }

    public void setTratamentA(String tratamentA) {
        this.tratamentA = tratamentA;
    }

    public String getTratamentB() {
        return tratamentB;
    }

    public void setTratamentB(String tratamentB) {
        this.tratamentB = tratamentB;
    }

    public Long getMedicamentAId() {
        return medicamentAId;
    }

    public void setMedicamentAId(Long medicamentAId) {
        this.medicamentAId = medicamentAId;
    }

    public Long getMedicamentBId() {
        return medicamentBId;
    }

    public void setMedicamentBId(Long medicamentBId) {
        this.medicamentBId = medicamentBId;
    }

    public Double getDoza() {
        return doza;
    }

    public void setDoza(Double doza) {
        this.doza = doza;
    }

    public String getFrecventa() {
        return frecventa;
    }

    public void setFrecventa(String frecventa) {
        this.frecventa = frecventa;
    }
}
