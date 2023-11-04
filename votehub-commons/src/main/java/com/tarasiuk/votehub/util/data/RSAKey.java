package com.tarasiuk.votehub.util.data;

import java.math.BigInteger;

public record RSAKey(
        BigInteger key,
        BigInteger n
) {
}
