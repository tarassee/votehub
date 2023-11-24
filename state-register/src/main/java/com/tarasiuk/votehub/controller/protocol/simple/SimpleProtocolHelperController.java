package com.tarasiuk.votehub.controller.protocol.simple;

import com.tarasiuk.votehub.data.protocol.simple.SimpleProtocolSignMessage;
import com.tarasiuk.votehub.data.protocol.simple.SimpleProtocolVoteMessage;
import com.tarasiuk.votehub.util.GammaUtil;
import com.tarasiuk.votehub.util.HashUtil;
import com.tarasiuk.votehub.util.JsonSerializer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RequiredArgsConstructor
@RequestMapping("/simpleProtocol/")
@RestController
public class SimpleProtocolHelperController {

    @PostMapping("/signMessage")
    public ResponseEntity<BigInteger> signMessage(@RequestBody @Valid SimpleProtocolSignMessage simpleProtocolSignMessage) {
        return ResponseEntity.ok(signMessageInternal(simpleProtocolSignMessage));
    }

    private static BigInteger signMessageInternal(SimpleProtocolSignMessage simpleProtocolSignMessage) {
        // Message hash calculation
        BigInteger messageHash = HashUtil.simplifiedQuadraticConvolutionHash(simpleProtocolSignMessage.message(), simpleProtocolSignMessage.modulus());
        // EDS creation
        BigInteger signature = messageHash.modPow(simpleProtocolSignMessage.privateExponent(), simpleProtocolSignMessage.modulus());

        return signature;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encryptMessage(@RequestBody SimpleProtocolVoteMessage encryptMessage) {
        return ResponseEntity.ok(encryptMessageInternal(encryptMessage));
    }

    private static String encryptMessageInternal(SimpleProtocolVoteMessage encryptMessage) {
        String serializedMessage = JsonSerializer.serialize(encryptMessage);
        String encryptedMessage = GammaUtil.encrypt(serializedMessage);
        return encryptedMessage;
    }

}
