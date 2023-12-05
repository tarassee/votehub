package com.tarasiuk.votehub.twocommissionsprotocol.util;

import com.tarasiuk.votehub.twocommissionsprotocol.util.data.dsa.DSASignatureResult;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

public final class DSASignatureUtil {

    private DSASignatureUtil() {
    }

    public static DSASignatureResult sign(byte[] message, DSAPrivateKey privateKey) {
        BigInteger p = privateKey.getParams().getP();
        BigInteger q = privateKey.getParams().getQ();
        BigInteger g = privateKey.getParams().getG();

        SecureRandom secureRandom = new SecureRandom();
        BigInteger k, r, s;
        do {
            k = new BigInteger(q.bitLength(), secureRandom).mod(q);
            r = g.modPow(k, p).mod(q);
        } while (r.equals(BigInteger.ZERO));

        do {
            s = k.modInverse(q).multiply(new BigInteger(message).add(privateKey.getX().multiply(r))).mod(q);
        } while (s.equals(BigInteger.ZERO));

        return new DSASignatureResult(r, s);
    }

    public static boolean verify(byte[] message, DSASignatureResult signature, DSAPublicKey publicKey) {
        BigInteger r = signature.r();
        BigInteger s = signature.s();
        BigInteger p = publicKey.getParams().getP();
        BigInteger q = publicKey.getParams().getQ();
        BigInteger g = publicKey.getParams().getG();

        BigInteger w = s.modInverse(q);
        BigInteger u1 = new BigInteger(message).multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = ((g.modPow(u1, p)).multiply(publicKey.getY().modPow(u2, p))).mod(p).mod(q);

        return r.equals(v);
    }

}