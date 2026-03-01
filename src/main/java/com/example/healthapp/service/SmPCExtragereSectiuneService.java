package com.example.healthapp.service;

import java.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class SmPCExtragereSectiuneService {

    public Map<String, List<String>> extrageSectiuni(byte[] pdfBytes) {
        String text = extrageText(pdfBytes);

        Map<String, List<String>> sectiuni = new LinkedHashMap<>();
        sectiuni.put("contraindicatii", colecteaza(text, List.of("Contraindica")));
        sectiuni.put("interactiuni", colecteaza(text, List.of("Interac")));
        sectiuni.put("reactiiAdverse", colecteaza(text, List.of("Reacții adverse", "Reacţii adverse")));
        sectiuni.put("avertizari", colecteaza(text, List.of("Atențion", "Avertiz")));

        return sectiuni;
    }

    private String extrageText(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) return "";
        try (PDDocument doc = PDDocument.load(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (Exception e) {
            return "";
        }
    }

    private List<String> colecteaza(String text, List<String> markere) {
        if (text == null || text.isBlank()) return List.of();

        String[] lines = text.split("\\R");
        List<String> out = new ArrayList<>();

        for (String l : lines) {
            String line = l.trim();
            if (line.isBlank()) continue;

            for (String m : markere) {
                if (line.toLowerCase().contains(m.toLowerCase())) {
                    out.add(line);
                    break;
                }
            }
            if (out.size() >= 20) break;
        }
        return out;
    }
}
