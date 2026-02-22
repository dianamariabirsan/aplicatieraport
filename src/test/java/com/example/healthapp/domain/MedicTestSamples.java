package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medic getMedicSample1() {
        return new Medic()
            .id(1L)
            .nume("nume1")
            .prenume("prenume1")
            .specializare("specializare1")
            .email("email1")
            .telefon("telefon1")
            .cabinet("cabinet1");
    }

    public static Medic getMedicSample2() {
        return new Medic()
            .id(2L)
            .nume("nume2")
            .prenume("prenume2")
            .specializare("specializare2")
            .email("email2")
            .telefon("telefon2")
            .cabinet("cabinet2");
    }

    public static Medic getMedicRandomSampleGenerator() {
        return new Medic()
            .id(longCount.incrementAndGet())
            .nume(UUID.randomUUID().toString())
            .prenume(UUID.randomUUID().toString())
            .specializare(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefon(UUID.randomUUID().toString())
            .cabinet(UUID.randomUUID().toString());
    }
}
