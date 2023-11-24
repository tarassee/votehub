package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.service.RSAKeyService;
import com.tarasiuk.votehub.util.data.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DefaultRSAKeyService implements RSAKeyService {

    @Value("${protocol.blind.rsa.publicExponent:18827}")
    private BigInteger publicExponent;

    @Value("${protocol.blind.rsa.privateExponent:18347}")
    private BigInteger privateExponent;

    @Value("${protocol.blind.rsa.modulus:24881}")
    private BigInteger modulus;

    @Cacheable("publicKey")
    @Override
    public RSAKey getPublicKey() {
        return new RSAKey(publicExponent, modulus);
    }

    @Cacheable("privateKey")
    @Override
    public RSAKey getPrivateKey() {
        return new RSAKey(privateExponent, modulus);
    }

}
