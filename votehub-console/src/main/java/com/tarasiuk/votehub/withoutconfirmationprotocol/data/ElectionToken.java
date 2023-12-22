package com.tarasiuk.votehub.withoutconfirmationprotocol.data;

import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPublicKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ElectionToken {
    private BigInteger voterId;
    private final ElGamalPublicKey elGamalPublicKey;
}
