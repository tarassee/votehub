package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.util.data.RSAKey;

public interface VoterService {

    void recordVoter(Integer passportId);

    boolean existsByPassportId(Integer passportId);

    RSAKey getPublicKeyByPassportId(Integer passportId);

    boolean hasVoted(Integer passportId);

}
