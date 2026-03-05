package com.example.healthapp.service;

import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.domain.enumeration.SeveritateReactie;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.dto.ImportPacientRowDTO;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for importing patients, treatments, decision logs and adverse reactions from a CSV file.
 * Each row is upserted by CNP. Data source (REAL/SIMULAT) is stored in the
 * patient's {@code comorbiditati} field as a {@code DATA_SOURCE=<value>} token until
 * a dedicated column is added to the entity.
 */
@Service
@Transactional
public class ImportPacientiService {

    private static final Pattern DATA_SOURCE_TOKEN = Pattern.compile("DATA_SOURCE=(REAL|SIMULAT)");
    /** Fallback e-mail used when the CSV does not provide one for a new patient. */
    private static final String DEFAULT_EMAIL_TEMPLATE = "import-%s@simulat.local";
    /** Fallback phone used when the CSV does not provide one for a new patient (10 digits required). */
    private static final String DEFAULT_PHONE = "0000000000";

    private static final Logger LOG = LoggerFactory.getLogger(ImportPacientiService.class);

    private final PacientRepository pacientRepository;
    private final MedicamentRepository medicamentRepository;
    private final AlocareTratamentRepository alocareTratamentRepository;
    private final AdministrareRepository administrareRepository;
    private final DecisionLogRepository decisionLogRepository;
    private final ReactieAdversaRepository reactieAdversaRepository;

    public ImportPacientiService(
        PacientRepository pacientRepository,
        MedicamentRepository medicamentRepository,
        AlocareTratamentRepository alocareTratamentRepository,
        AdministrareRepository administrareRepository,
        DecisionLogRepository decisionLogRepository,
        ReactieAdversaRepository reactieAdversaRepository
    ) {
        this.pacientRepository = pacientRepository;
        this.medicamentRepository = medicamentRepository;
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.administrareRepository = administrareRepository;
        this.decisionLogRepository = decisionLogRepository;
        this.reactieAdversaRepository = reactieAdversaRepository;
    }

