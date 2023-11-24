package com.tarasiuk.votehub.data.protocol.blind.send;

import java.util.List;

public record BlindProtocolSignedUnmaskedMessageSet(
        List<BlindProtocolSignedUnmaskedMessage> blindProtocolSignedUnmaskedMessageSet
) {
}
