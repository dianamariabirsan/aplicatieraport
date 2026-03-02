package com.example.healthapp.web.rest;

import static com.example.healthapp.domain.DecisionLogAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.healthapp.IntegrationTest;
import com.example.healthapp.domain.AlocareTratament;
import com.example.healthapp.domain.DecisionLog;
import com.example.healthapp.domain.enumeration.ActorType;
import com.example.healthapp.repository.DecisionLogRepository;
import com.example.healthapp.service.dto.DecisionLogDTO;
import com.example.healthapp.service.mapper.DecisionLogMapper;
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
 * Integration tests for the {@link DecisionLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecisionLogResourceIT {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ActorType DEFAULT_ACTOR_TYPE = ActorType.MEDIC;
    private static final ActorType UPDATED_ACTOR_TYPE = ActorType.SISTEM_AI;

    private static final String DEFAULT_RECOMANDARE = "AAAAAAAAAA";
    private static final String UPDATED_RECOMANDARE = "BBBBBBBBBB";

    private static final Double DEFAULT_MODEL_SCORE = 1D;
    private static final Double UPDATED_MODEL_SCORE = 2D;
    private static final Double SMALLER_MODEL_SCORE = 1D - 1D;

    private static final String DEFAULT_REGULI_TRIGGERED = "AAAAAAAAAA";
    private static final String UPDATED_REGULI_TRIGGERED = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_CHECKS = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_CHECKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/decision-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DecisionLogRepository decisionLogRepository;

    @Autowired
    private DecisionLogMapper decisionLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionLogMockMvc;

    private DecisionLog decisionLog;

    private DecisionLog insertedDecisionLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecisionLog createEntity() {
        return new DecisionLog()
            .timestamp(DEFAULT_TIMESTAMP)
            .actorType(DEFAULT_ACTOR_TYPE)
            .recomandare(DEFAULT_RECOMANDARE)
            .modelScore(DEFAULT_MODEL_SCORE)
            .reguliTriggered(DEFAULT_REGULI_TRIGGERED)
            .externalChecks(DEFAULT_EXTERNAL_CHECKS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecisionLog createUpdatedEntity() {
        return new DecisionLog()
            .timestamp(UPDATED_TIMESTAMP)
            .actorType(UPDATED_ACTOR_TYPE)
            .recomandare(UPDATED_RECOMANDARE)
            .modelScore(UPDATED_MODEL_SCORE)
            .reguliTriggered(UPDATED_REGULI_TRIGGERED)
            .externalChecks(UPDATED_EXTERNAL_CHECKS);
    }

    @BeforeEach
    void initTest() {
        decisionLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDecisionLog != null) {
            decisionLogRepository.delete(insertedDecisionLog);
            insertedDecisionLog = null;
        }
    }

    @Test
    @Transactional
    void createDecisionLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DecisionLog — should be blocked (read-only resource)
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);
        restDecisionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database (nothing should be created)
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void createDecisionLogWithExistingId() throws Exception {
        // Create the DecisionLog with an existing ID — should be blocked (read-only resource)
        decisionLog.setId(1L);
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        restDecisionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        decisionLog.setTimestamp(null);

        // Create the DecisionLog — should be blocked (read-only resource)
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        restDecisionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActorTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        decisionLog.setActorType(null);

        // Create the DecisionLog — should be blocked (read-only resource)
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        restDecisionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecisionLogs() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].actorType").value(hasItem(DEFAULT_ACTOR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recomandare").value(hasItem(DEFAULT_RECOMANDARE)))
            .andExpect(jsonPath("$.[*].modelScore").value(hasItem(DEFAULT_MODEL_SCORE)))
            .andExpect(jsonPath("$.[*].reguliTriggered").value(hasItem(DEFAULT_REGULI_TRIGGERED)))
            .andExpect(jsonPath("$.[*].externalChecks").value(hasItem(DEFAULT_EXTERNAL_CHECKS)));
    }

    @Test
    @Transactional
    void getDecisionLog() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get the decisionLog
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL_ID, decisionLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decisionLog.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.actorType").value(DEFAULT_ACTOR_TYPE.toString()))
            .andExpect(jsonPath("$.recomandare").value(DEFAULT_RECOMANDARE))
            .andExpect(jsonPath("$.modelScore").value(DEFAULT_MODEL_SCORE))
            .andExpect(jsonPath("$.reguliTriggered").value(DEFAULT_REGULI_TRIGGERED))
            .andExpect(jsonPath("$.externalChecks").value(DEFAULT_EXTERNAL_CHECKS));
    }

    @Test
    @Transactional
    void getDecisionLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        Long id = decisionLog.getId();

        defaultDecisionLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDecisionLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDecisionLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where timestamp equals to
        defaultDecisionLogFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where timestamp in
        defaultDecisionLogFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where timestamp is not null
        defaultDecisionLogFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByActorTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where actorType equals to
        defaultDecisionLogFiltering("actorType.equals=" + DEFAULT_ACTOR_TYPE, "actorType.equals=" + UPDATED_ACTOR_TYPE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByActorTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where actorType in
        defaultDecisionLogFiltering("actorType.in=" + DEFAULT_ACTOR_TYPE + "," + UPDATED_ACTOR_TYPE, "actorType.in=" + UPDATED_ACTOR_TYPE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByActorTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where actorType is not null
        defaultDecisionLogFiltering("actorType.specified=true", "actorType.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByRecomandareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where recomandare equals to
        defaultDecisionLogFiltering("recomandare.equals=" + DEFAULT_RECOMANDARE, "recomandare.equals=" + UPDATED_RECOMANDARE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByRecomandareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where recomandare in
        defaultDecisionLogFiltering(
            "recomandare.in=" + DEFAULT_RECOMANDARE + "," + UPDATED_RECOMANDARE,
            "recomandare.in=" + UPDATED_RECOMANDARE
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByRecomandareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where recomandare is not null
        defaultDecisionLogFiltering("recomandare.specified=true", "recomandare.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByRecomandareContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where recomandare contains
        defaultDecisionLogFiltering("recomandare.contains=" + DEFAULT_RECOMANDARE, "recomandare.contains=" + UPDATED_RECOMANDARE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByRecomandareNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where recomandare does not contain
        defaultDecisionLogFiltering(
            "recomandare.doesNotContain=" + UPDATED_RECOMANDARE,
            "recomandare.doesNotContain=" + DEFAULT_RECOMANDARE
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore equals to
        defaultDecisionLogFiltering("modelScore.equals=" + DEFAULT_MODEL_SCORE, "modelScore.equals=" + UPDATED_MODEL_SCORE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore in
        defaultDecisionLogFiltering(
            "modelScore.in=" + DEFAULT_MODEL_SCORE + "," + UPDATED_MODEL_SCORE,
            "modelScore.in=" + UPDATED_MODEL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore is not null
        defaultDecisionLogFiltering("modelScore.specified=true", "modelScore.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore is greater than or equal to
        defaultDecisionLogFiltering(
            "modelScore.greaterThanOrEqual=" + DEFAULT_MODEL_SCORE,
            "modelScore.greaterThanOrEqual=" + UPDATED_MODEL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore is less than or equal to
        defaultDecisionLogFiltering(
            "modelScore.lessThanOrEqual=" + DEFAULT_MODEL_SCORE,
            "modelScore.lessThanOrEqual=" + SMALLER_MODEL_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore is less than
        defaultDecisionLogFiltering("modelScore.lessThan=" + UPDATED_MODEL_SCORE, "modelScore.lessThan=" + DEFAULT_MODEL_SCORE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByModelScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where modelScore is greater than
        defaultDecisionLogFiltering("modelScore.greaterThan=" + SMALLER_MODEL_SCORE, "modelScore.greaterThan=" + DEFAULT_MODEL_SCORE);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByReguliTriggeredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where reguliTriggered equals to
        defaultDecisionLogFiltering(
            "reguliTriggered.equals=" + DEFAULT_REGULI_TRIGGERED,
            "reguliTriggered.equals=" + UPDATED_REGULI_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByReguliTriggeredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where reguliTriggered in
        defaultDecisionLogFiltering(
            "reguliTriggered.in=" + DEFAULT_REGULI_TRIGGERED + "," + UPDATED_REGULI_TRIGGERED,
            "reguliTriggered.in=" + UPDATED_REGULI_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByReguliTriggeredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where reguliTriggered is not null
        defaultDecisionLogFiltering("reguliTriggered.specified=true", "reguliTriggered.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByReguliTriggeredContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where reguliTriggered contains
        defaultDecisionLogFiltering(
            "reguliTriggered.contains=" + DEFAULT_REGULI_TRIGGERED,
            "reguliTriggered.contains=" + UPDATED_REGULI_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByReguliTriggeredNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where reguliTriggered does not contain
        defaultDecisionLogFiltering(
            "reguliTriggered.doesNotContain=" + UPDATED_REGULI_TRIGGERED,
            "reguliTriggered.doesNotContain=" + DEFAULT_REGULI_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByExternalChecksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where externalChecks equals to
        defaultDecisionLogFiltering("externalChecks.equals=" + DEFAULT_EXTERNAL_CHECKS, "externalChecks.equals=" + UPDATED_EXTERNAL_CHECKS);
    }

    @Test
    @Transactional
    void getAllDecisionLogsByExternalChecksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where externalChecks in
        defaultDecisionLogFiltering(
            "externalChecks.in=" + DEFAULT_EXTERNAL_CHECKS + "," + UPDATED_EXTERNAL_CHECKS,
            "externalChecks.in=" + UPDATED_EXTERNAL_CHECKS
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByExternalChecksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where externalChecks is not null
        defaultDecisionLogFiltering("externalChecks.specified=true", "externalChecks.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionLogsByExternalChecksContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where externalChecks contains
        defaultDecisionLogFiltering(
            "externalChecks.contains=" + DEFAULT_EXTERNAL_CHECKS,
            "externalChecks.contains=" + UPDATED_EXTERNAL_CHECKS
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByExternalChecksNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        // Get all the decisionLogList where externalChecks does not contain
        defaultDecisionLogFiltering(
            "externalChecks.doesNotContain=" + UPDATED_EXTERNAL_CHECKS,
            "externalChecks.doesNotContain=" + DEFAULT_EXTERNAL_CHECKS
        );
    }

    @Test
    @Transactional
    void getAllDecisionLogsByAlocareIsEqualToSomething() throws Exception {
        AlocareTratament alocare;
        if (TestUtil.findAll(em, AlocareTratament.class).isEmpty()) {
            decisionLogRepository.saveAndFlush(decisionLog);
            alocare = AlocareTratamentResourceIT.createEntity();
        } else {
            alocare = TestUtil.findAll(em, AlocareTratament.class).get(0);
        }
        em.persist(alocare);
        em.flush();
        decisionLog.setAlocare(alocare);
        decisionLogRepository.saveAndFlush(decisionLog);
        Long alocareId = alocare.getId();
        // Get all the decisionLogList where alocare equals to alocareId
        defaultDecisionLogShouldBeFound("alocareId.equals=" + alocareId);

        // Get all the decisionLogList where alocare equals to (alocareId + 1)
        defaultDecisionLogShouldNotBeFound("alocareId.equals=" + (alocareId + 1));
    }

    private void defaultDecisionLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDecisionLogShouldBeFound(shouldBeFound);
        defaultDecisionLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionLogShouldBeFound(String filter) throws Exception {
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].actorType").value(hasItem(DEFAULT_ACTOR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].recomandare").value(hasItem(DEFAULT_RECOMANDARE)))
            .andExpect(jsonPath("$.[*].modelScore").value(hasItem(DEFAULT_MODEL_SCORE)))
            .andExpect(jsonPath("$.[*].reguliTriggered").value(hasItem(DEFAULT_REGULI_TRIGGERED)))
            .andExpect(jsonPath("$.[*].externalChecks").value(hasItem(DEFAULT_EXTERNAL_CHECKS)));

        // Check, that the count call also returns 1
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionLogShouldNotBeFound(String filter) throws Exception {
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecisionLog() throws Exception {
        // Get the decisionLog
        restDecisionLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDecisionLog() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decisionLog — should be blocked (read-only resource)
        DecisionLog updatedDecisionLog = decisionLogRepository.findById(decisionLog.getId()).orElseThrow();
        em.detach(updatedDecisionLog);
        updatedDecisionLog
            .timestamp(UPDATED_TIMESTAMP)
            .actorType(UPDATED_ACTOR_TYPE)
            .recomandare(UPDATED_RECOMANDARE)
            .modelScore(UPDATED_MODEL_SCORE)
            .reguliTriggered(UPDATED_REGULI_TRIGGERED)
            .externalChecks(UPDATED_EXTERNAL_CHECKS);
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(updatedDecisionLog);

        restDecisionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decisionLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database (nothing should be updated)
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDecisionLogToMatchAllProperties(decisionLog);
    }

    @Test
    @Transactional
    void putNonExistingDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // PUT should be blocked regardless
        restDecisionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decisionLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // PUT should be blocked regardless
        restDecisionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decisionLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecisionLogWithPatch() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // PATCH should be blocked (read-only resource)
        DecisionLog partialUpdatedDecisionLog = new DecisionLog();
        partialUpdatedDecisionLog.setId(decisionLog.getId());
        partialUpdatedDecisionLog.timestamp(UPDATED_TIMESTAMP);

        restDecisionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecisionLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate nothing changed
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void fullUpdateDecisionLogWithPatch() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // PATCH should be blocked (read-only resource)
        DecisionLog partialUpdatedDecisionLog = new DecisionLog();
        partialUpdatedDecisionLog.setId(decisionLog.getId());
        partialUpdatedDecisionLog
            .timestamp(UPDATED_TIMESTAMP)
            .actorType(UPDATED_ACTOR_TYPE)
            .recomandare(UPDATED_RECOMANDARE)
            .modelScore(UPDATED_MODEL_SCORE)
            .reguliTriggered(UPDATED_REGULI_TRIGGERED)
            .externalChecks(UPDATED_EXTERNAL_CHECKS);

        restDecisionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecisionLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate nothing changed
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchNonExistingDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // PATCH should be blocked regardless
        restDecisionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decisionLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decisionLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // PATCH should be blocked regardless
        restDecisionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decisionLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecisionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionLog.setId(longCount.incrementAndGet());

        // Create the DecisionLog
        DecisionLogDTO decisionLogDTO = decisionLogMapper.toDto(decisionLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(decisionLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecisionLog() throws Exception {
        // Initialize the database
        insertedDecisionLog = decisionLogRepository.saveAndFlush(decisionLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // DELETE should be blocked (read-only resource)
        restDecisionLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, decisionLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isMethodNotAllowed());

        // Validate the database still contains the item
        assertSameRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return decisionLogRepository.count();
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

    protected DecisionLog getPersistedDecisionLog(DecisionLog decisionLog) {
        return decisionLogRepository.findById(decisionLog.getId()).orElseThrow();
    }

    protected void assertPersistedDecisionLogToMatchAllProperties(DecisionLog expectedDecisionLog) {
        assertDecisionLogAllPropertiesEquals(expectedDecisionLog, getPersistedDecisionLog(expectedDecisionLog));
    }

    protected void assertPersistedDecisionLogToMatchUpdatableProperties(DecisionLog expectedDecisionLog) {
        assertDecisionLogAllUpdatablePropertiesEquals(expectedDecisionLog, getPersistedDecisionLog(expectedDecisionLog));
    }
}
