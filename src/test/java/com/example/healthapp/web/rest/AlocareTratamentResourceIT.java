package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.AlocareTratamentAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.AlocareTratamentRepository;
import com.example.healthapp.service.AlocareTratamentService;
import com.example.healthapp.service.dto.AlocareTratamentDTO;
import com.example.healthapp.service.mapper.AlocareTratamentMapper;
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
 * Integration tests for the {@link AlocareTratamentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlocareTratamentResourceIT {

    private static final Instant DEFAULT_DATA_DECIZIE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_DECIZIE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TRATAMENT_PROPUS = "AAAAAAAAAA";
    private static final String UPDATED_TRATAMENT_PROPUS = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIV_DECIZIE = "AAAAAAAAAA";
    private static final String UPDATED_MOTIV_DECIZIE = "BBBBBBBBBB";

    private static final Double DEFAULT_SCOR_DECIZIE = 1D;
    private static final Double UPDATED_SCOR_DECIZIE = 2D;
    private static final Double SMALLER_SCOR_DECIZIE = 1D - 1D;

    private static final Boolean DEFAULT_DECIZIE_VALIDATA = false;
    private static final Boolean UPDATED_DECIZIE_VALIDATA = true;

    private static final String ENTITY_API_URL = "/api/alocare-trataments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlocareTratamentRepository alocareTratamentRepository;

    @Mock
    private AlocareTratamentRepository alocareTratamentRepositoryMock;

    @Autowired
    private AlocareTratamentMapper alocareTratamentMapper;

    @Mock
    private AlocareTratamentService alocareTratamentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlocareTratamentMockMvc;

    private AlocareTratament alocareTratament;

    private AlocareTratament insertedAlocareTratament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlocareTratament createEntity() {
        return new AlocareTratament()
            .dataDecizie(DEFAULT_DATA_DECIZIE)
            .tratamentPropus(DEFAULT_TRATAMENT_PROPUS)
            .motivDecizie(DEFAULT_MOTIV_DECIZIE)
            .scorDecizie(DEFAULT_SCOR_DECIZIE)
            .decizieValidata(DEFAULT_DECIZIE_VALIDATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlocareTratament createUpdatedEntity() {
        return new AlocareTratament()
            .dataDecizie(UPDATED_DATA_DECIZIE)
            .tratamentPropus(UPDATED_TRATAMENT_PROPUS)
            .motivDecizie(UPDATED_MOTIV_DECIZIE)
            .scorDecizie(UPDATED_SCOR_DECIZIE)
            .decizieValidata(UPDATED_DECIZIE_VALIDATA);
    }

    @BeforeEach
    void initTest() {
        alocareTratament = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlocareTratament != null) {
            alocareTratamentRepository.delete(insertedAlocareTratament);
            insertedAlocareTratament = null;
        }
    }

    @Test
    @Transactional
    void createAlocareTratament() throws Exception {
        // Persist required related entities (medicament and pacient are @NotNull in DTO)
        Medicament medicament = MedicamentResourceIT.createEntity();
        em.persist(medicament);
        Pacient pacient = PacientResourceIT.createEntity();
        em.persist(pacient);
        em.flush();
        alocareTratament.setMedicament(medicament);
        alocareTratament.setPacient(pacient);

        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);
        var returnedAlocareTratamentDTO = om.readValue(
            restAlocareTratamentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alocareTratamentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlocareTratamentDTO.class
        );

        // Validate the AlocareTratament in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlocareTratament = alocareTratamentMapper.toEntity(returnedAlocareTratamentDTO);
        assertAlocareTratamentUpdatableFieldsEquals(returnedAlocareTratament, getPersistedAlocareTratament(returnedAlocareTratament));

        insertedAlocareTratament = returnedAlocareTratament;
    }

    @Test
    @Transactional
    void createAlocareTratamentWithExistingId() throws Exception {
        // Create the AlocareTratament with an existing ID
        alocareTratament.setId(1L);
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlocareTratamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alocareTratamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataDecizieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alocareTratament.setDataDecizie(null);

        // Create the AlocareTratament, which fails.
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        restAlocareTratamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alocareTratamentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTratamentPropusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alocareTratament.setTratamentPropus(null);

        // Create the AlocareTratament, which fails.
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        restAlocareTratamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alocareTratamentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlocareTrataments() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alocareTratament.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDecizie").value(hasItem(DEFAULT_DATA_DECIZIE.toString())))
            .andExpect(jsonPath("$.[*].tratamentPropus").value(hasItem(DEFAULT_TRATAMENT_PROPUS)))
            .andExpect(jsonPath("$.[*].motivDecizie").value(hasItem(DEFAULT_MOTIV_DECIZIE)))
            .andExpect(jsonPath("$.[*].scorDecizie").value(hasItem(DEFAULT_SCOR_DECIZIE)))
            .andExpect(jsonPath("$.[*].decizieValidata").value(hasItem(DEFAULT_DECIZIE_VALIDATA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlocareTratamentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(alocareTratamentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlocareTratamentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alocareTratamentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlocareTratamentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alocareTratamentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlocareTratamentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alocareTratamentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlocareTratament() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get the alocareTratament
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL_ID, alocareTratament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alocareTratament.getId().intValue()))
            .andExpect(jsonPath("$.dataDecizie").value(DEFAULT_DATA_DECIZIE.toString()))
            .andExpect(jsonPath("$.tratamentPropus").value(DEFAULT_TRATAMENT_PROPUS))
            .andExpect(jsonPath("$.motivDecizie").value(DEFAULT_MOTIV_DECIZIE))
            .andExpect(jsonPath("$.scorDecizie").value(DEFAULT_SCOR_DECIZIE))
            .andExpect(jsonPath("$.decizieValidata").value(DEFAULT_DECIZIE_VALIDATA));
    }

    @Test
    @Transactional
    void getAlocareTratamentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        Long id = alocareTratament.getId();

        defaultAlocareTratamentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlocareTratamentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlocareTratamentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDataDecizieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where dataDecizie equals to
        defaultAlocareTratamentFiltering("dataDecizie.equals=" + DEFAULT_DATA_DECIZIE, "dataDecizie.equals=" + UPDATED_DATA_DECIZIE);
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDataDecizieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where dataDecizie in
        defaultAlocareTratamentFiltering(
            "dataDecizie.in=" + DEFAULT_DATA_DECIZIE + "," + UPDATED_DATA_DECIZIE,
            "dataDecizie.in=" + UPDATED_DATA_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDataDecizieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where dataDecizie is not null
        defaultAlocareTratamentFiltering("dataDecizie.specified=true", "dataDecizie.specified=false");
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByTratamentPropusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where tratamentPropus equals to
        defaultAlocareTratamentFiltering(
            "tratamentPropus.equals=" + DEFAULT_TRATAMENT_PROPUS,
            "tratamentPropus.equals=" + UPDATED_TRATAMENT_PROPUS
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByTratamentPropusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where tratamentPropus in
        defaultAlocareTratamentFiltering(
            "tratamentPropus.in=" + DEFAULT_TRATAMENT_PROPUS + "," + UPDATED_TRATAMENT_PROPUS,
            "tratamentPropus.in=" + UPDATED_TRATAMENT_PROPUS
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByTratamentPropusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where tratamentPropus is not null
        defaultAlocareTratamentFiltering("tratamentPropus.specified=true", "tratamentPropus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByTratamentPropusContainsSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where tratamentPropus contains
        defaultAlocareTratamentFiltering(
            "tratamentPropus.contains=" + DEFAULT_TRATAMENT_PROPUS,
            "tratamentPropus.contains=" + UPDATED_TRATAMENT_PROPUS
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByTratamentPropusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where tratamentPropus does not contain
        defaultAlocareTratamentFiltering(
            "tratamentPropus.doesNotContain=" + UPDATED_TRATAMENT_PROPUS,
            "tratamentPropus.doesNotContain=" + DEFAULT_TRATAMENT_PROPUS
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMotivDecizieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where motivDecizie equals to
        defaultAlocareTratamentFiltering("motivDecizie.equals=" + DEFAULT_MOTIV_DECIZIE, "motivDecizie.equals=" + UPDATED_MOTIV_DECIZIE);
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMotivDecizieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where motivDecizie in
        defaultAlocareTratamentFiltering(
            "motivDecizie.in=" + DEFAULT_MOTIV_DECIZIE + "," + UPDATED_MOTIV_DECIZIE,
            "motivDecizie.in=" + UPDATED_MOTIV_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMotivDecizieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where motivDecizie is not null
        defaultAlocareTratamentFiltering("motivDecizie.specified=true", "motivDecizie.specified=false");
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMotivDecizieContainsSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where motivDecizie contains
        defaultAlocareTratamentFiltering(
            "motivDecizie.contains=" + DEFAULT_MOTIV_DECIZIE,
            "motivDecizie.contains=" + UPDATED_MOTIV_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMotivDecizieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where motivDecizie does not contain
        defaultAlocareTratamentFiltering(
            "motivDecizie.doesNotContain=" + UPDATED_MOTIV_DECIZIE,
            "motivDecizie.doesNotContain=" + DEFAULT_MOTIV_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie equals to
        defaultAlocareTratamentFiltering("scorDecizie.equals=" + DEFAULT_SCOR_DECIZIE, "scorDecizie.equals=" + UPDATED_SCOR_DECIZIE);
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie in
        defaultAlocareTratamentFiltering(
            "scorDecizie.in=" + DEFAULT_SCOR_DECIZIE + "," + UPDATED_SCOR_DECIZIE,
            "scorDecizie.in=" + UPDATED_SCOR_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie is not null
        defaultAlocareTratamentFiltering("scorDecizie.specified=true", "scorDecizie.specified=false");
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie is greater than or equal to
        defaultAlocareTratamentFiltering(
            "scorDecizie.greaterThanOrEqual=" + DEFAULT_SCOR_DECIZIE,
            "scorDecizie.greaterThanOrEqual=" + UPDATED_SCOR_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie is less than or equal to
        defaultAlocareTratamentFiltering(
            "scorDecizie.lessThanOrEqual=" + DEFAULT_SCOR_DECIZIE,
            "scorDecizie.lessThanOrEqual=" + SMALLER_SCOR_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie is less than
        defaultAlocareTratamentFiltering("scorDecizie.lessThan=" + UPDATED_SCOR_DECIZIE, "scorDecizie.lessThan=" + DEFAULT_SCOR_DECIZIE);
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByScorDecizieIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where scorDecizie is greater than
        defaultAlocareTratamentFiltering(
            "scorDecizie.greaterThan=" + SMALLER_SCOR_DECIZIE,
            "scorDecizie.greaterThan=" + DEFAULT_SCOR_DECIZIE
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDecizieValidataIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where decizieValidata equals to
        defaultAlocareTratamentFiltering(
            "decizieValidata.equals=" + DEFAULT_DECIZIE_VALIDATA,
            "decizieValidata.equals=" + UPDATED_DECIZIE_VALIDATA
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDecizieValidataIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where decizieValidata in
        defaultAlocareTratamentFiltering(
            "decizieValidata.in=" + DEFAULT_DECIZIE_VALIDATA + "," + UPDATED_DECIZIE_VALIDATA,
            "decizieValidata.in=" + UPDATED_DECIZIE_VALIDATA
        );
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByDecizieValidataIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        // Get all the alocareTratamentList where decizieValidata is not null
        defaultAlocareTratamentFiltering("decizieValidata.specified=true", "decizieValidata.specified=false");
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMedicIsEqualToSomething() throws Exception {
        Medic medic;
        if (TestUtil.findAll(em, Medic.class).isEmpty()) {
            alocareTratamentRepository.saveAndFlush(alocareTratament);
            medic = MedicResourceIT.createEntity();
        } else {
            medic = TestUtil.findAll(em, Medic.class).get(0);
        }
        em.persist(medic);
        em.flush();
        alocareTratament.setMedic(medic);
        alocareTratamentRepository.saveAndFlush(alocareTratament);
        Long medicId = medic.getId();
        // Get all the alocareTratamentList where medic equals to medicId
        defaultAlocareTratamentShouldBeFound("medicId.equals=" + medicId);

        // Get all the alocareTratamentList where medic equals to (medicId + 1)
        defaultAlocareTratamentShouldNotBeFound("medicId.equals=" + (medicId + 1));
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByMedicamentIsEqualToSomething() throws Exception {
        Medicament medicament;
        if (TestUtil.findAll(em, Medicament.class).isEmpty()) {
            alocareTratamentRepository.saveAndFlush(alocareTratament);
            medicament = MedicamentResourceIT.createEntity();
        } else {
            medicament = TestUtil.findAll(em, Medicament.class).get(0);
        }
        em.persist(medicament);
        em.flush();
        alocareTratament.setMedicament(medicament);
        alocareTratamentRepository.saveAndFlush(alocareTratament);
        Long medicamentId = medicament.getId();
        // Get all the alocareTratamentList where medicament equals to medicamentId
        defaultAlocareTratamentShouldBeFound("medicamentId.equals=" + medicamentId);

        // Get all the alocareTratamentList where medicament equals to (medicamentId + 1)
        defaultAlocareTratamentShouldNotBeFound("medicamentId.equals=" + (medicamentId + 1));
    }

    @Test
    @Transactional
    void getAllAlocareTratamentsByPacientIsEqualToSomething() throws Exception {
        Pacient pacient;
        if (TestUtil.findAll(em, Pacient.class).isEmpty()) {
            alocareTratamentRepository.saveAndFlush(alocareTratament);
            pacient = PacientResourceIT.createEntity();
        } else {
            pacient = TestUtil.findAll(em, Pacient.class).get(0);
        }
        em.persist(pacient);
        em.flush();
        alocareTratament.setPacient(pacient);
        alocareTratamentRepository.saveAndFlush(alocareTratament);
        Long pacientId = pacient.getId();
        // Get all the alocareTratamentList where pacient equals to pacientId
        defaultAlocareTratamentShouldBeFound("pacientId.equals=" + pacientId);

        // Get all the alocareTratamentList where pacient equals to (pacientId + 1)
        defaultAlocareTratamentShouldNotBeFound("pacientId.equals=" + (pacientId + 1));
    }

    private void defaultAlocareTratamentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlocareTratamentShouldBeFound(shouldBeFound);
        defaultAlocareTratamentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlocareTratamentShouldBeFound(String filter) throws Exception {
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alocareTratament.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataDecizie").value(hasItem(DEFAULT_DATA_DECIZIE.toString())))
            .andExpect(jsonPath("$.[*].tratamentPropus").value(hasItem(DEFAULT_TRATAMENT_PROPUS)))
            .andExpect(jsonPath("$.[*].motivDecizie").value(hasItem(DEFAULT_MOTIV_DECIZIE)))
            .andExpect(jsonPath("$.[*].scorDecizie").value(hasItem(DEFAULT_SCOR_DECIZIE)))
            .andExpect(jsonPath("$.[*].decizieValidata").value(hasItem(DEFAULT_DECIZIE_VALIDATA)));

        // Check, that the count call also returns 1
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlocareTratamentShouldNotBeFound(String filter) throws Exception {
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlocareTratamentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlocareTratament() throws Exception {
        // Get the alocareTratament
        restAlocareTratamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlocareTratament() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alocareTratament
        AlocareTratament updatedAlocareTratament = alocareTratamentRepository.findById(alocareTratament.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlocareTratament are not directly saved in db
        em.detach(updatedAlocareTratament);

        // Persist required related entities (medicament and pacient are @NotNull in DTO)
        Medicament medicament = MedicamentResourceIT.createEntity();
        em.persist(medicament);
        Pacient pacient = PacientResourceIT.createEntity();
        em.persist(pacient);
        em.flush();
        updatedAlocareTratament.setMedicament(medicament);
        updatedAlocareTratament.setPacient(pacient);

        updatedAlocareTratament
            .dataDecizie(UPDATED_DATA_DECIZIE)
            .tratamentPropus(UPDATED_TRATAMENT_PROPUS)
            .motivDecizie(UPDATED_MOTIV_DECIZIE)
            .scorDecizie(UPDATED_SCOR_DECIZIE)
            .decizieValidata(UPDATED_DECIZIE_VALIDATA);
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(updatedAlocareTratament);

        restAlocareTratamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alocareTratamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alocareTratamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlocareTratamentToMatchAllProperties(updatedAlocareTratament);
    }

    @Test
    @Transactional
    void putNonExistingAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alocareTratamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alocareTratamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alocareTratamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alocareTratamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlocareTratamentWithPatch() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alocareTratament using partial update
        AlocareTratament partialUpdatedAlocareTratament = new AlocareTratament();
        partialUpdatedAlocareTratament.setId(alocareTratament.getId());

        partialUpdatedAlocareTratament.scorDecizie(UPDATED_SCOR_DECIZIE);

        restAlocareTratamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlocareTratament.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlocareTratament))
            )
            .andExpect(status().isOk());

        // Validate the AlocareTratament in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlocareTratamentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlocareTratament, alocareTratament),
            getPersistedAlocareTratament(alocareTratament)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlocareTratamentWithPatch() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alocareTratament using partial update
        AlocareTratament partialUpdatedAlocareTratament = new AlocareTratament();
        partialUpdatedAlocareTratament.setId(alocareTratament.getId());

        partialUpdatedAlocareTratament
            .dataDecizie(UPDATED_DATA_DECIZIE)
            .tratamentPropus(UPDATED_TRATAMENT_PROPUS)
            .motivDecizie(UPDATED_MOTIV_DECIZIE)
            .scorDecizie(UPDATED_SCOR_DECIZIE)
            .decizieValidata(UPDATED_DECIZIE_VALIDATA);

        restAlocareTratamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlocareTratament.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlocareTratament))
            )
            .andExpect(status().isOk());

        // Validate the AlocareTratament in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlocareTratamentUpdatableFieldsEquals(
            partialUpdatedAlocareTratament,
            getPersistedAlocareTratament(partialUpdatedAlocareTratament)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alocareTratamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alocareTratamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alocareTratamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlocareTratament() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alocareTratament.setId(longCount.incrementAndGet());

        // Create the AlocareTratament
        AlocareTratamentDTO alocareTratamentDTO = alocareTratamentMapper.toDto(alocareTratament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlocareTratamentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alocareTratamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlocareTratament in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlocareTratament() throws Exception {
        // Initialize the database
        insertedAlocareTratament = alocareTratamentRepository.saveAndFlush(alocareTratament);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alocareTratament
        restAlocareTratamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, alocareTratament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alocareTratamentRepository.count();
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

    protected AlocareTratament getPersistedAlocareTratament(AlocareTratament alocareTratament) {
        return alocareTratamentRepository.findById(alocareTratament.getId()).orElseThrow();
    }

    protected void assertPersistedAlocareTratamentToMatchAllProperties(AlocareTratament expectedAlocareTratament) {
        assertAlocareTratamentAllPropertiesEquals(expectedAlocareTratament, getPersistedAlocareTratament(expectedAlocareTratament));
    }

    protected void assertPersistedAlocareTratamentToMatchUpdatableProperties(AlocareTratament expectedAlocareTratament) {
        assertAlocareTratamentAllUpdatablePropertiesEquals(
            expectedAlocareTratament,
            getPersistedAlocareTratament(expectedAlocareTratament)
        );
    }
}
