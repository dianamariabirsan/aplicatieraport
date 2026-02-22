package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.MedicAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.Medic;
import com.example.healthapp.repository.MedicRepository;
import com.example.healthapp.service.dto.MedicDTO;
import com.example.healthapp.service.mapper.MedicMapper;
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
 * Integration tests for the {@link MedicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicResourceIT {

    private static final String DEFAULT_NUME = "AAAAAAAAAA";
    private static final String UPDATED_NUME = "BBBBBBBBBB";

    private static final String DEFAULT_PRENUME = "AAAAAAAAAA";
    private static final String UPDATED_PRENUME = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALIZARE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIZARE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String DEFAULT_CABINET = "AAAAAAAAAA";
    private static final String UPDATED_CABINET = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicRepository medicRepository;

    @Autowired
    private MedicMapper medicMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicMockMvc;

    private Medic medic;

    private Medic insertedMedic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medic createEntity() {
        return new Medic()
            .nume(DEFAULT_NUME)
            .prenume(DEFAULT_PRENUME)
            .specializare(DEFAULT_SPECIALIZARE)
            .email(DEFAULT_EMAIL)
            .telefon(DEFAULT_TELEFON)
            .cabinet(DEFAULT_CABINET);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medic createUpdatedEntity() {
        return new Medic()
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .specializare(UPDATED_SPECIALIZARE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .cabinet(UPDATED_CABINET);
    }

    @BeforeEach
    void initTest() {
        medic = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMedic != null) {
            medicRepository.delete(insertedMedic);
            insertedMedic = null;
        }
    }

    @Test
    @Transactional
    void createMedic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);
        var returnedMedicDTO = om.readValue(
            restMedicMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicDTO.class
        );

        // Validate the Medic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedic = medicMapper.toEntity(returnedMedicDTO);
        assertMedicUpdatableFieldsEquals(returnedMedic, getPersistedMedic(returnedMedic));

        insertedMedic = returnedMedic;
    }

    @Test
    @Transactional
    void createMedicWithExistingId() throws Exception {
        // Create the Medic with an existing ID
        medic.setId(1L);
        MedicDTO medicDTO = medicMapper.toDto(medic);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medic.setNume(null);

        // Create the Medic, which fails.
        MedicDTO medicDTO = medicMapper.toDto(medic);

        restMedicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenumeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medic.setPrenume(null);

        // Create the Medic, which fails.
        MedicDTO medicDTO = medicMapper.toDto(medic);

        restMedicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecializareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medic.setSpecializare(null);

        // Create the Medic, which fails.
        MedicDTO medicDTO = medicMapper.toDto(medic);

        restMedicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medic.setEmail(null);

        // Create the Medic, which fails.
        MedicDTO medicDTO = medicMapper.toDto(medic);

        restMedicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedics() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList
        restMedicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medic.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].specializare").value(hasItem(DEFAULT_SPECIALIZARE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)))
            .andExpect(jsonPath("$.[*].cabinet").value(hasItem(DEFAULT_CABINET)));
    }

    @Test
    @Transactional
    void getMedic() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get the medic
        restMedicMockMvc
            .perform(get(ENTITY_API_URL_ID, medic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medic.getId().intValue()))
            .andExpect(jsonPath("$.nume").value(DEFAULT_NUME))
            .andExpect(jsonPath("$.prenume").value(DEFAULT_PRENUME))
            .andExpect(jsonPath("$.specializare").value(DEFAULT_SPECIALIZARE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON))
            .andExpect(jsonPath("$.cabinet").value(DEFAULT_CABINET));
    }

    @Test
    @Transactional
    void getMedicsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        Long id = medic.getId();

        defaultMedicFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMedicFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMedicFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicsByNumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where nume equals to
        defaultMedicFiltering("nume.equals=" + DEFAULT_NUME, "nume.equals=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllMedicsByNumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where nume in
        defaultMedicFiltering("nume.in=" + DEFAULT_NUME + "," + UPDATED_NUME, "nume.in=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllMedicsByNumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where nume is not null
        defaultMedicFiltering("nume.specified=true", "nume.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsByNumeContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where nume contains
        defaultMedicFiltering("nume.contains=" + DEFAULT_NUME, "nume.contains=" + UPDATED_NUME);
    }

    @Test
    @Transactional
    void getAllMedicsByNumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where nume does not contain
        defaultMedicFiltering("nume.doesNotContain=" + UPDATED_NUME, "nume.doesNotContain=" + DEFAULT_NUME);
    }

    @Test
    @Transactional
    void getAllMedicsByPrenumeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where prenume equals to
        defaultMedicFiltering("prenume.equals=" + DEFAULT_PRENUME, "prenume.equals=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllMedicsByPrenumeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where prenume in
        defaultMedicFiltering("prenume.in=" + DEFAULT_PRENUME + "," + UPDATED_PRENUME, "prenume.in=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllMedicsByPrenumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where prenume is not null
        defaultMedicFiltering("prenume.specified=true", "prenume.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsByPrenumeContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where prenume contains
        defaultMedicFiltering("prenume.contains=" + DEFAULT_PRENUME, "prenume.contains=" + UPDATED_PRENUME);
    }

    @Test
    @Transactional
    void getAllMedicsByPrenumeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where prenume does not contain
        defaultMedicFiltering("prenume.doesNotContain=" + UPDATED_PRENUME, "prenume.doesNotContain=" + DEFAULT_PRENUME);
    }

    @Test
    @Transactional
    void getAllMedicsBySpecializareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where specializare equals to
        defaultMedicFiltering("specializare.equals=" + DEFAULT_SPECIALIZARE, "specializare.equals=" + UPDATED_SPECIALIZARE);
    }

    @Test
    @Transactional
    void getAllMedicsBySpecializareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where specializare in
        defaultMedicFiltering(
            "specializare.in=" + DEFAULT_SPECIALIZARE + "," + UPDATED_SPECIALIZARE,
            "specializare.in=" + UPDATED_SPECIALIZARE
        );
    }

    @Test
    @Transactional
    void getAllMedicsBySpecializareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where specializare is not null
        defaultMedicFiltering("specializare.specified=true", "specializare.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsBySpecializareContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where specializare contains
        defaultMedicFiltering("specializare.contains=" + DEFAULT_SPECIALIZARE, "specializare.contains=" + UPDATED_SPECIALIZARE);
    }

    @Test
    @Transactional
    void getAllMedicsBySpecializareNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where specializare does not contain
        defaultMedicFiltering("specializare.doesNotContain=" + UPDATED_SPECIALIZARE, "specializare.doesNotContain=" + DEFAULT_SPECIALIZARE);
    }

    @Test
    @Transactional
    void getAllMedicsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where email equals to
        defaultMedicFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMedicsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where email in
        defaultMedicFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMedicsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where email is not null
        defaultMedicFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where email contains
        defaultMedicFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllMedicsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where email does not contain
        defaultMedicFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllMedicsByTelefonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where telefon equals to
        defaultMedicFiltering("telefon.equals=" + DEFAULT_TELEFON, "telefon.equals=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllMedicsByTelefonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where telefon in
        defaultMedicFiltering("telefon.in=" + DEFAULT_TELEFON + "," + UPDATED_TELEFON, "telefon.in=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllMedicsByTelefonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where telefon is not null
        defaultMedicFiltering("telefon.specified=true", "telefon.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsByTelefonContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where telefon contains
        defaultMedicFiltering("telefon.contains=" + DEFAULT_TELEFON, "telefon.contains=" + UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void getAllMedicsByTelefonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where telefon does not contain
        defaultMedicFiltering("telefon.doesNotContain=" + UPDATED_TELEFON, "telefon.doesNotContain=" + DEFAULT_TELEFON);
    }

    @Test
    @Transactional
    void getAllMedicsByCabinetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where cabinet equals to
        defaultMedicFiltering("cabinet.equals=" + DEFAULT_CABINET, "cabinet.equals=" + UPDATED_CABINET);
    }

    @Test
    @Transactional
    void getAllMedicsByCabinetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where cabinet in
        defaultMedicFiltering("cabinet.in=" + DEFAULT_CABINET + "," + UPDATED_CABINET, "cabinet.in=" + UPDATED_CABINET);
    }

    @Test
    @Transactional
    void getAllMedicsByCabinetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where cabinet is not null
        defaultMedicFiltering("cabinet.specified=true", "cabinet.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicsByCabinetContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where cabinet contains
        defaultMedicFiltering("cabinet.contains=" + DEFAULT_CABINET, "cabinet.contains=" + UPDATED_CABINET);
    }

    @Test
    @Transactional
    void getAllMedicsByCabinetNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        // Get all the medicList where cabinet does not contain
        defaultMedicFiltering("cabinet.doesNotContain=" + UPDATED_CABINET, "cabinet.doesNotContain=" + DEFAULT_CABINET);
    }

    private void defaultMedicFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMedicShouldBeFound(shouldBeFound);
        defaultMedicShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicShouldBeFound(String filter) throws Exception {
        restMedicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medic.getId().intValue())))
            .andExpect(jsonPath("$.[*].nume").value(hasItem(DEFAULT_NUME)))
            .andExpect(jsonPath("$.[*].prenume").value(hasItem(DEFAULT_PRENUME)))
            .andExpect(jsonPath("$.[*].specializare").value(hasItem(DEFAULT_SPECIALIZARE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)))
            .andExpect(jsonPath("$.[*].cabinet").value(hasItem(DEFAULT_CABINET)));

        // Check, that the count call also returns 1
        restMedicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicShouldNotBeFound(String filter) throws Exception {
        restMedicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedic() throws Exception {
        // Get the medic
        restMedicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedic() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medic
        Medic updatedMedic = medicRepository.findById(medic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedic are not directly saved in db
        em.detach(updatedMedic);
        updatedMedic
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .specializare(UPDATED_SPECIALIZARE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .cabinet(UPDATED_CABINET);
        MedicDTO medicDTO = medicMapper.toDto(updatedMedic);

        restMedicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicToMatchAllProperties(updatedMedic);
    }

    @Test
    @Transactional
    void putNonExistingMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicWithPatch() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medic using partial update
        Medic partialUpdatedMedic = new Medic();
        partialUpdatedMedic.setId(medic.getId());

        partialUpdatedMedic.nume(UPDATED_NUME).prenume(UPDATED_PRENUME).email(UPDATED_EMAIL).cabinet(UPDATED_CABINET);

        restMedicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedic))
            )
            .andExpect(status().isOk());

        // Validate the Medic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedic, medic), getPersistedMedic(medic));
    }

    @Test
    @Transactional
    void fullUpdateMedicWithPatch() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medic using partial update
        Medic partialUpdatedMedic = new Medic();
        partialUpdatedMedic.setId(medic.getId());

        partialUpdatedMedic
            .nume(UPDATED_NUME)
            .prenume(UPDATED_PRENUME)
            .specializare(UPDATED_SPECIALIZARE)
            .email(UPDATED_EMAIL)
            .telefon(UPDATED_TELEFON)
            .cabinet(UPDATED_CABINET);

        restMedicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedic))
            )
            .andExpect(status().isOk());

        // Validate the Medic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicUpdatableFieldsEquals(partialUpdatedMedic, getPersistedMedic(partialUpdatedMedic));
    }

    @Test
    @Transactional
    void patchNonExistingMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medic.setId(longCount.incrementAndGet());

        // Create the Medic
        MedicDTO medicDTO = medicMapper.toDto(medic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedic() throws Exception {
        // Initialize the database
        insertedMedic = medicRepository.saveAndFlush(medic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medic
        restMedicMockMvc
            .perform(delete(ENTITY_API_URL_ID, medic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicRepository.count();
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

    protected Medic getPersistedMedic(Medic medic) {
        return medicRepository.findById(medic.getId()).orElseThrow();
    }

    protected void assertPersistedMedicToMatchAllProperties(Medic expectedMedic) {
        assertMedicAllPropertiesEquals(expectedMedic, getPersistedMedic(expectedMedic));
    }

    protected void assertPersistedMedicToMatchUpdatableProperties(Medic expectedMedic) {
        assertMedicAllUpdatablePropertiesEquals(expectedMedic, getPersistedMedic(expectedMedic));
    }
}
