package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO rezultat pentru POST /api/decizie/recomanda-ab.
 * Conține scorurile A și B, regulile declanșate, evidența studiilor clinice
 * și recomandarea finală cu explicabilitate completă.
 */
public class RecomandareAbRezultatDTO {

    private String denumireA;
    private String denumireB;
    private Double scorA;
    private Double scorB;
    private String recomandat;
    private List<String> motive = new ArrayList<>();
    private List<String> reguliA = new ArrayList<>();
    private List<String> reguliB = new ArrayList<>();
    private List<String> avertismente = new ArrayList<>();
    private EvidentaStudiuClinicDTO evidentaStudiuA;
    private EvidentaStudiuClinicDTO evidentaStudiuB;
    private Long decisionLogId;
    private Long alocareTratamentId;

    public String getDenumireA() {
        return denumireA;
    }

    public void setDenumireA(String denumireA) {
        this.denumireA = denumireA;
    }

    public String getDenumireB() {
        return denumireB;
    }

    public void setDenumireB(String denumireB) {
        this.denumireB = denumireB;
    }

    public Double getScorA() {
        return scorA;
    }

    public void setScorA(Double scorA) {
        this.scorA = scorA;
    }

    public Double getScorB() {
        return scorB;
    }

    public void setScorB(Double scorB) {
        this.scorB = scorB;
    }

    public String getRecomandat() {
        return recomandat;
    }

    public void setRecomandat(String recomandat) {
        this.recomandat = recomandat;
    }

    public List<String> getMotive() {
        return motive;
    }

    public void setMotive(List<String> motive) {
        this.motive = motive;
    }

    public List<String> getReguliA() {
        return reguliA;
    }

    public void setReguliA(List<String> reguliA) {
        this.reguliA = reguliA;
    }

    public List<String> getReguliB() {
        return reguliB;
    }

    public void setReguliB(List<String> reguliB) {
        this.reguliB = reguliB;
    }

    public List<String> getAvertismente() {
        return avertismente;
    }

    public void setAvertismente(List<String> avertismente) {
        this.avertismente = avertismente;
    }

    public EvidentaStudiuClinicDTO getEvidentaStudiuA() {
        return evidentaStudiuA;
    }

    public void setEvidentaStudiuA(EvidentaStudiuClinicDTO evidentaStudiuA) {
        this.evidentaStudiuA = evidentaStudiuA;
    }

    public EvidentaStudiuClinicDTO getEvidentaStudiuB() {
        return evidentaStudiuB;
    }

    public void setEvidentaStudiuB(EvidentaStudiuClinicDTO evidentaStudiuB) {
        this.evidentaStudiuB = evidentaStudiuB;
    }

    public Long getDecisionLogId() {
        return decisionLogId;
    }

    public void setDecisionLogId(Long decisionLogId) {
        this.decisionLogId = decisionLogId;
    }

    public Long getAlocareTratamentId() {
        return alocareTratamentId;
    }

    public void setAlocareTratamentId(Long alocareTratamentId) {
        this.alocareTratamentId = alocareTratamentId;
    }
}
