package com.tarasiuk.votehub.data.protocol.blind.mask;

import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessage;
import com.tarasiuk.votehub.util.data.RSAKey;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.util.List;

public record BlindProtocolUnMaskAndUnSignMessage(
        @NotNull
        BigInteger maskingKey,
        @NotNull
        RSAKey psPublicKey,
        @NotNull
        List<BlindProtocolSignedMaskedMessage> signedMaskedMessageSet
) {
}
