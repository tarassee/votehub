package com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal;

import java.math.BigInteger;

public record ElGamalPublicKey(
        BigInteger y,
        ElGamalParameters parameters
) {
}
