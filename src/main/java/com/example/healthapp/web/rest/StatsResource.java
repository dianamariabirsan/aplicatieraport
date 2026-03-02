package com.example.healthapp.web.rest;

import com.example.healthapp.service.StatsService;
import com.example.healthapp.service.dto.StatsSummaryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StatsResource {

    private final StatsService statsService;

    public StatsResource(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<StatsSummaryDTO> summary() {
        return ResponseEntity.ok(statsService.summary());
    }
}
