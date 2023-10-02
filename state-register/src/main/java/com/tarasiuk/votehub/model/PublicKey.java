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
    @Column
    private BigInteger publicExponent;
    @Column
    private BigInteger modulus;
    @OneToOne(mappedBy = "publicKey")
    private CitizenModel citizenModel;

}
