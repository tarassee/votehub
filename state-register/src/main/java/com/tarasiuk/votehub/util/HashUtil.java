package com.tarasiuk.votehub.util;

import java.math.BigInteger;

public final class HashUtil {

    private static final String KEYBOARD_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 `~!@#$%^&*()-=_+[]\\{}|;':\",./<>?";

    private HashUtil() {
    }

    public static BigInteger simplifiedQuadraticConvolutionHash(String message, BigInteger n) {
        BigInteger hashValue = BigInteger.ZERO;
        for (int i = 0; i < message.length(); i++) {
            int charIndex = KEYBOARD_SYMBOLS.indexOf(message.charAt(i));
            BigInteger charIndexSquared = BigInteger.valueOf(charIndex).pow(2);
            hashValue = hashValue.add(charIndexSquared).mod(n);
        }
        return hashValue;
    }

}
