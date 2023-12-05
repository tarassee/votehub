package com.tarasiuk.votehub.twocommissionsprotocol;

import com.tarasiuk.votehub.twocommissionsprotocol.data.Voter;
import com.tarasiuk.votehub.twocommissionsprotocol.facility.ElectionCommission;
import com.tarasiuk.votehub.twocommissionsprotocol.facility.RegistrationBureau;
import com.tarasiuk.votehub.twocommissionsprotocol.util.ElGamalUtil;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalKeys;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPrivateKey;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPublicKey;
import lombok.SneakyThrows;

import java.security.KeyPairGenerator;
import java.util.List;
import java.util.Objects;

import static com.tarasiuk.votehub.twocommissionsprotocol.util.ElectionUtil.createAndSendMessage;
import static com.tarasiuk.votehub.twocommissionsprotocol.util.ElectionUtil.registerVoter;
import static java.util.List.of;

public class Main {

    public static void main(String[] args) {
        if (Objects.equals(args[0], "green")) {
            greenScenario();
        } else if (Objects.equals(args[0], "red")) {
            redScenario();
        }
    }

    @SneakyThrows
    private static void greenScenario() {
        // Initialize ElGamal key pair
        ElGamalKeys elGamalKeys = ElGamalUtil.generateKeys();
        ElGamalPublicKey elGamalPublicKey = elGamalKeys.publicKey();
        ElGamalPrivateKey elGamalPrivateKey = elGamalKeys.privateKey();

        // Initialize Voters
        var voters = List.of(
                new Voter(101, "Tom", "Black", 32),
                new Voter(102, "John", "Lo", 34),
                new Voter(103, "Lia", "Lee", 23),
                new Voter(104, "Kim", "Chan", 54)
        );

        // Initialize DSA key pair for voters
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        voters.forEach(voter -> voter.setKeyPair(keyPairGenerator.generateKeyPair()));

        // Initialize Candidates
        var candidates = List.of(
                "Zaroshenko",
                "Pelensky"
        );

        // Initialize Election Commission
        var electionCommission = new ElectionCommission(elGamalPrivateKey);
        electionCommission.initializeCandidates(candidates);

        // Initialize Registration Bureau
        var registrationBureau = new RegistrationBureau();

        // ************************************
        // SCENARIOS
        // ************************************

        // 1. REGISTRATION BUREAU: Simulate voter registration - Voter ask for registration number
        voters.forEach(voter -> registerVoter(registrationBureau, voter));

        // 2. REGISTRATION BUREAU: Send registered voters to ELECTION COMMISSION
        electionCommission.setValidRegistrationNumbers(registrationBureau.getRegistrationNumbersSet());

        // 3. VOTER: Creates And Sends message with identification number, registration number, signature(candidate value)
        voters.forEach(voter -> createAndSendMessage(elGamalPublicKey, candidates, electionCommission, voter));

        // 5. ELECTION COMMISSION: Show election results
        System.out.println("\n===ELECTION RESULTS===");
        electionCommission.getBallotTally().forEach((key, value) -> System.out.printf("Candidate '%s' has '%d' votes.%n", key, value));

        // 6. ELECTION COMMISSION: Show voters identification ids and candidate values
        System.out.println("\n===CANDIDATE STATISTICS===");
        electionCommission.getVoterBallotMap().forEach((key, value) -> System.out.printf("Voter with id '%d' voted for '%s' candidate.%n", key, value));
    }

    @SneakyThrows
    private static void redScenario() {
        // Initialize ElGamal key pair
        ElGamalKeys elGamalKeys = ElGamalUtil.generateKeys();
        ElGamalPublicKey elGamalPublicKey = elGamalKeys.publicKey();
        ElGamalPrivateKey elGamalPrivateKey = elGamalKeys.privateKey();

        // Initialize Voters
        var voters = List.of(
                new Voter(101, "Tom", "Black", 32), // for imposter case
                new Voter(101, "TomImposter", "BlackImposter", 32), // for imposter & invalid registration id cases
                new Voter(102, "John", "Lo", 9), // for is not eligible to vote case
                new Voter(103, "Lia", "Lee", 23), // for already voted case (invalid registration id case)
                new Voter(104, "Kim", "Chan", 54) // for invalid candidate value vase
        );

        // Initialize DSA key pair for voters
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        voters.forEach(voter -> voter.setKeyPair(keyPairGenerator.generateKeyPair()));

        // Initialize Candidates
        var candidates = List.of(
                "Zaroshenko",
                "Pelensky"
        );

        // Initialize Election Commission
        var electionCommission = new ElectionCommission(elGamalPrivateKey);
        electionCommission.initializeCandidates(candidates);

        // Initialize Registration Bureau
        var registrationBureau = new RegistrationBureau();

        // ************************************
        // SCENARIOS
        // ************************************

        // 1. REGISTRATION BUREAU: Simulate voter registration - Voter ask for registration number
        voters.forEach(voter -> registerVoter(registrationBureau, voter));

        // 2. REGISTRATION BUREAU: Send registered voters to ELECTION COMMISSION
        electionCommission.setValidRegistrationNumbers(registrationBureau.getRegistrationNumbersSet());

        // 3. VOTER: Creates And Sends message with identification number, registration number, signature(candidate value)
        of(voters.get(0), voters.get(1), voters.get(2), voters.get(3))
                .forEach(voter -> createAndSendMessage(elGamalPublicKey, candidates, electionCommission, voter));

        // EXCEPTIONAL CASES
        // re-voting
        var voterHasAlreadyVoted = voters.get(3);
        createAndSendMessage(elGamalPublicKey, candidates, electionCommission, voterHasAlreadyVoted);
        // sending invalid candidate value
        var voterWithInvalidCandidateValue = voters.get(4);
        createAndSendMessage(elGamalPublicKey, List.of("InvalidCandidateName"), electionCommission, voterWithInvalidCandidateValue);

        // 5. ELECTION COMMISSION: Show election results
        System.out.println("\n===ELECTION RESULTS===");
        electionCommission.getBallotTally().forEach((key, value) -> System.out.printf("Candidate '%s' has '%d' votes.%n", key, value));

        // 6. ELECTION COMMISSION: Show voters identification ids and candidate values
        System.out.println("\n===CANDIDATE STATISTICS===");
        electionCommission.getVoterBallotMap().forEach((key, value) -> System.out.printf("Voter with id '%d' voted for '%s' candidate.%n", key, value));
    }

}