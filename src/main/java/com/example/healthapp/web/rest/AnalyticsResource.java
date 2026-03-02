package com.example.healthapp.web.rest;

import com.example.healthapp.service.AnalyticsService;
import com.example.healthapp.service.dto.ChartPointDTO;
import com.example.healthapp.service.dto.DoubleChartPointDTO;
import com.example.healthapp.service.dto.HistogramBinDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for analytics charts (allocations by medication, score histogram).
 * All endpoints are read-only (GET).
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsResource {

    private final AnalyticsService analyticsService;

    public AnalyticsResource(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * {@code GET /api/analytics/alocari/by-medicament?dataSource=ALL} :
     * bar-chart data — allocation counts grouped by medication.
     */
    @GetMapping("/alocari/by-medicament")
    public ResponseEntity<List<ChartPointDTO>> alocariByMedicament(
        @RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource
    ) {
        return ResponseEntity.ok(analyticsService.alocariByMedicament(dataSource));
    }

    /**
     * {@code GET /api/analytics/scor/histogram?medicament=Wegovy&bins=10&dataSource=ALL} :
     * histogram of decision scores.
     */
    @GetMapping("/scor/histogram")
    public ResponseEntity<List<HistogramBinDTO>> scorHistogram(
        @RequestParam(value = "medicament", required = false) String medicament,
        @RequestParam(value = "bins", defaultValue = "10") int bins,
        @RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource
    ) {
        return ResponseEntity.ok(analyticsService.scorHistogram(medicament, bins, dataSource));
    }

    /**
     * {@code GET /api/analytics/alocari/by-month?dataSource=ALL} :
     * line/bar-chart data — allocation counts grouped by year-month.
     */
    @GetMapping("/alocari/by-month")
    public ResponseEntity<List<ChartPointDTO>> alocariByMonth(@RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource) {
        return ResponseEntity.ok(analyticsService.alocariByMonth(dataSource));
    }

    /**
     * {@code GET /api/analytics/pacienti/by-age-group?dataSource=ALL} :
     * bar-chart data — patient counts in 10-year age buckets.
     */
    @GetMapping("/pacienti/by-age-group")
    public ResponseEntity<List<ChartPointDTO>> pacientByAgeGroup(
        @RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource
    ) {
        return ResponseEntity.ok(analyticsService.pacientByAgeGroup(dataSource));
    }

    /**
     * {@code GET /api/analytics/pacienti/by-sex?dataSource=ALL} :
     * bar-chart data — patient counts grouped by sex.
     */
    @GetMapping("/pacienti/by-sex")
    public ResponseEntity<List<ChartPointDTO>> pacientBySex(@RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource) {
        return ResponseEntity.ok(analyticsService.pacientBySex(dataSource));
    }

    /**
     * {@code GET /api/analytics/alocari/by-validated?dataSource=ALL} :
     * bar-chart data — allocations split by validation status.
     */
    @GetMapping("/alocari/by-validated")
    public ResponseEntity<List<ChartPointDTO>> alocariByValidated(
        @RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource
    ) {
        return ResponseEntity.ok(analyticsService.alocariByValidated(dataSource));
    }

    /**
     * {@code GET /api/analytics/alocari/avg-score-by-medicament?dataSource=ALL} :
     * bar-chart data — average decision score per medication.
     */
    @GetMapping("/alocari/avg-score-by-medicament")
    public ResponseEntity<List<DoubleChartPointDTO>> avgScoreByMedicament(
        @RequestParam(value = "dataSource", defaultValue = "ALL") String dataSource
    ) {
        return ResponseEntity.ok(analyticsService.avgScoreByMedicament(dataSource));
    }
}
