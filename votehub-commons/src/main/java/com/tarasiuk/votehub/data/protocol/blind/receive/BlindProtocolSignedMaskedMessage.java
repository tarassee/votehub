package com.tarasiuk.votehub.data.protocol.blind.receive;

import java.math.BigInteger;

public record BlindProtocolSignedMaskedMessage(
        BigInteger signedMaskedMessage
) {
}
