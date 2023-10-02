package com.tarasiuk.votehub.data;

import java.math.BigInteger;

public record SimpleProtocolVoteMessage(
        Integer passportId,
        String vote,
        BigInteger signature
) {
}
