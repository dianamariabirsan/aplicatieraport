package com.example.healthapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.mapper.MedicamentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicamentServiceTest {

    private MedicamentService medicamentService;

    @BeforeEach
    void setUp() {
        medicamentService = new MedicamentService(
            mock(MedicamentRepository.class),
            mock(MedicamentMapper.class),
            mock(ExternalDrugInfoRepository.class),
            new ObjectMapper()
        );
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldPopulateFieldsFromJson() throws Exception {
        String json =
            "{\"contraindicatii\":[\"CI1\",\"CI2\"],\"interactiuni\":[\"IA1\"],\"avertizari\":[\"AV1\"],\"indicatii\":[\"IND1\"],\"dozaRecomandata\":[\"DOZA1\"]}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1\nCI2");
        assertThat(medicament.getInteractiuni()).isEqualTo("IA1");
        assertThat(medicament.getAvertizari()).isEqualTo("AV1");
        assertThat(medicament.getIndicatii()).isEqualTo("IND1");
        assertThat(medicament.getDozaRecomandata()).isEqualTo("DOZA1");
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldNotThrowOnNullExternalDrugInfo() {
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");
        medicamentService.populateMedicamentFromExternalInfo(medicament, null);
        assertThat(medicament.getContraindicatii()).isNull();
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldNotThrowOnNullProductSummary() {
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test");
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");
        medicamentService.populateMedicamentFromExternalInfo(medicament, info);
        assertThat(medicament.getContraindicatii()).isNull();
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldIgnoreInvalidJson() {
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary("not valid json");
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");
        medicamentService.populateMedicamentFromExternalInfo(medicament, info);
        assertThat(medicament.getContraindicatii()).isNull();
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldHandlePartialJson() {
        String json = "{\"contraindicatii\":[\"CI1\"],\"dozaRecomandata\":\"10mg\"}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1");
        assertThat(medicament.getInteractiuni()).isNull();
        assertThat(medicament.getAvertizari()).isNull();
        assertThat(medicament.getIndicatii()).isNull();
        assertThat(medicament.getDozaRecomandata()).isEqualTo("10mg");
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldHandleNullAndEmptyValuesInArrays() {
        String json = "{\"contraindicatii\":[\"CI1\",\"\",null,\"CI2\"],\"interactiuni\":[]}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1\nCI2");
        assertThat(medicament.getInteractiuni()).isNull();
    }

    @Test
    void partialUpdate_shouldResolveInfoExternRelationship() {
        MedicamentRepository medicamentRepository = mock(MedicamentRepository.class);
        MedicamentMapper medicamentMapper = mock(MedicamentMapper.class);
        ExternalDrugInfoRepository externalDrugInfoRepository = mock(ExternalDrugInfoRepository.class);
        MedicamentService service = new MedicamentService(
            medicamentRepository,
            medicamentMapper,
            externalDrugInfoRepository,
            new ObjectMapper()
        );

        Medicament existing = new Medicament().id(10L).denumire("Med");
        ExternalDrugInfo linkedInfo = new ExternalDrugInfo().id(77L).source("SmPC");

        MedicamentDTO dto = new MedicamentDTO();
        dto.setId(10L);
        ExternalDrugInfoDTO infoDto = new ExternalDrugInfoDTO();
        infoDto.setId(77L);
        dto.setInfoExtern(infoDto);

        when(medicamentRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(externalDrugInfoRepository.findById(77L)).thenReturn(Optional.of(linkedInfo));
        doAnswer(invocation -> null).when(medicamentMapper).partialUpdate(any(Medicament.class), any(MedicamentDTO.class));
        when(medicamentRepository.save(any(Medicament.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(medicamentMapper.toDto(any(Medicament.class))).thenReturn(new MedicamentDTO());

        service.partialUpdate(dto);

        assertThat(existing.getInfoExtern()).isEqualTo(linkedInfo);
        verify(externalDrugInfoRepository).findById(77L);
    }
}
