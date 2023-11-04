package com.tarasiuk.votehub.util;

import com.tarasiuk.votehub.util.data.RSAKey;
import com.tarasiuk.votehub.util.data.RSAKeyPair;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class KeyGenerationUtil {

    private KeyGenerationUtil() {
    }

    public static RSAKeyPair generateRSAKeyPair(int bitsize) {
        BigInteger p = BigInteger.probablePrime(bitsize / 2, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(bitsize / 2, new SecureRandom());

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = new BigInteger(bitsize, new SecureRandom());
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0);

        BigInteger d = e.modInverse(phi);

        RSAKey publicKey = new RSAKey(e, n);
        RSAKey privateKey = new RSAKey(d, n);

        return new RSAKeyPair(publicKey, privateKey);
    }

}
