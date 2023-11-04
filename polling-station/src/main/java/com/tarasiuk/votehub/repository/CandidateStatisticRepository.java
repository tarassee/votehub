package com.tarasiuk.votehub.repository;

import com.tarasiuk.votehub.model.CandidateStatisticModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateStatisticRepository extends JpaRepository<CandidateStatisticModel, Long> {

    boolean existsByName(String name);

    CandidateStatisticModel getByName(String name);

}
