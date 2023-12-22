package com.tarasiuk.votehub.withoutconfirmationprotocol.facility;

import com.tarasiuk.votehub.withoutconfirmationprotocol.data.ElectionToken;
import com.tarasiuk.votehub.withoutconfirmationprotocol.data.Voter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

import static com.tarasiuk.votehub.withoutconfirmationprotocol.util.RandomUtil.generateRandomString;

public class RegistrationBureau {

    private static final SecureRandom RANDOM = new SecureRandom();
    private List<ElectionToken> votersTokensDb;
    private final Map<UUID, Voter> voterCredentialsDataDb = new HashMap<>();

    public RegistrationBureau() {

    }

    public Collection<BigInteger> generateIds(Integer numberOfVoters) {
        return Stream.generate(() -> new BigInteger(128, RANDOM))
                .limit(numberOfVoters)
                .toList();
    }

    public void receiveVotersTokens(Collection<ElectionToken> votersTokens) {
        votersTokensDb = new ArrayList<>(votersTokens);
    }

    public Voter registerAndUpdateVoter(Voter voter) {
        if (votersTokensDb.isEmpty()) {
            System.out.printf("Error! No tokens left for voters! Voter: %s %s\n", voter.getFirstName(), voter.getLastName());
            return null;
        }

        if (!voter.isEligibleToVote()) {
            System.out.printf("Error! Voter is not eligible to vote! Voter: %s %s\n", voter.getFirstName(), voter.getLastName());
            return null;
        }

        // Get the first token available and remove it from the database
        ElectionToken token = votersTokensDb.iterator().next();
        votersTokensDb.remove(token);

        // Generate username and password
        String username = generateRandomString();
        String password = generateRandomString();

        // Assign credentials to voter
        voter.setCredentials(new Voter.Credentials(username, password, token, UUID.randomUUID()));

        // Save voterData in db
        voterCredentialsDataDb.put(voter.getCredentials().tokenSerialNumber(), voter);

        return voter;
    }

    public boolean validateUserNameAndPassword(String userName, String password) {
        return voterCredentialsDataDb.values().stream()
                .anyMatch(voter -> {
                    Voter.Credentials credentials = voter.getCredentials();
                    return credentials.userName().equals(userName) && credentials.password().equals(password);
                });
    }

}
