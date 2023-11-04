package com.tarasiuk.votehub.repository;

import com.tarasiuk.votehub.model.VoterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<VoterModel, Long> {

    VoterModel getVoterModelByPassportId(Integer passportId);

    Optional<VoterModel> findVoterModelByPassportId(Integer passportId);

    boolean existsByPassportId(Integer passportId);

}