    /**
     * Parse {@code csvBytes} and persist each valid row.
     *
     * @param csvBytes          raw CSV content (UTF-8)
     * @param defaultDataSource fallback data-source tag when the row does not supply one
     * @return number of successfully imported rows
     */
    public int importCsv(byte[] csvBytes, String defaultDataSource) {
        List<ImportPacientRowDTO> rows = parse(csvBytes, defaultDataSource);
        int imported = 0;

        for (ImportPacientRowDTO r : rows) {
            try {
                imported += processRow(r, defaultDataSource);
            } catch (Exception e) {
                LOG.warn("ImportPacientiService: skipping row cnp={} due to: {}", r.cnp, e.getMessage());
            }
        }

        LOG.info("ImportPacientiService: imported {} / {} rows", imported, rows.size());
        return imported;
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private int processRow(ImportPacientRowDTO r, String defaultDataSource) {
        if (r.cnp == null || r.cnp.isBlank() || r.medicament == null || r.medicament.isBlank()) {
            return 0;
        }

        // 1) Pacient — upsert by CNP
        Optional<Pacient> existing = pacientRepository.findOneByCnp(r.cnp);
        boolean isNew = existing.isEmpty();
        Pacient pacient = existing.orElseGet(Pacient::new);
        pacient.setCnp(r.cnp);
        pacient.setNume(nvl(r.nume, "Importat"));
        pacient.setPrenume(nvl(r.prenume, "CSV"));
        pacient.setSex(nvl(r.sex, "NECUNOSCUT"));
        pacient.setVarsta(r.varsta != null ? r.varsta : 0);
        pacient.setGreutate(r.greutate);
        pacient.setInaltime(r.inaltime);

        // For new patients, email and telefon are required (@NotNull). Use CSV values or safe defaults.
        if (isNew) {
            String email = r.email != null && !r.email.isBlank()
                ? r.email.strip()
                : String.format(DEFAULT_EMAIL_TEMPLATE, r.cnp.replaceAll("[^a-zA-Z0-9]", ""));
            pacient.setEmail(email);
            String telefonStripped = r.telefon != null ? r.telefon.strip() : "";
            String telefon = telefonStripped.matches("\\d{10}") ? telefonStripped : DEFAULT_PHONE;
            pacient.setTelefon(telefon);
        }

        // Store data-source as a token in comorbiditati (replace any previous token)
        String source = nvl(r.dataSource, defaultDataSource).toUpperCase();
        String comorb = Optional.ofNullable(pacient.getComorbiditati()).orElse("");
        comorb = DATA_SOURCE_TOKEN.matcher(comorb).replaceAll("").strip();
        pacient.setComorbiditati((comorb + " DATA_SOURCE=" + source).strip());

        pacient = pacientRepository.save(pacient);

        // 2) Medicament — upsert by name (case-insensitive)
        Medicament medicament = medicamentRepository
            .findOneByDenumireIgnoreCase(r.medicament)
            .orElseGet(() -> {
                Medicament m = new Medicament();
                m.setDenumire(r.medicament.strip());
                m.setSubstanta("N/A");
                return m;
            });
        medicament = medicamentRepository.save(medicament);

        // 3) AlocareTratament — always insert a new allocation per row
        AlocareTratament aloc = new AlocareTratament();
        aloc.setPacient(pacient);
        aloc.setMedicament(medicament);
        aloc.setDataDecizie(r.dataDecizie != null ? r.dataDecizie : Instant.now());
        aloc.setScorDecizie(r.scorDecizie);
        aloc.setTratamentPropus(nvl(r.tratamentPropus, medicament.getDenumire()));
        aloc.setDecizieValidata(Boolean.TRUE.equals(r.decizieValidata));
        aloc = alocareTratamentRepository.save(aloc);

        // 4) DecisionLog — always create an entry linked to the allocation
        DecisionLog decisionLog = new DecisionLog();
        decisionLog.setAlocare(aloc);
        decisionLog.setTimestamp(r.dataDecizie != null ? r.dataDecizie : Instant.now());
        decisionLog.setActorType(parseActorType(r.actorType));
        decisionLog.setRecomandare(r.recomandare != null ? r.recomandare : aloc.getTratamentPropus());
        decisionLog.setModelScore(r.scorDecizie);
        decisionLog.setReguliTriggered(r.reguliTriggered);
        decisionLog.setExternalChecks(r.externalChecks);
        decisionLog.setFinalDecision(r.finalDecision);
        decisionLogRepository.save(decisionLog);

        // 5) Administrare (optional concomitent treatment)
        if (r.tratamentConcomitent != null && !r.tratamentConcomitent.isBlank()) {
            Administrare adm = new Administrare();
            adm.setPacient(pacient);
            adm.setTipTratament(r.tratamentConcomitent.strip());
            adm.setDataAdministrare(r.dataAdministrare != null ? r.dataAdministrare : Instant.now());
            administrareRepository.save(adm);
        }

        // 6) ReactieAdversa (optional) — created when CSV row contains adverse reaction description
        if (r.reactieDescriere != null && !r.reactieDescriere.isBlank()) {
            ReactieAdversa ra = new ReactieAdversa();
            ra.setPacient(pacient);
            ra.setMedicament(medicament);
            ra.setDescriere(r.reactieDescriere.strip());
            ra.setDataRaportare(r.reactieDataRaportare != null ? r.reactieDataRaportare : Instant.now());
            ra.setSeveritate(parseSeveritate(r.reactieSeveritate));
            ra.setEvolutie(r.reactieEvolutie);
            ra.setRaportatDe(r.reactieRaportatDe);
            reactieAdversaRepository.save(ra);
        }

        return 1;
    }

    private List<ImportPacientRowDTO> parse(byte[] csvBytes, String defaultDataSource) {
        if (csvBytes == null || csvBytes.length == 0) {
            return List.of();
        }
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8)) {
            char delimiter = detectDelimiter(csvBytes);
            CSVParser parser = CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()
                .parse(reader);

            LOG.info("ImportPacientiService: CSV delimiter='{}', headers={}", delimiter, parser.getHeaderMap().keySet());

            List<ImportPacientRowDTO> out = new ArrayList<>();
            for (CSVRecord rec : parser) {
                ImportPacientRowDTO r = new ImportPacientRowDTO();
                r.dataSource = firstNonBlank(rec, "dataSource", "data_source");
                if (r.dataSource == null) r.dataSource = defaultDataSource;
                r.cnp = firstNonBlank(rec, "cnp", "CNP", "patient_cnp", "patientId", "patient_id");
                if (r.cnp == null || r.cnp.isBlank()) {
                    r.cnp = "SIM-" + rec.getRecordNumber();
                }
                r.nume = firstNonBlank(rec, "nume", "lastName", "last_name");
                r.prenume = firstNonBlank(rec, "prenume", "firstName", "first_name");
                r.sex = firstNonBlank(rec, "sex", "gender");
                r.varsta = parseInt(firstNonBlank(rec, "varsta", "age"));
                r.greutate = parseDouble(firstNonBlank(rec, "greutate", "pacient_greutate", "weight", "weight_kg"));
                r.inaltime = parseDouble(firstNonBlank(rec, "inaltime", "pacient_inaltime", "height", "height_cm"));
                r.medicament = firstNonBlank(rec, "medicament", "medicament_denumire", "drug", "drug_name", "medication");
                r.dataDecizie = parseInstant(firstNonBlank(rec, "dataDecizie", "alocare_data_decizie", "decision_date", "date_decision"));
                r.scorDecizie = parseDouble(
                    firstNonBlank(rec, "scorDecizie", "alocare_scor_decizie", "model_score", "decision_score", "score")
                );
                r.tratamentPropus = firstNonBlank(rec, "tratamentPropus", "alocare_tratament_propus", "proposed_treatment");
                r.decizieValidata = parseBool(firstNonBlank(rec, "decizieValidata", "alocare_decizie_validata", "validated", "approved"));
                r.tratamentConcomitent = firstNonBlank(rec, "tratamentConcomitent", "concomitent_treatment");
                r.dataAdministrare = parseInstant(firstNonBlank(rec, "dataAdministrare", "administration_date"));
                r.actorType = firstNonBlank(rec, "actorType", "actor_type");
                r.recomandare = firstNonBlank(rec, "recomandare", "recommendation");
                r.reguliTriggered = firstNonBlank(rec, "reguliTriggered", "reguli_triggered", "rules_triggered");
                r.externalChecks = firstNonBlank(rec, "externalChecks", "external_checks");
                r.finalDecision = firstNonBlank(rec, "finalDecision", "final_decision");
                r.reactieDescriere = firstNonBlank(
                    rec,
                    "reactieDescriere",
                    "reactie_descriere",
                    "adverse_reaction",
                    "reaction_description"
                );
                r.reactieSeveritate = firstNonBlank(rec, "reactieSeveritate", "reactie_severitate", "reaction_severity");
                r.reactieEvolutie = firstNonBlank(rec, "reactieEvolutie", "reactie_evolutie", "reaction_evolution");
                r.reactieDataRaportare = parseInstant(
                    firstNonBlank(rec, "reactieDataRaportare", "reactie_data_raportare", "reaction_date")
                );
                r.reactieRaportatDe = firstNonBlank(rec, "reactieRaportatDe", "reactie_raportat_de", "reaction_reported_by");
                r.email = firstNonBlank(rec, "email", "pacient_email", "patient_email");
                r.telefon = firstNonBlank(rec, "telefon", "phone", "pacient_telefon", "patient_phone");
                LOG.debug("ImportPacientiService: parsed row => cnp={}, medicament={}, scor={}", r.cnp, r.medicament, r.scorDecizie);
                out.add(r);
            }
            return out;
        } catch (Exception e) {
            LOG.error("ImportPacientiService: CSV parse error: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Auto-detect the CSV delimiter by examining the first line of the file.
     * Precedence: tab &gt; semicolon &gt; comma. Comma is the default fallback.
     */
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
        if (tabs >= semicolons && tabs >= commas && tabs > 0) return '\t';
        if (semicolons >= commas && semicolons > 0) return ';';
        if (commas == 0) {
            LOG.warn("ImportPacientiService: no common delimiter detected in CSV header line; defaulting to comma");
        }
        return ',';
    }

    private static String get(CSVRecord rec, String key, String def) {
        try {
            if (!rec.isMapped(key)) return def;
            String v = rec.get(key);
            return (v == null || v.isBlank()) ? def : v;
        } catch (Exception e) {
            return def;
        }
    }

    private static String firstNonBlank(CSVRecord rec, String... keys) {
        for (String k : keys) {
            String v = get(rec, k, null);
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static String nvl(String s, String def) {
        return s == null ? def : s;
    }

    private static Integer parseInt(String s) {
        try {
            return s == null ? null : Integer.parseInt(s.strip());
        } catch (Exception e) {
            return null;
        }
    }

    private static Double parseDouble(String s) {
        try {
            return s == null ? null : Double.parseDouble(s.strip().replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean parseBool(String s) {
        if (s == null) return null;
        String x = s.strip().toLowerCase();
        if (x.equals("true") || x.equals("1") || x.equals("da") || x.equals("yes")) return true;
        if (x.equals("false") || x.equals("0") || x.equals("nu") || x.equals("no")) return false;
        return null;
    }

    private static Instant parseInstant(String s) {
        try {
            return s == null ? null : Instant.parse(s.strip());
        } catch (Exception e) {
            return null;
        }
    }

    private static ActorType parseActorType(String s) {
        if (s == null) return ActorType.SISTEM_AI;
        try {
            return ActorType.valueOf(s.strip().toUpperCase());
        } catch (Exception e) {
            return ActorType.SISTEM_AI;
        }
    }

    private static SeveritateReactie parseSeveritate(String s) {
        if (s == null) return null;
        try {
            return SeveritateReactie.valueOf(s.strip().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
