package com.tarasiuk.votehub.twocommissionsprotocol.facility;

import com.tarasiuk.votehub.twocommissionsprotocol.data.VoteMessage;
import com.tarasiuk.votehub.twocommissionsprotocol.util.DSASignatureUtil;
import com.tarasiuk.votehub.twocommissionsprotocol.util.ElGamalUtil;
import com.tarasiuk.votehub.util.JsonSerializer;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.dsa.DSASignatureResult;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPrivateKey;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.DSAPublicKey;
import java.util.*;

public class ElectionCommission {
    private static final Integer VOTE_ELIGIBILITY_AGE = 18;
    private Set<UUID> validRegistrationNumbers;
    private final Map<String, Integer> ballotTally;
    private final Map<Integer, String> voterBallotMap;
    private final ElGamalPrivateKey elGamalPrivateKey;

    public ElectionCommission(ElGamalPrivateKey elGamalPrivateKey) {
        this.elGamalPrivateKey = elGamalPrivateKey;
        this.ballotTally = new HashMap<>();
        this.voterBallotMap = new HashMap<>();
    }

    @SneakyThrows
    public void submitEncryptedVoteMessage(BigInteger[] encryptedVoteMessage, DSAPublicKey voterDSAPublicKey) {
        // Encrypt vote message
        String decryptedVoteMessage = ElGamalUtil.decrypt(encryptedVoteMessage, elGamalPrivateKey);
        VoteMessage voteMessage = JsonSerializer.deserialize(decryptedVoteMessage, VoteMessage.class);

        // Get data
        Integer voterId = voteMessage.getVoterId();
        Integer voterAge = voteMessage.getAge();
        UUID registrationNumber = voteMessage.getRegistrationNumber();
        DSASignatureResult signatureResult = voteMessage.getSignatureResult();
        String candidateValue = voteMessage.getCandidateValue();

        // Validate registration number
        boolean isValidRegistrationNumber = validateRegistrationNumber(registrationNumber);
        if (!isValidRegistrationNumber) {
            printErrorMessage("Voter with id '%d' sent invalid registration id!%n", voterId);
            return;
        }

        // Validate if voter has already voted
        boolean hasNotVoted = validateHasVoted(voterId);
        if (!hasNotVoted) {
            printErrorMessage("Voter with id '%d' has already voted!%n", voterId);
            return;
        }

        // Validate voter eligibility
        boolean isEligibleToVote = validateVoterAge(voterAge);
        if (!isEligibleToVote) {
            printErrorMessage("Voter with id '%d' is not eligible to vote!%n", voterId);
            return;
        }

        // Validate signature
        boolean isSignatureValid = validateSignature(signatureResult, candidateValue, voterDSAPublicKey);
        if (!isSignatureValid) {
            printErrorMessage("Voter with id '%d' sent invalid signature!%n", voterId);
            return;
        }

        // Validate candidate value
        boolean isCandidateValueValid = validateCandidateValue(candidateValue);
        if (!isCandidateValueValid) {
            printErrorMessage("Voter with id '%d' sent invalid candidate value!%n", voterId);
            return;
        }

        submitVote(voterId, candidateValue, registrationNumber);
    }

    private void printErrorMessage(String errorMessage, int voterId) {
        System.out.printf("Error during vote processing! " + errorMessage, voterId);
    }

    private boolean validateHasVoted(Integer voterId) {
        return !voterBallotMap.containsKey(voterId);
    }

    private boolean validateVoterAge(Integer voterAge) {
        return voterAge >= VOTE_ELIGIBILITY_AGE;
    }

    private boolean validateCandidateValue(String candidateValue) {
        return ballotTally.containsKey(candidateValue);
    }

    private boolean validateRegistrationNumber(UUID registrationNumber) {
        return validRegistrationNumbers.contains(registrationNumber);
    }

    private static boolean validateSignature(DSASignatureResult signatureResult, String candidateValue, DSAPublicKey voterDSAPublicKey) {
        return DSASignatureUtil.verify(candidateValue.getBytes(StandardCharsets.UTF_8), signatureResult, voterDSAPublicKey);
    }

    private void submitVote(Integer voterId, String candidateValue, UUID registrationNumber) {
        validRegistrationNumbers.remove(registrationNumber);
        voterBallotMap.put(voterId, candidateValue);
        ballotTally.put(candidateValue, ballotTally.get(candidateValue) + 1);
    }

    public void initializeCandidates(List<String> candidates) {
        candidates.forEach(candidate -> ballotTally.put(candidate, 0));
    }

    public void setValidRegistrationNumbers(Set<UUID> validRegistrationNumbers) {
        this.validRegistrationNumbers = validRegistrationNumbers;
    }

    public Map<String, Integer> getBallotTally() {
        return ballotTally;
    }

    public Map<Integer, String> getVoterBallotMap() {
        return voterBallotMap;
    }

}
