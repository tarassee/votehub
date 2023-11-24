package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.util.data.RSAKey;

public interface RSAKeyService {

    RSAKey getPublicKey();
    RSAKey getPrivateKey();

}
