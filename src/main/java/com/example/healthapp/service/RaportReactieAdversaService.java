package com.example.healthapp.service;

import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.dto.RaportReactieAdversaCerereDTO;
import com.example.healthapp.service.dto.RaportReactieAdversaRezultatDTO;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RaportReactieAdversaService {

    private final PacientRepository pacientRepository;
    private final AlocareTratamentRepository alocareTratamentRepository;
    private final ReactieAdversaRepository reactieAdversaRepository;

    public RaportReactieAdversaService(
        PacientRepository pacientRepository,
        AlocareTratamentRepository alocareTratamentRepository,
        ReactieAdversaRepository reactieAdversaRepository
    ) {
        this.pacientRepository = pacientRepository;
        this.alocareTratamentRepository = alocareTratamentRepository;
        this.reactieAdversaRepository = reactieAdversaRepository;
    }

    public RaportReactieAdversaRezultatDTO raporteaza(RaportReactieAdversaCerereDTO cerere) {
        Pacient pacient = pacientRepository
            .findById(cerere.getPacientId())
            .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent: " + cerere.getPacientId()));

        // preferăm alocarea validată
        AlocareTratament alocare = alocareTratamentRepository
            .findTopByPacientIdAndDecizieValidataTrueOrderByDataDecizieDesc(pacient.getId())
            .orElseGet(() ->
                alocareTratamentRepository
                    .findTopByPacientIdOrderByDataDecizieDesc(pacient.getId())
                    .orElseThrow(() -> new IllegalStateException("Pacientul nu are alocare de tratament."))
            );

        Medicament medicament = alocare.getMedicament();
        if (medicament == null) {
            throw new IllegalStateException("Alocarea nu are medicament asociat.");
        }

        ReactieAdversa ra = new ReactieAdversa();
        ra.setPacient(pacient);
        ra.setMedicament(medicament);
        ra.setAlocare(alocare); // Cap. 8.3: leagă reacția de alocarea concretă de tratament
        ra.setDataRaportare(Instant.now());
        ra.setDescriere(cerere.getDescriere());
        ra.setSeveritate(cerere.getSeveritate());
        ra.setEvolutie(cerere.getEvolutie());
        ra.setRaportatDe(cerere.getRaportatDe());

        ReactieAdversa salvat = reactieAdversaRepository.save(ra);

        RaportReactieAdversaRezultatDTO out = new RaportReactieAdversaRezultatDTO();
        out.setReactieAdversaId(salvat.getId());
        out.setPacientId(pacient.getId());
        out.setMedicamentId(medicament.getId());
        out.setMesaj("Raportare inregistrata cu succes.");
        return out;
    }
}
