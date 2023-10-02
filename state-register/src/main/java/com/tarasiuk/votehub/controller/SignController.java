package com.tarasiuk.votehub.controller;

import com.tarasiuk.votehub.data.SignMessage;
import com.tarasiuk.votehub.util.HashUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@RequestMapping("/edc/")
@RestController
public class SignController {

    @PostMapping("/signMessage")
    public ResponseEntity<BigInteger> signMessage(@RequestBody @Valid SignMessage signMessage) {
        requireNonNull(signMessage.message());
        requireNonNull(signMessage.privateExponent());
        requireNonNull(signMessage.modulus());

        return ResponseEntity.ok(createSignature(signMessage));
    }

    private static BigInteger createSignature(SignMessage signMessage) {

        // Message hash calculation
        BigInteger messageHash = HashUtil.simplifiedQuadraticConvolutionHash(signMessage.message(), signMessage.modulus());
        // EDS creation
        BigInteger signature = messageHash.modPow(signMessage.privateExponent(), signMessage.modulus());

        return signature;
    }

}
