package com.tarasiuk.votehub.controller;

import com.tarasiuk.votehub.service.CandidateStatisticsService;
import com.tarasiuk.votehub.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@RequestMapping("/publicStatistics")
@RestController
public class PublicStatisticsController {

    private final VoterService voterService;
    private final CandidateStatisticsService candidateStatisticsService;

    @GetMapping("/hasVoted/{passportId}")
    public ResponseEntity<Boolean> hasVoted(@PathVariable Integer passportId) {
        return ResponseEntity.ok(voterService.hasVoted(passportId));
    }

    @GetMapping("/getAllCandidates")
    public ResponseEntity<List<String>> getAllCandidates() {
        return ResponseEntity.ok(candidateStatisticsService.getAllCandidatesNames());
    }

    @GetMapping("/getElectionResults")
    public ResponseEntity<List<String>> getElectionResults() {
        return ResponseEntity.ok(getElectionResultsInternal());
    }

    private List<String> getElectionResultsInternal() {
        return candidateStatisticsService.getAll().stream()
                .map(val -> String.format("Candidate with name '%s' has '%d' votes", val.getName(), val.getCount()))
                .toList();
    }

    @GetMapping("/getVotersStatistic")
    public ResponseEntity<List<String>> getVotersStatistic() {
        return ResponseEntity.ok(getVotersStatisticInternal());
    }

    private List<String> getVotersStatisticInternal() {
        return voterService.getAllVoters().stream()
//                .filter(voter -> nonNull(voter.getCandidateValue()))
                .map(val -> String.format("Voter with passport '%s' voted for: '%s'", val.getPassportId(), val.getCandidateValue()))
                .toList();
    }

}
