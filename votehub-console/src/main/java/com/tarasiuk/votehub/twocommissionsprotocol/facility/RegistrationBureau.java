package com.tarasiuk.votehub.twocommissionsprotocol.facility;

import java.util.*;

public class RegistrationBureau {
    private final Map<Integer, UUID> voterIdToRegistraitonNumberMap;

    public RegistrationBureau() {
        this.voterIdToRegistraitonNumberMap = new HashMap<>();
    }

    public UUID registerVoter(Integer voterId) {
        if (voterIdToRegistraitonNumberMap.containsKey(voterId)) {
            System.out.printf("Error while registering! Voter with ID %d is already registered!%n", voterId);
            return null;
        } else {
            UUID registrationNumber = UUID.randomUUID();
            voterIdToRegistraitonNumberMap.put(voterId, registrationNumber);
            return registrationNumber;
        }
    }

    public Set<UUID> getRegistrationNumbersSet() {
        return new HashSet<>(voterIdToRegistraitonNumberMap.values());
    }

}
