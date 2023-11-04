package com.tarasiuk.votehub.data.protocol;

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
