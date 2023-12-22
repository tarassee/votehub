package com.tarasiuk.votehub.withoutconfirmationprotocol.facility;

import com.tarasiuk.votehub.twocommissionsprotocol.util.ElGamalUtil;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPrivateKey;
import com.tarasiuk.votehub.twocommissionsprotocol.util.data.elGamal.ElGamalPublicKey;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.BBSCipherKeys;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.ElectionToken;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.EncryptedMessage;
import com.tarasiuk.votehub.withoutconfirmationprotocol.bbs.BBSCipher;
import com.tarasiuk.votehub.withoutconfirmationprotocol.util.BBSCipherUtil;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElectionCommission {
    private final Map<BigInteger, ElGamalPrivateKey> voterIdToPrivateKeyDb = new HashMap<>();
    private Map<String, Integer> candidatesDb;
    private BBSCipher bbsCipher;
    private BBSCipherKeys bbsCipherKeys;

    public ElectionCommission(Collection<String> candidatesList) {
        this.candidatesDb = candidatesList.stream()
                .collect(Collectors.toMap(candidate -> candidate, candidate -> 0));
    }

    public Collection<ElectionToken> receiveVotersIdsAndGenerateTokens(Collection<BigInteger> votersIds) {
        // save voters' private keys & generate tokens
        List<ElectionToken> electionTokens = votersIds.stream()
                .map(voterId -> {
                    var elGamalKeys = ElGamalUtil.generateKeys();
                    saveVoterIdAndPrivateKey(voterId, elGamalKeys.privateKey());
                    return generateToken(voterId, elGamalKeys.publicKey());
                }).toList();

        // keys for ballot encryption/decryption
        BigInteger p = BigInteger.valueOf(834567834563L);
        BigInteger q = BigInteger.valueOf(2389473831923L);
        SecureRandom rand = new SecureRandom();
        BigInteger seed = new BigInteger(Integer.parseInt("482723"), rand);
        BigInteger x0 = seed.modPow(BigInteger.valueOf(2), p.multiply(q));
        this.bbsCipherKeys = new BBSCipherKeys(p, q, seed, x0);
        this.bbsCipher = new BBSCipher(p, q, seed);

        return electionTokens;
    }

    private ElectionToken generateToken(BigInteger voterId, ElGamalPublicKey publicKey) {
        return new ElectionToken(voterId, publicKey);
    }

    private void saveVoterIdAndPrivateKey(BigInteger voterId, ElGamalPrivateKey elGamalPrivateKey) {
        voterIdToPrivateKeyDb.put(voterId, elGamalPrivateKey);
    }

    public BBSCipherKeys getBbsCipherKeys() {
        return bbsCipherKeys;
    }

    public void processVoteMessage(EncryptedMessage encryptedMessage) {
        var encryptedBallotMessage = encryptedMessage.encryptedBallotMessage();
        var voterId = encryptedMessage.voterId();
        var decryptedBallotMessage =
                BBSCipherUtil.decryptMessage(encryptedBallotMessage, bbsCipherKeys.p(), bbsCipherKeys.q(), bbsCipherKeys.seed());

        if (!voterIdToPrivateKeyDb.containsKey(voterId)) {
            System.out.println("Error! Not valid voterId!");
            return;
        }

        var privateKey = voterIdToPrivateKeyDb.get(voterId);
        var candidateValue = ElGamalUtil.decrypt(decryptedBallotMessage, privateKey);

        if (!validateCandidateValue(candidateValue)) {
            System.out.println("Error! Not valid candidate value!");
            return;
        }

        candidatesDb.put(candidateValue, candidatesDb.get(candidateValue) + 1);

        // removing voted voterId
        voterIdToPrivateKeyDb.remove(voterId);
    }

    public void showVotingResults() {
        System.out.println("\n===ELECTION RESULTS===");
        candidatesDb.forEach((key, value) -> System.out.printf("Candidate '%s' has '%d' votes.%n", key, value));
    }

    private boolean validateCandidateValue(String candidateValue) {
        return candidatesDb.containsKey(candidateValue);
    }

}
