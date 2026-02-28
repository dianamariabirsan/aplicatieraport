package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO rezultat pentru POST /api/decizie/valideaza-propunere.
 * Descrie dacă propunerea medicului este validă, cu explicabilitate completă
 * (reguli, avertismente, verificări externe).
 */
public class ValidarePropunereRezultatDTO {

    private boolean propunereValida;
    private String medicamentDenumire;
    private List<String> reguli = new ArrayList<>();
    private List<String> avertismente = new ArrayList<>();
    private List<String> contraindicatiiGasite = new ArrayList<>();
    private List<String> interactiuniGasite = new ArrayList<>();
    private String motiv;
    private Long decisionLogId;

    public boolean isPropunereValida() {
        return propunereValida;
    }

    public void setPropunereValida(boolean propunereValida) {
        this.propunereValida = propunereValida;
    }

    public String getMedicamentDenumire() {
        return medicamentDenumire;
    }

    public void setMedicamentDenumire(String medicamentDenumire) {
        this.medicamentDenumire = medicamentDenumire;
    }

    public List<String> getReguli() {
        return reguli;
    }

    public void setReguli(List<String> reguli) {
        this.reguli = reguli;
    }

    public List<String> getAvertismente() {
        return avertismente;
    }

    public void setAvertismente(List<String> avertismente) {
        this.avertismente = avertismente;
    }

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

    public String getMotiv() {
        return motiv;
    }

    public void setMotiv(String motiv) {
        this.motiv = motiv;
    }

    public Long getDecisionLogId() {
        return decisionLogId;
    }

    public void setDecisionLogId(Long decisionLogId) {
        this.decisionLogId = decisionLogId;
    }
}
