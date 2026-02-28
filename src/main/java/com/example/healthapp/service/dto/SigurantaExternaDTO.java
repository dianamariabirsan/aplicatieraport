package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

public class SigurantaExternaDTO {

    private List<String> contraindicatiiGasite = new ArrayList<>();
    private List<String> interactiuniGasite = new ArrayList<>();
    private List<String> avertismente = new ArrayList<>();

    public List<String> getContraindicatiiGasite() {
        return contraindicatiiGasite;
    }

    public void setContraindicatiiGasite(List<String> contraindicatiiGasite) {
        this.contraindicatiiGasite = contraindicatiiGasite;
    }

    public List<String> getInteractiuniGasite() {
        return interactiuniGasite;
    }

    public void setInteractiuniGasite(List<String> interactiuniGasite) {
        this.interactiuniGasite = interactiuniGasite;
    }

    public List<String> getAvertismente() {
        return avertismente;
    }

    public void setAvertismente(List<String> avertismente) {
        this.avertismente = avertismente;
    }
}
