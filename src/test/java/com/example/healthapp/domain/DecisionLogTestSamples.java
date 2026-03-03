package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DecisionLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DecisionLog getDecisionLogSample1() {
        return new DecisionLog()
            .id(1L)
            .recomandare("recomandare1")
            .reguliTriggered("reguliTriggered1")
            .externalChecks("externalChecks1")
            .finalDecision("finalDecision1")
            .overrideReason("overrideReason1");
    }

    public static DecisionLog getDecisionLogSample2() {
        return new DecisionLog()
            .id(2L)
            .recomandare("recomandare2")
            .reguliTriggered("reguliTriggered2")
            .externalChecks("externalChecks2")
            .finalDecision("finalDecision2")
            .overrideReason("overrideReason2");
    }

    public static DecisionLog getDecisionLogRandomSampleGenerator() {
        return new DecisionLog()
            .id(longCount.incrementAndGet())
            .recomandare(UUID.randomUUID().toString())
            .reguliTriggered(UUID.randomUUID().toString())
            .externalChecks(UUID.randomUUID().toString())
            .finalDecision(UUID.randomUUID().toString())
            .overrideReason(UUID.randomUUID().toString());
    }
}
