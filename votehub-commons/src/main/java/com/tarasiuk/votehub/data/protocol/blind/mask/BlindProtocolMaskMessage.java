package com.tarasiuk.votehub.data.protocol.blind.mask;

import com.tarasiuk.votehub.util.data.RSAKey;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.util.List;

public record BlindProtocolMaskMessage(
        @NotNull
        BigInteger maskingKey,
        @NotNull
        RSAKey psPublicKey,
        @NotNull
        List<BlindProtocolMessageSet> messageSets
) {
}
