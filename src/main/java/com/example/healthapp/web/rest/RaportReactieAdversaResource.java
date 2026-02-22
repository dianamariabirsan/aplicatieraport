package com.example.healthapp.web.rest;

import com.example.healthapp.service.RaportReactieAdversaService;
import com.example.healthapp.service.dto.RaportReactieAdversaCerereDTO;
import com.example.healthapp.service.dto.RaportReactieAdversaRezultatDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adverse-reports")
public class RaportReactieAdversaResource {

    private final RaportReactieAdversaService service;

    public RaportReactieAdversaResource(RaportReactieAdversaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RaportReactieAdversaRezultatDTO> raporteaza(@Valid @RequestBody RaportReactieAdversaCerereDTO cerere) {
        return ResponseEntity.ok(service.raporteaza(cerere));
    }
}
