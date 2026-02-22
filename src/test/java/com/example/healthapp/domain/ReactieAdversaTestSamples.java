package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReactieAdversaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReactieAdversa getReactieAdversaSample1() {
        return new ReactieAdversa()
            .id(1L)
            .severitate("severitate1")
            .descriere("descriere1")
            .evolutie("evolutie1")
            .raportatDe("raportatDe1");
    }

    public static ReactieAdversa getReactieAdversaSample2() {
        return new ReactieAdversa()
            .id(2L)
            .severitate("severitate2")
            .descriere("descriere2")
            .evolutie("evolutie2")
            .raportatDe("raportatDe2");
    }

    public static ReactieAdversa getReactieAdversaRandomSampleGenerator() {
        return new ReactieAdversa()
            .id(longCount.incrementAndGet())
            .severitate(UUID.randomUUID().toString())
            .descriere(UUID.randomUUID().toString())
            .evolutie(UUID.randomUUID().toString())
            .raportatDe(UUID.randomUUID().toString());
    }
}
