package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.model.CandidateStatisticModel;

import java.util.List;

public interface CandidateStatisticsService {

    List<CandidateStatisticModel> getAll();

    boolean existsByName(String name);

    void incrementVoteCount(String name);

    List<String> getAllCandidatesNames();
}
