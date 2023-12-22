package com.tarasiuk.votehub.withoutconfirmationprotocol.data;

import java.math.BigInteger;

public record BBSCipherKeys(BigInteger p, BigInteger q, BigInteger seed, BigInteger x0) {
}
