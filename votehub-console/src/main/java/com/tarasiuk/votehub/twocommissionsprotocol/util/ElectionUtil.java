package com.tarasiuk.votehub.twocommissionsprotocol.util;

import com.tarasiuk.votehub.twocommissionsprotocol.data.VoteMessage;
import com.tarasiuk.votehub.twocommissionsprotocol.data.Voter;
import com.tarasiuk.votehub.twocommissionsprotocol.facility.ElectionCommission;
import com.tarasiuk.votehub.twocommissionsprotocol.facility.RegistrationBureau;
import com.tarasiuk.votehub.util.JsonSerializer;
import com.tarasiuk.votehub.util.RandomSelectionUtil;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.dsa.DSASignatureResult;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPublicKey;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.List;

public final class ElectionUtil {

    private ElectionUtil() {
    }

    public static void createAndSendMessage(ElGamalPublicKey elGamalPublicKey, List<String> candidates, ElectionCommission electionCommission, Voter voter) {
        // Get data
        var registrationNumber = voter.getRegistrationNumber();
        var voterId = voter.getVoterId();
        var voterAge = voter.getAge();
        DSAPrivateKey voterDSAPrivateKey = (DSAPrivateKey) voter.getKeyPair().getPrivate();
        DSAPublicKey voterDSAPublicKey = (DSAPublicKey) voter.getKeyPair().getPublic();
        var candidateValue = RandomSelectionUtil.selectOneRandomly(candidates);

        // Create signature
        var candidateValueDecimal = candidateValue.getBytes(StandardCharsets.UTF_8);
        DSASignatureResult signatureResult = DSASignatureUtil.sign(candidateValueDecimal, voterDSAPrivateKey);

        // Create message
        VoteMessage voteMessage = new VoteMessage(voterId, registrationNumber, signatureResult, candidateValue, voterAge);

        // Encrypt vote message
        String voteMessageString = JsonSerializer.serialize(voteMessage);
        BigInteger[] encryptVoteMessage = ElGamalUtil.encrypt(voteMessageString, elGamalPublicKey);

        // Send message
        electionCommission.submitEncryptedVoteMessage(encryptVoteMessage, voterDSAPublicKey);
    }

    public static void registerVoter(RegistrationBureau registrationBureau, Voter voter) {
        Integer voterId = voter.getVoterId();
        var registrationNumber = registrationBureau.registerVoter(voterId);
        voter.setRegistrationNumber(registrationNumber);
        System.out.printf("Voter with id '%d' received registration number '%s'\n", voterId, registrationNumber);
    }

}
