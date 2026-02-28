package com.example.healthapp.service.dto;

/**
 * DTO pentru datele unui studiu clinic (ClinicalTrials.gov).
 * Folosit de EvidentaStudiiCliniceService.
 */
public class EvidentaStudiuClinicDTO {

    private String nctId;
    private String titlu;
    private String status;
    private String faza;
    private Integer inrolare;
    private String dataInceput;
    private String dataFinal;
    private String sponsor;
    private String linkUrl;

    public String getNctId() {
        return nctId;
    }

    public void setNctId(String nctId) {
        this.nctId = nctId;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFaza() {
        return faza;
    }

    public void setFaza(String faza) {
        this.faza = faza;
    }

    public Integer getInrolare() {
        return inrolare;
    }

    public void setInrolare(Integer inrolare) {
        this.inrolare = inrolare;
    }

    public String getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(String dataInceput) {
        this.dataInceput = dataInceput;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
