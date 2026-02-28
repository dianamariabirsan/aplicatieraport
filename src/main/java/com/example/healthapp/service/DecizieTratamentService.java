package com.example.healthapp.service;

import com.example.healthapp.service.dto.EvaluareDecizieCerereDTO;
import com.example.healthapp.service.dto.EvaluareDecizieRezultatDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviciu de decizie tratament — delegă logica la MotorDecizionalService.
 * Păstrat pentru compatibilitate cu endpoint-ul existent /api/decizie-tratament.
 */
@Service
@Transactional
public class DecizieTratamentService {

    private final MotorDecizionalService motorDecizionalService;

    public DecizieTratamentService(MotorDecizionalService motorDecizionalService) {
        this.motorDecizionalService = motorDecizionalService;
    }

    @Transactional
    public EvaluareDecizieRezultatDTO evalueaza(EvaluareDecizieCerereDTO cerere) {
        return motorDecizionalService.evalueaza(cerere);
    }
}
