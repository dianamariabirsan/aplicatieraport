package com.example.healthapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.repository.RaportAnaliticRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImportRaportAnaliticServiceTest {

    @Mock
    private RaportAnaliticRepository raportAnaliticRepository;

    @Mock
    private MedicamentRepository medicamentRepository;

    @Mock
    private MedicRepository medicRepository;

    @InjectMocks
    private ImportRaportAnaliticService importRaportAnaliticService;

    @Test
    void importCsvCreatesRaportWithMedicamentAndMedic() {
        String csv =
            "perioadaStart,perioadaEnd,eficientaMedie,rataReactiiAdverse,observatii,concluzii,medicament,medicEmail,medicNume,medicPrenume,medicSpecializare\n" +
            "2026-01-01T00:00:00Z,2026-01-31T00:00:00Z,0.88,0.12,Observatie,Concluzie,Paracetamol,medic@example.com,Ionescu,Ana,Oncologie\n";

        when(medicamentRepository.findOneByDenumireIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(medicRepository.findOneByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(medicamentRepository.save(any(Medicament.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(medicRepository.save(any(Medic.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(raportAnaliticRepository.save(any(RaportAnalitic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int imported = importRaportAnaliticService.importCsv(csv.getBytes(StandardCharsets.UTF_8));

        assertThat(imported).isEqualTo(1);
        ArgumentCaptor<RaportAnalitic> raportCaptor = ArgumentCaptor.forClass(RaportAnalitic.class);
        verify(raportAnaliticRepository).save(raportCaptor.capture());
        RaportAnalitic raport = raportCaptor.getValue();
        assertThat(raport.getMedicament()).isNotNull();
        assertThat(raport.getMedicament().getDenumire()).isEqualTo("Paracetamol");
        assertThat(raport.getMedic()).isNotNull();
        assertThat(raport.getMedic().getEmail()).isEqualTo("medic@example.com");
        assertThat(raport.getEficientaMedie()).isEqualTo(0.88d);
        assertThat(raport.getRataReactiiAdverse()).isEqualTo(0.12d);
    }

    @Test
    void importCsvReturnsZeroForEmptyFile() {
        int imported = importRaportAnaliticService.importCsv(new byte[0]);

        assertThat(imported).isZero();
        verify(raportAnaliticRepository, never()).save(any(RaportAnalitic.class));
    }
}
