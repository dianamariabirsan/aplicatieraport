package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicamentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Medicament getMedicamentSample1() {
        return new Medicament()
            .id(1L)
            .denumire("denumire1")
            .substanta("substanta1")
            .indicatii("indicatii1")
            .contraindicatii("contraindicatii1")
            .interactiuni("interactiuni1")
            .avertizari("avertizari1")
            .dozaRecomandata("dozaRecomandata1")
            .formaFarmaceutica("formaFarmaceutica1");
    }

    public static Medicament getMedicamentSample2() {
        return new Medicament()
            .id(2L)
            .denumire("denumire2")
            .substanta("substanta2")
            .indicatii("indicatii2")
            .contraindicatii("contraindicatii2")
            .interactiuni("interactiuni2")
            .avertizari("avertizari2")
            .dozaRecomandata("dozaRecomandata2")
            .formaFarmaceutica("formaFarmaceutica2");
    }

    public static Medicament getMedicamentRandomSampleGenerator() {
        return new Medicament()
            .id(longCount.incrementAndGet())
            .denumire(UUID.randomUUID().toString())
            .substanta(UUID.randomUUID().toString())
            .indicatii(UUID.randomUUID().toString())
            .contraindicatii(UUID.randomUUID().toString())
            .interactiuni(UUID.randomUUID().toString())
            .avertizari(UUID.randomUUID().toString())
            .dozaRecomandata(UUID.randomUUID().toString())
            .formaFarmaceutica(UUID.randomUUID().toString());
    }
}
