package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.client.StateRegisterClient;
import com.tarasiuk.votehub.data.CitizenEligibilityResult;
import com.tarasiuk.votehub.service.VoterEligibilityService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultVoterEligibilityService implements VoterEligibilityService {

    private final StateRegisterClient stateRegisterClient;

    @Override
    public boolean isEligibleToVote(Integer passportId) {
        try {
            // todo: refactor hardcoded
            CitizenEligibilityResult result = stateRegisterClient.isEligibleToVote(passportId, Set.of("isCapable", "isNotPrisoner", "isAdult"));
            log.debug("Eligibility validation result for voter with passport id {}: {}", passportId, result);
            return result.eligible();
        } catch (ResponseStatusException | FeignException | WebServerException e) {
            log.error("" + e.getMessage());
            return false;
        }
    }

}
