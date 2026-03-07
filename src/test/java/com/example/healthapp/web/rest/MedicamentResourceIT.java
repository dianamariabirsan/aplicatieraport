package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.MedicamentAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.MedicamentDTO;
import com.example.healthapp.service.mapper.MedicamentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_ADMIN")
class MedicamentResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSTANTA = "AAAAAAAAAA";
    private static final String UPDATED_SUBSTANTA = "BBBBBBBBBB";

    private static final String DEFAULT_INDICATII = "AAAAAAAAAA";
    private static final String UPDATED_INDICATII = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRAINDICATII = "AAAAAAAAAA";
    private static final String UPDATED_CONTRAINDICATII = "BBBBBBBBBB";

    private static final String DEFAULT_INTERACTIUNI = "AAAAAAAAAA";
    private static final String UPDATED_INTERACTIUNI = "BBBBBBBBBB";

    private static final String DEFAULT_AVERTIZARI = "AAAAAAAAAA";
    private static final String UPDATED_AVERTIZARI = "BBBBBBBBBB";

    private static final String DEFAULT_DOZA_RECOMANDATA = "AAAAAAAAAA";
    private static final String UPDATED_DOZA_RECOMANDATA = "BBBBBBBBBB";

    private static final String DEFAULT_FORMA_FARMACEUTICA = "AAAAAAAAAA";
    private static final String UPDATED_FORMA_FARMACEUTICA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentMapper medicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentMockMvc;

    private Medicament medicament;

    private Medicament insertedMedicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createEntity() {
        return new Medicament()
            .denumire(DEFAULT_DENUMIRE)
            .substanta(DEFAULT_SUBSTANTA)
            .indicatii(DEFAULT_INDICATII)
            .contraindicatii(DEFAULT_CONTRAINDICATII)
            .interactiuni(DEFAULT_INTERACTIUNI)
            .avertizari(DEFAULT_AVERTIZARI)
            .dozaRecomandata(DEFAULT_DOZA_RECOMANDATA)
            .formaFarmaceutica(DEFAULT_FORMA_FARMACEUTICA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createUpdatedEntity() {
        return new Medicament()
            .denumire(UPDATED_DENUMIRE)
            .substanta(UPDATED_SUBSTANTA)
            .indicatii(UPDATED_INDICATII)
            .contraindicatii(UPDATED_CONTRAINDICATII)
            .interactiuni(UPDATED_INTERACTIUNI)
            .avertizari(UPDATED_AVERTIZARI)
            .dozaRecomandata(UPDATED_DOZA_RECOMANDATA)
            .formaFarmaceutica(UPDATED_FORMA_FARMACEUTICA);
    }

    @BeforeEach
    void initTest() {
        medicament = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMedicament != null) {
            medicamentRepository.delete(insertedMedicament);
            insertedMedicament = null;
        }
    }

    @Test
    @Transactional
    void createMedicament() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);
        var returnedMedicamentDTO = om.readValue(
            restMedicamentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicamentDTO.class
        );

        // Validate the Medicament in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicament = medicamentMapper.toEntity(returnedMedicamentDTO);
        assertMedicamentUpdatableFieldsEquals(returnedMedicament, getPersistedMedicament(returnedMedicament));

        insertedMedicament = returnedMedicament;
    }

    @Test
    @Transactional
    void createMedicamentWithExistingId() throws Exception {
        // Create the Medicament with an existing ID
        medicament.setId(1L);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDenumireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicament.setDenumire(null);

        // Create the Medicament, which fails.
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubstantaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicament.setSubstanta(null);

        // Create the Medicament, which fails.
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicaments() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].substanta").value(hasItem(DEFAULT_SUBSTANTA)))
            .andExpect(jsonPath("$.[*].indicatii").value(hasItem(DEFAULT_INDICATII)))
            .andExpect(jsonPath("$.[*].contraindicatii").value(hasItem(DEFAULT_CONTRAINDICATII)))
            .andExpect(jsonPath("$.[*].interactiuni").value(hasItem(DEFAULT_INTERACTIUNI)))
            .andExpect(jsonPath("$.[*].avertizari").value(hasItem(DEFAULT_AVERTIZARI)))
            .andExpect(jsonPath("$.[*].dozaRecomandata").value(hasItem(DEFAULT_DOZA_RECOMANDATA)))
            .andExpect(jsonPath("$.[*].formaFarmaceutica").value(hasItem(DEFAULT_FORMA_FARMACEUTICA)));
    }

    @Test
    @Transactional
    void getMedicament() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get the medicament
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, medicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicament.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE))
            .andExpect(jsonPath("$.substanta").value(DEFAULT_SUBSTANTA))
            .andExpect(jsonPath("$.indicatii").value(DEFAULT_INDICATII))
            .andExpect(jsonPath("$.contraindicatii").value(DEFAULT_CONTRAINDICATII))
            .andExpect(jsonPath("$.interactiuni").value(DEFAULT_INTERACTIUNI))
            .andExpect(jsonPath("$.avertizari").value(DEFAULT_AVERTIZARI))
            .andExpect(jsonPath("$.dozaRecomandata").value(DEFAULT_DOZA_RECOMANDATA))
            .andExpect(jsonPath("$.formaFarmaceutica").value(DEFAULT_FORMA_FARMACEUTICA));
    }

    @Test
    @Transactional
    void getMedicamentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        Long id = medicament.getId();

        defaultMedicamentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicamentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicamentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicamentsByDenumireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where denumire equals to
        defaultMedicamentFiltering("denumire.equals=" + DEFAULT_DENUMIRE, "denumire.equals=" + UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void getAllMedicamentsByDenumireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where denumire in
        defaultMedicamentFiltering("denumire.in=" + DEFAULT_DENUMIRE + "," + UPDATED_DENUMIRE, "denumire.in=" + UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void getAllMedicamentsByDenumireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where denumire is not null
        defaultMedicamentFiltering("denumire.specified=true", "denumire.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByDenumireContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where denumire contains
        defaultMedicamentFiltering("denumire.contains=" + DEFAULT_DENUMIRE, "denumire.contains=" + UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void getAllMedicamentsByDenumireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where denumire does not contain
        defaultMedicamentFiltering("denumire.doesNotContain=" + UPDATED_DENUMIRE, "denumire.doesNotContain=" + DEFAULT_DENUMIRE);
    }

    @Test
    @Transactional
    void getAllMedicamentsBySubstantaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where substanta equals to
        defaultMedicamentFiltering("substanta.equals=" + DEFAULT_SUBSTANTA, "substanta.equals=" + UPDATED_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllMedicamentsBySubstantaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where substanta in
        defaultMedicamentFiltering("substanta.in=" + DEFAULT_SUBSTANTA + "," + UPDATED_SUBSTANTA, "substanta.in=" + UPDATED_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllMedicamentsBySubstantaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where substanta is not null
        defaultMedicamentFiltering("substanta.specified=true", "substanta.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsBySubstantaContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where substanta contains
        defaultMedicamentFiltering("substanta.contains=" + DEFAULT_SUBSTANTA, "substanta.contains=" + UPDATED_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllMedicamentsBySubstantaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where substanta does not contain
        defaultMedicamentFiltering("substanta.doesNotContain=" + UPDATED_SUBSTANTA, "substanta.doesNotContain=" + DEFAULT_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllMedicamentsByIndicatiiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where indicatii equals to
        defaultMedicamentFiltering("indicatii.equals=" + DEFAULT_INDICATII, "indicatii.equals=" + UPDATED_INDICATII);
    }

    @Test
    @Transactional
    void getAllMedicamentsByIndicatiiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where indicatii in
        defaultMedicamentFiltering("indicatii.in=" + DEFAULT_INDICATII + "," + UPDATED_INDICATII, "indicatii.in=" + UPDATED_INDICATII);
    }

    @Test
    @Transactional
    void getAllMedicamentsByIndicatiiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where indicatii is not null
        defaultMedicamentFiltering("indicatii.specified=true", "indicatii.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByIndicatiiContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where indicatii contains
        defaultMedicamentFiltering("indicatii.contains=" + DEFAULT_INDICATII, "indicatii.contains=" + UPDATED_INDICATII);
    }

    @Test
    @Transactional
    void getAllMedicamentsByIndicatiiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where indicatii does not contain
        defaultMedicamentFiltering("indicatii.doesNotContain=" + UPDATED_INDICATII, "indicatii.doesNotContain=" + DEFAULT_INDICATII);
    }

    @Test
    @Transactional
    void getAllMedicamentsByContraindicatiiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where contraindicatii equals to
        defaultMedicamentFiltering(
            "contraindicatii.equals=" + DEFAULT_CONTRAINDICATII,
            "contraindicatii.equals=" + UPDATED_CONTRAINDICATII
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByContraindicatiiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where contraindicatii in
        defaultMedicamentFiltering(
            "contraindicatii.in=" + DEFAULT_CONTRAINDICATII + "," + UPDATED_CONTRAINDICATII,
            "contraindicatii.in=" + UPDATED_CONTRAINDICATII
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByContraindicatiiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where contraindicatii is not null
        defaultMedicamentFiltering("contraindicatii.specified=true", "contraindicatii.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByContraindicatiiContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where contraindicatii contains
        defaultMedicamentFiltering(
            "contraindicatii.contains=" + DEFAULT_CONTRAINDICATII,
            "contraindicatii.contains=" + UPDATED_CONTRAINDICATII
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByContraindicatiiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where contraindicatii does not contain
        defaultMedicamentFiltering(
            "contraindicatii.doesNotContain=" + UPDATED_CONTRAINDICATII,
            "contraindicatii.doesNotContain=" + DEFAULT_CONTRAINDICATII
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByInteractiuniIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where interactiuni equals to
        defaultMedicamentFiltering("interactiuni.equals=" + DEFAULT_INTERACTIUNI, "interactiuni.equals=" + UPDATED_INTERACTIUNI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByInteractiuniIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where interactiuni in
        defaultMedicamentFiltering(
            "interactiuni.in=" + DEFAULT_INTERACTIUNI + "," + UPDATED_INTERACTIUNI,
            "interactiuni.in=" + UPDATED_INTERACTIUNI
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByInteractiuniIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where interactiuni is not null
        defaultMedicamentFiltering("interactiuni.specified=true", "interactiuni.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByInteractiuniContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where interactiuni contains
        defaultMedicamentFiltering("interactiuni.contains=" + DEFAULT_INTERACTIUNI, "interactiuni.contains=" + UPDATED_INTERACTIUNI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByInteractiuniNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where interactiuni does not contain
        defaultMedicamentFiltering(
            "interactiuni.doesNotContain=" + UPDATED_INTERACTIUNI,
            "interactiuni.doesNotContain=" + DEFAULT_INTERACTIUNI
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByAvertizariIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where avertizari equals to
        defaultMedicamentFiltering("avertizari.equals=" + DEFAULT_AVERTIZARI, "avertizari.equals=" + UPDATED_AVERTIZARI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByAvertizariIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where avertizari in
        defaultMedicamentFiltering("avertizari.in=" + DEFAULT_AVERTIZARI + "," + UPDATED_AVERTIZARI, "avertizari.in=" + UPDATED_AVERTIZARI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByAvertizariIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where avertizari is not null
        defaultMedicamentFiltering("avertizari.specified=true", "avertizari.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByAvertizariContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where avertizari contains
        defaultMedicamentFiltering("avertizari.contains=" + DEFAULT_AVERTIZARI, "avertizari.contains=" + UPDATED_AVERTIZARI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByAvertizariNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where avertizari does not contain
        defaultMedicamentFiltering("avertizari.doesNotContain=" + UPDATED_AVERTIZARI, "avertizari.doesNotContain=" + DEFAULT_AVERTIZARI);
    }

    @Test
    @Transactional
    void getAllMedicamentsByDozaRecomandataIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where dozaRecomandata equals to
        defaultMedicamentFiltering(
            "dozaRecomandata.equals=" + DEFAULT_DOZA_RECOMANDATA,
            "dozaRecomandata.equals=" + UPDATED_DOZA_RECOMANDATA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByDozaRecomandataIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where dozaRecomandata in
        defaultMedicamentFiltering(
            "dozaRecomandata.in=" + DEFAULT_DOZA_RECOMANDATA + "," + UPDATED_DOZA_RECOMANDATA,
            "dozaRecomandata.in=" + UPDATED_DOZA_RECOMANDATA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByDozaRecomandataIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where dozaRecomandata is not null
        defaultMedicamentFiltering("dozaRecomandata.specified=true", "dozaRecomandata.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByDozaRecomandataContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where dozaRecomandata contains
        defaultMedicamentFiltering(
            "dozaRecomandata.contains=" + DEFAULT_DOZA_RECOMANDATA,
            "dozaRecomandata.contains=" + UPDATED_DOZA_RECOMANDATA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByDozaRecomandataNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where dozaRecomandata does not contain
        defaultMedicamentFiltering(
            "dozaRecomandata.doesNotContain=" + UPDATED_DOZA_RECOMANDATA,
            "dozaRecomandata.doesNotContain=" + DEFAULT_DOZA_RECOMANDATA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByFormaFarmaceuticaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where formaFarmaceutica equals to
        defaultMedicamentFiltering(
            "formaFarmaceutica.equals=" + DEFAULT_FORMA_FARMACEUTICA,
            "formaFarmaceutica.equals=" + UPDATED_FORMA_FARMACEUTICA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByFormaFarmaceuticaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where formaFarmaceutica in
        defaultMedicamentFiltering(
            "formaFarmaceutica.in=" + DEFAULT_FORMA_FARMACEUTICA + "," + UPDATED_FORMA_FARMACEUTICA,
            "formaFarmaceutica.in=" + UPDATED_FORMA_FARMACEUTICA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByFormaFarmaceuticaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where formaFarmaceutica is not null
        defaultMedicamentFiltering("formaFarmaceutica.specified=true", "formaFarmaceutica.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentsByFormaFarmaceuticaContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where formaFarmaceutica contains
        defaultMedicamentFiltering(
            "formaFarmaceutica.contains=" + DEFAULT_FORMA_FARMACEUTICA,
            "formaFarmaceutica.contains=" + UPDATED_FORMA_FARMACEUTICA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByFormaFarmaceuticaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where formaFarmaceutica does not contain
        defaultMedicamentFiltering(
            "formaFarmaceutica.doesNotContain=" + UPDATED_FORMA_FARMACEUTICA,
            "formaFarmaceutica.doesNotContain=" + DEFAULT_FORMA_FARMACEUTICA
        );
    }

    @Test
    @Transactional
    void getAllMedicamentsByInfoExternIsEqualToSomething() throws Exception {
        ExternalDrugInfo infoExtern;
        if (TestUtil.findAll(em, ExternalDrugInfo.class).isEmpty()) {
            medicamentRepository.saveAndFlush(medicament);
            infoExtern = ExternalDrugInfoResourceIT.createEntity();
        } else {
            infoExtern = TestUtil.findAll(em, ExternalDrugInfo.class).get(0);
        }
        em.persist(infoExtern);
        em.flush();
        medicament.setInfoExtern(infoExtern);
        medicamentRepository.saveAndFlush(medicament);
        Long infoExternId = infoExtern.getId();
        // Get all the medicamentList where infoExtern equals to infoExternId
        defaultMedicamentShouldBeFound("infoExternId.equals=" + infoExternId);

        // Get all the medicamentList where infoExtern equals to (infoExternId + 1)
        defaultMedicamentShouldNotBeFound("infoExternId.equals=" + (infoExternId + 1));
    }

    private void defaultMedicamentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicamentShouldBeFound(shouldBeFound);
        defaultMedicamentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicamentShouldBeFound(String filter) throws Exception {
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)))
            .andExpect(jsonPath("$.[*].substanta").value(hasItem(DEFAULT_SUBSTANTA)))
            .andExpect(jsonPath("$.[*].indicatii").value(hasItem(DEFAULT_INDICATII)))
            .andExpect(jsonPath("$.[*].contraindicatii").value(hasItem(DEFAULT_CONTRAINDICATII)))
            .andExpect(jsonPath("$.[*].interactiuni").value(hasItem(DEFAULT_INTERACTIUNI)))
            .andExpect(jsonPath("$.[*].avertizari").value(hasItem(DEFAULT_AVERTIZARI)))
            .andExpect(jsonPath("$.[*].dozaRecomandata").value(hasItem(DEFAULT_DOZA_RECOMANDATA)))
            .andExpect(jsonPath("$.[*].formaFarmaceutica").value(hasItem(DEFAULT_FORMA_FARMACEUTICA)));

        // Check, that the count call also returns 1
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicamentShouldNotBeFound(String filter) throws Exception {
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicament() throws Exception {
        // Get the medicament
        restMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicament() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicament
        Medicament updatedMedicament = medicamentRepository.findById(medicament.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicament are not directly saved in db
        em.detach(updatedMedicament);
        updatedMedicament
            .denumire(UPDATED_DENUMIRE)
            .substanta(UPDATED_SUBSTANTA)
            .indicatii(UPDATED_INDICATII)
            .contraindicatii(UPDATED_CONTRAINDICATII)
            .interactiuni(UPDATED_INTERACTIUNI)
            .avertizari(UPDATED_AVERTIZARI)
            .dozaRecomandata(UPDATED_DOZA_RECOMANDATA)
            .formaFarmaceutica(UPDATED_FORMA_FARMACEUTICA);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(updatedMedicament);

        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicamentToMatchAllProperties(updatedMedicament);
    }

    @Test
    @Transactional
    void putNonExistingMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament
            .substanta(UPDATED_SUBSTANTA)
            .indicatii(UPDATED_INDICATII)
            .contraindicatii(UPDATED_CONTRAINDICATII)
            .dozaRecomandata(UPDATED_DOZA_RECOMANDATA);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicament, medicament),
            getPersistedMedicament(medicament)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament
            .denumire(UPDATED_DENUMIRE)
            .substanta(UPDATED_SUBSTANTA)
            .indicatii(UPDATED_INDICATII)
            .contraindicatii(UPDATED_CONTRAINDICATII)
            .interactiuni(UPDATED_INTERACTIUNI)
            .avertizari(UPDATED_AVERTIZARI)
            .dozaRecomandata(UPDATED_DOZA_RECOMANDATA)
            .formaFarmaceutica(UPDATED_FORMA_FARMACEUTICA);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentUpdatableFieldsEquals(partialUpdatedMedicament, getPersistedMedicament(partialUpdatedMedicament));
    }

    @Test
    @Transactional
    void patchNonExistingMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicament.setId(longCount.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicament() throws Exception {
        // Initialize the database
        insertedMedicament = medicamentRepository.saveAndFlush(medicament);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicament
        restMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicamentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Medicament getPersistedMedicament(Medicament medicament) {
        return medicamentRepository.findById(medicament.getId()).orElseThrow();
    }

    protected void assertPersistedMedicamentToMatchAllProperties(Medicament expectedMedicament) {
        assertMedicamentAllPropertiesEquals(expectedMedicament, getPersistedMedicament(expectedMedicament));
    }

    protected void assertPersistedMedicamentToMatchUpdatableProperties(Medicament expectedMedicament) {
        assertMedicamentAllUpdatablePropertiesEquals(expectedMedicament, getPersistedMedicament(expectedMedicament));
    }
}
