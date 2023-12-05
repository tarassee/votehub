package com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal;

import java.math.BigInteger;

public record ElGamalParameters(
        BigInteger p,
        BigInteger g
) {
}
