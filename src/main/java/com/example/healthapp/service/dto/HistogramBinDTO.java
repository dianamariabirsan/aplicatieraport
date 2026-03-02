package com.example.healthapp.service.dto;

/** DTO for a single histogram bin (range + count). */
public class HistogramBinDTO {

    public double from;
    public double to;
    public long count;

    public HistogramBinDTO() {}

    public HistogramBinDTO(double from, double to, long count) {
        this.from = from;
        this.to = to;
        this.count = count;
    }
}
