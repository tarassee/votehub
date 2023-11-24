package com.tarasiuk.votehub.processor.protocol.blind;

import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessage;
import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolPreVoteMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolVoteMessage;
import com.tarasiuk.votehub.processor.Processor;

import java.math.BigInteger;
import java.util.List;

/**
 * This abstract class contains vote processing based on bling signature protocol.
 */
public abstract class AbstractBlindProtocolProcessor implements Processor<BlindProtocolVoteMessage> {

    public BlindProtocolSignedMaskedMessageSet preProcessVote(BlindProtocolPreVoteMessage blindProtocolPreVoteMessage) {
        Integer passportId = blindProtocolPreVoteMessage.passportId();
        BigInteger maskingKey = blindProtocolPreVoteMessage.maskingKey();
        List<BlindProtocolMaskedMessageSet> messageSets = blindProtocolPreVoteMessage.maskedMessageSets();

        validatePassportId(passportId);
        validateEligibility(passportId);
        validateHasSignatureMessageSet(passportId);
        validateMessageSetsSize(messageSets);

        BlindProtocolMaskedMessageSet selectedMaskedMessageSet = selectMessageSetAndValidateRemaining(passportId, maskingKey, messageSets);
        BlindProtocolSignedMaskedMessageSet blindProtocolSignedMaskedMessageSet = signSelectedMaskedMessageSet(selectedMaskedMessageSet, passportId);

        return blindProtocolSignedMaskedMessageSet;
    }

    protected abstract void validatePassportId(Integer passportId);

    protected abstract void validateEligibility(Integer passportId);

    protected abstract void validateHasSignatureMessageSet(Integer passportId);

    protected abstract void validateMessageSetsSize(List<BlindProtocolMaskedMessageSet> messageSets);

    protected abstract BlindProtocolMaskedMessageSet selectMessageSetAndValidateRemaining(Integer passportId, BigInteger maskingKey, List<BlindProtocolMaskedMessageSet> messageSets);

    protected abstract BlindProtocolSignedMaskedMessageSet signSelectedMaskedMessageSet(BlindProtocolMaskedMessageSet maskedMessageSet, Integer passportId);

    @Override
    public void process(BlindProtocolVoteMessage blindProtocolVoteMessage) {
        BigInteger encryptedSignatureMessage = blindProtocolVoteMessage.encryptedSignatureMessage();

        BlindProtocolMessage decryptedAndUnSignedMessage = decryptAndUnSignMessage(encryptedSignatureMessage);
        Integer passportId = decryptedAndUnSignedMessage.passportId();
        String candidateValue = decryptedAndUnSignedMessage.candidateValue();

        validatePassportId(passportId);
        validateEligibility(passportId);
        validateCandidateValue(candidateValue);
        validateHasVoted(passportId);

        processVote(candidateValue, passportId);
    }

    protected abstract BlindProtocolMessage decryptAndUnSignMessage(BigInteger encryptedSignatureMessage);

    protected abstract void validateCandidateValue(String candidateValue);

    protected abstract void validateHasVoted(Integer passportId);

    protected abstract void processVote(String candidateValue, Integer passportId);

}
