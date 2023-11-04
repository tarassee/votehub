package com.tarasiuk.votehub.data.protocol.simple;

import java.math.BigInteger;

public record SimpleProtocolVoteMessage(
        Integer passportId,
        String vote,
        BigInteger signature
) {
}
