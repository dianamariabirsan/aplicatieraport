package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.PacientAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.PacientRepository;
import com.example.healthapp.service.PacientService;
import com.example.healthapp.service.dto.PacientDTO;
import com.example.healthapp.service.mapper.PacientMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PacientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PacientResourceIT {

    private static final String DEFAULT_NUME = "AAAAAAAAAA";
    private static final String UPDATED_NUME = "BBBBBBBBBB";

    private static final String DEFAULT_PRENUME = "AAAAAAAAAA";
    private static final String UPDATED_PRENUME = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final Integer DEFAULT_VARSTA = 1;
    private static final Integer UPDATED_VARSTA = 2;
    private static final Integer SMALLER_VARSTA = 1 - 1;

    private static final Double DEFAULT_GREUTATE = 70D;
    private static final Double UPDATED_GREUTATE = 75D;
    private static final Double SMALLER_GREUTATE = 65D;

    private static final Double DEFAULT_INALTIME = 170D;
    private static final Double UPDATED_INALTIME = 175D;
    private static final Double SMALLER_INALTIME = 165D;

    private static final Double DEFAULT_CIRCUMFERINTA_ABDOMINALA = 80D;
    private static final Double UPDATED_CIRCUMFERINTA_ABDOMINALA = 85D;
    private static final Double SMALLER_CIRCUMFERINTA_ABDOMINALA = 75D;

    private static final String DEFAULT_CNP = "1234567890123";
    private static final String UPDATED_CNP = "9876543210987";

    private static final String DEFAULT_COMORBIDITATI = "AAAAAAAAAA";
    private static final String UPDATED_COMORBIDITATI = "BBBBBBBBBB";

    private static final String DEFAULT_GRAD_SEDENTARISM = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_SEDENTARISM = "BBBBBBBBBB";

    private static final String DEFAULT_ISTORIC_TRATAMENT = "AAAAAAAAAA";
    private static final String UPDATED_ISTORIC_TRATAMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TOLERANTA = "AAAAAAAAAA";
    private static final String UPDATED_TOLERANTA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "default@example.com";
    private static final String UPDATED_EMAIL = "updated@example.com";

    private static final String DEFAULT_TELEFON = "0712345678";
    private static final String UPDATED_TELEFON = "0798765432";

    private static final String ENTITY_API_URL = "/api/pacients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacientRepository pacientRepository;

    @Mock
    private PacientRepository pacientRepositoryMock;

    @Autowired
    private PacientMapper pacientMapper;

    @Mock
    private PacientService pacientServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacientMockMvc;

    private Pacient pacient;

    private Pacient insertedPacient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pacient createEntity() {
        return new Pacient()
            .nume(DEFAULT_NUME)
            .prenume(DEFAULT_PRENUME)
            .sex(DEFAULT_SEX)
            .varsta(DEFAULT_VARSTA)
            .greutate(DEFAULT_GREUTATE)
            .inaltime(DEFAULT_INALTIME)
            .circumferintaAbdominala(DEFAULT_CIRCUMFERINTA_ABDOMINALA)
            .cnp(DEFAULT_CNP)
            .comorbiditati(DEFAULT_COMORBIDITATI)
            .gradSedentarism(DEFAULT_GRAD_SEDENTARISM)
            .istoricTratament(DEFAULT_ISTORIC_TRATAMENT)
            .toleranta(DEFAULT_TOLERANTA)
            .email(DEFAULT_EMAIL)
            .telefon(DEFAULT_TELEFON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pacient createUpdatedEntity() {
        return new Pacient()
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .sex(UPDATED_SEX)
            .varsta(UPDATED_VARSTA)
            .greutate(UPDATED_GREUTATE)
            .inaltime(UPDATED_INALTIME)
            .circumferintaAbdominala(UPDATED_CIRCUMFERINTA_ABDOMINALA)
            .cnp(UPDATED_CNP)
            .comorbiditati(UPDATED_COMORBIDITATI)
            .gradSedentarism(UPDATED_GRAD_SEDENTARISM)
            .istoricTratament(UPDATED_ISTORIC_TRATAMENT)
            .toleranta(UPDATED_TOLERANTA)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);
    }

    @BeforeEach
    void initTest() {
        pacient = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPacient != null) {
            pacientRepository.delete(insertedPacient);
            insertedPacient = null;
        }
    }

    @Test
    @Transactional
    void createPacient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);
        var returnedPacientDTO = om.readValue(
            restPacientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PacientDTO.class
        );

        // Validate the Pacient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPacient = pacientMapper.toEntity(returnedPacientDTO);
        assertPacientUpdatableFieldsEquals(returnedPacient, getPersistedPacient(returnedPacient));

        insertedPacient = returnedPacient;
    }

    @Test
    @Transactional
    void createPacientWithExistingId() throws Exception {
        // Create the Pacient with an existing ID
        pacient.setId(1L);
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacient.setNume(null);

        // Create the Pacient, which fails.
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        restPacientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacient.setPrenume(null);

        // Create the Pacient, which fails.
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        restPacientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacient.setSex(null);

        // Create the Pacient, which fails.
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        restPacientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVarstaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pacient.setVarsta(null);

        // Create the Pacient, which fails.
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        restPacientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPacients() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList
        restPacientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacient.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].varsta").value(hasItem(DEFAULT_VARSTA)))
            .andExpect(jsonPath("$.[*].greutate").value(hasItem(DEFAULT_GREUTATE)))
            .andExpect(jsonPath("$.[*].inaltime").value(hasItem(DEFAULT_INALTIME)))
            .andExpect(jsonPath("$.[*].circumferintaAbdominala").value(hasItem(DEFAULT_CIRCUMFERINTA_ABDOMINALA)))
            .andExpect(jsonPath("$.[*].cnp").value(hasItem(DEFAULT_CNP)))
            .andExpect(jsonPath("$.[*].comorbiditati").value(hasItem(DEFAULT_COMORBIDITATI)))
            .andExpect(jsonPath("$.[*].gradSedentarism").value(hasItem(DEFAULT_GRAD_SEDENTARISM)))
            .andExpect(jsonPath("$.[*].istoricTratament").value(hasItem(DEFAULT_ISTORIC_TRATAMENT)))
            .andExpect(jsonPath("$.[*].toleranta").value(hasItem(DEFAULT_TOLERANTA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pacientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPacientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pacientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPacientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pacientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPacientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pacientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPacient() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get the pacient
        restPacientMockMvc
            .perform(get(ENTITY_API_URL_ID, pacient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pacient.getId().intValue()))
            .andExpect(jsonPath("$.nume").value(DEFAULT_NUME))
            .andExpect(jsonPath("$.prenume").value(DEFAULT_PRENUME))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX))
            .andExpect(jsonPath("$.varsta").value(DEFAULT_VARSTA))
            .andExpect(jsonPath("$.greutate").value(DEFAULT_GREUTATE))
            .andExpect(jsonPath("$.inaltime").value(DEFAULT_INALTIME))
            .andExpect(jsonPath("$.circumferintaAbdominala").value(DEFAULT_CIRCUMFERINTA_ABDOMINALA))
            .andExpect(jsonPath("$.cnp").value(DEFAULT_CNP))
            .andExpect(jsonPath("$.comorbiditati").value(DEFAULT_COMORBIDITATI))
            .andExpect(jsonPath("$.gradSedentarism").value(DEFAULT_GRAD_SEDENTARISM))
            .andExpect(jsonPath("$.istoricTratament").value(DEFAULT_ISTORIC_TRATAMENT))
            .andExpect(jsonPath("$.toleranta").value(DEFAULT_TOLERANTA))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON));
    }

    @Test
    @Transactional
    void getPacientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        Long id = pacient.getId();

        defaultPacientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPacientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPacientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPacientsByNumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where nume equals to
        defaultPacientFiltering("nume.equals=" + DEFAULT_NUME, "nume.equals=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllPacientsByNumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where nume in
        defaultPacientFiltering("nume.in=" + DEFAULT_NUME + "," + UPDATED_NUME, "nume.in=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllPacientsByNumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where nume is not null
        defaultPacientFiltering("nume.specified=true", "nume.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByNumeContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where nume contains
        defaultPacientFiltering("nume.contains=" + DEFAULT_NUME, "nume.contains=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllPacientsByNumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where nume does not contain
        defaultPacientFiltering("nume.doesNotContain=" + UPDATED_NUME, "nume.doesNotContain=" + DEFAULT_NUME);
    }

    @Test
    @Transactional
    void getAllPacientsByPrenumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where prenume equals to
        defaultPacientFiltering("prenume.equals=" + DEFAULT_PRENUME, "prenume.equals=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllPacientsByPrenumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where prenume in
        defaultPacientFiltering("prenume.in=" + DEFAULT_PRENUME + "," + UPDATED_PRENUME, "prenume.in=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllPacientsByPrenumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where prenume is not null
        defaultPacientFiltering("prenume.specified=true", "prenume.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByPrenumeContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where prenume contains
        defaultPacientFiltering("prenume.contains=" + DEFAULT_PRENUME, "prenume.contains=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllPacientsByPrenumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where prenume does not contain
        defaultPacientFiltering("prenume.doesNotContain=" + UPDATED_PRENUME, "prenume.doesNotContain=" + DEFAULT_PRENUME);
    }

    @Test
    @Transactional
    void getAllPacientsBySexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where sex equals to
        defaultPacientFiltering("sex.equals=" + DEFAULT_SEX, "sex.equals=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPacientsBySexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where sex in
        defaultPacientFiltering("sex.in=" + DEFAULT_SEX + "," + UPDATED_SEX, "sex.in=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPacientsBySexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where sex is not null
        defaultPacientFiltering("sex.specified=true", "sex.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsBySexContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where sex contains
        defaultPacientFiltering("sex.contains=" + DEFAULT_SEX, "sex.contains=" + UPDATED_SEX);
    }

    @Test
    @Transactional
    void getAllPacientsBySexNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where sex does not contain
        defaultPacientFiltering("sex.doesNotContain=" + UPDATED_SEX, "sex.doesNotContain=" + DEFAULT_SEX);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta equals to
        defaultPacientFiltering("varsta.equals=" + DEFAULT_VARSTA, "varsta.equals=" + UPDATED_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta in
        defaultPacientFiltering("varsta.in=" + DEFAULT_VARSTA + "," + UPDATED_VARSTA, "varsta.in=" + UPDATED_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta is not null
        defaultPacientFiltering("varsta.specified=true", "varsta.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta is greater than or equal to
        defaultPacientFiltering("varsta.greaterThanOrEqual=" + DEFAULT_VARSTA, "varsta.greaterThanOrEqual=" + UPDATED_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta is less than or equal to
        defaultPacientFiltering("varsta.lessThanOrEqual=" + DEFAULT_VARSTA, "varsta.lessThanOrEqual=" + SMALLER_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta is less than
        defaultPacientFiltering("varsta.lessThan=" + UPDATED_VARSTA, "varsta.lessThan=" + DEFAULT_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByVarstaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where varsta is greater than
        defaultPacientFiltering("varsta.greaterThan=" + SMALLER_VARSTA, "varsta.greaterThan=" + DEFAULT_VARSTA);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate equals to
        defaultPacientFiltering("greutate.equals=" + DEFAULT_GREUTATE, "greutate.equals=" + UPDATED_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate in
        defaultPacientFiltering("greutate.in=" + DEFAULT_GREUTATE + "," + UPDATED_GREUTATE, "greutate.in=" + UPDATED_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate is not null
        defaultPacientFiltering("greutate.specified=true", "greutate.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate is greater than or equal to
        defaultPacientFiltering("greutate.greaterThanOrEqual=" + DEFAULT_GREUTATE, "greutate.greaterThanOrEqual=" + UPDATED_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate is less than or equal to
        defaultPacientFiltering("greutate.lessThanOrEqual=" + DEFAULT_GREUTATE, "greutate.lessThanOrEqual=" + SMALLER_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate is less than
        defaultPacientFiltering("greutate.lessThan=" + UPDATED_GREUTATE, "greutate.lessThan=" + DEFAULT_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByGreutateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where greutate is greater than
        defaultPacientFiltering("greutate.greaterThan=" + SMALLER_GREUTATE, "greutate.greaterThan=" + DEFAULT_GREUTATE);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime equals to
        defaultPacientFiltering("inaltime.equals=" + DEFAULT_INALTIME, "inaltime.equals=" + UPDATED_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime in
        defaultPacientFiltering("inaltime.in=" + DEFAULT_INALTIME + "," + UPDATED_INALTIME, "inaltime.in=" + UPDATED_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime is not null
        defaultPacientFiltering("inaltime.specified=true", "inaltime.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime is greater than or equal to
        defaultPacientFiltering("inaltime.greaterThanOrEqual=" + DEFAULT_INALTIME, "inaltime.greaterThanOrEqual=" + UPDATED_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime is less than or equal to
        defaultPacientFiltering("inaltime.lessThanOrEqual=" + DEFAULT_INALTIME, "inaltime.lessThanOrEqual=" + SMALLER_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime is less than
        defaultPacientFiltering("inaltime.lessThan=" + UPDATED_INALTIME, "inaltime.lessThan=" + DEFAULT_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByInaltimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where inaltime is greater than
        defaultPacientFiltering("inaltime.greaterThan=" + SMALLER_INALTIME, "inaltime.greaterThan=" + DEFAULT_INALTIME);
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala equals to
        defaultPacientFiltering(
            "circumferintaAbdominala.equals=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.equals=" + UPDATED_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala in
        defaultPacientFiltering(
            "circumferintaAbdominala.in=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA + "," + UPDATED_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.in=" + UPDATED_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala is not null
        defaultPacientFiltering("circumferintaAbdominala.specified=true", "circumferintaAbdominala.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala is greater than or equal to
        defaultPacientFiltering(
            "circumferintaAbdominala.greaterThanOrEqual=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.greaterThanOrEqual=" + UPDATED_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala is less than or equal to
        defaultPacientFiltering(
            "circumferintaAbdominala.lessThanOrEqual=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.lessThanOrEqual=" + SMALLER_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala is less than
        defaultPacientFiltering(
            "circumferintaAbdominala.lessThan=" + UPDATED_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.lessThan=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCircumferintaAbdominalaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where circumferintaAbdominala is greater than
        defaultPacientFiltering(
            "circumferintaAbdominala.greaterThan=" + SMALLER_CIRCUMFERINTA_ABDOMINALA,
            "circumferintaAbdominala.greaterThan=" + DEFAULT_CIRCUMFERINTA_ABDOMINALA
        );
    }

    @Test
    @Transactional
    void getAllPacientsByCnpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where cnp equals to
        defaultPacientFiltering("cnp.equals=" + DEFAULT_CNP, "cnp.equals=" + UPDATED_CNP);
    }

    @Test
    @Transactional
    void getAllPacientsByCnpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where cnp in
        defaultPacientFiltering("cnp.in=" + DEFAULT_CNP + "," + UPDATED_CNP, "cnp.in=" + UPDATED_CNP);
    }

    @Test
    @Transactional
    void getAllPacientsByCnpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where cnp is not null
        defaultPacientFiltering("cnp.specified=true", "cnp.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByCnpContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where cnp contains
        defaultPacientFiltering("cnp.contains=" + DEFAULT_CNP, "cnp.contains=" + UPDATED_CNP);
    }

    @Test
    @Transactional
    void getAllPacientsByCnpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where cnp does not contain
        defaultPacientFiltering("cnp.doesNotContain=" + UPDATED_CNP, "cnp.doesNotContain=" + DEFAULT_CNP);
    }

    @Test
    @Transactional
    void getAllPacientsByComorbiditatiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where comorbiditati equals to
        defaultPacientFiltering("comorbiditati.equals=" + DEFAULT_COMORBIDITATI, "comorbiditati.equals=" + UPDATED_COMORBIDITATI);
    }

    @Test
    @Transactional
    void getAllPacientsByComorbiditatiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where comorbiditati in
        defaultPacientFiltering(
            "comorbiditati.in=" + DEFAULT_COMORBIDITATI + "," + UPDATED_COMORBIDITATI,
            "comorbiditati.in=" + UPDATED_COMORBIDITATI
        );
    }

    @Test
    @Transactional
    void getAllPacientsByComorbiditatiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where comorbiditati is not null
        defaultPacientFiltering("comorbiditati.specified=true", "comorbiditati.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByComorbiditatiContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where comorbiditati contains
        defaultPacientFiltering("comorbiditati.contains=" + DEFAULT_COMORBIDITATI, "comorbiditati.contains=" + UPDATED_COMORBIDITATI);
    }

    @Test
    @Transactional
    void getAllPacientsByComorbiditatiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where comorbiditati does not contain
        defaultPacientFiltering(
            "comorbiditati.doesNotContain=" + UPDATED_COMORBIDITATI,
            "comorbiditati.doesNotContain=" + DEFAULT_COMORBIDITATI
        );
    }

    @Test
    @Transactional
    void getAllPacientsByGradSedentarismIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where gradSedentarism equals to
        defaultPacientFiltering("gradSedentarism.equals=" + DEFAULT_GRAD_SEDENTARISM, "gradSedentarism.equals=" + UPDATED_GRAD_SEDENTARISM);
    }

    @Test
    @Transactional
    void getAllPacientsByGradSedentarismIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where gradSedentarism in
        defaultPacientFiltering(
            "gradSedentarism.in=" + DEFAULT_GRAD_SEDENTARISM + "," + UPDATED_GRAD_SEDENTARISM,
            "gradSedentarism.in=" + UPDATED_GRAD_SEDENTARISM
        );
    }

    @Test
    @Transactional
    void getAllPacientsByGradSedentarismIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where gradSedentarism is not null
        defaultPacientFiltering("gradSedentarism.specified=true", "gradSedentarism.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByGradSedentarismContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where gradSedentarism contains
        defaultPacientFiltering(
            "gradSedentarism.contains=" + DEFAULT_GRAD_SEDENTARISM,
            "gradSedentarism.contains=" + UPDATED_GRAD_SEDENTARISM
        );
    }

    @Test
    @Transactional
    void getAllPacientsByGradSedentarismNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where gradSedentarism does not contain
        defaultPacientFiltering(
            "gradSedentarism.doesNotContain=" + UPDATED_GRAD_SEDENTARISM,
            "gradSedentarism.doesNotContain=" + DEFAULT_GRAD_SEDENTARISM
        );
    }

    @Test
    @Transactional
    void getAllPacientsByIstoricTratamentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where istoricTratament equals to
        defaultPacientFiltering(
            "istoricTratament.equals=" + DEFAULT_ISTORIC_TRATAMENT,
            "istoricTratament.equals=" + UPDATED_ISTORIC_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllPacientsByIstoricTratamentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where istoricTratament in
        defaultPacientFiltering(
            "istoricTratament.in=" + DEFAULT_ISTORIC_TRATAMENT + "," + UPDATED_ISTORIC_TRATAMENT,
            "istoricTratament.in=" + UPDATED_ISTORIC_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllPacientsByIstoricTratamentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where istoricTratament is not null
        defaultPacientFiltering("istoricTratament.specified=true", "istoricTratament.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByIstoricTratamentContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where istoricTratament contains
        defaultPacientFiltering(
            "istoricTratament.contains=" + DEFAULT_ISTORIC_TRATAMENT,
            "istoricTratament.contains=" + UPDATED_ISTORIC_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllPacientsByIstoricTratamentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where istoricTratament does not contain
        defaultPacientFiltering(
            "istoricTratament.doesNotContain=" + UPDATED_ISTORIC_TRATAMENT,
            "istoricTratament.doesNotContain=" + DEFAULT_ISTORIC_TRATAMENT
        );
    }

    @Test
    @Transactional
    void getAllPacientsByTolerantaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where toleranta equals to
        defaultPacientFiltering("toleranta.equals=" + DEFAULT_TOLERANTA, "toleranta.equals=" + UPDATED_TOLERANTA);
    }

    @Test
    @Transactional
    void getAllPacientsByTolerantaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where toleranta in
        defaultPacientFiltering("toleranta.in=" + DEFAULT_TOLERANTA + "," + UPDATED_TOLERANTA, "toleranta.in=" + UPDATED_TOLERANTA);
    }

    @Test
    @Transactional
    void getAllPacientsByTolerantaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where toleranta is not null
        defaultPacientFiltering("toleranta.specified=true", "toleranta.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByTolerantaContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where toleranta contains
        defaultPacientFiltering("toleranta.contains=" + DEFAULT_TOLERANTA, "toleranta.contains=" + UPDATED_TOLERANTA);
    }

    @Test
    @Transactional
    void getAllPacientsByTolerantaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where toleranta does not contain
        defaultPacientFiltering("toleranta.doesNotContain=" + UPDATED_TOLERANTA, "toleranta.doesNotContain=" + DEFAULT_TOLERANTA);
    }

    @Test
    @Transactional
    void getAllPacientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where email equals to
        defaultPacientFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPacientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where email in
        defaultPacientFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPacientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where email is not null
        defaultPacientFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where email contains
        defaultPacientFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPacientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where email does not contain
        defaultPacientFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllPacientsByTelefonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where telefon equals to
        defaultPacientFiltering("telefon.equals=" + DEFAULT_TELEFON, "telefon.equals=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllPacientsByTelefonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where telefon in
        defaultPacientFiltering("telefon.in=" + DEFAULT_TELEFON + "," + UPDATED_TELEFON, "telefon.in=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllPacientsByTelefonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where telefon is not null
        defaultPacientFiltering("telefon.specified=true", "telefon.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientsByTelefonContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where telefon contains
        defaultPacientFiltering("telefon.contains=" + DEFAULT_TELEFON, "telefon.contains=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllPacientsByTelefonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        // Get all the pacientList where telefon does not contain
        defaultPacientFiltering("telefon.doesNotContain=" + UPDATED_TELEFON, "telefon.doesNotContain=" + DEFAULT_TELEFON);
    }

    @Test
    @Transactional
    void getAllPacientsByMedicIsEqualToSomething() throws Exception {
        Medic medic;
        if (TestUtil.findAll(em, Medic.class).isEmpty()) {
            pacientRepository.saveAndFlush(pacient);
            medic = MedicResourceIT.createEntity();
        } else {
            medic = TestUtil.findAll(em, Medic.class).get(0);
        }
        em.persist(medic);
        em.flush();
        pacient.setMedic(medic);
        pacientRepository.saveAndFlush(pacient);
        Long medicId = medic.getId();
        // Get all the pacientList where medic equals to medicId
        defaultPacientShouldBeFound("medicId.equals=" + medicId);

        // Get all the pacientList where medic equals to (medicId + 1)
        defaultPacientShouldNotBeFound("medicId.equals=" + (medicId + 1));
    }

    @Test
    @Transactional
    void getAllPacientsByFarmacistIsEqualToSomething() throws Exception {
        Farmacist farmacist;
        if (TestUtil.findAll(em, Farmacist.class).isEmpty()) {
            pacientRepository.saveAndFlush(pacient);
            farmacist = FarmacistResourceIT.createEntity();
        } else {
            farmacist = TestUtil.findAll(em, Farmacist.class).get(0);
        }
        em.persist(farmacist);
        em.flush();
        pacient.setFarmacist(farmacist);
        pacientRepository.saveAndFlush(pacient);
        Long farmacistId = farmacist.getId();
        // Get all the pacientList where farmacist equals to farmacistId
        defaultPacientShouldBeFound("farmacistId.equals=" + farmacistId);

        // Get all the pacientList where farmacist equals to (farmacistId + 1)
        defaultPacientShouldNotBeFound("farmacistId.equals=" + (farmacistId + 1));
    }

    private void defaultPacientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPacientShouldBeFound(shouldBeFound);
        defaultPacientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacientShouldBeFound(String filter) throws Exception {
        restPacientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pacient.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].varsta").value(hasItem(DEFAULT_VARSTA)))
            .andExpect(jsonPath("$.[*].greutate").value(hasItem(DEFAULT_GREUTATE)))
            .andExpect(jsonPath("$.[*].inaltime").value(hasItem(DEFAULT_INALTIME)))
            .andExpect(jsonPath("$.[*].circumferintaAbdominala").value(hasItem(DEFAULT_CIRCUMFERINTA_ABDOMINALA)))
            .andExpect(jsonPath("$.[*].cnp").value(hasItem(DEFAULT_CNP)))
            .andExpect(jsonPath("$.[*].comorbiditati").value(hasItem(DEFAULT_COMORBIDITATI)))
            .andExpect(jsonPath("$.[*].gradSedentarism").value(hasItem(DEFAULT_GRAD_SEDENTARISM)))
            .andExpect(jsonPath("$.[*].istoricTratament").value(hasItem(DEFAULT_ISTORIC_TRATAMENT)))
            .andExpect(jsonPath("$.[*].toleranta").value(hasItem(DEFAULT_TOLERANTA)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)));

        // Check, that the count call also returns 1
        restPacientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacientShouldNotBeFound(String filter) throws Exception {
        restPacientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPacient() throws Exception {
        // Get the pacient
        restPacientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPacient() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacient
        Pacient updatedPacient = pacientRepository.findById(pacient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPacient are not directly saved in db
        em.detach(updatedPacient);
        updatedPacient
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .sex(UPDATED_SEX)
            .varsta(UPDATED_VARSTA)
            .greutate(UPDATED_GREUTATE)
            .inaltime(UPDATED_INALTIME)
            .circumferintaAbdominala(UPDATED_CIRCUMFERINTA_ABDOMINALA)
            .cnp(UPDATED_CNP)
            .comorbiditati(UPDATED_COMORBIDITATI)
            .gradSedentarism(UPDATED_GRAD_SEDENTARISM)
            .istoricTratament(UPDATED_ISTORIC_TRATAMENT)
            .toleranta(UPDATED_TOLERANTA)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);
        PacientDTO pacientDTO = pacientMapper.toDto(updatedPacient);

        restPacientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacientToMatchAllProperties(updatedPacient);
    }

    @Test
    @Transactional
    void putNonExistingPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pacientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pacientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacientWithPatch() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacient using partial update
        Pacient partialUpdatedPacient = new Pacient();
        partialUpdatedPacient.setId(pacient.getId());

        partialUpdatedPacient
            .prenume(UPDATED_PRENUME)
            .sex(UPDATED_SEX)
            .varsta(UPDATED_VARSTA)
            .inaltime(UPDATED_INALTIME)
            .circumferintaAbdominala(UPDATED_CIRCUMFERINTA_ABDOMINALA)
            .cnp(UPDATED_CNP)
            .gradSedentarism(UPDATED_GRAD_SEDENTARISM)
            .toleranta(UPDATED_TOLERANTA);

        restPacientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPacient))
            )
            .andExpect(status().isOk());

        // Validate the Pacient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPacient, pacient), getPersistedPacient(pacient));
    }

    @Test
    @Transactional
    void fullUpdatePacientWithPatch() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pacient using partial update
        Pacient partialUpdatedPacient = new Pacient();
        partialUpdatedPacient.setId(pacient.getId());

        partialUpdatedPacient
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .sex(UPDATED_SEX)
            .varsta(UPDATED_VARSTA)
            .greutate(UPDATED_GREUTATE)
            .inaltime(UPDATED_INALTIME)
            .circumferintaAbdominala(UPDATED_CIRCUMFERINTA_ABDOMINALA)
            .cnp(UPDATED_CNP)
            .comorbiditati(UPDATED_COMORBIDITATI)
            .gradSedentarism(UPDATED_GRAD_SEDENTARISM)
            .istoricTratament(UPDATED_ISTORIC_TRATAMENT)
            .toleranta(UPDATED_TOLERANTA)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);

        restPacientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPacient))
            )
            .andExpect(status().isOk());

        // Validate the Pacient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacientUpdatableFieldsEquals(partialUpdatedPacient, getPersistedPacient(partialUpdatedPacient));
    }

    @Test
    @Transactional
    void patchNonExistingPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pacientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pacientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPacient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pacient.setId(longCount.incrementAndGet());

        // Create the Pacient
        PacientDTO pacientDTO = pacientMapper.toDto(pacient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pacientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pacient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePacient() throws Exception {
        // Initialize the database
        insertedPacient = pacientRepository.saveAndFlush(pacient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pacient
        restPacientMockMvc
            .perform(delete(ENTITY_API_URL_ID, pacient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacientRepository.count();
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

    protected Pacient getPersistedPacient(Pacient pacient) {
        return pacientRepository.findById(pacient.getId()).orElseThrow();
    }

    protected void assertPersistedPacientToMatchAllProperties(Pacient expectedPacient) {
        assertPacientAllPropertiesEquals(expectedPacient, getPersistedPacient(expectedPacient));
    }

    protected void assertPersistedPacientToMatchUpdatableProperties(Pacient expectedPacient) {
        assertPacientAllUpdatablePropertiesEquals(expectedPacient, getPersistedPacient(expectedPacient));
    }
}
