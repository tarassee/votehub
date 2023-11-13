package com.tarasiuk.votehub.controller.protocol.blind;

import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolPreVoteMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolVoteMessage;
import com.tarasiuk.votehub.processor.protocol.blind.AbstractBlindProtocolProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/blindProtocol")
@RestController
public class BlindProtocolVoteController {

    private final AbstractBlindProtocolProcessor defaultAbstractBlindProtocolVoteProcessor;

    @PostMapping("/preVote")
    public ResponseEntity<BlindProtocolSignedMaskedMessageSet> preVote(@RequestBody BlindProtocolPreVoteMessage blindProtocolPreVoteMessage) {

        BlindProtocolSignedMaskedMessageSet signedMaskedMessageSet
                = defaultAbstractBlindProtocolVoteProcessor.preProcessVote(blindProtocolPreVoteMessage);

        return ResponseEntity.ok(signedMaskedMessageSet);
    }

    @PostMapping("/vote")
    public ResponseEntity<Void> voteForCandidate(@RequestBody BlindProtocolVoteMessage blindProtocolVoteMessage) {

        defaultAbstractBlindProtocolVoteProcessor.process(blindProtocolVoteMessage);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
