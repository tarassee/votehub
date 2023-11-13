package com.tarasiuk.votehub.util;

import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessageSet;
import com.tarasiuk.votehub.data.protocol.blind.send.BlindProtocolMaskedMessageSet;
import com.tarasiuk.votehub.util.data.RSAKey;

import java.math.BigInteger;
import java.util.List;

public final class UnMaskingUtil {

    private UnMaskingUtil() {
    }

    public static List<BlindProtocolMessageSet> unMaskMessageSets(List<BlindProtocolMaskedMessageSet> maskedMessageSets, BigInteger maskingKey, RSAKey privateKey) {
        return maskedMessageSets.stream()
                .map(maskedMessageSet -> UnMaskingUtil.unMaskMessageSet(maskedMessageSet, maskingKey, privateKey))
                .toList();
    }

    public static BlindProtocolMessageSet unMaskMessageSet(BlindProtocolMaskedMessageSet maskedMessageSet, BigInteger maskingKey, RSAKey privateKey) {
        var unmaskedMessageSet = maskedMessageSet.maskedMessageSet().stream()
                .map(maskedMessage -> BlindSignatureUtil.unSignWithMaskingKey(maskingKey, privateKey, maskedMessage.maskedMessage()))
                .map(StringBigIntegerUtil::bigIntegerToString)
                .map(serialized -> JsonSerializer.deserialize(serialized, BlindProtocolMessage.class))
                .toList();
        return new BlindProtocolMessageSet(unmaskedMessageSet);
    }

}
