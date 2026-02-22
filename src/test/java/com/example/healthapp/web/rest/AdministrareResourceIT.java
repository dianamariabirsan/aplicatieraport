package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.AdministrareAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Administrare;
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.AdministrareRepository;
import com.example.healthapp.service.AdministrareService;
import com.example.healthapp.service.dto.AdministrareDTO;
import com.example.healthapp.service.mapper.AdministrareMapper;
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
 * Integration tests for the {@link AdministrareResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdministrareResourceIT {

    private static final Instant DEFAULT_DATA_ADMINISTRARE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_ADMINISTRARE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TIP_TRATAMENT = "AAAAAAAAAA";
    private static final String UPDATED_TIP_TRATAMENT = "BBBBBBBBBB";

    private static final Double DEFAULT_DOZA = 1D;
    private static final Double UPDATED_DOZA = 2D;
    private static final Double SMALLER_DOZA = 1D - 1D;

    private static final String DEFAULT_UNITATE = "AAAAAAAAAA";
    private static final String UPDATED_UNITATE = "BBBBBBBBBB";

    private static final String DEFAULT_MOD_ADMINISTRARE = "AAAAAAAAAA";
    private static final String UPDATED_MOD_ADMINISTRARE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATII = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATII = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/administrares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdministrareRepository administrareRepository;

    @Mock
    private AdministrareRepository administrareRepositoryMock;

    @Autowired
    private AdministrareMapper administrareMapper;

    @Mock
    private AdministrareService administrareServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministrareMockMvc;

    private Administrare administrare;

    private Administrare insertedAdministrare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrare createEntity() {
        return new Administrare()
            .dataAdministrare(DEFAULT_DATA_ADMINISTRARE)
            .tipTratament(DEFAULT_TIP_TRATAMENT)
            .doza(DEFAULT_DOZA)
            .unitate(DEFAULT_UNITATE)
            .modAdministrare(DEFAULT_MOD_ADMINISTRARE)
            .observatii(DEFAULT_OBSERVATII);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrare createUpdatedEntity() {
        return new Administrare()
            .dataAdministrare(UPDATED_DATA_ADMINISTRARE)
            .tipTratament(UPDATED_TIP_TRATAMENT)
            .doza(UPDATED_DOZA)
            .unitate(UPDATED_UNITATE)
            .modAdministrare(UPDATED_MOD_ADMINISTRARE)
            .observatii(UPDATED_OBSERVATII);
    }

    @BeforeEach
    void initTest() {
        administrare = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdministrare != null) {
            administrareRepository.delete(insertedAdministrare);
            insertedAdministrare = null;
        }
    }

    @Test
    @Transactional
    void createAdministrare() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);
        var returnedAdministrareDTO = om.readValue(
            restAdministrareMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrareDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdministrareDTO.class
        );

        // Validate the Administrare in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdministrare = administrareMapper.toEntity(returnedAdministrareDTO);
        assertAdministrareUpdatableFieldsEquals(returnedAdministrare, getPersistedAdministrare(returnedAdministrare));

        insertedAdministrare = returnedAdministrare;
    }

    @Test
    @Transactional
    void createAdministrareWithExistingId() throws Exception {
        // Create the Administrare with an existing ID
        administrare.setId(1L);
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrareDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataAdministrareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrare.setDataAdministrare(null);

        // Create the Administrare, which fails.
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        restAdministrareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrareDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipTratamentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrare.setTipTratament(null);

        // Create the Administrare, which fails.
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        restAdministrareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrareDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministrares() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrare.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAdministrare").value(hasItem(DEFAULT_DATA_ADMINISTRARE.toString())))
            .andExpect(jsonPath("$.[*].tipTratament").value(hasItem(DEFAULT_TIP_TRATAMENT)))
            .andExpect(jsonPath("$.[*].doza").value(hasItem(DEFAULT_DOZA)))
            .andExpect(jsonPath("$.[*].unitate").value(hasItem(DEFAULT_UNITATE)))
            .andExpect(jsonPath("$.[*].modAdministrare").value(hasItem(DEFAULT_MOD_ADMINISTRARE)))
            .andExpect(jsonPath("$.[*].observatii").value(hasItem(DEFAULT_OBSERVATII)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministraresWithEagerRelationshipsIsEnabled() throws Exception {
        when(administrareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(administrareServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministraresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(administrareServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrareMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(administrareRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAdministrare() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get the administrare
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL_ID, administrare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrare.getId().intValue()))
            .andExpect(jsonPath("$.dataAdministrare").value(DEFAULT_DATA_ADMINISTRARE.toString()))
            .andExpect(jsonPath("$.tipTratament").value(DEFAULT_TIP_TRATAMENT))
            .andExpect(jsonPath("$.doza").value(DEFAULT_DOZA))
            .andExpect(jsonPath("$.unitate").value(DEFAULT_UNITATE))
            .andExpect(jsonPath("$.modAdministrare").value(DEFAULT_MOD_ADMINISTRARE))
            .andExpect(jsonPath("$.observatii").value(DEFAULT_OBSERVATII));
    }

    @Test
    @Transactional
    void getAdministraresByIdFiltering() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        Long id = administrare.getId();

        defaultAdministrareFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAdministrareFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAdministrareFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdministraresByDataAdministrareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where dataAdministrare equals to
        defaultAdministrareFiltering(
            "dataAdministrare.equals=" + DEFAULT_DATA_ADMINISTRARE,
            "dataAdministrare.equals=" + UPDATED_DATA_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByDataAdministrareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where dataAdministrare in
        defaultAdministrareFiltering(
            "dataAdministrare.in=" + DEFAULT_DATA_ADMINISTRARE + "," + UPDATED_DATA_ADMINISTRARE,
            "dataAdministrare.in=" + UPDATED_DATA_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByDataAdministrareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where dataAdministrare is not null
        defaultAdministrareFiltering("dataAdministrare.specified=true", "dataAdministrare.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByTipTratamentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where tipTratament equals to
        defaultAdministrareFiltering("tipTratament.equals=" + DEFAULT_TIP_TRATAMENT, "tipTratament.equals=" + UPDATED_TIP_TRATAMENT);
    }

    @Test
    @Transactional
    void getAllAdministraresByTipTratamentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where tipTratament in
        defaultAdministrareFiltering(
            "tipTratament.in=" + DEFAULT_TIP_TRATAMENT + "," + UPDATED_TIP_TRATAMENT,
            "tipTratament.in=" + UPDATED_TIP_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByTipTratamentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where tipTratament is not null
        defaultAdministrareFiltering("tipTratament.specified=true", "tipTratament.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByTipTratamentContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where tipTratament contains
        defaultAdministrareFiltering("tipTratament.contains=" + DEFAULT_TIP_TRATAMENT, "tipTratament.contains=" + UPDATED_TIP_TRATAMENT);
    }

    @Test
    @Transactional
    void getAllAdministraresByTipTratamentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where tipTratament does not contain
        defaultAdministrareFiltering(
            "tipTratament.doesNotContain=" + UPDATED_TIP_TRATAMENT,
            "tipTratament.doesNotContain=" + DEFAULT_TIP_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza equals to
        defaultAdministrareFiltering("doza.equals=" + DEFAULT_DOZA, "doza.equals=" + UPDATED_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza in
        defaultAdministrareFiltering("doza.in=" + DEFAULT_DOZA + "," + UPDATED_DOZA, "doza.in=" + UPDATED_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza is not null
        defaultAdministrareFiltering("doza.specified=true", "doza.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza is greater than or equal to
        defaultAdministrareFiltering("doza.greaterThanOrEqual=" + DEFAULT_DOZA, "doza.greaterThanOrEqual=" + UPDATED_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza is less than or equal to
        defaultAdministrareFiltering("doza.lessThanOrEqual=" + DEFAULT_DOZA, "doza.lessThanOrEqual=" + SMALLER_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza is less than
        defaultAdministrareFiltering("doza.lessThan=" + UPDATED_DOZA, "doza.lessThan=" + DEFAULT_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByDozaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where doza is greater than
        defaultAdministrareFiltering("doza.greaterThan=" + SMALLER_DOZA, "doza.greaterThan=" + DEFAULT_DOZA);
    }

    @Test
    @Transactional
    void getAllAdministraresByUnitateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where unitate equals to
        defaultAdministrareFiltering("unitate.equals=" + DEFAULT_UNITATE, "unitate.equals=" + UPDATED_UNITATE);
    }

    @Test
    @Transactional
    void getAllAdministraresByUnitateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where unitate in
        defaultAdministrareFiltering("unitate.in=" + DEFAULT_UNITATE + "," + UPDATED_UNITATE, "unitate.in=" + UPDATED_UNITATE);
    }

    @Test
    @Transactional
    void getAllAdministraresByUnitateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where unitate is not null
        defaultAdministrareFiltering("unitate.specified=true", "unitate.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByUnitateContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where unitate contains
        defaultAdministrareFiltering("unitate.contains=" + DEFAULT_UNITATE, "unitate.contains=" + UPDATED_UNITATE);
    }

    @Test
    @Transactional
    void getAllAdministraresByUnitateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where unitate does not contain
        defaultAdministrareFiltering("unitate.doesNotContain=" + UPDATED_UNITATE, "unitate.doesNotContain=" + DEFAULT_UNITATE);
    }

    @Test
    @Transactional
    void getAllAdministraresByModAdministrareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where modAdministrare equals to
        defaultAdministrareFiltering(
            "modAdministrare.equals=" + DEFAULT_MOD_ADMINISTRARE,
            "modAdministrare.equals=" + UPDATED_MOD_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByModAdministrareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where modAdministrare in
        defaultAdministrareFiltering(
            "modAdministrare.in=" + DEFAULT_MOD_ADMINISTRARE + "," + UPDATED_MOD_ADMINISTRARE,
            "modAdministrare.in=" + UPDATED_MOD_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByModAdministrareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where modAdministrare is not null
        defaultAdministrareFiltering("modAdministrare.specified=true", "modAdministrare.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByModAdministrareContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where modAdministrare contains
        defaultAdministrareFiltering(
            "modAdministrare.contains=" + DEFAULT_MOD_ADMINISTRARE,
            "modAdministrare.contains=" + UPDATED_MOD_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByModAdministrareNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where modAdministrare does not contain
        defaultAdministrareFiltering(
            "modAdministrare.doesNotContain=" + UPDATED_MOD_ADMINISTRARE,
            "modAdministrare.doesNotContain=" + DEFAULT_MOD_ADMINISTRARE
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByObservatiiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where observatii equals to
        defaultAdministrareFiltering("observatii.equals=" + DEFAULT_OBSERVATII, "observatii.equals=" + UPDATED_OBSERVATII);
    }

    @Test
    @Transactional
    void getAllAdministraresByObservatiiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where observatii in
        defaultAdministrareFiltering(
            "observatii.in=" + DEFAULT_OBSERVATII + "," + UPDATED_OBSERVATII,
            "observatii.in=" + UPDATED_OBSERVATII
        );
    }

    @Test
    @Transactional
    void getAllAdministraresByObservatiiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where observatii is not null
        defaultAdministrareFiltering("observatii.specified=true", "observatii.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministraresByObservatiiContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where observatii contains
        defaultAdministrareFiltering("observatii.contains=" + DEFAULT_OBSERVATII, "observatii.contains=" + UPDATED_OBSERVATII);
    }

    @Test
    @Transactional
    void getAllAdministraresByObservatiiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        // Get all the administrareList where observatii does not contain
        defaultAdministrareFiltering("observatii.doesNotContain=" + UPDATED_OBSERVATII, "observatii.doesNotContain=" + DEFAULT_OBSERVATII);
    }

    @Test
    @Transactional
    void getAllAdministraresByPacientIsEqualToSomething() throws Exception {
        Pacient pacient;
        if (TestUtil.findAll(em, Pacient.class).isEmpty()) {
            administrareRepository.saveAndFlush(administrare);
            pacient = PacientResourceIT.createEntity();
        } else {
            pacient = TestUtil.findAll(em, Pacient.class).get(0);
        }
        em.persist(pacient);
        em.flush();
        administrare.setPacient(pacient);
        administrareRepository.saveAndFlush(administrare);
        Long pacientId = pacient.getId();
        // Get all the administrareList where pacient equals to pacientId
        defaultAdministrareShouldBeFound("pacientId.equals=" + pacientId);

        // Get all the administrareList where pacient equals to (pacientId + 1)
        defaultAdministrareShouldNotBeFound("pacientId.equals=" + (pacientId + 1));
    }

    @Test
    @Transactional
    void getAllAdministraresByFarmacistIsEqualToSomething() throws Exception {
        Farmacist farmacist;
        if (TestUtil.findAll(em, Farmacist.class).isEmpty()) {
            administrareRepository.saveAndFlush(administrare);
            farmacist = FarmacistResourceIT.createEntity();
        } else {
            farmacist = TestUtil.findAll(em, Farmacist.class).get(0);
        }
        em.persist(farmacist);
        em.flush();
        administrare.setFarmacist(farmacist);
        administrareRepository.saveAndFlush(administrare);
        Long farmacistId = farmacist.getId();
        // Get all the administrareList where farmacist equals to farmacistId
        defaultAdministrareShouldBeFound("farmacistId.equals=" + farmacistId);

        // Get all the administrareList where farmacist equals to (farmacistId + 1)
        defaultAdministrareShouldNotBeFound("farmacistId.equals=" + (farmacistId + 1));
    }

    private void defaultAdministrareFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAdministrareShouldBeFound(shouldBeFound);
        defaultAdministrareShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdministrareShouldBeFound(String filter) throws Exception {
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrare.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAdministrare").value(hasItem(DEFAULT_DATA_ADMINISTRARE.toString())))
            .andExpect(jsonPath("$.[*].tipTratament").value(hasItem(DEFAULT_TIP_TRATAMENT)))
            .andExpect(jsonPath("$.[*].doza").value(hasItem(DEFAULT_DOZA)))
            .andExpect(jsonPath("$.[*].unitate").value(hasItem(DEFAULT_UNITATE)))
            .andExpect(jsonPath("$.[*].modAdministrare").value(hasItem(DEFAULT_MOD_ADMINISTRARE)))
            .andExpect(jsonPath("$.[*].observatii").value(hasItem(DEFAULT_OBSERVATII)));

        // Check, that the count call also returns 1
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdministrareShouldNotBeFound(String filter) throws Exception {
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdministrareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdministrare() throws Exception {
        // Get the administrare
        restAdministrareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrare() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrare
        Administrare updatedAdministrare = administrareRepository.findById(administrare.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrare are not directly saved in db
        em.detach(updatedAdministrare);
        updatedAdministrare
            .dataAdministrare(UPDATED_DATA_ADMINISTRARE)
            .tipTratament(UPDATED_TIP_TRATAMENT)
            .doza(UPDATED_DOZA)
            .unitate(UPDATED_UNITATE)
            .modAdministrare(UPDATED_MOD_ADMINISTRARE)
            .observatii(UPDATED_OBSERVATII);
        AdministrareDTO administrareDTO = administrareMapper.toDto(updatedAdministrare);

        restAdministrareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrareDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrareDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdministrareToMatchAllProperties(updatedAdministrare);
    }

    @Test
    @Transactional
    void putNonExistingAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrareDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrareDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrareWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrare using partial update
        Administrare partialUpdatedAdministrare = new Administrare();
        partialUpdatedAdministrare.setId(administrare.getId());

        partialUpdatedAdministrare.tipTratament(UPDATED_TIP_TRATAMENT).observatii(UPDATED_OBSERVATII);

        restAdministrareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrare))
            )
            .andExpect(status().isOk());

        // Validate the Administrare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrareUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdministrare, administrare),
            getPersistedAdministrare(administrare)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdministrareWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrare using partial update
        Administrare partialUpdatedAdministrare = new Administrare();
        partialUpdatedAdministrare.setId(administrare.getId());

        partialUpdatedAdministrare
            .dataAdministrare(UPDATED_DATA_ADMINISTRARE)
            .tipTratament(UPDATED_TIP_TRATAMENT)
            .doza(UPDATED_DOZA)
            .unitate(UPDATED_UNITATE)
            .modAdministrare(UPDATED_MOD_ADMINISTRARE)
            .observatii(UPDATED_OBSERVATII);

        restAdministrareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrare))
            )
            .andExpect(status().isOk());

        // Validate the Administrare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrareUpdatableFieldsEquals(partialUpdatedAdministrare, getPersistedAdministrare(partialUpdatedAdministrare));
    }

    @Test
    @Transactional
    void patchNonExistingAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrareDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrare.setId(longCount.incrementAndGet());

        // Create the Administrare
        AdministrareDTO administrareDTO = administrareMapper.toDto(administrare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(administrareDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrare() throws Exception {
        // Initialize the database
        insertedAdministrare = administrareRepository.saveAndFlush(administrare);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the administrare
        restAdministrareMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrare.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return administrareRepository.count();
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

    protected Administrare getPersistedAdministrare(Administrare administrare) {
        return administrareRepository.findById(administrare.getId()).orElseThrow();
    }

    protected void assertPersistedAdministrareToMatchAllProperties(Administrare expectedAdministrare) {
        assertAdministrareAllPropertiesEquals(expectedAdministrare, getPersistedAdministrare(expectedAdministrare));
    }

    protected void assertPersistedAdministrareToMatchUpdatableProperties(Administrare expectedAdministrare) {
        assertAdministrareAllUpdatablePropertiesEquals(expectedAdministrare, getPersistedAdministrare(expectedAdministrare));
    }
}
