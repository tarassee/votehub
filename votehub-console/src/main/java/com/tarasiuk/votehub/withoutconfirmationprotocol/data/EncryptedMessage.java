package com.tarasiuk.votehub.withoutconfirmationprotocol.data;

import java.math.BigInteger;

public record EncryptedMessage(BigInteger encryptedBallotMessage, BigInteger voterId) {
}
