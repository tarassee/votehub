package com.tarasiuk.votehub.twocommissionsprotocol.util;

import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalKeys;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPrivateKey;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalParameters;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPublicKey;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class ElGamalUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private ElGamalUtil() {
    }

    public static ElGamalKeys generateKeys() {
        var p = BigInteger.probablePrime(8, RANDOM);
        var g = BigInteger.probablePrime(8, RANDOM).mod(p);
        var x = new BigInteger(8, RANDOM).mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.ONE);
        var y = g.modPow(x, p);
        var parameters = new ElGamalParameters(p, g);
        var privateKey = new ElGamalPrivateKey(x, parameters);
        var publicKey = new ElGamalPublicKey(y, parameters);
        return new ElGamalKeys(publicKey, privateKey);
    }

    public static BigInteger[] encrypt(String msg, ElGamalPublicKey publicKey) {
        BigInteger[] encryptedMessage = new BigInteger[msg.length() * 2];

        for (int i = 0; i < msg.length(); i++) {
            BigInteger k = new BigInteger(8, RANDOM).mod(publicKey.parameters().p().subtract(BigInteger.ONE)).add(BigInteger.ONE);
            BigInteger a = publicKey.parameters().g().modPow(k, publicKey.parameters().p());
            BigInteger b = publicKey.y().modPow(k, publicKey.parameters().p()).multiply(BigInteger.valueOf(msg.charAt(i))).mod(publicKey.parameters().p());

            encryptedMessage[2 * i] = a;
            encryptedMessage[2 * i + 1] = b;
        }

        return encryptedMessage;
    }

    public static String decrypt(BigInteger[] encryptedMsg, ElGamalPrivateKey privateKey) {
        StringBuilder decryptedMsg = new StringBuilder();

        for (int i = 0; i < encryptedMsg.length; i += 2) {
            BigInteger a = encryptedMsg[i];
            BigInteger b = encryptedMsg[i + 1];

            BigInteger m = b.multiply(a.modPow(privateKey.x(), privateKey.parameters().p()).modInverse(privateKey.parameters().p()))
                    .mod(privateKey.parameters().p());
            decryptedMsg.append((char) m.intValue());
        }

        return decryptedMsg.toString();
    }

}
