package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonitorizareTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Monitorizare getMonitorizareSample1() {
        return new Monitorizare().id(1L).puls(1).comentarii("comentarii1");
    }

    public static Monitorizare getMonitorizareSample2() {
        return new Monitorizare().id(2L).puls(2).comentarii("comentarii2");
    }

    public static Monitorizare getMonitorizareRandomSampleGenerator() {
        return new Monitorizare().id(longCount.incrementAndGet()).puls(intCount.incrementAndGet()).comentarii(UUID.randomUUID().toString());
    }
}
