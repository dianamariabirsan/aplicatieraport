package com.example.healthapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.mapper.MedicamentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            "{\"contraindicatii\":[\"CI1\",\"CI2\"],\"interactiuni\":[\"IA1\"],\"avertizari\":[\"AV1\"],\"indicatii\":[\"IND1\"]}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1\nCI2");
        assertThat(medicament.getInteractiuni()).isEqualTo("IA1");
        assertThat(medicament.getAvertizari()).isEqualTo("AV1");
        assertThat(medicament.getIndicatii()).isEqualTo("IND1");
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
        String json = "{\"contraindicatii\":[\"CI1\"]}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1");
        assertThat(medicament.getInteractiuni()).isNull();
        assertThat(medicament.getAvertizari()).isNull();
        assertThat(medicament.getIndicatii()).isNull();
    }

    @Test
    void populateMedicamentFromExternalInfo_shouldHandleNullAndEmptyValuesInArrays() {
        String json = "{\"contraindicatii\":[\"CI1\",\"\",null,\"CI2\"],\"interactiuni\":[]}";
        ExternalDrugInfo info = new ExternalDrugInfo().id(1L).source("test").productSummary(json);
        Medicament medicament = new Medicament().id(1L).denumire("Test").substanta("X");

        medicamentService.populateMedicamentFromExternalInfo(medicament, info);

        assertThat(medicament.getContraindicatii()).isEqualTo("CI1\n\nnull\nCI2");
        assertThat(medicament.getInteractiuni()).isEqualTo("");
    }
}
