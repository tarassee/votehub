package com.tarasiuk.votehub.data.protocol.blind.send;

import java.util.List;

public record BlindProtocolMaskedMessageSet(
        List<BlindProtocolMaskedMessage> maskedMessageSet
) {
}
