package com.tarasiuk.votehub.twocommissionsprotocol.data;

import com.tarasiuk.votehub.twocommissionsprotocol.util.data.dsa.DSASignatureResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteMessage {

    private Integer voterId;
    private UUID registrationNumber;
    private DSASignatureResult signatureResult;
    private String candidateValue;
    private Integer age;

}
