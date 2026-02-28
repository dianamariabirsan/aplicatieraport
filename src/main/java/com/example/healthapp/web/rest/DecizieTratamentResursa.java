package com.example.healthapp.web.rest;

import com.example.healthapp.service.DecizieTratamentService;
import com.example.healthapp.service.dto.EvaluareDecizieCerereDTO;
import com.example.healthapp.service.dto.EvaluareDecizieRezultatDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DecizieTratamentResursa {

    private final DecizieTratamentService service;

    public DecizieTratamentResursa(DecizieTratamentService service) {
        this.service = service;
    }

    @PostMapping("/decizie-tratament")
    public ResponseEntity<EvaluareDecizieRezultatDTO> evalueaza(@Valid @RequestBody EvaluareDecizieCerereDTO cerere) {
        return ResponseEntity.ok(service.evalueaza(cerere));
    }
}
