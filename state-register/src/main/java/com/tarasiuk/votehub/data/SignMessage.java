package com.tarasiuk.votehub.data;

import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;

public record SignMessage(
        @NotNull
        String message,
        @NotNull
        BigInteger privateExponent,
        @NotNull
        BigInteger modulus
) {
}
