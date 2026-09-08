package com.railway.util;

import java.util.concurrent.ThreadLocalRandom;

public class PNRGenerator {

    private PNRGenerator() {
    }

    public static String generatePNR() {

        long number = ThreadLocalRandom.current()
                .nextLong(1000000000L, 9999999999L);

        return String.valueOf(number);
    }
}