package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.client.StateRegisterClient;
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

    private final VoterRepository voterRepository;
    private final StateRegisterClient stateRegisterClient;

    @Override
    public void recordVoter(Integer passportId) {
        existsByPassportId(passportId);
        VoterModel voterModel = new VoterModel();
        voterModel.setPassportId(passportId);
        voterModel.setVoted(true);
        voterRepository.save(voterModel);
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
            throw e;
        }
    }

    @Override
    public boolean hasVoted(Integer passportId) {
        return voterRepository.findVoterModelByPassportId(passportId)
                .map(VoterModel::isVoted)
                .orElse(false);
    }

}
