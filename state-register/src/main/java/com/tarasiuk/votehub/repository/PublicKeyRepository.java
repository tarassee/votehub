package com.tarasiuk.votehub.repository;

import com.tarasiuk.votehub.model.PublicKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicKeyRepository extends JpaRepository<PublicKey, Long> {
}
