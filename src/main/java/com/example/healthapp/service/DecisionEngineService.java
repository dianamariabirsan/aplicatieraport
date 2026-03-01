package com.example.healthapp.service;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.repository.DecisionLogRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor decizional: evaluează administrările pacientului față de contraindicațiile/interacțiunile
 * medicamentului propus și creează automat un DecisionLog cu scorul calculat.
 */
@Service
@Transactional
public class DecisionEngineService {

    private static final int MIN_TOKEN_LENGTH = 4;

    private final AdministrareRepository administrareRepository;
    private final DecisionLogRepository decisionLogRepository;

    public DecisionEngineService(AdministrareRepository administrareRepository, DecisionLogRepository decisionLogRepository) {
        this.administrareRepository = administrareRepository;
        this.decisionLogRepository = decisionLogRepository;
    }

    public EngineResult evaluateAndLog(AlocareTratament alocare) {
        if (alocare == null || alocare.getPacient() == null || alocare.getMedicament() == null) {
            return new EngineResult(0.0, List.of("DATE_INSUFICIENTE"), "Nu pot evalua: lipsesc pacient sau medicament.");
        }

        Medicament med = alocare.getMedicament();

        String contraind = safeLower(med.getContraindicatii());
        String interact = safeLower(med.getInteractiuni());
        String avert = safeLower(med.getAvertizari());

        List<Administrare> admin = administrareRepository.findAllByPacientId(alocare.getPacient().getId());

        List<String> reguli = new ArrayList<>();
        List<String> checks = new ArrayList<>();

        for (Administrare a : admin) {
            String tip = safeLower(a.getTipTratament());
            if (tip.isBlank()) continue;

            if (!contraind.isBlank() && containsWord(contraind, tip)) {
                reguli.add("CONTRAINDICATIE: " + a.getTipTratament());
                checks.add("SmPC.contraindicatii conține '" + firstToken(tip) + "'");
            }
            if (!interact.isBlank() && containsWord(interact, tip)) {
                reguli.add("INTERACTIUNE: " + a.getTipTratament());
                checks.add("SmPC.interactiuni conține '" + firstToken(tip) + "'");
            }
            if (!avert.isBlank() && containsWord(avert, tip)) {
                reguli.add("AVERTIZARE: " + a.getTipTratament());
                checks.add("SmPC.avertizari conține '" + firstToken(tip) + "'");
            }
        }

        long contraCount = reguli.stream().filter(r -> r.startsWith("CONTRAINDICATIE")).count();
        long interCount = reguli.stream().filter(r -> r.startsWith("INTERACTIUNE")).count();
        long avertCount = reguli.stream().filter(r -> r.startsWith("AVERTIZARE")).count();

        double score = 1.0;
        score -= contraCount * 0.6;
        score -= interCount * 0.3;
        score -= avertCount * 0.1;
        if (score < 0) score = 0;

        String recomandare;
        if (contraCount > 0) {
            recomandare = "NU recomand: contraindicații detectate (" + contraCount + ").";
        } else if (interCount > 0) {
            recomandare = "ATENȚIE: interacțiuni detectate (" + interCount + "). Necesită evaluare suplimentară.";
        } else if (avertCount > 0) {
            recomandare = "ATENȚIE: avertizări detectate (" + avertCount + "). Verificați prospectul.";
        } else {
            recomandare = "OK: nu s-au detectat contraindicații/interacțiuni pe baza administrărilor pacientului.";
        }

        DecisionLog log = new DecisionLog()
            .timestamp(Instant.now())
            .actorType(ActorType.SISTEM_AI)
            .recomandare(recomandare)
            .modelScore(score)
            .reguliTriggered(reguli.isEmpty() ? null : String.join(" | ", reguli))
            .externalChecks(checks.isEmpty() ? null : String.join(" | ", checks))
            .alocare(alocare);

        decisionLogRepository.save(log);

        return new EngineResult(score, reguli, recomandare);
    }

    private String safeLower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }

    private String firstToken(String s) {
        if (s == null || s.isBlank()) return s == null ? "" : s;
        return s.split("\\s+")[0].trim();
    }

    private boolean containsWord(String haystack, String needle) {
        String token = firstToken(needle);
        if (token.length() < MIN_TOKEN_LENGTH) return false;
        String pattern = "(?s).*(?:^|\\W)" + java.util.regex.Pattern.quote(token) + "(?:\\W|$).*";
        return haystack.matches(pattern);
    }

    public record EngineResult(Double score, List<String> reguli, String recomandare) {}
}
