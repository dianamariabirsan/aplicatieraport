package com.example.healthapp.service;

import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.RaportAnaliticRepository;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ImportRaportAnaliticService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportRaportAnaliticService.class);

    private final RaportAnaliticRepository raportAnaliticRepository;
    private final MedicamentRepository medicamentRepository;
    private final MedicRepository medicRepository;

    public ImportRaportAnaliticService(
        RaportAnaliticRepository raportAnaliticRepository,
        MedicamentRepository medicamentRepository,
        MedicRepository medicRepository
    ) {
        this.raportAnaliticRepository = raportAnaliticRepository;
        this.medicamentRepository = medicamentRepository;
        this.medicRepository = medicRepository;
    }

    public int importCsv(byte[] csvBytes) {
        if (csvBytes == null || csvBytes.length == 0) {
            return 0;
        }

        int imported = 0;
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8)) {
            char delimiter = detectDelimiter(csvBytes);
            CSVParser parser = CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()
                .parse(reader);

            for (CSVRecord rec : parser) {
                try {
                    if (processRow(rec)) {
                        imported++;
                    }
                } catch (Exception e) {
                    LOG.warn("ImportRaportAnaliticService: skipping row {} due to {}", rec.getRecordNumber(), e.getMessage());
                }
            }
        } catch (Exception e) {
            LOG.error("ImportRaportAnaliticService: CSV parse error: {}", e.getMessage());
            return 0;
        }
        return imported;
    }

    private boolean processRow(CSVRecord rec) {
        String medicamentDenumire = firstNonBlank(rec, "medicament", "medicament_denumire", "drug", "drug_name");
        if (medicamentDenumire == null) {
            return false;
        }

        Medicament medicament = medicamentRepository
            .findOneByDenumireIgnoreCase(medicamentDenumire)
            .orElseGet(() -> {
                Medicament m = new Medicament();
                m.setDenumire(medicamentDenumire.strip());
                m.setSubstanta("N/A");
                return m;
            });
        medicament = medicamentRepository.save(medicament);

        String medicEmail = firstNonBlank(rec, "medicEmail", "medic_email", "doctor_email");
        Medic medic = null;
        if (medicEmail != null) {
            medic = medicRepository
                .findOneByEmailIgnoreCase(medicEmail)
                .orElseGet(() -> {
                    Medic newMedic = new Medic();
                    newMedic.setEmail(medicEmail.strip().toLowerCase());
                    newMedic.setNume(nvl(firstNonBlank(rec, "medicNume", "medic_nume", "doctor_last_name"), "Import"));
                    newMedic.setPrenume(nvl(firstNonBlank(rec, "medicPrenume", "medic_prenume", "doctor_first_name"), "CSV"));
                    newMedic.setSpecializare(nvl(firstNonBlank(rec, "medicSpecializare", "medic_specializare", "specializare"), "General"));
                    newMedic.setTelefon(firstNonBlank(rec, "medicTelefon", "medic_telefon", "doctor_phone"));
                    newMedic.setCabinet(firstNonBlank(rec, "medicCabinet", "medic_cabinet", "cabinet"));
                    return newMedic;
                });
            medic = medicRepository.save(medic);
        }

        RaportAnalitic raport = new RaportAnalitic();
        raport.setPerioadaStart(parseInstant(firstNonBlank(rec, "perioadaStart", "perioada_start", "start_date")));
        raport.setPerioadaEnd(parseInstant(firstNonBlank(rec, "perioadaEnd", "perioada_end", "end_date")));
        raport.setEficientaMedie(parseDouble(firstNonBlank(rec, "eficientaMedie", "eficienta_medie")));
        raport.setRataReactiiAdverse(parseDouble(firstNonBlank(rec, "rataReactiiAdverse", "rata_reactii_adverse")));
        raport.setObservatii(firstNonBlank(rec, "observatii", "notes"));
        raport.setConcluzii(firstNonBlank(rec, "concluzii", "conclusions"));
        raport.setMedicament(medicament);
        raport.setMedic(medic);

        raportAnaliticRepository.save(raport);
        return true;
    }

    private static char detectDelimiter(byte[] csvBytes) {
        String content = new String(csvBytes, StandardCharsets.UTF_8);
        String[] lines = content.split("\\r?\\n", 2);
        if (lines.length == 0 || lines[0].isBlank()) {
            return ',';
        }
        String firstLine = lines[0];
        long tabs = firstLine.chars().filter(c -> c == '\t').count();
        long semicolons = firstLine.chars().filter(c -> c == ';').count();
        long commas = firstLine.chars().filter(c -> c == ',').count();
        if (tabs >= semicolons && tabs >= commas && tabs > 0) {
            return '\t';
        }
        if (semicolons >= commas && semicolons > 0) {
            return ';';
        }
        return ',';
    }

    private static String get(CSVRecord rec, String key) {
        if (!rec.isMapped(key)) {
            return null;
        }
        String value = rec.get(key);
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }

    private static String firstNonBlank(CSVRecord rec, String... keys) {
        for (String key : keys) {
            String value = get(rec, key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private static String nvl(String value, String fallback) {
        return value == null ? fallback : value;
    }

    private static Instant parseInstant(String value) {
        try {
            return value == null ? null : Instant.parse(value.strip());
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDouble(String value) {
        try {
            return value == null ? null : Double.parseDouble(value.strip().replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }
}
