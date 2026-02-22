package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FarmacistTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Farmacist getFarmacistSample1() {
        return new Farmacist().id(1L).nume("nume1").prenume("prenume1").farmacie("farmacie1").email("email1").telefon("telefon1");
    }

    public static Farmacist getFarmacistSample2() {
        return new Farmacist().id(2L).nume("nume2").prenume("prenume2").farmacie("farmacie2").email("email2").telefon("telefon2");
    }

    public static Farmacist getFarmacistRandomSampleGenerator() {
        return new Farmacist()
            .id(longCount.incrementAndGet())
            .nume(UUID.randomUUID().toString())
            .prenume(UUID.randomUUID().toString())
            .farmacie(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefon(UUID.randomUUID().toString());
    }
}
