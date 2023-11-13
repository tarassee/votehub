package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.model.VoterModel;
import com.tarasiuk.votehub.util.data.RSAKey;

import java.util.List;

public interface VoterService {

    void recordVotedVoter(Integer passportId);

    void recordVoterCandidateValue(Integer passportId, String candidateValue);

    void recordHasSignatureMessageSetVoter(Integer passportId);

    boolean existsByPassportId(Integer passportId);

    RSAKey getPublicKeyByPassportId(Integer passportId);

    boolean hasVoted(Integer passportId);

    boolean hasSignatureMessageSet(Integer passportId);

    List<VoterModel> getAllVoters();
}
