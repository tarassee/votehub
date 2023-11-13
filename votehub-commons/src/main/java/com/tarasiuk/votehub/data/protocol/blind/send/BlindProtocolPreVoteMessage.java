package com.tarasiuk.votehub.data.protocol.blind.send;

import java.math.BigInteger;
import java.util.List;

public record BlindProtocolPreVoteMessage(
        Integer passportId,
        BigInteger maskingKey,
        List<BlindProtocolMaskedMessageSet> maskedMessageSets
) {
}
