package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.RaportAnaliticAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.domain.RaportAnalitic;
import com.example.healthapp.repository.RaportAnaliticRepository;
import com.example.healthapp.service.RaportAnaliticService;
import com.example.healthapp.service.dto.RaportAnaliticDTO;
import com.example.healthapp.service.mapper.RaportAnaliticMapper;
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
 * Integration tests for the {@link RaportAnaliticResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RaportAnaliticResourceIT {

    private static final Instant DEFAULT_PERIOADA_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOADA_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PERIOADA_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOADA_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_EFICIENTA_MEDIE = 1D;
    private static final Double UPDATED_EFICIENTA_MEDIE = 2D;
    private static final Double SMALLER_EFICIENTA_MEDIE = 1D - 1D;

    private static final Double DEFAULT_RATA_REACTII_ADVERSE = 1D;
    private static final Double UPDATED_RATA_REACTII_ADVERSE = 2D;
    private static final Double SMALLER_RATA_REACTII_ADVERSE = 1D - 1D;

    private static final String DEFAULT_OBSERVATII = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATII = "BBBBBBBBBB";

    private static final String DEFAULT_CONCLUZII = "AAAAAAAAAA";
    private static final String UPDATED_CONCLUZII = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/raport-analitics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RaportAnaliticRepository raportAnaliticRepository;

    @Mock
    private RaportAnaliticRepository raportAnaliticRepositoryMock;

    @Autowired
    private RaportAnaliticMapper raportAnaliticMapper;

    @Mock
    private RaportAnaliticService raportAnaliticServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaportAnaliticMockMvc;

    private RaportAnalitic raportAnalitic;

    private RaportAnalitic insertedRaportAnalitic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaportAnalitic createEntity() {
        return new RaportAnalitic()
            .perioadaStart(DEFAULT_PERIOADA_START)
            .perioadaEnd(DEFAULT_PERIOADA_END)
            .eficientaMedie(DEFAULT_EFICIENTA_MEDIE)
            .rataReactiiAdverse(DEFAULT_RATA_REACTII_ADVERSE)
            .observatii(DEFAULT_OBSERVATII)
            .concluzii(DEFAULT_CONCLUZII);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaportAnalitic createUpdatedEntity() {
        return new RaportAnalitic()
            .perioadaStart(UPDATED_PERIOADA_START)
            .perioadaEnd(UPDATED_PERIOADA_END)
            .eficientaMedie(UPDATED_EFICIENTA_MEDIE)
            .rataReactiiAdverse(UPDATED_RATA_REACTII_ADVERSE)
            .observatii(UPDATED_OBSERVATII)
            .concluzii(UPDATED_CONCLUZII);
    }

    @BeforeEach
    void initTest() {
        raportAnalitic = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRaportAnalitic != null) {
            raportAnaliticRepository.delete(insertedRaportAnalitic);
            insertedRaportAnalitic = null;
        }
    }

    @Test
    @Transactional
    void createRaportAnalitic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);
        var returnedRaportAnaliticDTO = om.readValue(
            restRaportAnaliticMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(raportAnaliticDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RaportAnaliticDTO.class
        );

        // Validate the RaportAnalitic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRaportAnalitic = raportAnaliticMapper.toEntity(returnedRaportAnaliticDTO);
        assertRaportAnaliticUpdatableFieldsEquals(returnedRaportAnalitic, getPersistedRaportAnalitic(returnedRaportAnalitic));

        insertedRaportAnalitic = returnedRaportAnalitic;
    }

    @Test
    @Transactional
    void createRaportAnaliticWithExistingId() throws Exception {
        // Create the RaportAnalitic with an existing ID
        raportAnalitic.setId(1L);
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaportAnaliticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(raportAnaliticDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRaportAnalitics() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raportAnalitic.getId().intValue())))
            .andExpect(jsonPath("$.[*].perioadaStart").value(hasItem(DEFAULT_PERIOADA_START.toString())))
            .andExpect(jsonPath("$.[*].perioadaEnd").value(hasItem(DEFAULT_PERIOADA_END.toString())))
            .andExpect(jsonPath("$.[*].eficientaMedie").value(hasItem(DEFAULT_EFICIENTA_MEDIE)))
            .andExpect(jsonPath("$.[*].rataReactiiAdverse").value(hasItem(DEFAULT_RATA_REACTII_ADVERSE)))
            .andExpect(jsonPath("$.[*].observatii").value(hasItem(DEFAULT_OBSERVATII)))
            .andExpect(jsonPath("$.[*].concluzii").value(hasItem(DEFAULT_CONCLUZII)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRaportAnaliticsWithEagerRelationshipsIsEnabled() throws Exception {
        when(raportAnaliticServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRaportAnaliticMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(raportAnaliticServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRaportAnaliticsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(raportAnaliticServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRaportAnaliticMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(raportAnaliticRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRaportAnalitic() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get the raportAnalitic
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL_ID, raportAnalitic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raportAnalitic.getId().intValue()))
            .andExpect(jsonPath("$.perioadaStart").value(DEFAULT_PERIOADA_START.toString()))
            .andExpect(jsonPath("$.perioadaEnd").value(DEFAULT_PERIOADA_END.toString()))
            .andExpect(jsonPath("$.eficientaMedie").value(DEFAULT_EFICIENTA_MEDIE))
            .andExpect(jsonPath("$.rataReactiiAdverse").value(DEFAULT_RATA_REACTII_ADVERSE))
            .andExpect(jsonPath("$.observatii").value(DEFAULT_OBSERVATII))
            .andExpect(jsonPath("$.concluzii").value(DEFAULT_CONCLUZII));
    }

    @Test
    @Transactional
    void getRaportAnaliticsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        Long id = raportAnalitic.getId();

        defaultRaportAnaliticFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRaportAnaliticFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRaportAnaliticFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaStart equals to
        defaultRaportAnaliticFiltering("perioadaStart.equals=" + DEFAULT_PERIOADA_START, "perioadaStart.equals=" + UPDATED_PERIOADA_START);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaStart in
        defaultRaportAnaliticFiltering(
            "perioadaStart.in=" + DEFAULT_PERIOADA_START + "," + UPDATED_PERIOADA_START,
            "perioadaStart.in=" + UPDATED_PERIOADA_START
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaStart is not null
        defaultRaportAnaliticFiltering("perioadaStart.specified=true", "perioadaStart.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaEndIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaEnd equals to
        defaultRaportAnaliticFiltering("perioadaEnd.equals=" + DEFAULT_PERIOADA_END, "perioadaEnd.equals=" + UPDATED_PERIOADA_END);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaEndIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaEnd in
        defaultRaportAnaliticFiltering(
            "perioadaEnd.in=" + DEFAULT_PERIOADA_END + "," + UPDATED_PERIOADA_END,
            "perioadaEnd.in=" + UPDATED_PERIOADA_END
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByPerioadaEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where perioadaEnd is not null
        defaultRaportAnaliticFiltering("perioadaEnd.specified=true", "perioadaEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie equals to
        defaultRaportAnaliticFiltering(
            "eficientaMedie.equals=" + DEFAULT_EFICIENTA_MEDIE,
            "eficientaMedie.equals=" + UPDATED_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie in
        defaultRaportAnaliticFiltering(
            "eficientaMedie.in=" + DEFAULT_EFICIENTA_MEDIE + "," + UPDATED_EFICIENTA_MEDIE,
            "eficientaMedie.in=" + UPDATED_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie is not null
        defaultRaportAnaliticFiltering("eficientaMedie.specified=true", "eficientaMedie.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie is greater than or equal to
        defaultRaportAnaliticFiltering(
            "eficientaMedie.greaterThanOrEqual=" + DEFAULT_EFICIENTA_MEDIE,
            "eficientaMedie.greaterThanOrEqual=" + UPDATED_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie is less than or equal to
        defaultRaportAnaliticFiltering(
            "eficientaMedie.lessThanOrEqual=" + DEFAULT_EFICIENTA_MEDIE,
            "eficientaMedie.lessThanOrEqual=" + SMALLER_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie is less than
        defaultRaportAnaliticFiltering(
            "eficientaMedie.lessThan=" + UPDATED_EFICIENTA_MEDIE,
            "eficientaMedie.lessThan=" + DEFAULT_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByEficientaMedieIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where eficientaMedie is greater than
        defaultRaportAnaliticFiltering(
            "eficientaMedie.greaterThan=" + SMALLER_EFICIENTA_MEDIE,
            "eficientaMedie.greaterThan=" + DEFAULT_EFICIENTA_MEDIE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse equals to
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.equals=" + DEFAULT_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.equals=" + UPDATED_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse in
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.in=" + DEFAULT_RATA_REACTII_ADVERSE + "," + UPDATED_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.in=" + UPDATED_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse is not null
        defaultRaportAnaliticFiltering("rataReactiiAdverse.specified=true", "rataReactiiAdverse.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse is greater than or equal to
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.greaterThanOrEqual=" + DEFAULT_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.greaterThanOrEqual=" + UPDATED_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse is less than or equal to
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.lessThanOrEqual=" + DEFAULT_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.lessThanOrEqual=" + SMALLER_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse is less than
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.lessThan=" + UPDATED_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.lessThan=" + DEFAULT_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByRataReactiiAdverseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where rataReactiiAdverse is greater than
        defaultRaportAnaliticFiltering(
            "rataReactiiAdverse.greaterThan=" + SMALLER_RATA_REACTII_ADVERSE,
            "rataReactiiAdverse.greaterThan=" + DEFAULT_RATA_REACTII_ADVERSE
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByObservatiiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where observatii equals to
        defaultRaportAnaliticFiltering("observatii.equals=" + DEFAULT_OBSERVATII, "observatii.equals=" + UPDATED_OBSERVATII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByObservatiiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where observatii in
        defaultRaportAnaliticFiltering(
            "observatii.in=" + DEFAULT_OBSERVATII + "," + UPDATED_OBSERVATII,
            "observatii.in=" + UPDATED_OBSERVATII
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByObservatiiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where observatii is not null
        defaultRaportAnaliticFiltering("observatii.specified=true", "observatii.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByObservatiiContainsSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where observatii contains
        defaultRaportAnaliticFiltering("observatii.contains=" + DEFAULT_OBSERVATII, "observatii.contains=" + UPDATED_OBSERVATII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByObservatiiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where observatii does not contain
        defaultRaportAnaliticFiltering(
            "observatii.doesNotContain=" + UPDATED_OBSERVATII,
            "observatii.doesNotContain=" + DEFAULT_OBSERVATII
        );
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByConcluziiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where concluzii equals to
        defaultRaportAnaliticFiltering("concluzii.equals=" + DEFAULT_CONCLUZII, "concluzii.equals=" + UPDATED_CONCLUZII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByConcluziiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where concluzii in
        defaultRaportAnaliticFiltering("concluzii.in=" + DEFAULT_CONCLUZII + "," + UPDATED_CONCLUZII, "concluzii.in=" + UPDATED_CONCLUZII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByConcluziiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where concluzii is not null
        defaultRaportAnaliticFiltering("concluzii.specified=true", "concluzii.specified=false");
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByConcluziiContainsSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where concluzii contains
        defaultRaportAnaliticFiltering("concluzii.contains=" + DEFAULT_CONCLUZII, "concluzii.contains=" + UPDATED_CONCLUZII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByConcluziiNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        // Get all the raportAnaliticList where concluzii does not contain
        defaultRaportAnaliticFiltering("concluzii.doesNotContain=" + UPDATED_CONCLUZII, "concluzii.doesNotContain=" + DEFAULT_CONCLUZII);
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByMedicamentIsEqualToSomething() throws Exception {
        Medicament medicament;
        if (TestUtil.findAll(em, Medicament.class).isEmpty()) {
            raportAnaliticRepository.saveAndFlush(raportAnalitic);
            medicament = MedicamentResourceIT.createEntity();
        } else {
            medicament = TestUtil.findAll(em, Medicament.class).get(0);
        }
        em.persist(medicament);
        em.flush();
        raportAnalitic.setMedicament(medicament);
        raportAnaliticRepository.saveAndFlush(raportAnalitic);
        Long medicamentId = medicament.getId();
        // Get all the raportAnaliticList where medicament equals to medicamentId
        defaultRaportAnaliticShouldBeFound("medicamentId.equals=" + medicamentId);

        // Get all the raportAnaliticList where medicament equals to (medicamentId + 1)
        defaultRaportAnaliticShouldNotBeFound("medicamentId.equals=" + (medicamentId + 1));
    }

    @Test
    @Transactional
    void getAllRaportAnaliticsByMedicIsEqualToSomething() throws Exception {
        Medic medic;
        if (TestUtil.findAll(em, Medic.class).isEmpty()) {
            raportAnaliticRepository.saveAndFlush(raportAnalitic);
            medic = MedicResourceIT.createEntity();
        } else {
            medic = TestUtil.findAll(em, Medic.class).get(0);
        }
        em.persist(medic);
        em.flush();
        raportAnalitic.setMedic(medic);
        raportAnaliticRepository.saveAndFlush(raportAnalitic);
        Long medicId = medic.getId();
        // Get all the raportAnaliticList where medic equals to medicId
        defaultRaportAnaliticShouldBeFound("medicId.equals=" + medicId);

        // Get all the raportAnaliticList where medic equals to (medicId + 1)
        defaultRaportAnaliticShouldNotBeFound("medicId.equals=" + (medicId + 1));
    }

    private void defaultRaportAnaliticFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRaportAnaliticShouldBeFound(shouldBeFound);
        defaultRaportAnaliticShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRaportAnaliticShouldBeFound(String filter) throws Exception {
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raportAnalitic.getId().intValue())))
            .andExpect(jsonPath("$.[*].perioadaStart").value(hasItem(DEFAULT_PERIOADA_START.toString())))
            .andExpect(jsonPath("$.[*].perioadaEnd").value(hasItem(DEFAULT_PERIOADA_END.toString())))
            .andExpect(jsonPath("$.[*].eficientaMedie").value(hasItem(DEFAULT_EFICIENTA_MEDIE)))
            .andExpect(jsonPath("$.[*].rataReactiiAdverse").value(hasItem(DEFAULT_RATA_REACTII_ADVERSE)))
            .andExpect(jsonPath("$.[*].observatii").value(hasItem(DEFAULT_OBSERVATII)))
            .andExpect(jsonPath("$.[*].concluzii").value(hasItem(DEFAULT_CONCLUZII)));

        // Check, that the count call also returns 1
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRaportAnaliticShouldNotBeFound(String filter) throws Exception {
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRaportAnaliticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRaportAnalitic() throws Exception {
        // Get the raportAnalitic
        restRaportAnaliticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRaportAnalitic() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raportAnalitic
        RaportAnalitic updatedRaportAnalitic = raportAnaliticRepository.findById(raportAnalitic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRaportAnalitic are not directly saved in db
        em.detach(updatedRaportAnalitic);
        updatedRaportAnalitic
            .perioadaStart(UPDATED_PERIOADA_START)
            .perioadaEnd(UPDATED_PERIOADA_END)
            .eficientaMedie(UPDATED_EFICIENTA_MEDIE)
            .rataReactiiAdverse(UPDATED_RATA_REACTII_ADVERSE)
            .observatii(UPDATED_OBSERVATII)
            .concluzii(UPDATED_CONCLUZII);
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(updatedRaportAnalitic);

        restRaportAnaliticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raportAnaliticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(raportAnaliticDTO))
            )
            .andExpect(status().isOk());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRaportAnaliticToMatchAllProperties(updatedRaportAnalitic);
    }

    @Test
    @Transactional
    void putNonExistingRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raportAnaliticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(raportAnaliticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(raportAnaliticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(raportAnaliticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRaportAnaliticWithPatch() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raportAnalitic using partial update
        RaportAnalitic partialUpdatedRaportAnalitic = new RaportAnalitic();
        partialUpdatedRaportAnalitic.setId(raportAnalitic.getId());

        partialUpdatedRaportAnalitic.perioadaStart(UPDATED_PERIOADA_START);

        restRaportAnaliticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaportAnalitic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaportAnalitic))
            )
            .andExpect(status().isOk());

        // Validate the RaportAnalitic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRaportAnaliticUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRaportAnalitic, raportAnalitic),
            getPersistedRaportAnalitic(raportAnalitic)
        );
    }

    @Test
    @Transactional
    void fullUpdateRaportAnaliticWithPatch() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raportAnalitic using partial update
        RaportAnalitic partialUpdatedRaportAnalitic = new RaportAnalitic();
        partialUpdatedRaportAnalitic.setId(raportAnalitic.getId());

        partialUpdatedRaportAnalitic
            .perioadaStart(UPDATED_PERIOADA_START)
            .perioadaEnd(UPDATED_PERIOADA_END)
            .eficientaMedie(UPDATED_EFICIENTA_MEDIE)
            .rataReactiiAdverse(UPDATED_RATA_REACTII_ADVERSE)
            .observatii(UPDATED_OBSERVATII)
            .concluzii(UPDATED_CONCLUZII);

        restRaportAnaliticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaportAnalitic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaportAnalitic))
            )
            .andExpect(status().isOk());

        // Validate the RaportAnalitic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRaportAnaliticUpdatableFieldsEquals(partialUpdatedRaportAnalitic, getPersistedRaportAnalitic(partialUpdatedRaportAnalitic));
    }

    @Test
    @Transactional
    void patchNonExistingRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, raportAnaliticDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(raportAnaliticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(raportAnaliticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaportAnalitic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raportAnalitic.setId(longCount.incrementAndGet());

        // Create the RaportAnalitic
        RaportAnaliticDTO raportAnaliticDTO = raportAnaliticMapper.toDto(raportAnalitic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaportAnaliticMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(raportAnaliticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaportAnalitic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaportAnalitic() throws Exception {
        // Initialize the database
        insertedRaportAnalitic = raportAnaliticRepository.saveAndFlush(raportAnalitic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the raportAnalitic
        restRaportAnaliticMockMvc
            .perform(delete(ENTITY_API_URL_ID, raportAnalitic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return raportAnaliticRepository.count();
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

    protected RaportAnalitic getPersistedRaportAnalitic(RaportAnalitic raportAnalitic) {
        return raportAnaliticRepository.findById(raportAnalitic.getId()).orElseThrow();
    }

    protected void assertPersistedRaportAnaliticToMatchAllProperties(RaportAnalitic expectedRaportAnalitic) {
        assertRaportAnaliticAllPropertiesEquals(expectedRaportAnalitic, getPersistedRaportAnalitic(expectedRaportAnalitic));
    }

    protected void assertPersistedRaportAnaliticToMatchUpdatableProperties(RaportAnalitic expectedRaportAnalitic) {
        assertRaportAnaliticAllUpdatablePropertiesEquals(expectedRaportAnalitic, getPersistedRaportAnalitic(expectedRaportAnalitic));
    }
}
