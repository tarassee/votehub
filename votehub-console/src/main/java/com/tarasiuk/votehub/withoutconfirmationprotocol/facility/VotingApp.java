package com.tarasiuk.votehub.withoutconfirmationprotocol.facility;


import com.tarasiuk.votehub.twocommissionsprotocol.util.ElGamalUtil;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.BBSCipherKeys;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.ElectionToken;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.EncryptedMessage;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.Voter;
import com.tarasiuk.votehub.withoutconfirmationprotocol.util.BBSCipherUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class VotingApp {

    private final Map<Voter, ElectionToken> votingAppContext = new HashMap<>();

    public boolean authenticateVoterThroughRegistrationBureau(String userName, String password, RegistrationBureau registrationBureau) {
        return registrationBureau.validateUserNameAndPassword(userName, password);
    }

    public void activateVoterWithToken(Voter voter, ElectionToken electionToken) {
        votingAppContext.put(voter, electionToken);
    }

    public void startVotingProcess(Voter voter, String candidate, ElectionCommission electionCommission) {
        // flow validation
        if (!votingAppContext.containsKey(voter)) {
            System.out.printf("Voter was not activated! Voter: %s %s\n", voter.getFirstName(), voter.getLastName());
        }

        // data preparation
        var electionToken = voter.getCredentials().electionToken();
        var elGamalPublicKey = electionToken.getElGamalPublicKey();
        var voterId = electionToken.getVoterId();

        // ballot message encryption
        var encryptedBallot = ElGamalUtil.encrypt(candidate, elGamalPublicKey);
        var cipherKeys = electionCommission.getBbsCipherKeys();
        var encryptedBallotMessage = getEncryptedBallotMessage(voterId, encryptedBallot, cipherKeys);
        var encryptedMessage = new EncryptedMessage(encryptedBallotMessage, voterId);
        electionCommission.processVoteMessage(encryptedMessage);
    }

    private static BigInteger getEncryptedBallotMessage(BigInteger voterId, BigInteger[] encryptedBallot, BBSCipherKeys cipherKeys) {
        return BBSCipherUtil.encryptMessage(encryptedBallot, voterId, cipherKeys.x0(), cipherKeys.p(), cipherKeys.q(), cipherKeys.seed());
    }
}
