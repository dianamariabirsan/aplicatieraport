package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

public class EvaluareMedicamentDTO {

    private String denumire;
    private String rezumatSiguranta;
    private Double scor;
    private String sursaUrl;
    private String contraindicatii;
    private String interactiuni;
    private String indicatii;
    private List<String> reguliTriggered = new ArrayList<>();

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getRezumatSiguranta() {
        return rezumatSiguranta;
    }

    public void setRezumatSiguranta(String rezumatSiguranta) {
        this.rezumatSiguranta = rezumatSiguranta;
    }

    public Double getScor() {
        return scor;
    }

    public void setScor(Double scor) {
        this.scor = scor;
    }

    public String getSursaUrl() {
        return sursaUrl;
    }

    public void setSursaUrl(String sursaUrl) {
        this.sursaUrl = sursaUrl;
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

    public String getIndicatii() {
        return indicatii;
    }

    public void setIndicatii(String indicatii) {
        this.indicatii = indicatii;
    }

    public List<String> getReguliTriggered() {
        return reguliTriggered;
    }

    public void setReguliTriggered(List<String> reguliTriggered) {
        this.reguliTriggered = reguliTriggered;
    }
}
