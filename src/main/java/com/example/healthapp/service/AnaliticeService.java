package com.example.healthapp.service;

import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.FeedbackRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.dto.AnaliticeMedicamentDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviciu pentru analitice reale (real-world) per medicament.
 * Folosit de GET /api/analitice/medicament/{id}
 */
@Service
@Transactional(readOnly = true)
public class AnaliticeService {

    private final MedicamentRepository medicamentRepository;
    private final ReactieAdversaRepository reactieAdversaRepository;
    private final AlocareTratamentRepository alocareTratamentRepository;
    private final FeedbackRepository feedbackRepository;

    public AnaliticeService(
        MedicamentRepository medicamentRepository,
        ReactieAdversaRepository reactieAdversaRepository,
        AlocareTratamentRepository alocareTratamentRepository,
        FeedbackRepository feedbackRepository
    ) {
        this.medicamentRepository = medicamentRepository;
        this.reactieAdversaRepository = reactieAdversaRepository;
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Calculează analiticele pentru un medicament specific.
     *
     * @param medicamentId ID-ul medicamentului
     * @return AnaliticeMedicamentDTO cu statistici real-world
     */
    public AnaliticeMedicamentDTO calculeazaAnaliticePentruMedicament(Long medicamentId) {
        Medicament medicament = medicamentRepository
            .findById(medicamentId)
            .orElseThrow(() -> new IllegalArgumentException("Medicament inexistent: " + medicamentId));

        AnaliticeMedicamentDTO dto = new AnaliticeMedicamentDTO();
        dto.setMedicamentId(medicamentId);
        dto.setDenumire(medicament.getDenumire());

        long totalReactii = reactieAdversaRepository.countByMedicamentId(medicamentId);
        long reactiiSevere = reactieAdversaRepository.countByMedicamentIdAndSeveritate(medicamentId, "severa");

        dto.setTotalReactiiAdverse(totalReactii);
        dto.setReactiiSevere(reactiiSevere);

        long totalAlocari = alocareTratamentRepository.countByMedicamentId(medicamentId);
        dto.setTotalAlocari(totalAlocari);

        // Rata reacții adverse = nr reacții / nr alocări (dacă există alocări)
        if (totalAlocari > 0) {
            dto.setRataReactiiAdverse((double) totalReactii / totalAlocari);
        } else {
            dto.setRataReactiiAdverse(0.0);
        }

        // Eficiență medie din Feedback (scor mediu 1-10)
        Double eficienta = feedbackRepository.calculeazaScorMediuPentruMedicament(medicamentId);
        dto.setEficientaMedie(eficienta);

        return dto;
    }

    /**
     * Calculează analiticele pentru toate medicamentele din baza de date.
     *
     * @return lista de AnaliticeMedicamentDTO
     */
    public List<AnaliticeMedicamentDTO> calculeazaToateAnaliticele() {
        List<Medicament> medicamente = medicamentRepository.findAll();
        List<AnaliticeMedicamentDTO> rezultate = new ArrayList<>();
        for (Medicament m : medicamente) {
            rezultate.add(calculeazaAnaliticePentruMedicament(m.getId()));
        }
        return rezultate;
    }
}
