package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdministrareTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Administrare getAdministrareSample1() {
        return new Administrare()
            .id(1L)
            .tipTratament("tipTratament1")
            .unitate("unitate1")
            .modAdministrare("modAdministrare1")
            .observatii("observatii1");
    }

    public static Administrare getAdministrareSample2() {
        return new Administrare()
            .id(2L)
            .tipTratament("tipTratament2")
            .unitate("unitate2")
            .modAdministrare("modAdministrare2")
            .observatii("observatii2");
    }

    public static Administrare getAdministrareRandomSampleGenerator() {
        return new Administrare()
            .id(longCount.incrementAndGet())
            .tipTratament(UUID.randomUUID().toString())
            .unitate(UUID.randomUUID().toString())
            .modAdministrare(UUID.randomUUID().toString())
            .observatii(UUID.randomUUID().toString());
    }
}
