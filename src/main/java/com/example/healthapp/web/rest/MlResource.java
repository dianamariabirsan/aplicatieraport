package com.example.healthapp.web.rest;

import com.example.healthapp.service.ml.MlTrainingService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoint for triggering ML model training.
 */
@RestController
@RequestMapping("/api/ml")
public class MlResource {

    private static final Logger LOG = LoggerFactory.getLogger(MlResource.class);

    private final MlTrainingService mlTrainingService;

    public MlResource(MlTrainingService mlTrainingService) {
        this.mlTrainingService = mlTrainingService;
    }

    /**
     * POST /api/ml/train : Train the RandomForest model on current DB data.
     */
    @PostMapping("/train")
    public ResponseEntity<Map<String, Object>> train() {
        LOG.debug("REST request to train ML model");
        int samples = mlTrainingService.trainAndActivate();
        if (samples < 0) {
            return ResponseEntity.ok(Map.of(
                "status", "FAILED",
                "message", "Not enough training samples or training error."
            ));
        }
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "trainingSamples", samples
        ));
    }
}
