package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudiiLiteraturaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudiiLiteratura getStudiiLiteraturaSample1() {
        return new StudiiLiteratura()
            .id(1L)
            .titlu("titlu1")
            .autori("autori1")
            .anul(1)
            .tipStudiu("tipStudiu1")
            .substanta("substanta1")
            .concluzie("concluzie1")
            .link("link1");
    }

    public static StudiiLiteratura getStudiiLiteraturaSample2() {
        return new StudiiLiteratura()
            .id(2L)
            .titlu("titlu2")
            .autori("autori2")
            .anul(2)
            .tipStudiu("tipStudiu2")
            .substanta("substanta2")
            .concluzie("concluzie2")
            .link("link2");
    }

    public static StudiiLiteratura getStudiiLiteraturaRandomSampleGenerator() {
        return new StudiiLiteratura()
            .id(longCount.incrementAndGet())
            .titlu(UUID.randomUUID().toString())
            .autori(UUID.randomUUID().toString())
            .anul(intCount.incrementAndGet())
            .tipStudiu(UUID.randomUUID().toString())
            .substanta(UUID.randomUUID().toString())
            .concluzie(UUID.randomUUID().toString())
            .link(UUID.randomUUID().toString());
    }
}
