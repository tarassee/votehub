package com.tarasiuk.votehub.withoutconfirmationprotocol.bbs;

import java.math.BigInteger;

public class BBSCipher {
    private BBSGenerator generator;
    private int bitLength = 2048;

    public BBSCipher(BigInteger p, BigInteger q, BigInteger seed) {
        generator = new BBSGenerator(p, q, seed);
    }

    public BigInteger encrypt(BigInteger message) {
        BigInteger ciphered = BigInteger.ZERO;
        for (int i = 0; i < bitLength; i++) {
            ciphered = ciphered.shiftLeft(1);
            ciphered = ciphered.add(message.testBit(bitLength - i - 1) != generator.generate().testBit(0) ? BigInteger.ONE : BigInteger.ZERO);
        }
        return ciphered;
    }

    public BigInteger decrypt(BigInteger message) {
        return encrypt(message);
    }
}
