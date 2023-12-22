package com.tarasiuk.votehub.withoutconfirmationprotocol.bbs;

import java.math.BigInteger;

class BBSGenerator {
    private BigInteger state, n;

    public BBSGenerator(BigInteger p, BigInteger q, BigInteger seed) {
        this.n = p.multiply(q);
        this.state = seed.mod(n);
    }

    public BigInteger generate() {
        state = state.modPow(BigInteger.valueOf(2), n);
        return state;
    }
}