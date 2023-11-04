package com.tarasiuk.votehub.processor.protocol.simple;

import com.tarasiuk.votehub.data.protocol.simple.SimpleProtocolVoteMessage;
import com.tarasiuk.votehub.processor.VoteProcessor;
import com.tarasiuk.votehub.util.GammaUtil;
import com.tarasiuk.votehub.util.JsonSerializer;

import java.math.BigInteger;

/**
 * This abstract class contains vote processing based on simple protocol.
 */
public abstract class AbstractSimpleProtocolVoteProcessor implements VoteProcessor {

    @Override
    public final void processVote(String encryptedMessage) {
        String decryptedMessage = GammaUtil.decrypt(encryptedMessage);
        SimpleProtocolVoteMessage simpleProtocolVoteData = JsonSerializer.deserialize(decryptedMessage, SimpleProtocolVoteMessage.class);

        Integer passportId = simpleProtocolVoteData.passportId();
        String candidateValue = simpleProtocolVoteData.vote();
        BigInteger signature = simpleProtocolVoteData.signature();

        validatePassportId(passportId);
        validateCandidateValue(candidateValue);
        validateEligibility(passportId);
        validateSignature(signature, candidateValue, passportId);
        validateHasVoted(passportId);

        processVote(candidateValue, passportId);
    }

    // todo: try to refactor, consider solution with composite predicates
    protected abstract void validatePassportId(Integer passportId);

    protected abstract void validateCandidateValue(String candidateValue);

    protected abstract void validateEligibility(Integer passportId);

    protected abstract void validateHasVoted(Integer passportId);

    protected abstract void validateSignature(BigInteger signature, String candidateValue, Integer passportId);

    protected abstract void processVote(String candidateValue, Integer passportId);

}
