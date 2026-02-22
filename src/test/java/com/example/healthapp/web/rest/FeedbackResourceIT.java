package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.FeedbackAsserts.*;
import static com.example.healthapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.Feedback;
import com.example.healthapp.repository.FeedbackRepository;
import com.example.healthapp.service.dto.FeedbackDTO;
import com.example.healthapp.service.mapper.FeedbackMapper;
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
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResourceIT {

    private static final Integer DEFAULT_SCOR = 1;
    private static final Integer UPDATED_SCOR = 2;
    private static final Integer SMALLER_SCOR = 1 - 1;

    private static final String DEFAULT_COMENTARIU = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_FEEDBACK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FEEDBACK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    private Feedback insertedFeedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity() {
        return new Feedback().scor(DEFAULT_SCOR).comentariu(DEFAULT_COMENTARIU).dataFeedback(DEFAULT_DATA_FEEDBACK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity() {
        return new Feedback().scor(UPDATED_SCOR).comentariu(UPDATED_COMENTARIU).dataFeedback(UPDATED_DATA_FEEDBACK);
    }

    @BeforeEach
    void initTest() {
        feedback = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFeedback != null) {
            feedbackRepository.delete(insertedFeedback);
            insertedFeedback = null;
        }
    }

    @Test
    @Transactional
    void createFeedback() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);
        var returnedFeedbackDTO = om.readValue(
            restFeedbackMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackDTO.class
        );

        // Validate the Feedback in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedback = feedbackMapper.toEntity(returnedFeedbackDTO);
        assertFeedbackUpdatableFieldsEquals(returnedFeedback, getPersistedFeedback(returnedFeedback));

        insertedFeedback = returnedFeedback;
    }

    @Test
    @Transactional
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setScor(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbacks() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].scor").value(hasItem(DEFAULT_SCOR)))
            .andExpect(jsonPath("$.[*].comentariu").value(hasItem(DEFAULT_COMENTARIU)))
            .andExpect(jsonPath("$.[*].dataFeedback").value(hasItem(DEFAULT_DATA_FEEDBACK.toString())));
    }

    @Test
    @Transactional
    void getFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.scor").value(DEFAULT_SCOR))
            .andExpect(jsonPath("$.comentariu").value(DEFAULT_COMENTARIU))
            .andExpect(jsonPath("$.dataFeedback").value(DEFAULT_DATA_FEEDBACK.toString()));
    }

    @Test
    @Transactional
    void getFeedbacksByIdFiltering() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        Long id = feedback.getId();

        defaultFeedbackFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFeedbackFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFeedbackFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor equals to
        defaultFeedbackFiltering("scor.equals=" + DEFAULT_SCOR, "scor.equals=" + UPDATED_SCOR);
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor in
        defaultFeedbackFiltering("scor.in=" + DEFAULT_SCOR + "," + UPDATED_SCOR, "scor.in=" + UPDATED_SCOR);
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor is not null
        defaultFeedbackFiltering("scor.specified=true", "scor.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor is greater than or equal to
        defaultFeedbackFiltering("scor.greaterThanOrEqual=" + DEFAULT_SCOR, "scor.greaterThanOrEqual=" + (DEFAULT_SCOR + 1));
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor is less than or equal to
        defaultFeedbackFiltering("scor.lessThanOrEqual=" + DEFAULT_SCOR, "scor.lessThanOrEqual=" + SMALLER_SCOR);
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor is less than
        defaultFeedbackFiltering("scor.lessThan=" + (DEFAULT_SCOR + 1), "scor.lessThan=" + DEFAULT_SCOR);
    }

    @Test
    @Transactional
    void getAllFeedbacksByScorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where scor is greater than
        defaultFeedbackFiltering("scor.greaterThan=" + SMALLER_SCOR, "scor.greaterThan=" + DEFAULT_SCOR);
    }

    @Test
    @Transactional
    void getAllFeedbacksByComentariuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where comentariu equals to
        defaultFeedbackFiltering("comentariu.equals=" + DEFAULT_COMENTARIU, "comentariu.equals=" + UPDATED_COMENTARIU);
    }

    @Test
    @Transactional
    void getAllFeedbacksByComentariuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where comentariu in
        defaultFeedbackFiltering("comentariu.in=" + DEFAULT_COMENTARIU + "," + UPDATED_COMENTARIU, "comentariu.in=" + UPDATED_COMENTARIU);
    }

    @Test
    @Transactional
    void getAllFeedbacksByComentariuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where comentariu is not null
        defaultFeedbackFiltering("comentariu.specified=true", "comentariu.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByComentariuContainsSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where comentariu contains
        defaultFeedbackFiltering("comentariu.contains=" + DEFAULT_COMENTARIU, "comentariu.contains=" + UPDATED_COMENTARIU);
    }

    @Test
    @Transactional
    void getAllFeedbacksByComentariuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where comentariu does not contain
        defaultFeedbackFiltering("comentariu.doesNotContain=" + UPDATED_COMENTARIU, "comentariu.doesNotContain=" + DEFAULT_COMENTARIU);
    }

    @Test
    @Transactional
    void getAllFeedbacksByDataFeedbackIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dataFeedback equals to
        defaultFeedbackFiltering("dataFeedback.equals=" + DEFAULT_DATA_FEEDBACK, "dataFeedback.equals=" + UPDATED_DATA_FEEDBACK);
    }

    @Test
    @Transactional
    void getAllFeedbacksByDataFeedbackIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dataFeedback in
        defaultFeedbackFiltering(
            "dataFeedback.in=" + DEFAULT_DATA_FEEDBACK + "," + UPDATED_DATA_FEEDBACK,
            "dataFeedback.in=" + UPDATED_DATA_FEEDBACK
        );
    }

    @Test
    @Transactional
    void getAllFeedbacksByDataFeedbackIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dataFeedback is not null
        defaultFeedbackFiltering("dataFeedback.specified=true", "dataFeedback.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByAlocareIsEqualToSomething() throws Exception {
        AlocareTratament alocare;
        if (TestUtil.findAll(em, AlocareTratament.class).isEmpty()) {
            feedbackRepository.saveAndFlush(feedback);
            alocare = AlocareTratamentResourceIT.createEntity();
        } else {
            alocare = TestUtil.findAll(em, AlocareTratament.class).get(0);
        }
        em.persist(alocare);
        em.flush();
        feedback.setAlocare(alocare);
        feedbackRepository.saveAndFlush(feedback);
        Long alocareId = alocare.getId();
        // Get all the feedbackList where alocare equals to alocareId
        defaultFeedbackShouldBeFound("alocareId.equals=" + alocareId);

        // Get all the feedbackList where alocare equals to (alocareId + 1)
        defaultFeedbackShouldNotBeFound("alocareId.equals=" + (alocareId + 1));
    }

    private void defaultFeedbackFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFeedbackShouldBeFound(shouldBeFound);
        defaultFeedbackShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeedbackShouldBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].scor").value(hasItem(DEFAULT_SCOR)))
            .andExpect(jsonPath("$.[*].comentariu").value(hasItem(DEFAULT_COMENTARIU)))
            .andExpect(jsonPath("$.[*].dataFeedback").value(hasItem(DEFAULT_DATA_FEEDBACK.toString())));

        // Check, that the count call also returns 1
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeedbackShouldNotBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedback are not directly saved in db
        em.detach(updatedFeedback);
        updatedFeedback.scor(UPDATED_SCOR).comentariu(UPDATED_COMENTARIU).dataFeedback(UPDATED_DATA_FEEDBACK);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(updatedFeedback);

        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackToMatchAllProperties(updatedFeedback);
    }

    @Test
    @Transactional
    void putNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback.dataFeedback(UPDATED_DATA_FEEDBACK);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeedback, feedback), getPersistedFeedback(feedback));
    }

    @Test
    @Transactional
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback.scor(UPDATED_SCOR).comentariu(UPDATED_COMENTARIU).dataFeedback(UPDATED_DATA_FEEDBACK);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(partialUpdatedFeedback, getPersistedFeedback(partialUpdatedFeedback));
    }

    @Test
    @Transactional
    void patchNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedback
        restFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedback.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackRepository.count();
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

    protected Feedback getPersistedFeedback(Feedback feedback) {
        return feedbackRepository.findById(feedback.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackToMatchAllProperties(Feedback expectedFeedback) {
        assertFeedbackAllPropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }

    protected void assertPersistedFeedbackToMatchUpdatableProperties(Feedback expectedFeedback) {
        assertFeedbackAllUpdatablePropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }
}
