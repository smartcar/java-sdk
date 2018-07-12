package com.smartcar.sdk;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Provides supporting utilities for integration tests.
 */
class IntegrationUtils {
    private static final char[] nonceChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final Random nonceRandom = new SecureRandom();

    /**
     * Generates a nonce string of the specified length.
     *
     * @param length the desired string length
     *
     * @return the resulting random alphanumeric
     */
    static String nonce(int length) {
        char[] result = new char[length];

        if(length < 1) {
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < length; i++) {
            result[i] = IntegrationUtils.nonceChars[IntegrationUtils.nonceRandom.nextInt(IntegrationUtils.nonceChars.length)];
        }

        return new String(result);
    }
}
