package com.tarasiuk.votehub.withoutconfirmationprotocol.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class RandomUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomUtil() {
    }

    public static String generateRandomString() {
        return new BigInteger(60, RANDOM).toString(32);
    }

}
