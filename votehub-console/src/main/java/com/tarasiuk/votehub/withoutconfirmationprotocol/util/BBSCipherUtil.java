package com.tarasiuk.votehub.withoutconfirmationprotocol.util;

import com.tarasiuk.votehub.withoutconfirmationprotocol.bbs.BBSCipher;

import java.math.BigInteger;

import static com.tarasiuk.votehub.withoutconfirmationprotocol.constant.ElectionConstant.SEPARATOR;

public final class BBSCipherUtil {

    private static final boolean IS_ENABLED = false;

    private BBSCipherUtil() {
    }

    public static BigInteger encryptMessage(BigInteger[] messageArray, BigInteger voterId, BigInteger x0, BigInteger p, BigInteger q, BigInteger seed) {
        var message = processMessage(messageArray, voterId, x0);
        BBSCipher cipher = new BBSCipher(p, q, seed);
        return cipher.encrypt(message);
    }

    public static BigInteger[] decryptMessage(BigInteger encryptedMessage, BigInteger p, BigInteger q, BigInteger seed) {
        BBSCipher decipher = new BBSCipher(p, q, seed);
        BigInteger decryptedMessage = decipher.decrypt(encryptedMessage);

        String decryptedMessageString = new String(decryptedMessage.toByteArray());

        // Splitting the decrypted message and constructing Big Integer array
        String[] parts = decryptedMessageString.split(SEPARATOR);
        BigInteger[] originalValues = new BigInteger[parts.length];
        for (int i = 0; i < parts.length; i++) {
            originalValues[i] = new BigInteger(parts[i]);
        }
        return originalValues;
    }

    private static BigInteger processMessage(BigInteger[] inputArray, BigInteger voterId, BigInteger x0) {

        StringBuilder builder = new StringBuilder();
        for (BigInteger bi : inputArray) {
            builder.append(bi).append(SEPARATOR);
        }
        if (IS_ENABLED) {
            builder.append(x0).append(SEPARATOR).append(voterId);
        }
        return new BigInteger(builder.toString().getBytes());
    }

}
