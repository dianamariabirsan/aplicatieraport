package com.example.healthapp.service.dto;

import java.util.ArrayList;
import java.util.List;

public class EvaluareDecizieRezultatDTO {

    private String recomandare;
    private String motivare;

    private EvaluareMedicamentDTO evaluareA;
    private EvaluareMedicamentDTO evaluareB;

    private StudiiCliniceStatisticaDTO studiiClinice;
    private SigurantaExternaDTO sigurantaExterna;

    private List<String> linkuri = new ArrayList<>();

    /** Scor numeric A (0–100) — necesar UI */
    private Double scorA;

    /** Scor numeric B (0–100) — necesar UI */
    private Double scorB;

    /** Referință audit: id-ul AlocareTratament persisted */
    private Long alocareTratamentId;

    /** Referință audit: id-ul DecisionLog (SISTEM_AI) */
    private Long decisionLogId;

    /** Evidența studiilor clinice pentru A */
    private EvidentaStudiuClinicDTO evidentaStudiuA;

    /** Evidența studiilor clinice pentru B */
    private EvidentaStudiuClinicDTO evidentaStudiuB;

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public String getMotivare() {
        return motivare;
    }

    public void setMotivare(String motivare) {
        this.motivare = motivare;
    }

    public EvaluareMedicamentDTO getEvaluareA() {
        return evaluareA;
    }

    public void setEvaluareA(EvaluareMedicamentDTO evaluareA) {
        this.evaluareA = evaluareA;
    }

    public EvaluareMedicamentDTO getEvaluareB() {
        return evaluareB;
    }

    public void setEvaluareB(EvaluareMedicamentDTO evaluareB) {
        this.evaluareB = evaluareB;
    }

    public StudiiCliniceStatisticaDTO getStudiiClinice() {
        return studiiClinice;
    }

    public void setStudiiClinice(StudiiCliniceStatisticaDTO studiiClinice) {
        this.studiiClinice = studiiClinice;
    }

    public SigurantaExternaDTO getSigurantaExterna() {
        return sigurantaExterna;
    }

    public void setSigurantaExterna(SigurantaExternaDTO sigurantaExterna) {
        this.sigurantaExterna = sigurantaExterna;
    }

    public List<String> getLinkuri() {
        return linkuri;
    }

    public void setLinkuri(List<String> linkuri) {
        this.linkuri = linkuri;
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

    public Long getAlocareTratamentId() {
        return alocareTratamentId;
    }

    public void setAlocareTratamentId(Long alocareTratamentId) {
        this.alocareTratamentId = alocareTratamentId;
    }

    public Long getDecisionLogId() {
        return decisionLogId;
    }

    public void setDecisionLogId(Long decisionLogId) {
        this.decisionLogId = decisionLogId;
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
}
