package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.model.PublicKey;
import com.tarasiuk.votehub.repository.CitizenRepository;
import com.tarasiuk.votehub.repository.PublicKeyRepository;
import com.tarasiuk.votehub.service.CitizenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultCitizenService implements CitizenService {

    private final CitizenRepository citizenRepository;
    private final PublicKeyRepository publicKeyRepository;

    @Override
    public boolean existsByPassportId(Integer passportId) {
        return citizenRepository.existsByPassportId(passportId);
    }

    @Override
    public CitizenModel getByPassportId(Integer passportId) {
        return citizenRepository.getCitizenModelByPassportId(passportId);
    }

    @Override
    public void deletePublicKeyFor(CitizenModel citizenModel) {
        Optional.ofNullable(citizenModel.getPublicKey()).ifPresent(publicKeyRepository::delete);
    }

    @Override
    public CitizenModel savePublicKeyFor(BigInteger publicExponent, BigInteger modulus, Integer passportId) {
        var citizenModel = getByPassportId(passportId);

        deletePublicKeyFor(citizenModel);
        citizenModel.setPublicKey(buildPublicKey(publicExponent, modulus));

        return citizenRepository.save(citizenModel);
    }

    private PublicKey buildPublicKey(BigInteger publicExponent, BigInteger modulus) {
        PublicKey key = new PublicKey();
        key.setPublicExponent(publicExponent);
        key.setModulus(modulus);
        return key;
    }

}
