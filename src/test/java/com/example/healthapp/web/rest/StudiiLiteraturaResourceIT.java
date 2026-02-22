package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.StudiiLiteraturaAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.StudiiLiteratura;
import com.example.healthapp.repository.StudiiLiteraturaRepository;
import com.example.healthapp.service.dto.StudiiLiteraturaDTO;
import com.example.healthapp.service.mapper.StudiiLiteraturaMapper;
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
 * Integration tests for the {@link StudiiLiteraturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudiiLiteraturaResourceIT {

    private static final String DEFAULT_TITLU = "AAAAAAAAAA";
    private static final String UPDATED_TITLU = "BBBBBBBBBB";

    private static final String DEFAULT_AUTORI = "AAAAAAAAAA";
    private static final String UPDATED_AUTORI = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANUL = 1;
    private static final Integer UPDATED_ANUL = 2;
    private static final Integer SMALLER_ANUL = 1 - 1;

    private static final String DEFAULT_TIP_STUDIU = "AAAAAAAAAA";
    private static final String UPDATED_TIP_STUDIU = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSTANTA = "AAAAAAAAAA";
    private static final String UPDATED_SUBSTANTA = "BBBBBBBBBB";

    private static final String DEFAULT_CONCLUZIE = "AAAAAAAAAA";
    private static final String UPDATED_CONCLUZIE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/studii-literaturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudiiLiteraturaRepository studiiLiteraturaRepository;

    @Autowired
    private StudiiLiteraturaMapper studiiLiteraturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudiiLiteraturaMockMvc;

    private StudiiLiteratura studiiLiteratura;

    private StudiiLiteratura insertedStudiiLiteratura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudiiLiteratura createEntity() {
        return new StudiiLiteratura()
            .titlu(DEFAULT_TITLU)
            .autori(DEFAULT_AUTORI)
            .anul(DEFAULT_ANUL)
            .tipStudiu(DEFAULT_TIP_STUDIU)
            .substanta(DEFAULT_SUBSTANTA)
            .concluzie(DEFAULT_CONCLUZIE)
            .link(DEFAULT_LINK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudiiLiteratura createUpdatedEntity() {
        return new StudiiLiteratura()
            .titlu(UPDATED_TITLU)
            .autori(UPDATED_AUTORI)
            .anul(UPDATED_ANUL)
            .tipStudiu(UPDATED_TIP_STUDIU)
            .substanta(UPDATED_SUBSTANTA)
            .concluzie(UPDATED_CONCLUZIE)
            .link(UPDATED_LINK);
    }

    @BeforeEach
    void initTest() {
        studiiLiteratura = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStudiiLiteratura != null) {
            studiiLiteraturaRepository.delete(insertedStudiiLiteratura);
            insertedStudiiLiteratura = null;
        }
    }

    @Test
    @Transactional
    void createStudiiLiteratura() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);
        var returnedStudiiLiteraturaDTO = om.readValue(
            restStudiiLiteraturaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studiiLiteraturaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudiiLiteraturaDTO.class
        );

        // Validate the StudiiLiteratura in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudiiLiteratura = studiiLiteraturaMapper.toEntity(returnedStudiiLiteraturaDTO);
        assertStudiiLiteraturaUpdatableFieldsEquals(returnedStudiiLiteratura, getPersistedStudiiLiteratura(returnedStudiiLiteratura));

        insertedStudiiLiteratura = returnedStudiiLiteratura;
    }

    @Test
    @Transactional
    void createStudiiLiteraturaWithExistingId() throws Exception {
        // Create the StudiiLiteratura with an existing ID
        studiiLiteratura.setId(1L);
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudiiLiteraturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studiiLiteraturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitluIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studiiLiteratura.setTitlu(null);

        // Create the StudiiLiteratura, which fails.
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        restStudiiLiteraturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studiiLiteraturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturas() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studiiLiteratura.getId().intValue())))
            .andExpect(jsonPath("$.[*].titlu").value(hasItem(DEFAULT_TITLU)))
            .andExpect(jsonPath("$.[*].autori").value(hasItem(DEFAULT_AUTORI)))
            .andExpect(jsonPath("$.[*].anul").value(hasItem(DEFAULT_ANUL)))
            .andExpect(jsonPath("$.[*].tipStudiu").value(hasItem(DEFAULT_TIP_STUDIU)))
            .andExpect(jsonPath("$.[*].substanta").value(hasItem(DEFAULT_SUBSTANTA)))
            .andExpect(jsonPath("$.[*].concluzie").value(hasItem(DEFAULT_CONCLUZIE)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));
    }

    @Test
    @Transactional
    void getStudiiLiteratura() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get the studiiLiteratura
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL_ID, studiiLiteratura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studiiLiteratura.getId().intValue()))
            .andExpect(jsonPath("$.titlu").value(DEFAULT_TITLU))
            .andExpect(jsonPath("$.autori").value(DEFAULT_AUTORI))
            .andExpect(jsonPath("$.anul").value(DEFAULT_ANUL))
            .andExpect(jsonPath("$.tipStudiu").value(DEFAULT_TIP_STUDIU))
            .andExpect(jsonPath("$.substanta").value(DEFAULT_SUBSTANTA))
            .andExpect(jsonPath("$.concluzie").value(DEFAULT_CONCLUZIE))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK));
    }

    @Test
    @Transactional
    void getStudiiLiteraturasByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        Long id = studiiLiteratura.getId();

        defaultStudiiLiteraturaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudiiLiteraturaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudiiLiteraturaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTitluIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where titlu equals to
        defaultStudiiLiteraturaFiltering("titlu.equals=" + DEFAULT_TITLU, "titlu.equals=" + UPDATED_TITLU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTitluIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where titlu in
        defaultStudiiLiteraturaFiltering("titlu.in=" + DEFAULT_TITLU + "," + UPDATED_TITLU, "titlu.in=" + UPDATED_TITLU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTitluIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where titlu is not null
        defaultStudiiLiteraturaFiltering("titlu.specified=true", "titlu.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTitluContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where titlu contains
        defaultStudiiLiteraturaFiltering("titlu.contains=" + DEFAULT_TITLU, "titlu.contains=" + UPDATED_TITLU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTitluNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where titlu does not contain
        defaultStudiiLiteraturaFiltering("titlu.doesNotContain=" + UPDATED_TITLU, "titlu.doesNotContain=" + DEFAULT_TITLU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAutoriIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where autori equals to
        defaultStudiiLiteraturaFiltering("autori.equals=" + DEFAULT_AUTORI, "autori.equals=" + UPDATED_AUTORI);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAutoriIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where autori in
        defaultStudiiLiteraturaFiltering("autori.in=" + DEFAULT_AUTORI + "," + UPDATED_AUTORI, "autori.in=" + UPDATED_AUTORI);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAutoriIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where autori is not null
        defaultStudiiLiteraturaFiltering("autori.specified=true", "autori.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAutoriContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where autori contains
        defaultStudiiLiteraturaFiltering("autori.contains=" + DEFAULT_AUTORI, "autori.contains=" + UPDATED_AUTORI);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAutoriNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where autori does not contain
        defaultStudiiLiteraturaFiltering("autori.doesNotContain=" + UPDATED_AUTORI, "autori.doesNotContain=" + DEFAULT_AUTORI);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul equals to
        defaultStudiiLiteraturaFiltering("anul.equals=" + DEFAULT_ANUL, "anul.equals=" + UPDATED_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul in
        defaultStudiiLiteraturaFiltering("anul.in=" + DEFAULT_ANUL + "," + UPDATED_ANUL, "anul.in=" + UPDATED_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul is not null
        defaultStudiiLiteraturaFiltering("anul.specified=true", "anul.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul is greater than or equal to
        defaultStudiiLiteraturaFiltering("anul.greaterThanOrEqual=" + DEFAULT_ANUL, "anul.greaterThanOrEqual=" + UPDATED_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul is less than or equal to
        defaultStudiiLiteraturaFiltering("anul.lessThanOrEqual=" + DEFAULT_ANUL, "anul.lessThanOrEqual=" + SMALLER_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul is less than
        defaultStudiiLiteraturaFiltering("anul.lessThan=" + UPDATED_ANUL, "anul.lessThan=" + DEFAULT_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByAnulIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where anul is greater than
        defaultStudiiLiteraturaFiltering("anul.greaterThan=" + SMALLER_ANUL, "anul.greaterThan=" + DEFAULT_ANUL);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTipStudiuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where tipStudiu equals to
        defaultStudiiLiteraturaFiltering("tipStudiu.equals=" + DEFAULT_TIP_STUDIU, "tipStudiu.equals=" + UPDATED_TIP_STUDIU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTipStudiuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where tipStudiu in
        defaultStudiiLiteraturaFiltering(
            "tipStudiu.in=" + DEFAULT_TIP_STUDIU + "," + UPDATED_TIP_STUDIU,
            "tipStudiu.in=" + UPDATED_TIP_STUDIU
        );
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTipStudiuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where tipStudiu is not null
        defaultStudiiLiteraturaFiltering("tipStudiu.specified=true", "tipStudiu.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTipStudiuContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where tipStudiu contains
        defaultStudiiLiteraturaFiltering("tipStudiu.contains=" + DEFAULT_TIP_STUDIU, "tipStudiu.contains=" + UPDATED_TIP_STUDIU);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByTipStudiuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where tipStudiu does not contain
        defaultStudiiLiteraturaFiltering(
            "tipStudiu.doesNotContain=" + UPDATED_TIP_STUDIU,
            "tipStudiu.doesNotContain=" + DEFAULT_TIP_STUDIU
        );
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasBySubstantaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where substanta equals to
        defaultStudiiLiteraturaFiltering("substanta.equals=" + DEFAULT_SUBSTANTA, "substanta.equals=" + UPDATED_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasBySubstantaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where substanta in
        defaultStudiiLiteraturaFiltering(
            "substanta.in=" + DEFAULT_SUBSTANTA + "," + UPDATED_SUBSTANTA,
            "substanta.in=" + UPDATED_SUBSTANTA
        );
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasBySubstantaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where substanta is not null
        defaultStudiiLiteraturaFiltering("substanta.specified=true", "substanta.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasBySubstantaContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where substanta contains
        defaultStudiiLiteraturaFiltering("substanta.contains=" + DEFAULT_SUBSTANTA, "substanta.contains=" + UPDATED_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasBySubstantaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where substanta does not contain
        defaultStudiiLiteraturaFiltering("substanta.doesNotContain=" + UPDATED_SUBSTANTA, "substanta.doesNotContain=" + DEFAULT_SUBSTANTA);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByConcluzieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where concluzie equals to
        defaultStudiiLiteraturaFiltering("concluzie.equals=" + DEFAULT_CONCLUZIE, "concluzie.equals=" + UPDATED_CONCLUZIE);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByConcluzieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where concluzie in
        defaultStudiiLiteraturaFiltering(
            "concluzie.in=" + DEFAULT_CONCLUZIE + "," + UPDATED_CONCLUZIE,
            "concluzie.in=" + UPDATED_CONCLUZIE
        );
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByConcluzieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where concluzie is not null
        defaultStudiiLiteraturaFiltering("concluzie.specified=true", "concluzie.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByConcluzieContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where concluzie contains
        defaultStudiiLiteraturaFiltering("concluzie.contains=" + DEFAULT_CONCLUZIE, "concluzie.contains=" + UPDATED_CONCLUZIE);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByConcluzieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where concluzie does not contain
        defaultStudiiLiteraturaFiltering("concluzie.doesNotContain=" + UPDATED_CONCLUZIE, "concluzie.doesNotContain=" + DEFAULT_CONCLUZIE);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where link equals to
        defaultStudiiLiteraturaFiltering("link.equals=" + DEFAULT_LINK, "link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where link in
        defaultStudiiLiteraturaFiltering("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK, "link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where link is not null
        defaultStudiiLiteraturaFiltering("link.specified=true", "link.specified=false");
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByLinkContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where link contains
        defaultStudiiLiteraturaFiltering("link.contains=" + DEFAULT_LINK, "link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        // Get all the studiiLiteraturaList where link does not contain
        defaultStudiiLiteraturaFiltering("link.doesNotContain=" + UPDATED_LINK, "link.doesNotContain=" + DEFAULT_LINK);
    }

    @Test
    @Transactional
    void getAllStudiiLiteraturasByMedicamentIsEqualToSomething() throws Exception {
        Medicament medicament;
        if (TestUtil.findAll(em, Medicament.class).isEmpty()) {
            studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);
            medicament = MedicamentResourceIT.createEntity();
        } else {
            medicament = TestUtil.findAll(em, Medicament.class).get(0);
        }
        em.persist(medicament);
        em.flush();
        studiiLiteratura.setMedicament(medicament);
        studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);
        Long medicamentId = medicament.getId();
        // Get all the studiiLiteraturaList where medicament equals to medicamentId
        defaultStudiiLiteraturaShouldBeFound("medicamentId.equals=" + medicamentId);

        // Get all the studiiLiteraturaList where medicament equals to (medicamentId + 1)
        defaultStudiiLiteraturaShouldNotBeFound("medicamentId.equals=" + (medicamentId + 1));
    }

    private void defaultStudiiLiteraturaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudiiLiteraturaShouldBeFound(shouldBeFound);
        defaultStudiiLiteraturaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudiiLiteraturaShouldBeFound(String filter) throws Exception {
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studiiLiteratura.getId().intValue())))
            .andExpect(jsonPath("$.[*].titlu").value(hasItem(DEFAULT_TITLU)))
            .andExpect(jsonPath("$.[*].autori").value(hasItem(DEFAULT_AUTORI)))
            .andExpect(jsonPath("$.[*].anul").value(hasItem(DEFAULT_ANUL)))
            .andExpect(jsonPath("$.[*].tipStudiu").value(hasItem(DEFAULT_TIP_STUDIU)))
            .andExpect(jsonPath("$.[*].substanta").value(hasItem(DEFAULT_SUBSTANTA)))
            .andExpect(jsonPath("$.[*].concluzie").value(hasItem(DEFAULT_CONCLUZIE)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));

        // Check, that the count call also returns 1
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudiiLiteraturaShouldNotBeFound(String filter) throws Exception {
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudiiLiteraturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudiiLiteratura() throws Exception {
        // Get the studiiLiteratura
        restStudiiLiteraturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudiiLiteratura() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studiiLiteratura
        StudiiLiteratura updatedStudiiLiteratura = studiiLiteraturaRepository.findById(studiiLiteratura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudiiLiteratura are not directly saved in db
        em.detach(updatedStudiiLiteratura);
        updatedStudiiLiteratura
            .titlu(UPDATED_TITLU)
            .autori(UPDATED_AUTORI)
            .anul(UPDATED_ANUL)
            .tipStudiu(UPDATED_TIP_STUDIU)
            .substanta(UPDATED_SUBSTANTA)
            .concluzie(UPDATED_CONCLUZIE)
            .link(UPDATED_LINK);
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(updatedStudiiLiteratura);

        restStudiiLiteraturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studiiLiteraturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studiiLiteraturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudiiLiteraturaToMatchAllProperties(updatedStudiiLiteratura);
    }

    @Test
    @Transactional
    void putNonExistingStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studiiLiteraturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studiiLiteraturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studiiLiteraturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studiiLiteraturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudiiLiteraturaWithPatch() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studiiLiteratura using partial update
        StudiiLiteratura partialUpdatedStudiiLiteratura = new StudiiLiteratura();
        partialUpdatedStudiiLiteratura.setId(studiiLiteratura.getId());

        partialUpdatedStudiiLiteratura.autori(UPDATED_AUTORI).tipStudiu(UPDATED_TIP_STUDIU).concluzie(UPDATED_CONCLUZIE).link(UPDATED_LINK);

        restStudiiLiteraturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudiiLiteratura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudiiLiteratura))
            )
            .andExpect(status().isOk());

        // Validate the StudiiLiteratura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudiiLiteraturaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudiiLiteratura, studiiLiteratura),
            getPersistedStudiiLiteratura(studiiLiteratura)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudiiLiteraturaWithPatch() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studiiLiteratura using partial update
        StudiiLiteratura partialUpdatedStudiiLiteratura = new StudiiLiteratura();
        partialUpdatedStudiiLiteratura.setId(studiiLiteratura.getId());

        partialUpdatedStudiiLiteratura
            .titlu(UPDATED_TITLU)
            .autori(UPDATED_AUTORI)
            .anul(UPDATED_ANUL)
            .tipStudiu(UPDATED_TIP_STUDIU)
            .substanta(UPDATED_SUBSTANTA)
            .concluzie(UPDATED_CONCLUZIE)
            .link(UPDATED_LINK);

        restStudiiLiteraturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudiiLiteratura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudiiLiteratura))
            )
            .andExpect(status().isOk());

        // Validate the StudiiLiteratura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudiiLiteraturaUpdatableFieldsEquals(
            partialUpdatedStudiiLiteratura,
            getPersistedStudiiLiteratura(partialUpdatedStudiiLiteratura)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studiiLiteraturaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studiiLiteraturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studiiLiteraturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudiiLiteratura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studiiLiteratura.setId(longCount.incrementAndGet());

        // Create the StudiiLiteratura
        StudiiLiteraturaDTO studiiLiteraturaDTO = studiiLiteraturaMapper.toDto(studiiLiteratura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudiiLiteraturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studiiLiteraturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudiiLiteratura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudiiLiteratura() throws Exception {
        // Initialize the database
        insertedStudiiLiteratura = studiiLiteraturaRepository.saveAndFlush(studiiLiteratura);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studiiLiteratura
        restStudiiLiteraturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, studiiLiteratura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studiiLiteraturaRepository.count();
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

    protected StudiiLiteratura getPersistedStudiiLiteratura(StudiiLiteratura studiiLiteratura) {
        return studiiLiteraturaRepository.findById(studiiLiteratura.getId()).orElseThrow();
    }

    protected void assertPersistedStudiiLiteraturaToMatchAllProperties(StudiiLiteratura expectedStudiiLiteratura) {
        assertStudiiLiteraturaAllPropertiesEquals(expectedStudiiLiteratura, getPersistedStudiiLiteratura(expectedStudiiLiteratura));
    }

    protected void assertPersistedStudiiLiteraturaToMatchUpdatableProperties(StudiiLiteratura expectedStudiiLiteratura) {
        assertStudiiLiteraturaAllUpdatablePropertiesEquals(
            expectedStudiiLiteratura,
            getPersistedStudiiLiteratura(expectedStudiiLiteratura)
        );
    }
}
