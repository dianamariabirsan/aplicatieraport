package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExternalDrugInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExternalDrugInfo getExternalDrugInfoSample1() {
        return new ExternalDrugInfo().id(1L).source("source1").productSummary("productSummary1").sourceUrl("sourceUrl1");
    }

    public static ExternalDrugInfo getExternalDrugInfoSample2() {
        return new ExternalDrugInfo().id(2L).source("source2").productSummary("productSummary2").sourceUrl("sourceUrl2");
    }

    public static ExternalDrugInfo getExternalDrugInfoRandomSampleGenerator() {
        return new ExternalDrugInfo()
            .id(longCount.incrementAndGet())
            .source(UUID.randomUUID().toString())
            .productSummary(UUID.randomUUID().toString())
            .sourceUrl(UUID.randomUUID().toString());
    }
}
