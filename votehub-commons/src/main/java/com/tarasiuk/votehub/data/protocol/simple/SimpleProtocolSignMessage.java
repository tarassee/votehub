package com.tarasiuk.votehub.data.protocol.simple;

import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public record SimpleProtocolSignMessage(
        @NotNull
        String message,
        @NotNull
        BigInteger privateExponent,
        @NotNull
        BigInteger modulus
) {
}
