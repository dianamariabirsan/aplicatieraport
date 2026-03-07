package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.ReactieAdversaAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.domain.ReactieAdversa;
import com.example.healthapp.domain.enumeration.SeveritateReactie;
import com.example.healthapp.repository.ReactieAdversaRepository;
import com.example.healthapp.service.ReactieAdversaService;
import com.example.healthapp.service.dto.ReactieAdversaDTO;
import com.example.healthapp.service.mapper.ReactieAdversaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReactieAdversaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_ADMIN")
class ReactieAdversaResourceIT {

    private static final Instant DEFAULT_DATA_RAPORTARE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_RAPORTARE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SeveritateReactie DEFAULT_SEVERITATE = SeveritateReactie.MICA;
    private static final SeveritateReactie UPDATED_SEVERITATE = SeveritateReactie.MEDIE;

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    private static final String DEFAULT_EVOLUTIE = "AAAAAAAAAA";
    private static final String UPDATED_EVOLUTIE = "BBBBBBBBBB";

    private static final String DEFAULT_RAPORTAT_DE = "AAAAAAAAAA";
    private static final String UPDATED_RAPORTAT_DE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reactie-adversas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReactieAdversaRepository reactieAdversaRepository;

    @Mock
    private ReactieAdversaRepository reactieAdversaRepositoryMock;

    @Autowired
    private ReactieAdversaMapper reactieAdversaMapper;

    @Mock
    private ReactieAdversaService reactieAdversaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactieAdversaMockMvc;

    private ReactieAdversa reactieAdversa;

    private ReactieAdversa insertedReactieAdversa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReactieAdversa createEntity() {
        return new ReactieAdversa()
            .dataRaportare(DEFAULT_DATA_RAPORTARE)
            .severitate(DEFAULT_SEVERITATE)
            .descriere(DEFAULT_DESCRIERE)
            .evolutie(DEFAULT_EVOLUTIE)
            .raportatDe(DEFAULT_RAPORTAT_DE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReactieAdversa createUpdatedEntity() {
        return new ReactieAdversa()
            .dataRaportare(UPDATED_DATA_RAPORTARE)
            .severitate(UPDATED_SEVERITATE)
            .descriere(UPDATED_DESCRIERE)
            .evolutie(UPDATED_EVOLUTIE)
            .raportatDe(UPDATED_RAPORTAT_DE);
    }

    @BeforeEach
    void initTest() {
        reactieAdversa = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReactieAdversa != null) {
            reactieAdversaRepository.delete(insertedReactieAdversa);
            insertedReactieAdversa = null;
        }
    }

    @Test
    @Transactional
    void createReactieAdversa() throws Exception {
        // Persist required related entities (medicament and pacient are @NotNull in DTO)
        Medicament medicament = MedicamentResourceIT.createEntity();
        em.persist(medicament);
        Pacient pacient = PacientResourceIT.createEntity();
        em.persist(pacient);
        em.flush();
        reactieAdversa.setMedicament(medicament);
        reactieAdversa.setPacient(pacient);

        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);
        var returnedReactieAdversaDTO = om.readValue(
            restReactieAdversaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactieAdversaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReactieAdversaDTO.class
        );

        // Validate the ReactieAdversa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReactieAdversa = reactieAdversaMapper.toEntity(returnedReactieAdversaDTO);
        assertReactieAdversaUpdatableFieldsEquals(returnedReactieAdversa, getPersistedReactieAdversa(returnedReactieAdversa));

        insertedReactieAdversa = returnedReactieAdversa;
    }

