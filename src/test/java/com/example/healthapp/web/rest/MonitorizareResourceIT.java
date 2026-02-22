package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.MonitorizareAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Monitorizare;
import com.example.healthapp.domain.Pacient;
import com.example.healthapp.repository.MonitorizareRepository;
import com.example.healthapp.service.dto.MonitorizareDTO;
import com.example.healthapp.service.mapper.MonitorizareMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MonitorizareResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitorizareResourceIT {

    private static final Instant DEFAULT_DATA_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TENSIUNE_SIST = 1D;
    private static final Double UPDATED_TENSIUNE_SIST = 2D;
    private static final Double SMALLER_TENSIUNE_SIST = 1D - 1D;

    private static final Double DEFAULT_TENSIUNE_DIAST = 1D;
    private static final Double UPDATED_TENSIUNE_DIAST = 2D;
    private static final Double SMALLER_TENSIUNE_DIAST = 1D - 1D;

    private static final Integer DEFAULT_PULS = 1;
    private static final Integer UPDATED_PULS = 2;
    private static final Integer SMALLER_PULS = 1 - 1;

    private static final Double DEFAULT_GLICEMIE = 1D;
    private static final Double UPDATED_GLICEMIE = 2D;
    private static final Double SMALLER_GLICEMIE = 1D - 1D;

    private static final Double DEFAULT_SCOR_EFICACITATE = 1D;
    private static final Double UPDATED_SCOR_EFICACITATE = 2D;
    private static final Double SMALLER_SCOR_EFICACITATE = 1D - 1D;

    private static final String DEFAULT_COMENTARII = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARII = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/monitorizares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitorizareRepository monitorizareRepository;

    @Autowired
    private MonitorizareMapper monitorizareMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitorizareMockMvc;

    private Monitorizare monitorizare;

    private Monitorizare insertedMonitorizare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monitorizare createEntity() {
        return new Monitorizare()
            .dataInstant(DEFAULT_DATA_INSTANT)
            .tensiuneSist(DEFAULT_TENSIUNE_SIST)
            .tensiuneDiast(DEFAULT_TENSIUNE_DIAST)
            .puls(DEFAULT_PULS)
            .glicemie(DEFAULT_GLICEMIE)
            .scorEficacitate(DEFAULT_SCOR_EFICACITATE)
            .comentarii(DEFAULT_COMENTARII);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monitorizare createUpdatedEntity() {
        return new Monitorizare()
            .dataInstant(UPDATED_DATA_INSTANT)
            .tensiuneSist(UPDATED_TENSIUNE_SIST)
            .tensiuneDiast(UPDATED_TENSIUNE_DIAST)
            .puls(UPDATED_PULS)
            .glicemie(UPDATED_GLICEMIE)
            .scorEficacitate(UPDATED_SCOR_EFICACITATE)
            .comentarii(UPDATED_COMENTARII);
    }

    @BeforeEach
    void initTest() {
        monitorizare = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitorizare != null) {
            monitorizareRepository.delete(insertedMonitorizare);
            insertedMonitorizare = null;
        }
    }

    @Test
    @Transactional
    void createMonitorizare() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);
        var returnedMonitorizareDTO = om.readValue(
            restMonitorizareMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitorizareDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitorizareDTO.class
        );

        // Validate the Monitorizare in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitorizare = monitorizareMapper.toEntity(returnedMonitorizareDTO);
        assertMonitorizareUpdatableFieldsEquals(returnedMonitorizare, getPersistedMonitorizare(returnedMonitorizare));

        insertedMonitorizare = returnedMonitorizare;
    }

    @Test
    @Transactional
    void createMonitorizareWithExistingId() throws Exception {
        // Create the Monitorizare with an existing ID
        monitorizare.setId(1L);
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitorizareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitorizareDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataInstantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitorizare.setDataInstant(null);

        // Create the Monitorizare, which fails.
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        restMonitorizareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitorizareDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitorizares() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitorizare.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInstant").value(hasItem(DEFAULT_DATA_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].tensiuneSist").value(hasItem(DEFAULT_TENSIUNE_SIST)))
            .andExpect(jsonPath("$.[*].tensiuneDiast").value(hasItem(DEFAULT_TENSIUNE_DIAST)))
            .andExpect(jsonPath("$.[*].puls").value(hasItem(DEFAULT_PULS)))
            .andExpect(jsonPath("$.[*].glicemie").value(hasItem(DEFAULT_GLICEMIE)))
            .andExpect(jsonPath("$.[*].scorEficacitate").value(hasItem(DEFAULT_SCOR_EFICACITATE)))
            .andExpect(jsonPath("$.[*].comentarii").value(hasItem(DEFAULT_COMENTARII)));
    }

    @Test
    @Transactional
    void getMonitorizare() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get the monitorizare
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL_ID, monitorizare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitorizare.getId().intValue()))
            .andExpect(jsonPath("$.dataInstant").value(DEFAULT_DATA_INSTANT.toString()))
            .andExpect(jsonPath("$.tensiuneSist").value(DEFAULT_TENSIUNE_SIST))
            .andExpect(jsonPath("$.tensiuneDiast").value(DEFAULT_TENSIUNE_DIAST))
            .andExpect(jsonPath("$.puls").value(DEFAULT_PULS))
            .andExpect(jsonPath("$.glicemie").value(DEFAULT_GLICEMIE))
            .andExpect(jsonPath("$.scorEficacitate").value(DEFAULT_SCOR_EFICACITATE))
            .andExpect(jsonPath("$.comentarii").value(DEFAULT_COMENTARII));
    }

    @Test
    @Transactional
    void getMonitorizaresByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        Long id = monitorizare.getId();

        defaultMonitorizareFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitorizareFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitorizareFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByDataInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where dataInstant equals to
        defaultMonitorizareFiltering("dataInstant.equals=" + DEFAULT_DATA_INSTANT, "dataInstant.equals=" + UPDATED_DATA_INSTANT);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByDataInstantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where dataInstant in
        defaultMonitorizareFiltering(
            "dataInstant.in=" + DEFAULT_DATA_INSTANT + "," + UPDATED_DATA_INSTANT,
            "dataInstant.in=" + UPDATED_DATA_INSTANT
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByDataInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where dataInstant is not null
        defaultMonitorizareFiltering("dataInstant.specified=true", "dataInstant.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist equals to
        defaultMonitorizareFiltering("tensiuneSist.equals=" + DEFAULT_TENSIUNE_SIST, "tensiuneSist.equals=" + UPDATED_TENSIUNE_SIST);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist in
        defaultMonitorizareFiltering(
            "tensiuneSist.in=" + DEFAULT_TENSIUNE_SIST + "," + UPDATED_TENSIUNE_SIST,
            "tensiuneSist.in=" + UPDATED_TENSIUNE_SIST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist is not null
        defaultMonitorizareFiltering("tensiuneSist.specified=true", "tensiuneSist.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist is greater than or equal to
        defaultMonitorizareFiltering(
            "tensiuneSist.greaterThanOrEqual=" + DEFAULT_TENSIUNE_SIST,
            "tensiuneSist.greaterThanOrEqual=" + UPDATED_TENSIUNE_SIST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist is less than or equal to
        defaultMonitorizareFiltering(
            "tensiuneSist.lessThanOrEqual=" + DEFAULT_TENSIUNE_SIST,
            "tensiuneSist.lessThanOrEqual=" + SMALLER_TENSIUNE_SIST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist is less than
        defaultMonitorizareFiltering("tensiuneSist.lessThan=" + UPDATED_TENSIUNE_SIST, "tensiuneSist.lessThan=" + DEFAULT_TENSIUNE_SIST);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneSistIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneSist is greater than
        defaultMonitorizareFiltering(
            "tensiuneSist.greaterThan=" + SMALLER_TENSIUNE_SIST,
            "tensiuneSist.greaterThan=" + DEFAULT_TENSIUNE_SIST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast equals to
        defaultMonitorizareFiltering("tensiuneDiast.equals=" + DEFAULT_TENSIUNE_DIAST, "tensiuneDiast.equals=" + UPDATED_TENSIUNE_DIAST);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast in
        defaultMonitorizareFiltering(
            "tensiuneDiast.in=" + DEFAULT_TENSIUNE_DIAST + "," + UPDATED_TENSIUNE_DIAST,
            "tensiuneDiast.in=" + UPDATED_TENSIUNE_DIAST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast is not null
        defaultMonitorizareFiltering("tensiuneDiast.specified=true", "tensiuneDiast.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast is greater than or equal to
        defaultMonitorizareFiltering(
            "tensiuneDiast.greaterThanOrEqual=" + DEFAULT_TENSIUNE_DIAST,
            "tensiuneDiast.greaterThanOrEqual=" + UPDATED_TENSIUNE_DIAST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast is less than or equal to
        defaultMonitorizareFiltering(
            "tensiuneDiast.lessThanOrEqual=" + DEFAULT_TENSIUNE_DIAST,
            "tensiuneDiast.lessThanOrEqual=" + SMALLER_TENSIUNE_DIAST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast is less than
        defaultMonitorizareFiltering(
            "tensiuneDiast.lessThan=" + UPDATED_TENSIUNE_DIAST,
            "tensiuneDiast.lessThan=" + DEFAULT_TENSIUNE_DIAST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByTensiuneDiastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where tensiuneDiast is greater than
        defaultMonitorizareFiltering(
            "tensiuneDiast.greaterThan=" + SMALLER_TENSIUNE_DIAST,
            "tensiuneDiast.greaterThan=" + DEFAULT_TENSIUNE_DIAST
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls equals to
        defaultMonitorizareFiltering("puls.equals=" + DEFAULT_PULS, "puls.equals=" + UPDATED_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls in
        defaultMonitorizareFiltering("puls.in=" + DEFAULT_PULS + "," + UPDATED_PULS, "puls.in=" + UPDATED_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls is not null
        defaultMonitorizareFiltering("puls.specified=true", "puls.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls is greater than or equal to
        defaultMonitorizareFiltering("puls.greaterThanOrEqual=" + DEFAULT_PULS, "puls.greaterThanOrEqual=" + UPDATED_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls is less than or equal to
        defaultMonitorizareFiltering("puls.lessThanOrEqual=" + DEFAULT_PULS, "puls.lessThanOrEqual=" + SMALLER_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls is less than
        defaultMonitorizareFiltering("puls.lessThan=" + UPDATED_PULS, "puls.lessThan=" + DEFAULT_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPulsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where puls is greater than
        defaultMonitorizareFiltering("puls.greaterThan=" + SMALLER_PULS, "puls.greaterThan=" + DEFAULT_PULS);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie equals to
        defaultMonitorizareFiltering("glicemie.equals=" + DEFAULT_GLICEMIE, "glicemie.equals=" + UPDATED_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie in
        defaultMonitorizareFiltering("glicemie.in=" + DEFAULT_GLICEMIE + "," + UPDATED_GLICEMIE, "glicemie.in=" + UPDATED_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie is not null
        defaultMonitorizareFiltering("glicemie.specified=true", "glicemie.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie is greater than or equal to
        defaultMonitorizareFiltering("glicemie.greaterThanOrEqual=" + DEFAULT_GLICEMIE, "glicemie.greaterThanOrEqual=" + UPDATED_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie is less than or equal to
        defaultMonitorizareFiltering("glicemie.lessThanOrEqual=" + DEFAULT_GLICEMIE, "glicemie.lessThanOrEqual=" + SMALLER_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie is less than
        defaultMonitorizareFiltering("glicemie.lessThan=" + UPDATED_GLICEMIE, "glicemie.lessThan=" + DEFAULT_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByGlicemieIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where glicemie is greater than
        defaultMonitorizareFiltering("glicemie.greaterThan=" + SMALLER_GLICEMIE, "glicemie.greaterThan=" + DEFAULT_GLICEMIE);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate equals to
        defaultMonitorizareFiltering(
            "scorEficacitate.equals=" + DEFAULT_SCOR_EFICACITATE,
            "scorEficacitate.equals=" + UPDATED_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate in
        defaultMonitorizareFiltering(
            "scorEficacitate.in=" + DEFAULT_SCOR_EFICACITATE + "," + UPDATED_SCOR_EFICACITATE,
            "scorEficacitate.in=" + UPDATED_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate is not null
        defaultMonitorizareFiltering("scorEficacitate.specified=true", "scorEficacitate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate is greater than or equal to
        defaultMonitorizareFiltering(
            "scorEficacitate.greaterThanOrEqual=" + DEFAULT_SCOR_EFICACITATE,
            "scorEficacitate.greaterThanOrEqual=" + UPDATED_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate is less than or equal to
        defaultMonitorizareFiltering(
            "scorEficacitate.lessThanOrEqual=" + DEFAULT_SCOR_EFICACITATE,
            "scorEficacitate.lessThanOrEqual=" + SMALLER_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate is less than
        defaultMonitorizareFiltering(
            "scorEficacitate.lessThan=" + UPDATED_SCOR_EFICACITATE,
            "scorEficacitate.lessThan=" + DEFAULT_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByScorEficacitateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where scorEficacitate is greater than
        defaultMonitorizareFiltering(
            "scorEficacitate.greaterThan=" + SMALLER_SCOR_EFICACITATE,
            "scorEficacitate.greaterThan=" + DEFAULT_SCOR_EFICACITATE
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByComentariiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where comentarii equals to
        defaultMonitorizareFiltering("comentarii.equals=" + DEFAULT_COMENTARII, "comentarii.equals=" + UPDATED_COMENTARII);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByComentariiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where comentarii in
        defaultMonitorizareFiltering(
            "comentarii.in=" + DEFAULT_COMENTARII + "," + UPDATED_COMENTARII,
            "comentarii.in=" + UPDATED_COMENTARII
        );
    }

    @Test
    @Transactional
    void getAllMonitorizaresByComentariiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where comentarii is not null
        defaultMonitorizareFiltering("comentarii.specified=true", "comentarii.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitorizaresByComentariiContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where comentarii contains
        defaultMonitorizareFiltering("comentarii.contains=" + DEFAULT_COMENTARII, "comentarii.contains=" + UPDATED_COMENTARII);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByComentariiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        // Get all the monitorizareList where comentarii does not contain
        defaultMonitorizareFiltering("comentarii.doesNotContain=" + UPDATED_COMENTARII, "comentarii.doesNotContain=" + DEFAULT_COMENTARII);
    }

    @Test
    @Transactional
    void getAllMonitorizaresByPacientIsEqualToSomething() throws Exception {
        Pacient pacient;
        if (TestUtil.findAll(em, Pacient.class).isEmpty()) {
            monitorizareRepository.saveAndFlush(monitorizare);
            pacient = PacientResourceIT.createEntity();
        } else {
            pacient = TestUtil.findAll(em, Pacient.class).get(0);
        }
        em.persist(pacient);
        em.flush();
        monitorizare.setPacient(pacient);
        monitorizareRepository.saveAndFlush(monitorizare);
        Long pacientId = pacient.getId();
        // Get all the monitorizareList where pacient equals to pacientId
        defaultMonitorizareShouldBeFound("pacientId.equals=" + pacientId);

        // Get all the monitorizareList where pacient equals to (pacientId + 1)
        defaultMonitorizareShouldNotBeFound("pacientId.equals=" + (pacientId + 1));
    }

    private void defaultMonitorizareFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitorizareShouldBeFound(shouldBeFound);
        defaultMonitorizareShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitorizareShouldBeFound(String filter) throws Exception {
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitorizare.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInstant").value(hasItem(DEFAULT_DATA_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].tensiuneSist").value(hasItem(DEFAULT_TENSIUNE_SIST)))
            .andExpect(jsonPath("$.[*].tensiuneDiast").value(hasItem(DEFAULT_TENSIUNE_DIAST)))
            .andExpect(jsonPath("$.[*].puls").value(hasItem(DEFAULT_PULS)))
            .andExpect(jsonPath("$.[*].glicemie").value(hasItem(DEFAULT_GLICEMIE)))
            .andExpect(jsonPath("$.[*].scorEficacitate").value(hasItem(DEFAULT_SCOR_EFICACITATE)))
            .andExpect(jsonPath("$.[*].comentarii").value(hasItem(DEFAULT_COMENTARII)));

        // Check, that the count call also returns 1
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitorizareShouldNotBeFound(String filter) throws Exception {
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitorizareMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitorizare() throws Exception {
        // Get the monitorizare
        restMonitorizareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitorizare() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitorizare
        Monitorizare updatedMonitorizare = monitorizareRepository.findById(monitorizare.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMonitorizare are not directly saved in db
        em.detach(updatedMonitorizare);
        updatedMonitorizare
            .dataInstant(UPDATED_DATA_INSTANT)
            .tensiuneSist(UPDATED_TENSIUNE_SIST)
            .tensiuneDiast(UPDATED_TENSIUNE_DIAST)
            .puls(UPDATED_PULS)
            .glicemie(UPDATED_GLICEMIE)
            .scorEficacitate(UPDATED_SCOR_EFICACITATE)
            .comentarii(UPDATED_COMENTARII);
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(updatedMonitorizare);

        restMonitorizareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitorizareDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitorizareDTO))
            )
            .andExpect(status().isOk());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitorizareToMatchAllProperties(updatedMonitorizare);
    }

    @Test
    @Transactional
    void putNonExistingMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitorizareDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitorizareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitorizareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitorizareDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitorizareWithPatch() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitorizare using partial update
        Monitorizare partialUpdatedMonitorizare = new Monitorizare();
        partialUpdatedMonitorizare.setId(monitorizare.getId());

        partialUpdatedMonitorizare
            .dataInstant(UPDATED_DATA_INSTANT)
            .tensiuneDiast(UPDATED_TENSIUNE_DIAST)
            .glicemie(UPDATED_GLICEMIE)
            .scorEficacitate(UPDATED_SCOR_EFICACITATE);

        restMonitorizareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitorizare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitorizare))
            )
            .andExpect(status().isOk());

        // Validate the Monitorizare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitorizareUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitorizare, monitorizare),
            getPersistedMonitorizare(monitorizare)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitorizareWithPatch() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitorizare using partial update
        Monitorizare partialUpdatedMonitorizare = new Monitorizare();
        partialUpdatedMonitorizare.setId(monitorizare.getId());

        partialUpdatedMonitorizare
            .dataInstant(UPDATED_DATA_INSTANT)
            .tensiuneSist(UPDATED_TENSIUNE_SIST)
            .tensiuneDiast(UPDATED_TENSIUNE_DIAST)
            .puls(UPDATED_PULS)
            .glicemie(UPDATED_GLICEMIE)
            .scorEficacitate(UPDATED_SCOR_EFICACITATE)
            .comentarii(UPDATED_COMENTARII);

        restMonitorizareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitorizare.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitorizare))
            )
            .andExpect(status().isOk());

        // Validate the Monitorizare in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitorizareUpdatableFieldsEquals(partialUpdatedMonitorizare, getPersistedMonitorizare(partialUpdatedMonitorizare));
    }

    @Test
    @Transactional
    void patchNonExistingMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitorizareDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitorizareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitorizareDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitorizare() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitorizare.setId(longCount.incrementAndGet());

        // Create the Monitorizare
        MonitorizareDTO monitorizareDTO = monitorizareMapper.toDto(monitorizare);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitorizareMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitorizareDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monitorizare in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitorizare() throws Exception {
        // Initialize the database
        insertedMonitorizare = monitorizareRepository.saveAndFlush(monitorizare);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitorizare
        restMonitorizareMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitorizare.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitorizareRepository.count();
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

    protected Monitorizare getPersistedMonitorizare(Monitorizare monitorizare) {
        return monitorizareRepository.findById(monitorizare.getId()).orElseThrow();
    }

    protected void assertPersistedMonitorizareToMatchAllProperties(Monitorizare expectedMonitorizare) {
        assertMonitorizareAllPropertiesEquals(expectedMonitorizare, getPersistedMonitorizare(expectedMonitorizare));
    }

    protected void assertPersistedMonitorizareToMatchUpdatableProperties(Monitorizare expectedMonitorizare) {
        assertMonitorizareAllUpdatablePropertiesEquals(expectedMonitorizare, getPersistedMonitorizare(expectedMonitorizare));
    }
}
