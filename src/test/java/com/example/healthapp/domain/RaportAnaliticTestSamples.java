package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RaportAnaliticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RaportAnalitic getRaportAnaliticSample1() {
        return new RaportAnalitic().id(1L).observatii("observatii1").concluzii("concluzii1");
    }

    public static RaportAnalitic getRaportAnaliticSample2() {
        return new RaportAnalitic().id(2L).observatii("observatii2").concluzii("concluzii2");
    }

    public static RaportAnalitic getRaportAnaliticRandomSampleGenerator() {
        return new RaportAnalitic()
            .id(longCount.incrementAndGet())
            .observatii(UUID.randomUUID().toString())
            .concluzii(UUID.randomUUID().toString());
    }
}
