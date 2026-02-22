package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PacientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Pacient getPacientSample1() {
        return new Pacient()
            .id(1L)
            .nume("nume1")
            .prenume("prenume1")
            .sex("sex1")
            .varsta(1)
            .cnp("cnp1")
            .comorbiditati("comorbiditati1")
            .gradSedentarism("gradSedentarism1")
            .istoricTratament("istoricTratament1")
            .toleranta("toleranta1")
            .email("email1")
            .telefon("telefon1");
    }

    public static Pacient getPacientSample2() {
        return new Pacient()
            .id(2L)
            .nume("nume2")
            .prenume("prenume2")
            .sex("sex2")
            .varsta(2)
            .cnp("cnp2")
            .comorbiditati("comorbiditati2")
            .gradSedentarism("gradSedentarism2")
            .istoricTratament("istoricTratament2")
            .toleranta("toleranta2")
            .email("email2")
            .telefon("telefon2");
    }

    public static Pacient getPacientRandomSampleGenerator() {
        return new Pacient()
            .id(longCount.incrementAndGet())
            .nume(UUID.randomUUID().toString())
            .prenume(UUID.randomUUID().toString())
            .sex(UUID.randomUUID().toString())
            .varsta(intCount.incrementAndGet())
            .cnp(UUID.randomUUID().toString())
            .comorbiditati(UUID.randomUUID().toString())
            .gradSedentarism(UUID.randomUUID().toString())
            .istoricTratament(UUID.randomUUID().toString())
            .toleranta(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefon(UUID.randomUUID().toString());
    }
}
