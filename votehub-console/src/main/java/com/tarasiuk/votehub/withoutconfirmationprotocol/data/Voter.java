package com.tarasiuk.votehub.withoutconfirmationprotocol.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Voter {
    private final String firstName;
    private final String lastName;
    private final boolean isEligibleToVote;
    private Credentials credentials;
    private boolean isAuthenticatedToVote;

    public record Credentials(String userName, String password, ElectionToken electionToken, UUID tokenSerialNumber) {
    }
}
