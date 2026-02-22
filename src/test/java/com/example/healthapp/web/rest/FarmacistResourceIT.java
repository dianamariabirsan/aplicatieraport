package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.FarmacistAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Farmacist;
import com.example.healthapp.repository.FarmacistRepository;
import com.example.healthapp.service.dto.FarmacistDTO;
import com.example.healthapp.service.mapper.FarmacistMapper;
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
 * Integration tests for the {@link FarmacistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmacistResourceIT {

    private static final String DEFAULT_NUME = "AAAAAAAAAA";
    private static final String UPDATED_NUME = "BBBBBBBBBB";

    private static final String DEFAULT_PRENUME = "AAAAAAAAAA";
    private static final String UPDATED_PRENUME = "BBBBBBBBBB";

    private static final String DEFAULT_FARMACIE = "AAAAAAAAAA";
    private static final String UPDATED_FARMACIE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/farmacists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FarmacistRepository farmacistRepository;

    @Autowired
    private FarmacistMapper farmacistMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmacistMockMvc;

    private Farmacist farmacist;

    private Farmacist insertedFarmacist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmacist createEntity() {
        return new Farmacist()
            .nume(DEFAULT_NUME)
            .prenume(DEFAULT_PRENUME)
            .farmacie(DEFAULT_FARMACIE)
            .email(DEFAULT_EMAIL)
            .telefon(DEFAULT_TELEFON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmacist createUpdatedEntity() {
        return new Farmacist()
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .farmacie(UPDATED_FARMACIE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);
    }

    @BeforeEach
    void initTest() {
        farmacist = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFarmacist != null) {
            farmacistRepository.delete(insertedFarmacist);
            insertedFarmacist = null;
        }
    }

    @Test
    @Transactional
    void createFarmacist() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);
        var returnedFarmacistDTO = om.readValue(
            restFarmacistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FarmacistDTO.class
        );

        // Validate the Farmacist in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFarmacist = farmacistMapper.toEntity(returnedFarmacistDTO);
        assertFarmacistUpdatableFieldsEquals(returnedFarmacist, getPersistedFarmacist(returnedFarmacist));

        insertedFarmacist = returnedFarmacist;
    }

    @Test
    @Transactional
    void createFarmacistWithExistingId() throws Exception {
        // Create the Farmacist with an existing ID
        farmacist.setId(1L);
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmacistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmacist.setNume(null);

        // Create the Farmacist, which fails.
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        restFarmacistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmacist.setPrenume(null);

        // Create the Farmacist, which fails.
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        restFarmacistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFarmacieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmacist.setFarmacie(null);

        // Create the Farmacist, which fails.
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        restFarmacistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        farmacist.setEmail(null);

        // Create the Farmacist, which fails.
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        restFarmacistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFarmacists() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmacist.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].farmacie").value(hasItem(DEFAULT_FARMACIE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)));
    }

    @Test
    @Transactional
    void getFarmacist() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get the farmacist
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL_ID, farmacist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmacist.getId().intValue()))
            .andExpect(jsonPath("$.nume").value(DEFAULT_NUME))
            .andExpect(jsonPath("$.prenume").value(DEFAULT_PRENUME))
            .andExpect(jsonPath("$.farmacie").value(DEFAULT_FARMACIE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON));
    }

    @Test
    @Transactional
    void getFarmacistsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        Long id = farmacist.getId();

        defaultFarmacistFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFarmacistFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFarmacistFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmacistsByNumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where nume equals to
        defaultFarmacistFiltering("nume.equals=" + DEFAULT_NUME, "nume.equals=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByNumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where nume in
        defaultFarmacistFiltering("nume.in=" + DEFAULT_NUME + "," + UPDATED_NUME, "nume.in=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByNumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where nume is not null
        defaultFarmacistFiltering("nume.specified=true", "nume.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmacistsByNumeContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where nume contains
        defaultFarmacistFiltering("nume.contains=" + DEFAULT_NUME, "nume.contains=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByNumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where nume does not contain
        defaultFarmacistFiltering("nume.doesNotContain=" + UPDATED_NUME, "nume.doesNotContain=" + DEFAULT_NUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByPrenumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where prenume equals to
        defaultFarmacistFiltering("prenume.equals=" + DEFAULT_PRENUME, "prenume.equals=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByPrenumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where prenume in
        defaultFarmacistFiltering("prenume.in=" + DEFAULT_PRENUME + "," + UPDATED_PRENUME, "prenume.in=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByPrenumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where prenume is not null
        defaultFarmacistFiltering("prenume.specified=true", "prenume.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmacistsByPrenumeContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where prenume contains
        defaultFarmacistFiltering("prenume.contains=" + DEFAULT_PRENUME, "prenume.contains=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByPrenumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where prenume does not contain
        defaultFarmacistFiltering("prenume.doesNotContain=" + UPDATED_PRENUME, "prenume.doesNotContain=" + DEFAULT_PRENUME);
    }

    @Test
    @Transactional
    void getAllFarmacistsByFarmacieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where farmacie equals to
        defaultFarmacistFiltering("farmacie.equals=" + DEFAULT_FARMACIE, "farmacie.equals=" + UPDATED_FARMACIE);
    }

    @Test
    @Transactional
    void getAllFarmacistsByFarmacieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where farmacie in
        defaultFarmacistFiltering("farmacie.in=" + DEFAULT_FARMACIE + "," + UPDATED_FARMACIE, "farmacie.in=" + UPDATED_FARMACIE);
    }

    @Test
    @Transactional
    void getAllFarmacistsByFarmacieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where farmacie is not null
        defaultFarmacistFiltering("farmacie.specified=true", "farmacie.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmacistsByFarmacieContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where farmacie contains
        defaultFarmacistFiltering("farmacie.contains=" + DEFAULT_FARMACIE, "farmacie.contains=" + UPDATED_FARMACIE);
    }

    @Test
    @Transactional
    void getAllFarmacistsByFarmacieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where farmacie does not contain
        defaultFarmacistFiltering("farmacie.doesNotContain=" + UPDATED_FARMACIE, "farmacie.doesNotContain=" + DEFAULT_FARMACIE);
    }

    @Test
    @Transactional
    void getAllFarmacistsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where email equals to
        defaultFarmacistFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFarmacistsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where email in
        defaultFarmacistFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFarmacistsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where email is not null
        defaultFarmacistFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmacistsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where email contains
        defaultFarmacistFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllFarmacistsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where email does not contain
        defaultFarmacistFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllFarmacistsByTelefonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where telefon equals to
        defaultFarmacistFiltering("telefon.equals=" + DEFAULT_TELEFON, "telefon.equals=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllFarmacistsByTelefonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where telefon in
        defaultFarmacistFiltering("telefon.in=" + DEFAULT_TELEFON + "," + UPDATED_TELEFON, "telefon.in=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllFarmacistsByTelefonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where telefon is not null
        defaultFarmacistFiltering("telefon.specified=true", "telefon.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmacistsByTelefonContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where telefon contains
        defaultFarmacistFiltering("telefon.contains=" + DEFAULT_TELEFON, "telefon.contains=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllFarmacistsByTelefonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        // Get all the farmacistList where telefon does not contain
        defaultFarmacistFiltering("telefon.doesNotContain=" + UPDATED_TELEFON, "telefon.doesNotContain=" + DEFAULT_TELEFON);
    }

    private void defaultFarmacistFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFarmacistShouldBeFound(shouldBeFound);
        defaultFarmacistShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmacistShouldBeFound(String filter) throws Exception {
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmacist.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].farmacie").value(hasItem(DEFAULT_FARMACIE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)));

        // Check, that the count call also returns 1
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmacistShouldNotBeFound(String filter) throws Exception {
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmacistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmacist() throws Exception {
        // Get the farmacist
        restFarmacistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFarmacist() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmacist
        Farmacist updatedFarmacist = farmacistRepository.findById(farmacist.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFarmacist are not directly saved in db
        em.detach(updatedFarmacist);
        updatedFarmacist
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .farmacie(UPDATED_FARMACIE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(updatedFarmacist);

        restFarmacistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmacistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmacistDTO))
            )
            .andExpect(status().isOk());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFarmacistToMatchAllProperties(updatedFarmacist);
    }

    @Test
    @Transactional
    void putNonExistingFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmacistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmacistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(farmacistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmacistWithPatch() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmacist using partial update
        Farmacist partialUpdatedFarmacist = new Farmacist();
        partialUpdatedFarmacist.setId(farmacist.getId());

        partialUpdatedFarmacist.farmacie(UPDATED_FARMACIE).telefon(UPDATED_TELEFON);

        restFarmacistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmacist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmacist))
            )
            .andExpect(status().isOk());

        // Validate the Farmacist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmacistUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFarmacist, farmacist),
            getPersistedFarmacist(farmacist)
        );
    }

    @Test
    @Transactional
    void fullUpdateFarmacistWithPatch() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the farmacist using partial update
        Farmacist partialUpdatedFarmacist = new Farmacist();
        partialUpdatedFarmacist.setId(farmacist.getId());

        partialUpdatedFarmacist
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .farmacie(UPDATED_FARMACIE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON);

        restFarmacistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmacist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFarmacist))
            )
            .andExpect(status().isOk());

        // Validate the Farmacist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFarmacistUpdatableFieldsEquals(partialUpdatedFarmacist, getPersistedFarmacist(partialUpdatedFarmacist));
    }

    @Test
    @Transactional
    void patchNonExistingFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmacistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmacistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(farmacistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmacist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        farmacist.setId(longCount.incrementAndGet());

        // Create the Farmacist
        FarmacistDTO farmacistDTO = farmacistMapper.toDto(farmacist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmacistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(farmacistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmacist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmacist() throws Exception {
        // Initialize the database
        insertedFarmacist = farmacistRepository.saveAndFlush(farmacist);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the farmacist
        restFarmacistMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmacist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return farmacistRepository.count();
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

    protected Farmacist getPersistedFarmacist(Farmacist farmacist) {
        return farmacistRepository.findById(farmacist.getId()).orElseThrow();
    }

    protected void assertPersistedFarmacistToMatchAllProperties(Farmacist expectedFarmacist) {
        assertFarmacistAllPropertiesEquals(expectedFarmacist, getPersistedFarmacist(expectedFarmacist));
    }

    protected void assertPersistedFarmacistToMatchUpdatableProperties(Farmacist expectedFarmacist) {
        assertFarmacistAllUpdatablePropertiesEquals(expectedFarmacist, getPersistedFarmacist(expectedFarmacist));
    }
}
