package com.tarasiuk.votehub.service;

public interface VoterEligibilityService {

    boolean isEligibleToVote(Integer passportId);

}
