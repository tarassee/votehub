package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.repository.CandidateStatisticRepository;
import com.tarasiuk.votehub.service.CandidateStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultCandidateStatisticsService implements CandidateStatisticsService {

    private final CandidateStatisticRepository candidateStatisticRepository;

    @Override
    public boolean existsByName(String name) {
        return candidateStatisticRepository.existsByName(name);
    }

    @Override
    public void incrementVoteCount(String name) {
        var candidate = candidateStatisticRepository.getByName(name);
        int count = candidate.getCount() + 1;
        candidate.setCount(count);
        candidateStatisticRepository.save(candidate);
    }

}
