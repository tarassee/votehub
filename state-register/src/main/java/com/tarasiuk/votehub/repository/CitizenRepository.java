package com.tarasiuk.votehub.repository;

import com.tarasiuk.votehub.model.CitizenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<CitizenModel, Long> {

    boolean existsByPassportId(Integer passportId);

    CitizenModel getCitizenModelByPassportId(Integer passportId);

}
