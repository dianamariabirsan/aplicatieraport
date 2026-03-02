package com.example.healthapp.service.dto;

/** DTO for a single chart data-point (label + count). */
public class ChartPointDTO {

    public String label;
    public Long value;

    public ChartPointDTO() {}

    public ChartPointDTO(String label, Long value) {
        this.label = label;
        this.value = value;
    }
}
