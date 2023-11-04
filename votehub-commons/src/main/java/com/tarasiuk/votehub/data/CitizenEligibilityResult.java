package com.tarasiuk.votehub.data;

public record CitizenEligibilityResult(
        boolean eligible,
        String errorMessage
) {
}
