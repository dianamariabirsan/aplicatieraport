package com.example.healthapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback().id(1L).scor(1).comentariu("comentariu1");
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback().id(2L).scor(2).comentariu("comentariu2");
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback().id(longCount.incrementAndGet()).scor(intCount.incrementAndGet()).comentariu(UUID.randomUUID().toString());
    }
}
