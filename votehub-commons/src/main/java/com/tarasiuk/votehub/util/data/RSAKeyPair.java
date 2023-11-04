package com.tarasiuk.votehub.util.data;

public record RSAKeyPair(
        RSAKey publicKey,
        RSAKey privateKey
) {
}
