package com.tarasiuk.votehub.service;

public interface CandidateStatisticsService {

    boolean existsByName(String name);

    void incrementVoteCount(String name);

}
