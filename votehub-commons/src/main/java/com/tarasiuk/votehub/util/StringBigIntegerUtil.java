package com.tarasiuk.votehub.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public final class StringBigIntegerUtil {

    private StringBigIntegerUtil() {
    }

    public static BigInteger stringToBigInteger(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return new BigInteger(1, bytes);
    }

    public static String bigIntegerToString(BigInteger bi) {
        byte[] bytes = bi.toByteArray();
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
