package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlocareTratamentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AlocareTratament getAlocareTratamentSample1() {
        return new AlocareTratament().id(1L).tratamentPropus("tratamentPropus1").motivDecizie("motivDecizie1");
    }

    public static AlocareTratament getAlocareTratamentSample2() {
        return new AlocareTratament().id(2L).tratamentPropus("tratamentPropus2").motivDecizie("motivDecizie2");
    }

    public static AlocareTratament getAlocareTratamentRandomSampleGenerator() {
        return new AlocareTratament()
            .id(longCount.incrementAndGet())
            .tratamentPropus(UUID.randomUUID().toString())
            .motivDecizie(UUID.randomUUID().toString());
    }
}
