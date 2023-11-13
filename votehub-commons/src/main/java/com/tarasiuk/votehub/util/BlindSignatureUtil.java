package com.tarasiuk.votehub.util;

import com.tarasiuk.votehub.util.data.RSAKey;

import java.math.BigInteger;

public final class BlindSignatureUtil {

    private BlindSignatureUtil() {
    }

    public static BigInteger signWithMaskingKey(BigInteger maskingKey, RSAKey psPublicKey, BigInteger messageHash) {
        return messageHash.multiply(maskingKey).modPow(psPublicKey.key(), psPublicKey.n());
    }

    public static BigInteger unSignWithMaskingKey(BigInteger maskingKey, RSAKey psPrivateKey, BigInteger masked) {
        BigInteger maskingKeyInverse = maskingKey.modInverse(psPrivateKey.n());
        return masked.modPow(psPrivateKey.key(), psPrivateKey.n()).multiply(maskingKeyInverse).mod(psPrivateKey.n());
    }

    public static BigInteger sign(BigInteger maskedMessage, RSAKey privateKey) {
        return maskedMessage.modPow(privateKey.key(), privateKey.n());
    }

    public static BigInteger unMaskAndUnSign(BigInteger maskingKey, RSAKey psPublicKey, BigInteger signed) {
        BigInteger maskingInverse = maskingKey.modInverse(psPublicKey.n());
        return signed.multiply(maskingInverse).mod(psPublicKey.n());
    }

    public static BigInteger unMaskOnly(BigInteger maskingKey, RSAKey psPublicKey, BigInteger masked) {
        BigInteger maskingInverse = maskingKey.modInverse(psPublicKey.n());
        return masked.multiply(maskingInverse);
    }

    public static BigInteger unSignUnMasked(RSAKey privateKey, BigInteger encryptedSignatureMessage) {
        return encryptedSignatureMessage.mod(privateKey.n());
    }

}
