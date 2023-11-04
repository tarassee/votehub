package com.tarasiuk.votehub.controller.protocol;

import com.tarasiuk.votehub.data.protocol.SimpleProtocolSignMessage;
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
public class SimpleProtocolSignSignController {

    @PostMapping("/signMessage")
    public ResponseEntity<BigInteger> signMessage(@RequestBody @Valid SimpleProtocolSignMessage simpleProtocolSignMessage) {
        requireNonNull(simpleProtocolSignMessage.message());
        requireNonNull(simpleProtocolSignMessage.privateExponent());
        requireNonNull(simpleProtocolSignMessage.modulus());

        return ResponseEntity.ok(createSignature(simpleProtocolSignMessage));
    }

    private static BigInteger createSignature(SimpleProtocolSignMessage simpleProtocolSignMessage) {

        // Message hash calculation
        BigInteger messageHash = HashUtil.simplifiedQuadraticConvolutionHash(simpleProtocolSignMessage.message(), simpleProtocolSignMessage.modulus());
        // EDS creation
        BigInteger signature = messageHash.modPow(simpleProtocolSignMessage.privateExponent(), simpleProtocolSignMessage.modulus());

        return signature;
    }

}
