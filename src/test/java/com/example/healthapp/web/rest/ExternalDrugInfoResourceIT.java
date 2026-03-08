package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.ExternalDrugInfoAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.ExternalDrugInfo;
import com.example.healthapp.domain.Medicament;
import com.example.healthapp.repository.ExternalDrugInfoRepository;
import com.example.healthapp.repository.MedicamentRepository;
import com.example.healthapp.service.dto.ExternalDrugInfoDTO;
import com.example.healthapp.service.mapper.ExternalDrugInfoMapper;
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
 * Integration tests for the {@link ExternalDrugInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = "ROLE_ADMIN")
class ExternalDrugInfoResourceIT {

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_SUMMARY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/external-drug-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExternalDrugInfoRepository externalDrugInfoRepository;

    @Autowired
    private ExternalDrugInfoMapper externalDrugInfoMapper;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExternalDrugInfoMockMvc;

    private ExternalDrugInfo externalDrugInfo;

    private ExternalDrugInfo insertedExternalDrugInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalDrugInfo createEntity() {
        return new ExternalDrugInfo()
            .source(DEFAULT_SOURCE)
            .productSummary(DEFAULT_PRODUCT_SUMMARY)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .sourceUrl(DEFAULT_SOURCE_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExternalDrugInfo createUpdatedEntity() {
        return new ExternalDrugInfo()
            .source(UPDATED_SOURCE)
            .productSummary(UPDATED_PRODUCT_SUMMARY)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sourceUrl(UPDATED_SOURCE_URL);
    }

    @BeforeEach
    void initTest() {
        externalDrugInfo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExternalDrugInfo != null) {
            externalDrugInfoRepository.delete(insertedExternalDrugInfo);
            insertedExternalDrugInfo = null;
        }
    }

    @Test
    @Transactional
    void createExternalDrugInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);
        var returnedExternalDrugInfoDTO = om.readValue(
            restExternalDrugInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalDrugInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExternalDrugInfoDTO.class
        );

        // Validate the ExternalDrugInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExternalDrugInfo = externalDrugInfoMapper.toEntity(returnedExternalDrugInfoDTO);
        assertExternalDrugInfoUpdatableFieldsEquals(returnedExternalDrugInfo, getPersistedExternalDrugInfo(returnedExternalDrugInfo));

