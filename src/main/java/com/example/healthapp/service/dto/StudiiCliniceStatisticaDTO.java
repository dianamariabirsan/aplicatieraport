package com.example.healthapp.service.dto;

public class StudiiCliniceStatisticaDTO {

    private String linkClinicalTrials;
    private String interogare;
    private Integer numarStudii;

    public String getLinkClinicalTrials() {
        return linkClinicalTrials;
    }

    public void setLinkClinicalTrials(String linkClinicalTrials) {
        this.linkClinicalTrials = linkClinicalTrials;
    }

    public String getInterogare() {
        return interogare;
    }

    public void setInterogare(String interogare) {
        this.interogare = interogare;
    }

    public Integer getNumarStudii() {
        return numarStudii;
    }

    public void setNumarStudii(Integer numarStudii) {
        this.numarStudii = numarStudii;
    }
}
