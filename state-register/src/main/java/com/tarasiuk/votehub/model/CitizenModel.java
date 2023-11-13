package com.tarasiuk.votehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CitizenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Integer passportId;
    @Column
    private Integer age;
    @Column
    private boolean isCapable;
    @Column
    private boolean isPrisoner;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "public_key_id", referencedColumnName = "id")
    private PublicKey publicKey;
    @Column
    private String firstName;
    @Column
    private String lastName;

}