        insertedExternalDrugInfo = returnedExternalDrugInfo;
    }

    @Test
    @Transactional
    void createExternalDrugInfoShouldAutoCreateMedicamentFromProductSummary() throws Exception {
        long externalInfoCountBeforeCreate = getRepositoryCount();
        long medicamentCountBeforeCreate = medicamentRepository.count();

        externalDrugInfo.setProductSummary("{\"productName\":\"Med auto\",\"activeSubstance\":\"Sub auto\",\"indicatii\":[\"I1\"]}");
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        var returnedExternalDrugInfoDTO = om.readValue(
            restExternalDrugInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalDrugInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExternalDrugInfoDTO.class
        );

        assertIncrementedRepositoryCount(externalInfoCountBeforeCreate);
        assertThat(medicamentRepository.count()).isEqualTo(medicamentCountBeforeCreate + 1);

        ExternalDrugInfo savedInfo = getPersistedExternalDrugInfo(externalDrugInfoMapper.toEntity(returnedExternalDrugInfoDTO));
        Medicament linkedMedicament = medicamentRepository.findOneByInfoExternId(savedInfo.getId()).orElseThrow();
        assertThat(linkedMedicament.getDenumire()).isEqualTo("Med auto");
        assertThat(linkedMedicament.getSubstanta()).isEqualTo("Sub auto");
        assertThat(linkedMedicament.getIndicatii()).isEqualTo("I1");
    }

    @Test
    @Transactional
    void createExternalDrugInfoWithExistingId() throws Exception {
        // Create the ExternalDrugInfo with an existing ID
        externalDrugInfo.setId(1L);
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExternalDrugInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalDrugInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSourceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        externalDrugInfo.setSource(null);

        // Create the ExternalDrugInfo, which fails.
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        restExternalDrugInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalDrugInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfos() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalDrugInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].productSummary").value(hasItem(DEFAULT_PRODUCT_SUMMARY)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)));
    }

    @Test
    @Transactional
    void getExternalDrugInfo() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get the externalDrugInfo
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, externalDrugInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(externalDrugInfo.getId().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.productSummary").value(DEFAULT_PRODUCT_SUMMARY))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL));
    }

    @Test
    @Transactional
    void getExternalDrugInfosByIdFiltering() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        Long id = externalDrugInfo.getId();

        defaultExternalDrugInfoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExternalDrugInfoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExternalDrugInfoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where source equals to
        defaultExternalDrugInfoFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where source in
        defaultExternalDrugInfoFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where source is not null
        defaultExternalDrugInfoFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where source contains
        defaultExternalDrugInfoFiltering("source.contains=" + DEFAULT_SOURCE, "source.contains=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where source does not contain
        defaultExternalDrugInfoFiltering("source.doesNotContain=" + UPDATED_SOURCE, "source.doesNotContain=" + DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByProductSummaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where productSummary equals to
        defaultExternalDrugInfoFiltering(
            "productSummary.equals=" + DEFAULT_PRODUCT_SUMMARY,
            "productSummary.equals=" + UPDATED_PRODUCT_SUMMARY
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByProductSummaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where productSummary in
        defaultExternalDrugInfoFiltering(
            "productSummary.in=" + DEFAULT_PRODUCT_SUMMARY + "," + UPDATED_PRODUCT_SUMMARY,
            "productSummary.in=" + UPDATED_PRODUCT_SUMMARY
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByProductSummaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where productSummary is not null
        defaultExternalDrugInfoFiltering("productSummary.specified=true", "productSummary.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByProductSummaryContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where productSummary contains
        defaultExternalDrugInfoFiltering(
            "productSummary.contains=" + DEFAULT_PRODUCT_SUMMARY,
            "productSummary.contains=" + UPDATED_PRODUCT_SUMMARY
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByProductSummaryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where productSummary does not contain
        defaultExternalDrugInfoFiltering(
            "productSummary.doesNotContain=" + UPDATED_PRODUCT_SUMMARY,
            "productSummary.doesNotContain=" + DEFAULT_PRODUCT_SUMMARY
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where lastUpdated equals to
        defaultExternalDrugInfoFiltering("lastUpdated.equals=" + DEFAULT_LAST_UPDATED, "lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where lastUpdated in
        defaultExternalDrugInfoFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where lastUpdated is not null
        defaultExternalDrugInfoFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where sourceUrl equals to
        defaultExternalDrugInfoFiltering("sourceUrl.equals=" + DEFAULT_SOURCE_URL, "sourceUrl.equals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where sourceUrl in
        defaultExternalDrugInfoFiltering(
            "sourceUrl.in=" + DEFAULT_SOURCE_URL + "," + UPDATED_SOURCE_URL,
            "sourceUrl.in=" + UPDATED_SOURCE_URL
        );
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where sourceUrl is not null
        defaultExternalDrugInfoFiltering("sourceUrl.specified=true", "sourceUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where sourceUrl contains
        defaultExternalDrugInfoFiltering("sourceUrl.contains=" + DEFAULT_SOURCE_URL, "sourceUrl.contains=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    void getAllExternalDrugInfosBySourceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        // Get all the externalDrugInfoList where sourceUrl does not contain
        defaultExternalDrugInfoFiltering(
            "sourceUrl.doesNotContain=" + UPDATED_SOURCE_URL,
            "sourceUrl.doesNotContain=" + DEFAULT_SOURCE_URL
        );
    }

    private void defaultExternalDrugInfoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExternalDrugInfoShouldBeFound(shouldBeFound);
        defaultExternalDrugInfoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExternalDrugInfoShouldBeFound(String filter) throws Exception {
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(externalDrugInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].productSummary").value(hasItem(DEFAULT_PRODUCT_SUMMARY)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)));

        // Check, that the count call also returns 1
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExternalDrugInfoShouldNotBeFound(String filter) throws Exception {
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExternalDrugInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExternalDrugInfo() throws Exception {
        // Get the externalDrugInfo
        restExternalDrugInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExternalDrugInfo() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalDrugInfo
        ExternalDrugInfo updatedExternalDrugInfo = externalDrugInfoRepository.findById(externalDrugInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExternalDrugInfo are not directly saved in db
        em.detach(updatedExternalDrugInfo);
        updatedExternalDrugInfo
            .source(UPDATED_SOURCE)
            .productSummary(UPDATED_PRODUCT_SUMMARY)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sourceUrl(UPDATED_SOURCE_URL);
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(updatedExternalDrugInfo);

        restExternalDrugInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalDrugInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalDrugInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExternalDrugInfoToMatchAllProperties(updatedExternalDrugInfo);
    }

    @Test
    @Transactional
    void putNonExistingExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, externalDrugInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalDrugInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(externalDrugInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(externalDrugInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExternalDrugInfoWithPatch() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalDrugInfo using partial update
        ExternalDrugInfo partialUpdatedExternalDrugInfo = new ExternalDrugInfo();
        partialUpdatedExternalDrugInfo.setId(externalDrugInfo.getId());

        partialUpdatedExternalDrugInfo
            .source(UPDATED_SOURCE)
            .productSummary(UPDATED_PRODUCT_SUMMARY)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sourceUrl(UPDATED_SOURCE_URL);

        restExternalDrugInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalDrugInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalDrugInfo))
            )
            .andExpect(status().isOk());

        // Validate the ExternalDrugInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalDrugInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExternalDrugInfo, externalDrugInfo),
            getPersistedExternalDrugInfo(externalDrugInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateExternalDrugInfoWithPatch() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the externalDrugInfo using partial update
        ExternalDrugInfo partialUpdatedExternalDrugInfo = new ExternalDrugInfo();
        partialUpdatedExternalDrugInfo.setId(externalDrugInfo.getId());

        partialUpdatedExternalDrugInfo
            .source(UPDATED_SOURCE)
            .productSummary(UPDATED_PRODUCT_SUMMARY)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .sourceUrl(UPDATED_SOURCE_URL);

        restExternalDrugInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExternalDrugInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExternalDrugInfo))
            )
            .andExpect(status().isOk());

        // Validate the ExternalDrugInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExternalDrugInfoUpdatableFieldsEquals(
            partialUpdatedExternalDrugInfo,
            getPersistedExternalDrugInfo(partialUpdatedExternalDrugInfo)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, externalDrugInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalDrugInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(externalDrugInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExternalDrugInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        externalDrugInfo.setId(longCount.incrementAndGet());

        // Create the ExternalDrugInfo
        ExternalDrugInfoDTO externalDrugInfoDTO = externalDrugInfoMapper.toDto(externalDrugInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExternalDrugInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(externalDrugInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExternalDrugInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExternalDrugInfo() throws Exception {
        // Initialize the database
        insertedExternalDrugInfo = externalDrugInfoRepository.saveAndFlush(externalDrugInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the externalDrugInfo
        restExternalDrugInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, externalDrugInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return externalDrugInfoRepository.count();
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

    protected ExternalDrugInfo getPersistedExternalDrugInfo(ExternalDrugInfo externalDrugInfo) {
        return externalDrugInfoRepository.findById(externalDrugInfo.getId()).orElseThrow();
    }

    protected void assertPersistedExternalDrugInfoToMatchAllProperties(ExternalDrugInfo expectedExternalDrugInfo) {
        assertExternalDrugInfoAllPropertiesEquals(expectedExternalDrugInfo, getPersistedExternalDrugInfo(expectedExternalDrugInfo));
    }

    protected void assertPersistedExternalDrugInfoToMatchUpdatableProperties(ExternalDrugInfo expectedExternalDrugInfo) {
        assertExternalDrugInfoAllUpdatablePropertiesEquals(
            expectedExternalDrugInfo,
            getPersistedExternalDrugInfo(expectedExternalDrugInfo)
        );
    }
}
