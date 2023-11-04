package com.tarasiuk.votehub.controller.protocol;

import com.tarasiuk.votehub.data.protocol.simple.SimpleProtocolVoteMessage;
import com.tarasiuk.votehub.util.GammaUtil;
import com.tarasiuk.votehub.util.JsonSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/encrypt/")
@RestController
public class EncryptController {

    @PostMapping("/{encryptStrategy}")
    public ResponseEntity<String> gammaEncryptMessage(@RequestBody SimpleProtocolVoteMessage encryptMessage,
                                                      @PathVariable String encryptStrategy) {

        String encryptedMessage = encrypt(encryptMessage, encryptStrategy);

        return ResponseEntity.ok(encryptedMessage);
    }

    // todo: refactor, remove switch, implement strategies
    private static String encrypt(SimpleProtocolVoteMessage encryptMessage, String encryptStrategy) {
        String serializedMessage = JsonSerializer.serialize(encryptMessage);

        String encryptedMessage = null;

        switch (encryptStrategy) {
            case "gamma":
                encryptedMessage = GammaUtil.encrypt(serializedMessage);
                break;
            default:
                break;
        }

        return encryptedMessage;
    }

}
