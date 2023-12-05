package com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal;

import java.math.BigInteger;

public record ElGamalPrivateKey(
        BigInteger x,
        ElGamalParameters parameters
) {
}
