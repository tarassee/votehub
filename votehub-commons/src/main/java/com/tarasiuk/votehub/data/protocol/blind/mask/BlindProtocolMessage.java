package com.tarasiuk.votehub.data.protocol.blind.mask;

public record BlindProtocolMessage(
        Integer passportId,
        String candidateValue
) {
}
