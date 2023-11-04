package com.tarasiuk.votehub.processor.protocol.simple.impl;

import com.tarasiuk.votehub.exception.VoteProcessingException;
import com.tarasiuk.votehub.processor.protocol.simple.AbstractSimpleProtocolVoteProcessor;
import com.tarasiuk.votehub.service.CandidateStatisticsService;
import com.tarasiuk.votehub.service.VoterEligibilityService;
import com.tarasiuk.votehub.service.VoterService;
import com.tarasiuk.votehub.util.HashUtil;
import com.tarasiuk.votehub.util.data.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Service
public class DefaultAbstractSimpleProtocolVoteProcessor extends AbstractSimpleProtocolVoteProcessor {

    private final VoterService voterService;
    private final CandidateStatisticsService candidateStatisticsService;
    private final VoterEligibilityService voterEligibilityService;

    @Override
    protected void validatePassportId(Integer passportId) {
        validate(passportId, voterService::existsByPassportId, String.format("Error! Passport ID '%s' is not found!", passportId));
    }

    @Override
    protected void validateCandidateValue(String candidateValue) {
        validate(candidateValue, candidateStatisticsService::existsByName, String.format("Error! Candidate '%s' does not exist!", candidateValue));
    }

    @Override
    protected void validateEligibility(Integer passportId) {
        validate(passportId, voterEligibilityService::isEligibleToVote, String.format("Error! The person with passport ID '%s' is not eligible to vote!", passportId));
    }

    @Override
    protected void validateHasVoted(Integer passportId) {
        validate(passportId, Predicate.not(voterService::hasVoted), String.format("Error! The person with passport ID '%s' has already voted!", passportId));
    }

    @Override
    protected void validateSignature(BigInteger signature, String candidateValue, Integer passportId) {
        RSAKey publicKey = voterService.getPublicKeyByPassportId(passportId);

        // Отримуємо хеш з ЕЦП
        BigInteger signatureHash = signature.modPow(publicKey.key(), publicKey.n());
        // Хешуємо повідомлення
        BigInteger decryptedMessageHash = HashUtil.simplifiedQuadraticConvolutionHash(candidateValue, publicKey.n());
        // Порівнюємо два хеша
        boolean comparingResult = signatureHash.compareTo(decryptedMessageHash) == 0;

        validate(comparingResult, bool -> bool, "Error! The signature is not valid!");
    }

    private <T> void validate(T value, Predicate<T> predicate, String errorMsg) {
        if (!predicate.test(value)) {
            throw new VoteProcessingException(errorMsg);
        }
    }

    @Transactional
    @Override
    public void processVote(String candidateValue, Integer passportId) {
        candidateStatisticsService.incrementVoteCount(candidateValue);
        voterService.recordVoter(passportId);
    }

}
