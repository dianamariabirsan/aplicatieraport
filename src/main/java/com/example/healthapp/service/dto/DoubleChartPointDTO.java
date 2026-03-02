package com.example.healthapp.service.dto;

/** DTO for a single chart data-point with a floating-point value (e.g. averages). */
public class DoubleChartPointDTO {

    public String label;
    public Double value;

    public DoubleChartPointDTO() {}

    public DoubleChartPointDTO(String label, Double value) {
        this.label = label;
        this.value = value;
    }
}
