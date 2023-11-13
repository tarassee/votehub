package com.tarasiuk.votehub.data.protocol.blind.send;

import java.math.BigInteger;

public record BlindProtocolMaskedMessage(
        BigInteger maskedMessage
) {
}
