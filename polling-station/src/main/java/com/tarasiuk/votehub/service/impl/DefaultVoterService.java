package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.client.StateRegisterClient;
import com.tarasiuk.votehub.exception.NotFoundException;
import com.tarasiuk.votehub.model.VoterModel;
import com.tarasiuk.votehub.repository.VoterRepository;
import com.tarasiuk.votehub.service.VoterService;
import com.tarasiuk.votehub.util.data.RSAKey;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultVoterService implements VoterService {

    private static final String NO_PUBLIC_KEY_FOUND_FOR_PASSPORT_ID = "No public key found for passport id '%s'";
    private final VoterRepository voterRepository;
    private final StateRegisterClient stateRegisterClient;

    @Override
    public void recordVoter(Integer passportId) {
        existsByPassportId(passportId);
        VoterModel voterModel = getOrCreateVoter(passportId);
        voterModel.setPassportId(passportId);
        voterModel.setVoted(true);
        voterRepository.save(voterModel);
    }

    // todo: refactor
    private VoterModel getOrCreateVoter(Integer passportId) {
        return voterRepository.findVoterModelByPassportId(passportId)
                .orElseGet(() -> {
                    VoterModel newVoter = new VoterModel();
                    newVoter.setPassportId(passportId);
                    return newVoter;
                });
    }

    @Override
    public boolean existsByPassportId(Integer passportId) {
        try {
            return stateRegisterClient.existsByPassportId(passportId);
        } catch (ResponseStatusException | FeignException | WebServerException e) {
            log.error("" + e.getMessage());
            throw e;
        }
    }

    @Override
    public RSAKey getPublicKeyByPassportId(Integer passportId) {
        try {
            return stateRegisterClient.getPublicKey(passportId);
        } catch (ResponseStatusException | FeignException | WebServerException e) {
            // todo: refactor
            log.error("" + e.getMessage());
            throw new NotFoundException(String.format(NO_PUBLIC_KEY_FOUND_FOR_PASSPORT_ID, passportId));
        }
    }

    @Override
    public boolean hasVoted(Integer passportId) {
        return voterRepository.findVoterModelByPassportId(passportId)
                .map(VoterModel::isVoted)
                .orElse(false);
    }

}
