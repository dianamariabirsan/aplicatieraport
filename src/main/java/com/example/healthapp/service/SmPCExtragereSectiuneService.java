package com.example.healthapp.service;

import java.util.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class SmPCExtragereSectiuneService {

    /** Maximum lines to collect per section (prevents runaway collection). */
    private static final int MAX_SECTION_LINES = 80;

    /**
     * All known section markers (superset of individual section markers).
     * When any of these appear while collecting another section, collection stops.
     */
    private static final List<String> ALL_SECTION_MARKERS = List.of(
        "contraindica",
        "interac",
        "reacții adverse",
        "reacţii adverse",
        "atenţionări",
        "avertiz",
        "indicații terapeutice",
        "indicaţii terapeutice",
        "posologie",
        "mod de administrare",
        "farmacocinetică",
        "farmacodinamie",
        "proprietăți",
        "date preclinice",
        "excipienți",
        "incompatibilități",
        "condiții de păstrare",
        "natura și conținut",
        "instrucțiuni",
        "titularul"
    );

    public Map<String, List<String>> extrageSectiuni(byte[] pdfBytes) {
        String text = extrageText(pdfBytes);

        Map<String, List<String>> sectiuni = new LinkedHashMap<>();
        sectiuni.put("contraindicatii", colecteaza(text, List.of("contraindica")));
        sectiuni.put("interactiuni", colecteaza(text, List.of("interac")));
        sectiuni.put("reactiiAdverse", colecteaza(text, List.of("reacții adverse", "reacţii adverse")));
        sectiuni.put("avertizari", colecteaza(text, List.of("atenţionări", "avertiz")));

        return sectiuni;
    }

    private String extrageText(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) return "";
        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Finds the first line matching any of {@code markere} (section header), then collects
     * the subsequent non-blank lines until another known section header is encountered or
     * {@link #MAX_SECTION_LINES} lines have been gathered.
     */
    private List<String> colecteaza(String text, List<String> markere) {
        if (text == null || text.isBlank()) return List.of();

        String[] lines = text.split("\\R");
        List<String> out = new ArrayList<>();
        boolean collecting = false;

        for (String l : lines) {
            String line = l.trim();
            if (line.isBlank()) continue;

            String lineLower = line.toLowerCase(Locale.ROOT);

            if (!collecting) {
                // Look for our target section header
                for (String m : markere) {
                    if (lineLower.contains(m)) {
                        collecting = true;
                        break;
                    }
                }
                // Skip the header line itself; content starts on the next line
                continue;
            }

            // We are collecting — stop if we hit any other known section header
            boolean isOtherSection = false;
            for (String m : ALL_SECTION_MARKERS) {
                // Ignore our own markers (allow sub-mentions inside the section)
                boolean isOwn = markere.stream().anyMatch(own -> own.equals(m));
                if (!isOwn && lineLower.contains(m)) {
                    isOtherSection = true;
                    break;
                }
            }
            if (isOtherSection || out.size() >= MAX_SECTION_LINES) break;

            out.add(line);
        }

        return out;
    }
}
