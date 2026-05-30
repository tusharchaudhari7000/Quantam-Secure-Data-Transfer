package com.dypcoe.qsdta.simulation.utility.singleton;

import java.security.SecureRandom;

public abstract class CryptoUtilityRandom {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static int nextInt() {
        return SECURE_RANDOM.nextInt();
    }

    public static boolean nextBoolean() {
        return SECURE_RANDOM.nextBoolean();
    }

}
