package com.example.healthapp.service.ml;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.service.DecisionEngineService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Antrenează un model SMILE RandomForest pe datele din DB.
 * Label = decizieValidata (true=1, false/null=0).
 */
@Service
@Transactional(readOnly = true)
public class MlTrainingService {

    private static final Logger LOG = LoggerFactory.getLogger(MlTrainingService.class);

    private static final int MIN_TRAINING_SAMPLES = 10;
    private final AlocareTratamentRepository alocareTratamentRepository;
    private final AdministrareRepository administrareRepository;
    private final DecisionEngineService decisionEngineService;

    public MlTrainingService(
        AlocareTratamentRepository alocareTratamentRepository,
        AdministrareRepository administrareRepository,
        DecisionEngineService decisionEngineService
    ) {
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.administrareRepository = administrareRepository;
        this.decisionEngineService = decisionEngineService;
    }

    /**
     * Trains a SMILE RandomForest model on all AlocareTratament records.
     * Uses decizieValidata as label (1=validated, 0=not validated).
     * @return number of training samples used, or -1 if training failed.
     */
    public int trainAndActivate() {
        List<AlocareTratament> alocari = alocareTratamentRepository.findAll();

        List<double[]> xList = new ArrayList<>();
        List<Integer> yList = new ArrayList<>();

        for (AlocareTratament a : alocari) {
            if (a.getPacient() == null || a.getMedicament() == null) continue;

            var administrari = administrareRepository.findAllByPacientId(a.getPacient().getId());
            Set<String> concomitente = new HashSet<>();
            for (var adm : administrari) {
                if (adm.getTipTratament() != null) concomitente.add(adm.getTipTratament().toLowerCase(Locale.ROOT));
            }

            DecisionFeatures fx = buildFeatures(a, administrari, concomitente);
            xList.add(fx.toArray());
            yList.add(Boolean.TRUE.equals(a.getDecizieValidata()) ? 1 : 0);
        }

        int n = xList.size();
        if (n < MIN_TRAINING_SAMPLES) {
            LOG.warn("MlTrainingService: not enough training samples ({}). Need at least {}.", n, MIN_TRAINING_SAMPLES);
            return -1;
        }

        try {
            double[][] X = xList.toArray(new double[0][]);
            int[] y = yList.stream().mapToInt(Integer::intValue).toArray();

            // Use SMILE DataFrame API via reflection to avoid hard compile-time dependency
            Class<?> dfClass = Class.forName("smile.data.DataFrame");
            java.lang.reflect.Method ofMethod = dfClass.getMethod("of", double[][].class, String[].class);
            Object dfX = ofMethod.invoke(null, X, DecisionEngineService.FEATURE_NAMES);

            // Add label column
            Class<?> intVectorClass = Class.forName("smile.data.vector.IntVector");
            java.lang.reflect.Method intVecOf = intVectorClass.getMethod("of", String.class, int[].class);
            Object labelVec = intVecOf.invoke(null, "label", y);

            Class<?> baseVecClass = Class.forName("smile.data.vector.BaseVector");
            java.lang.reflect.Method mergeMethod = dfClass.getMethod("merge", baseVecClass.arrayType());
            Object baseVecArray = java.lang.reflect.Array.newInstance(baseVecClass, 1);
            java.lang.reflect.Array.set(baseVecArray, 0, labelVec);
            Object df = mergeMethod.invoke(dfX, baseVecArray);

            // Create formula and train
            Class<?> formulaClass = Class.forName("smile.data.formula.Formula");
            java.lang.reflect.Method lhsMethod = formulaClass.getMethod("lhs", String.class);
            Object formula = lhsMethod.invoke(null, "label");

            Class<?> rfClass = Class.forName("smile.classification.RandomForest");
            java.lang.reflect.Method fitMethod = rfClass.getMethod("fit", formulaClass, dfClass);
            Object model = fitMethod.invoke(null, formula, df);

            decisionEngineService.setSmileModel(model);
            LOG.info("MlTrainingService: RandomForest trained on {} samples. Model activated.", n);
            return n;
        } catch (Exception e) {
            LOG.error("MlTrainingService: training failed: {}", e.getMessage(), e);
            return -1;
        }
    }

    private DecisionFeatures buildFeatures(
        AlocareTratament a,
        List<com.example.healthapp.domain.Administrare> administrari,
        Set<String> concomitente
    ) {
        var p = a.getPacient();
        var m = a.getMedicament();

        int varsta = p.getVarsta() != null ? p.getVarsta() : 0;
        int sexF = p.getSex() != null && p.getSex().toLowerCase(Locale.ROOT).startsWith("f") ? 1 : 0;
        double greutate = p.getGreutate() != null ? p.getGreutate() : 0.0;
        double inaltime = p.getInaltime() != null ? p.getInaltime() : 0.0;

        String com = p.getComorbiditati() != null ? p.getComorbiditati().toLowerCase(Locale.ROOT) : "";
        int hasDiabet = com.contains("diabet") ? 1 : 0;
        int hasHTA = (com.contains("hta") || com.contains("hipertensi")) ? 1 : 0;

        int adminCount = administrari.size();
        int hasMetformin = concomitente.stream().anyMatch(s -> s.contains("metformin")) ? 1 : 0;
        int hasInsulina = concomitente.stream().anyMatch(s -> s.contains("insulin")) ? 1 : 0;

        String den = m.getDenumire() != null ? m.getDenumire().toLowerCase(Locale.ROOT) : "";
        int isWegovy = den.contains("wegovy") ? 1 : 0;
        int isMounjaro = den.contains("mounjaro") ? 1 : 0;

        return new DecisionFeatures(
            varsta,
            sexF,
            greutate,
            inaltime,
            hasDiabet,
            hasHTA,
            adminCount,
            hasMetformin,
            hasInsulina,
            isWegovy,
            isMounjaro
        );
    }
}
