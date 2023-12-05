package com.tarasiuk.votehub.twocommissionsprotocol.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.KeyPair;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voter {
    private Integer voterId;
    private String firstName;
    private String lastName;
    private KeyPair keyPair;
    private UUID registrationNumber;
    private Integer identificationNumber;
    private Integer age;

    public Voter(int voterId, String firstName, String lastName, Integer age) {
        this.voterId = voterId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

}
