package com.example.healthapp.service.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de cerere pentru POST /api/decizie/recomanda-ab.
 * Sistemul recomandă între tratamentul A și tratamentul B.
 */
public class RecomandareAbCerereDTO {

    @NotNull
    private Long pacientId;

    private Long medicId;

    @NotNull
    private Long medicamentAId;

    @NotNull
    private Long medicamentBId;

    public Long getPacientId() {
        return pacientId;
    }

    public void setPacientId(Long pacientId) {
        this.pacientId = pacientId;
    }

    public Long getMedicId() {
        return medicId;
    }

    public void setMedicId(Long medicId) {
        this.medicId = medicId;
    }

    public Long getMedicamentAId() {
        return medicamentAId;
    }

    public void setMedicamentAId(Long medicamentAId) {
        this.medicamentAId = medicamentAId;
    }

    public Long getMedicamentBId() {
        return medicamentBId;
    }

    public void setMedicamentBId(Long medicamentBId) {
        this.medicamentBId = medicamentBId;
    }
}
