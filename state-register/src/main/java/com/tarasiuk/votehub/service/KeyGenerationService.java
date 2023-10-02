package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.util.data.RSAKeyPair;

public interface KeyGenerationService {

    RSAKeyPair generateAndRegister(Integer passportId);

}
