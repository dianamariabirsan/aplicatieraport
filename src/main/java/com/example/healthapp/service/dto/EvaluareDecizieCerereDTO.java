package com.example.healthapp.service.dto;

import jakarta.validation.constraints.NotNull;

public class EvaluareDecizieCerereDTO {

    @NotNull
    private Long pacientId;

    // opțional: forțezi comparația A/B
    private String tratamentA = "Mounjaro";
    private String tratamentB = "Wegovy";

    public Long getPacientId() {
        return pacientId;
    }
    public void setPacientId(Long pacientId) {
        this.pacientId = pacientId;
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
}
