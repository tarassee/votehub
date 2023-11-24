package com.tarasiuk.votehub.controller.protocol.blind;


import com.tarasiuk.votehub.service.RSAKeyService;
import com.tarasiuk.votehub.util.data.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/blindProtocol")
@RestController
public class BlindProtocolPublicController {

    private final RSAKeyService rsaKeyService;

    @GetMapping("/getPublicKey")
    public ResponseEntity<RSAKey> getPublicKey() {
        return ResponseEntity.ok(rsaKeyService.getPublicKey());
    }

}
