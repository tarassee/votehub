package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.util.data.RSAKeyPair;

import java.math.BigInteger;

public interface KeyService {

    RSAKeyPair generateAndRegister(Integer passportId);

    BigInteger getMaskingKeyFor(BigInteger phi);

}
