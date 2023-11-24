package com.tarasiuk.votehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PublicKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] publicExponentBytes;

    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] modulusBytes;

    @OneToOne(mappedBy = "publicKey")
    private CitizenModel citizenModel;

    public BigInteger getPublicExponent() {
        return new BigInteger(publicExponentBytes);
    }

    public void setPublicExponent(BigInteger publicExponent) {
        this.publicExponentBytes = publicExponent.toByteArray();
    }

    public BigInteger getModulus() {
        return new BigInteger(modulusBytes);
    }

    public void setModulus(BigInteger modulus) {
        this.modulusBytes = modulus.toByteArray();
    }

}
