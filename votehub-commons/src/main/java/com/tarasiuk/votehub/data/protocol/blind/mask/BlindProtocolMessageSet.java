package com.tarasiuk.votehub.data.protocol.blind.mask;

import java.util.List;

public record BlindProtocolMessageSet(
        List<BlindProtocolMessage> messageSet
) {
}
