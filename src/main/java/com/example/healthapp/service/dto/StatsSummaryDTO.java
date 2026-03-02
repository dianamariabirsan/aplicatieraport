package com.example.healthapp.service.dto;

import java.io.Serializable;
import java.util.Map;

public class StatsSummaryDTO implements Serializable {

    private long totalPacienti;
    private Map<String, Long> distributieTratament;
    private Map<Integer, Long> histVarsta;
    private Map<Integer, Long> histIMC;

    public long getTotalPacienti() { return totalPacienti; }
    public void setTotalPacienti(long totalPacienti) { this.totalPacienti = totalPacienti; }

    public Map<String, Long> getDistributieTratament() { return distributieTratament; }
    public void setDistributieTratament(Map<String, Long> distributieTratament) { this.distributieTratament = distributieTratament; }

    public Map<Integer, Long> getHistVarsta() { return histVarsta; }
    public void setHistVarsta(Map<Integer, Long> histVarsta) { this.histVarsta = histVarsta; }

    public Map<Integer, Long> getHistIMC() { return histIMC; }
    public void setHistIMC(Map<Integer, Long> histIMC) { this.histIMC = histIMC; }
}
