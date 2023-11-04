package com.tarasiuk.votehub.controller.protocol.simple;

import com.tarasiuk.votehub.processor.VoteProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/simpleProtocol")
@RestController
public class SimpleProtocolVoteController {

    private final VoteProcessor defaultSimpleProtocolVoteProcessor;

    @PostMapping("/vote")
    public ResponseEntity<Void> voteForCandidate(@RequestBody String encryptedMessage) {

        defaultSimpleProtocolVoteProcessor.processVote(encryptedMessage);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
