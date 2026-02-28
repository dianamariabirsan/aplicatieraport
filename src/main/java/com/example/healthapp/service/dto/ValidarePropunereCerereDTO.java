package com.example.healthapp.service.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de cerere pentru POST /api/decizie/valideaza-propunere.
 * Medicul propune un tratament; sistemul îl validează.
 */
public class ValidarePropunereCerereDTO {

    @NotNull
    private Long pacientId;

    private Long medicId;

    @NotNull
    private Long medicamentPropusId;

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

    public Long getMedicamentPropusId() {
        return medicamentPropusId;
    }

    public void setMedicamentPropusId(Long medicamentPropusId) {
        this.medicamentPropusId = medicamentPropusId;
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
