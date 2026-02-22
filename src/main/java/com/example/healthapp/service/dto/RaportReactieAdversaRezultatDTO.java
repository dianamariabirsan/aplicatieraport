package com.example.healthapp.service.dto;

public class RaportReactieAdversaRezultatDTO {

    private Long reactieAdversaId;
    private Long pacientId;
    private Long medicamentId;
    private String mesaj;

    public Long getReactieAdversaId() { return reactieAdversaId; }
    public void setReactieAdversaId(Long reactieAdversaId) { this.reactieAdversaId = reactieAdversaId; }

    public Long getPacientId() { return pacientId; }
    public void setPacientId(Long pacientId) { this.pacientId = pacientId; }

    public Long getMedicamentId() { return medicamentId; }
    public void setMedicamentId(Long medicamentId) { this.medicamentId = medicamentId; }

    public String getMesaj() { return mesaj; }
    public void setMesaj(String mesaj) { this.mesaj = mesaj; }
}
