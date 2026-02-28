package com.example.healthapp.service.dto;

/**
 * DTO pentru analitice per medicament.
 * Folosit de GET /api/analitice/medicament/{id}
 */
public class AnaliticeMedicamentDTO {

    private Long medicamentId;
    private String denumire;
    private long totalReactiiAdverse;
    private long reactiiSevere;
    private double rataReactiiAdverse;
    private Double eficientaMedie;
    private long totalAlocari;

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public long getTotalReactiiAdverse() {
        return totalReactiiAdverse;
    }

    public void setTotalReactiiAdverse(long totalReactiiAdverse) {
        this.totalReactiiAdverse = totalReactiiAdverse;
    }

    public long getReactiiSevere() {
        return reactiiSevere;
    }

    public void setReactiiSevere(long reactiiSevere) {
        this.reactiiSevere = reactiiSevere;
    }

    public double getRataReactiiAdverse() {
        return rataReactiiAdverse;
    }

    public void setRataReactiiAdverse(double rataReactiiAdverse) {
        this.rataReactiiAdverse = rataReactiiAdverse;
    }

    public Double getEficientaMedie() {
        return eficientaMedie;
    }

    public void setEficientaMedie(Double eficientaMedie) {
        this.eficientaMedie = eficientaMedie;
    }

    public long getTotalAlocari() {
        return totalAlocari;
    }

    public void setTotalAlocari(long totalAlocari) {
        this.totalAlocari = totalAlocari;
    }
}
