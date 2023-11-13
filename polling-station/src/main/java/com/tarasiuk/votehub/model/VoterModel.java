package com.tarasiuk.votehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VoterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Integer passportId;
    @Column
    private boolean voted;
    @Column
    private boolean hasSignatureMessageSet;
    @Column
    private String candidateValue;

}
