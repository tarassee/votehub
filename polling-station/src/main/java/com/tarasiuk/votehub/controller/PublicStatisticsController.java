package com.tarasiuk.votehub.controller;

import com.tarasiuk.votehub.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/publicStatistics")
@RestController
public class PublicStatisticsController {

    private final VoterService voterService;

    @GetMapping("/hasVoted/{passportId}")
    public ResponseEntity<Boolean> hasVoted(@PathVariable Integer passportId) {
        return ResponseEntity.ok(voterService.hasVoted(passportId));
    }

}
