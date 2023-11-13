package com.tarasiuk.votehub.controller.protocol.blind;

import com.tarasiuk.votehub.data.protocol.blind.encrypt.BlindProtocolEncryptMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMaskMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolUnMaskAndUnSignMessage;
import com.tarasiuk.votehub.data.protocol.blind.receive.BlindProtocolSignedMaskedMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolSignedUnmaskedMessage;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolSignedUnmaskedMessageSet;
import com.tarasiuk.votehub.util.BlindSignatureUtil;
import com.tarasiuk.votehub.util.JsonSerializer;
import com.tarasiuk.votehub.util.StringBigIntegerUtil;
import com.tarasiuk.votehub.util.data.RSAKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/blindProtocol/")
@RestController
public class BlindProtocolHelperController {

    @PostMapping("/maskMessage")
    public ResponseEntity<List<BlindProtocolMaskedMessageSet>> maskMessage(@RequestBody @Valid BlindProtocolMaskMessage blindProtocolMaskMessage) {
        return ResponseEntity.ok(maskMessageInternal(blindProtocolMaskMessage));
    }

    private List<BlindProtocolMaskedMessageSet> maskMessageInternal(BlindProtocolMaskMessage blindProtocolMaskMessage) {
        BigInteger maskingKey = blindProtocolMaskMessage.maskingKey();
        RSAKey psPublicKey = blindProtocolMaskMessage.psPublicKey();
        List<BlindProtocolMessageSet> messageSets = blindProtocolMaskMessage.messageSets();

        List<BlindProtocolMaskedMessageSet> maskedMessageSets = new ArrayList<>();
        for (BlindProtocolMessageSet messageSet : messageSets) {

            List<BlindProtocolMaskedMessage> maskedMessageSet = new ArrayList<>();
            for (BlindProtocolMessage message : messageSet.messageSet()) {
                // представити у числовому вигляді
                String serializedMessage = JsonSerializer.serialize(message);
                BigInteger messageInNumber = StringBigIntegerUtil.stringToBigInteger(serializedMessage);

                // обрахунок підпису з maskingKey
                BigInteger signature = BlindSignatureUtil.signWithMaskingKey(maskingKey, psPublicKey, messageInNumber);

                // занесення бюлетня у набір повідомлень
                maskedMessageSet.add(new BlindProtocolMaskedMessage(signature));
            }

            // занесення набору в набір повідомлень
            maskedMessageSets.add(new BlindProtocolMaskedMessageSet(maskedMessageSet));
        }

        return maskedMessageSets;
    }

    @PostMapping("/unMaskMessage")
    public ResponseEntity<BlindProtocolSignedUnmaskedMessageSet> unMaskMessage(@RequestBody @Valid BlindProtocolUnMaskAndUnSignMessage blindProtocolunMaskAndUnSignAndUnSignMessage) {
        return ResponseEntity.ok(unMaskMessageInternal(blindProtocolunMaskAndUnSignAndUnSignMessage));
    }

    private BlindProtocolSignedUnmaskedMessageSet unMaskMessageInternal(BlindProtocolUnMaskAndUnSignMessage blindProtocolunMaskAndUnSignAndUnSignMessage) {
        List<BlindProtocolSignedMaskedMessage> signedMaskedMessageSet = blindProtocolunMaskAndUnSignAndUnSignMessage.signedMaskedMessageSet();
        RSAKey psPublicKey = blindProtocolunMaskAndUnSignAndUnSignMessage.psPublicKey();
        BigInteger maskingKey = blindProtocolunMaskAndUnSignAndUnSignMessage.maskingKey();

        List<BlindProtocolSignedUnmaskedMessage> unMaskedMaskedMessages = signedMaskedMessageSet.stream()
                .map(message -> BlindSignatureUtil.unMaskOnly(maskingKey, psPublicKey, message.signedMaskedMessage()))
                .map(BlindProtocolSignedUnmaskedMessage::new)
                .toList();

        return new BlindProtocolSignedUnmaskedMessageSet(unMaskedMaskedMessages);
    }

    @PostMapping("/unMaskAndUnSignMessage")
    public ResponseEntity<BlindProtocolMessageSet> unMaskAndUnSignMessage(@RequestBody @Valid BlindProtocolUnMaskAndUnSignMessage blindProtocolUnMaskAndUnSignAndUnSignMessage) {
        return ResponseEntity.ok(unMaskAndUnSignMessageInternal(blindProtocolUnMaskAndUnSignAndUnSignMessage));
    }

    private BlindProtocolMessageSet unMaskAndUnSignMessageInternal(BlindProtocolUnMaskAndUnSignMessage blindProtocolUnMaskAndUnSignAndUnSignMessage) {
        List<BlindProtocolSignedMaskedMessage> signedMaskedMessageSet = blindProtocolUnMaskAndUnSignAndUnSignMessage.signedMaskedMessageSet();
        RSAKey psPublicKey = blindProtocolUnMaskAndUnSignAndUnSignMessage.psPublicKey();
        BigInteger maskingKey = blindProtocolUnMaskAndUnSignAndUnSignMessage.maskingKey();

        List<BlindProtocolMessage> unMaskAndUnSignedMaskedMessages = signedMaskedMessageSet.stream()
                .map(message -> BlindSignatureUtil.unMaskAndUnSign(maskingKey, psPublicKey, message.signedMaskedMessage()))
                .map(StringBigIntegerUtil::bigIntegerToString)
                .map(messageInNumber -> JsonSerializer.deserialize(messageInNumber, BlindProtocolMessage.class))
                .toList();

        return new BlindProtocolMessageSet(unMaskAndUnSignedMaskedMessages);
    }

    @PostMapping("/encryptMessage")
    public ResponseEntity<BigInteger> encryptMessage(@RequestBody @Valid BlindProtocolEncryptMessage blindProtocolEncryptMessage) {
        return ResponseEntity.ok(encryptMessageInternal(blindProtocolEncryptMessage));
    }

    private BigInteger encryptMessageInternal(BlindProtocolEncryptMessage blindProtocolEncryptMessage) {
        RSAKey publicKey = blindProtocolEncryptMessage.psPublicKey();
        BigInteger unMaskedSignedMessage = new BigInteger(blindProtocolEncryptMessage.unMaskedSignedMessageStr());

        // сформувати підпис
        BigInteger signature = BlindSignatureUtil.sign(unMaskedSignedMessage, publicKey);

        return signature;
    }

}
