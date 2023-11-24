package com.tarasiuk.votehub.processor.protocol.blind.impl;

import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessage;
import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessageSet;
import com.tarasiuk.votehub.exception.ValidationProcessingException;
import com.tarasiuk.votehub.processor.protocol.blind.AbstractBlindProtocolProcessor;
import com.tarasiuk.votehub.processor.protocol.blind.AbstractMessageSetsValidationProcessor;
import com.tarasiuk.votehub.service.CandidateStatisticsService;
import com.tarasiuk.votehub.service.RSAKeyService;
import com.tarasiuk.votehub.service.VoterEligibilityService;
import com.tarasiuk.votehub.service.VoterService;
import com.tarasiuk.votehub.util.*;
import com.tarasiuk.votehub.util.data.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class DefaultBlindProtocolProcessor extends AbstractBlindProtocolProcessor {

    @Value("${protocol.blind.rsa.message-sets-quantity:10}")
    private Integer messageSetsQuantity;
    private final VoterService voterService;
    private final VoterEligibilityService voterEligibilityService;
    private final RSAKeyService rsaKeyService;
    private final CandidateStatisticsService candidateStatisticsService;
    private final AbstractMessageSetsValidationProcessor defaultMessageSetsValidationProcessor;

    @Override
    protected void validatePassportId(Integer passportId) {
        validate(passportId, voterService::existsByPassportId, String.format("Error! Passport ID '%s' is not found!", passportId));
    }

    @Override
    protected void validateEligibility(Integer passportId) {
        validate(passportId, voterEligibilityService::isEligibleToVote, String.format("Error! The person with passport ID '%s' is not eligible to vote!", passportId));
    }

    @Override
    protected void validateHasSignatureMessageSet(Integer passportId) {
        validate(passportId, Predicate.not(voterService::hasSignatureMessageSet), String.format("Error! The person with passport ID '%s' has signature message set!", passportId));
    }

    @Override
    protected void validateMessageSetsSize(List<BlindProtocolMaskedMessageSet> messageSets) {
        validate(messageSets.size() == messageSetsQuantity, bool -> bool, String.format("Error! '%s' message sets should be received instead of '%s'!", messageSetsQuantity, messageSets.size()));
    }

    @Override
    protected BlindProtocolMaskedMessageSet selectMessageSetAndValidateRemaining(Integer passportId, BigInteger maskingKey, List<BlindProtocolMaskedMessageSet> maskedMessageSets) {
        // select random message set
        BlindProtocolMaskedMessageSet selectedMessageSet = RandomSelectionUtil.selectOneRandomly(maskedMessageSets);
        maskedMessageSets.remove(selectedMessageSet);

        // unmask remaining
        List<BlindProtocolMessageSet> unMaskedMessageSets;
        try {
            RSAKey privateKey = rsaKeyService.getPrivateKey();
            unMaskedMessageSets = UnMaskingUtil.unMaskMessageSets(maskedMessageSets, maskingKey, privateKey);
        } catch (Exception e) {
            throw new ValidationProcessingException("Error! Invalid masked message set!");
        }

        // validate unmasked
        defaultMessageSetsValidationProcessor.process(Map.entry(passportId, unMaskedMessageSets));

        return selectedMessageSet;
    }

    @Override
    protected BlindProtocolSignedMaskedMessageSet signSelectedMaskedMessageSet(BlindProtocolMaskedMessageSet selectedMaskedMessageSet, Integer passportId) {
        RSAKey privateKey = rsaKeyService.getPrivateKey();
        List<BlindProtocolMaskedMessage> maskedMessageSet = selectedMaskedMessageSet.maskedMessageSet();

        // sign selected masked message set
        List<BlindProtocolSignedMaskedMessage> signedMaskedMessageSet = maskedMessageSet.stream()
                .map(BlindProtocolMaskedMessage::maskedMessage)
                .map(maskedMessage -> BlindSignatureUtil.sign(maskedMessage, privateKey))
                .map(BlindProtocolSignedMaskedMessage::new)
                .toList();

        // mark voter with send passportId
        voterService.recordHasSignatureMessageSetVoter(passportId);

        return new BlindProtocolSignedMaskedMessageSet(signedMaskedMessageSet);
    }

    @Override
    protected BlindProtocolMessage decryptAndUnSignMessage(BigInteger encryptedSignatureMessage) {
        try {
            RSAKey privateKey = rsaKeyService.getPrivateKey();
            BigInteger decryptedSignatureMessage = BlindSignatureUtil.sign(encryptedSignatureMessage, privateKey);
            BigInteger unSignedAndUnMaskedMessage = BlindSignatureUtil.unSignUnMasked(privateKey, decryptedSignatureMessage);
            String serializedMessage = StringBigIntegerUtil.bigIntegerToString(unSignedAndUnMaskedMessage);
            BlindProtocolMessage blindProtocolMessage = JsonSerializer.deserialize(serializedMessage, BlindProtocolMessage.class);
            return blindProtocolMessage;
        } catch (Exception e) {
            throw new ValidationProcessingException("Error! Signature is not valid!");
        }
    }

    @Override
    protected void validateCandidateValue(String candidateValue) {
        validate(candidateValue, candidateStatisticsService::existsByName, String.format("Error! Candidate '%s' does not exist!", candidateValue));
    }

    @Override
    protected void validateHasVoted(Integer passportId) {
        validate(passportId, Predicate.not(voterService::hasVoted), String.format("Error! The person with passport ID '%s' has already voted!", passportId));
    }

    @Transactional
    @Override
    public void processVote(String candidateValue, Integer passportId) {
        candidateStatisticsService.incrementVoteCount(candidateValue);
        voterService.recordVotedVoter(passportId);
        voterService.recordVoterCandidateValue(passportId, candidateValue);
    }

    private <T> void validate(T value, Predicate<T> predicate, String errorMsg) {
        if (!predicate.test(value)) {
            throw new ValidationProcessingException(errorMsg);
        }
    }

}
