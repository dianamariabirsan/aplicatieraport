package com.example.healthapp.service;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.dto.StatsSummaryDTO;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private static final double HEIGHT_CM_THRESHOLD = 3.0;

    private final PacientRepository pacientRepository;
    private final AlocareTratamentRepository alocareTratamentRepository;

    public StatsService(PacientRepository pacientRepository, AlocareTratamentRepository alocareTratamentRepository) {
        this.pacientRepository = pacientRepository;
        this.alocareTratamentRepository = alocareTratamentRepository;
    }

    public StatsSummaryDTO summary() {
        var pacienti = pacientRepository.findAll();
        var alocari = alocareTratamentRepository.findAll();

        StatsSummaryDTO dto = new StatsSummaryDTO();
        dto.setTotalPacienti(pacienti.size());

        Map<String, Long> distTrat = alocari.stream()
            .map(a -> {
                String t = a.getTratamentPropus() == null ? "NESETAT" : a.getTratamentPropus().trim().toUpperCase();
                if (t.contains("MOUNJARO")) return "MOUNJARO";
                if (t.contains("WEGOVY")) return "WEGOVY";
                return t;
            })
            .collect(Collectors.groupingBy(t -> t, TreeMap::new, Collectors.counting()));
        dto.setDistributieTratament(distTrat);

        Map<Integer, Long> histVarsta = pacienti.stream()
            .filter(p -> p.getVarsta() != null)
            .collect(Collectors.groupingBy(Pacient::getVarsta, TreeMap::new, Collectors.counting()));
        dto.setHistVarsta(histVarsta);

        Map<Integer, Long> histImc = new TreeMap<>();
        for (Pacient p : pacienti) {
            if (p.getGreutate() == null || p.getInaltime() == null) continue;
            double h = p.getInaltime();
            if (h > HEIGHT_CM_THRESHOLD) h = h / 100.0;
            if (h <= 0) continue;
            double imc = p.getGreutate() / (h * h);
            int bucket = (int) Math.round(imc);
            histImc.merge(bucket, 1L, Long::sum);
        }
        dto.setHistIMC(histImc);

        return dto;
    }
}
