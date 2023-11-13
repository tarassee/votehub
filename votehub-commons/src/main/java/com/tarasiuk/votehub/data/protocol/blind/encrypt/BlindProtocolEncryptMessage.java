package com.tarasiuk.votehub.data.protocol.blind.encrypt;

import com.tarasiuk.votehub.util.data.RSAKey;

public record BlindProtocolEncryptMessage(
        RSAKey psPublicKey,
        String unMaskedSignedMessageStr
) {
}