    @Test
    @Transactional
    void createReactieAdversaWithExistingId() throws Exception {
        // Create the ReactieAdversa with an existing ID
        reactieAdversa.setId(1L);
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactieAdversaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactieAdversaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataRaportareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reactieAdversa.setDataRaportare(null);

        // Create the ReactieAdversa, which fails.
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        restReactieAdversaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactieAdversaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriereIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reactieAdversa.setDescriere(null);

        // Create the ReactieAdversa, which fails.
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        restReactieAdversaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactieAdversaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReactieAdversas() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reactieAdversa.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataRaportare").value(hasItem(DEFAULT_DATA_RAPORTARE.toString())))
            .andExpect(jsonPath("$.[*].severitate").value(hasItem(DEFAULT_SEVERITATE.name())))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)))
            .andExpect(jsonPath("$.[*].evolutie").value(hasItem(DEFAULT_EVOLUTIE)))
            .andExpect(jsonPath("$.[*].raportatDe").value(hasItem(DEFAULT_RAPORTAT_DE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReactieAdversasWithEagerRelationshipsIsEnabled() throws Exception {
        when(reactieAdversaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReactieAdversaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reactieAdversaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReactieAdversasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reactieAdversaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReactieAdversaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reactieAdversaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReactieAdversa() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get the reactieAdversa
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL_ID, reactieAdversa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reactieAdversa.getId().intValue()))
            .andExpect(jsonPath("$.dataRaportare").value(DEFAULT_DATA_RAPORTARE.toString()))
            .andExpect(jsonPath("$.severitate").value(DEFAULT_SEVERITATE.name()))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE))
            .andExpect(jsonPath("$.evolutie").value(DEFAULT_EVOLUTIE))
            .andExpect(jsonPath("$.raportatDe").value(DEFAULT_RAPORTAT_DE));
    }

    @Test
    @Transactional
    void getReactieAdversasByIdFiltering() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        Long id = reactieAdversa.getId();

        defaultReactieAdversaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReactieAdversaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReactieAdversaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDataRaportareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where dataRaportare equals to
        defaultReactieAdversaFiltering("dataRaportare.equals=" + DEFAULT_DATA_RAPORTARE, "dataRaportare.equals=" + UPDATED_DATA_RAPORTARE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDataRaportareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where dataRaportare in
        defaultReactieAdversaFiltering(
            "dataRaportare.in=" + DEFAULT_DATA_RAPORTARE + "," + UPDATED_DATA_RAPORTARE,
            "dataRaportare.in=" + UPDATED_DATA_RAPORTARE
        );
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDataRaportareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where dataRaportare is not null
        defaultReactieAdversaFiltering("dataRaportare.specified=true", "dataRaportare.specified=false");
    }

    @Test
    @Transactional
    void getAllReactieAdversasBySeveritateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where severitate equals to
        defaultReactieAdversaFiltering("severitate.equals=" + DEFAULT_SEVERITATE, "severitate.equals=" + UPDATED_SEVERITATE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasBySeveritateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where severitate in
        defaultReactieAdversaFiltering(
            "severitate.in=" + DEFAULT_SEVERITATE + "," + UPDATED_SEVERITATE,
            "severitate.in=" + UPDATED_SEVERITATE
        );
    }

    @Test
    @Transactional
    void getAllReactieAdversasBySeveritateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where severitate is not null
        defaultReactieAdversaFiltering("severitate.specified=true", "severitate.specified=false");
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDescriereIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where descriere equals to
        defaultReactieAdversaFiltering("descriere.equals=" + DEFAULT_DESCRIERE, "descriere.equals=" + UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDescriereIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where descriere in
        defaultReactieAdversaFiltering("descriere.in=" + DEFAULT_DESCRIERE + "," + UPDATED_DESCRIERE, "descriere.in=" + UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDescriereIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where descriere is not null
        defaultReactieAdversaFiltering("descriere.specified=true", "descriere.specified=false");
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDescriereContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where descriere contains
        defaultReactieAdversaFiltering("descriere.contains=" + DEFAULT_DESCRIERE, "descriere.contains=" + UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByDescriereNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where descriere does not contain
        defaultReactieAdversaFiltering("descriere.doesNotContain=" + UPDATED_DESCRIERE, "descriere.doesNotContain=" + DEFAULT_DESCRIERE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByEvolutieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where evolutie equals to
        defaultReactieAdversaFiltering("evolutie.equals=" + DEFAULT_EVOLUTIE, "evolutie.equals=" + UPDATED_EVOLUTIE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByEvolutieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where evolutie in
        defaultReactieAdversaFiltering("evolutie.in=" + DEFAULT_EVOLUTIE + "," + UPDATED_EVOLUTIE, "evolutie.in=" + UPDATED_EVOLUTIE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByEvolutieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where evolutie is not null
        defaultReactieAdversaFiltering("evolutie.specified=true", "evolutie.specified=false");
    }

    @Test
    @Transactional
    void getAllReactieAdversasByEvolutieContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where evolutie contains
        defaultReactieAdversaFiltering("evolutie.contains=" + DEFAULT_EVOLUTIE, "evolutie.contains=" + UPDATED_EVOLUTIE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByEvolutieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where evolutie does not contain
        defaultReactieAdversaFiltering("evolutie.doesNotContain=" + UPDATED_EVOLUTIE, "evolutie.doesNotContain=" + DEFAULT_EVOLUTIE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByRaportatDeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where raportatDe equals to
        defaultReactieAdversaFiltering("raportatDe.equals=" + DEFAULT_RAPORTAT_DE, "raportatDe.equals=" + UPDATED_RAPORTAT_DE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByRaportatDeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where raportatDe in
        defaultReactieAdversaFiltering(
            "raportatDe.in=" + DEFAULT_RAPORTAT_DE + "," + UPDATED_RAPORTAT_DE,
            "raportatDe.in=" + UPDATED_RAPORTAT_DE
        );
    }

    @Test
    @Transactional
    void getAllReactieAdversasByRaportatDeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where raportatDe is not null
        defaultReactieAdversaFiltering("raportatDe.specified=true", "raportatDe.specified=false");
    }

    @Test
    @Transactional
    void getAllReactieAdversasByRaportatDeContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where raportatDe contains
        defaultReactieAdversaFiltering("raportatDe.contains=" + DEFAULT_RAPORTAT_DE, "raportatDe.contains=" + UPDATED_RAPORTAT_DE);
    }

    @Test
    @Transactional
    void getAllReactieAdversasByRaportatDeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        // Get all the reactieAdversaList where raportatDe does not contain
        defaultReactieAdversaFiltering(
            "raportatDe.doesNotContain=" + UPDATED_RAPORTAT_DE,
            "raportatDe.doesNotContain=" + DEFAULT_RAPORTAT_DE
        );
    }

    @Test
    @Transactional
    void getAllReactieAdversasByMedicamentIsEqualToSomething() throws Exception {
        Medicament medicament;
        if (TestUtil.findAll(em, Medicament.class).isEmpty()) {
            reactieAdversaRepository.saveAndFlush(reactieAdversa);
            medicament = MedicamentResourceIT.createEntity();
        } else {
            medicament = TestUtil.findAll(em, Medicament.class).get(0);
        }
        em.persist(medicament);
        em.flush();
        reactieAdversa.setMedicament(medicament);
        reactieAdversaRepository.saveAndFlush(reactieAdversa);
        Long medicamentId = medicament.getId();
        // Get all the reactieAdversaList where medicament equals to medicamentId
        defaultReactieAdversaShouldBeFound("medicamentId.equals=" + medicamentId);

        // Get all the reactieAdversaList where medicament equals to (medicamentId + 1)
        defaultReactieAdversaShouldNotBeFound("medicamentId.equals=" + (medicamentId + 1));
    }

    @Test
    @Transactional
    void getAllReactieAdversasByPacientIsEqualToSomething() throws Exception {
        Pacient pacient;
        if (TestUtil.findAll(em, Pacient.class).isEmpty()) {
            reactieAdversaRepository.saveAndFlush(reactieAdversa);
            pacient = PacientResourceIT.createEntity();
        } else {
            pacient = TestUtil.findAll(em, Pacient.class).get(0);
        }
        em.persist(pacient);
        em.flush();
        reactieAdversa.setPacient(pacient);
        reactieAdversaRepository.saveAndFlush(reactieAdversa);
        Long pacientId = pacient.getId();
        // Get all the reactieAdversaList where pacient equals to pacientId
        defaultReactieAdversaShouldBeFound("pacientId.equals=" + pacientId);

        // Get all the reactieAdversaList where pacient equals to (pacientId + 1)
        defaultReactieAdversaShouldNotBeFound("pacientId.equals=" + (pacientId + 1));
    }

    private void defaultReactieAdversaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReactieAdversaShouldBeFound(shouldBeFound);
        defaultReactieAdversaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReactieAdversaShouldBeFound(String filter) throws Exception {
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reactieAdversa.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataRaportare").value(hasItem(DEFAULT_DATA_RAPORTARE.toString())))
            .andExpect(jsonPath("$.[*].severitate").value(hasItem(DEFAULT_SEVERITATE.name())))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)))
            .andExpect(jsonPath("$.[*].evolutie").value(hasItem(DEFAULT_EVOLUTIE)))
            .andExpect(jsonPath("$.[*].raportatDe").value(hasItem(DEFAULT_RAPORTAT_DE)));

        // Check, that the count call also returns 1
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReactieAdversaShouldNotBeFound(String filter) throws Exception {
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReactieAdversaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReactieAdversa() throws Exception {
        // Get the reactieAdversa
        restReactieAdversaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReactieAdversa() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactieAdversa
        ReactieAdversa updatedReactieAdversa = reactieAdversaRepository.findById(reactieAdversa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReactieAdversa are not directly saved in db
        em.detach(updatedReactieAdversa);

        // Persist required related entities (medicament and pacient are @NotNull in DTO)
        Medicament medicament = MedicamentResourceIT.createEntity();
        em.persist(medicament);
        Pacient pacient = PacientResourceIT.createEntity();
        em.persist(pacient);
        em.flush();
        updatedReactieAdversa.setMedicament(medicament);
        updatedReactieAdversa.setPacient(pacient);

        updatedReactieAdversa
            .dataRaportare(UPDATED_DATA_RAPORTARE)
            .severitate(UPDATED_SEVERITATE)
            .descriere(UPDATED_DESCRIERE)
            .evolutie(UPDATED_EVOLUTIE)
            .raportatDe(UPDATED_RAPORTAT_DE);
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(updatedReactieAdversa);

        restReactieAdversaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactieAdversaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactieAdversaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReactieAdversaToMatchAllProperties(updatedReactieAdversa);
    }

    @Test
    @Transactional
    void putNonExistingReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactieAdversaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactieAdversaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactieAdversaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactieAdversaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactieAdversaWithPatch() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactieAdversa using partial update
        ReactieAdversa partialUpdatedReactieAdversa = new ReactieAdversa();
        partialUpdatedReactieAdversa.setId(reactieAdversa.getId());

        partialUpdatedReactieAdversa
            .dataRaportare(UPDATED_DATA_RAPORTARE)
            .severitate(UPDATED_SEVERITATE)
            .evolutie(UPDATED_EVOLUTIE)
            .raportatDe(UPDATED_RAPORTAT_DE);

        restReactieAdversaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactieAdversa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReactieAdversa))
            )
            .andExpect(status().isOk());

        // Validate the ReactieAdversa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactieAdversaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReactieAdversa, reactieAdversa),
            getPersistedReactieAdversa(reactieAdversa)
        );
    }

    @Test
    @Transactional
    void fullUpdateReactieAdversaWithPatch() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactieAdversa using partial update
        ReactieAdversa partialUpdatedReactieAdversa = new ReactieAdversa();
        partialUpdatedReactieAdversa.setId(reactieAdversa.getId());

        partialUpdatedReactieAdversa
            .dataRaportare(UPDATED_DATA_RAPORTARE)
            .severitate(UPDATED_SEVERITATE)
            .descriere(UPDATED_DESCRIERE)
            .evolutie(UPDATED_EVOLUTIE)
            .raportatDe(UPDATED_RAPORTAT_DE);

        restReactieAdversaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactieAdversa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReactieAdversa))
            )
            .andExpect(status().isOk());

        // Validate the ReactieAdversa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactieAdversaUpdatableFieldsEquals(partialUpdatedReactieAdversa, getPersistedReactieAdversa(partialUpdatedReactieAdversa));
    }

    @Test
    @Transactional
    void patchNonExistingReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reactieAdversaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactieAdversaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactieAdversaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReactieAdversa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactieAdversa.setId(longCount.incrementAndGet());

        // Create the ReactieAdversa
        ReactieAdversaDTO reactieAdversaDTO = reactieAdversaMapper.toDto(reactieAdversa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactieAdversaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reactieAdversaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReactieAdversa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReactieAdversa() throws Exception {
        // Initialize the database
        insertedReactieAdversa = reactieAdversaRepository.saveAndFlush(reactieAdversa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reactieAdversa
        restReactieAdversaMockMvc
            .perform(delete(ENTITY_API_URL_ID, reactieAdversa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reactieAdversaRepository.count();
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

    protected ReactieAdversa getPersistedReactieAdversa(ReactieAdversa reactieAdversa) {
        return reactieAdversaRepository.findById(reactieAdversa.getId()).orElseThrow();
    }

    protected void assertPersistedReactieAdversaToMatchAllProperties(ReactieAdversa expectedReactieAdversa) {
        assertReactieAdversaAllPropertiesEquals(expectedReactieAdversa, getPersistedReactieAdversa(expectedReactieAdversa));
    }

    protected void assertPersistedReactieAdversaToMatchUpdatableProperties(ReactieAdversa expectedReactieAdversa) {
        assertReactieAdversaAllUpdatablePropertiesEquals(expectedReactieAdversa, getPersistedReactieAdversa(expectedReactieAdversa));
    }
}
