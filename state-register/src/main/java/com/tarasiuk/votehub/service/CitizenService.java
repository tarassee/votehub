package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.util.data.RSAKey;

import java.math.BigInteger;

public interface CitizenService {

    boolean existsByPassportId(Integer passportId);

    CitizenModel getByPassportId(Integer passportId);

    void deletePublicKeyFor(CitizenModel citizenModel);

    CitizenModel savePublicKeyFor(BigInteger publicExponent, BigInteger modulus, Integer passportId);

}
