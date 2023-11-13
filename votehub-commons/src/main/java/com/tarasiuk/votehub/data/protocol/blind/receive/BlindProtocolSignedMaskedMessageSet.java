package com.tarasiuk.votehub.data.protocol.blind.receive;

import java.util.List;

public record BlindProtocolSignedMaskedMessageSet(
        List<BlindProtocolSignedMaskedMessage> signedMaskedMessageSet
) {
}
