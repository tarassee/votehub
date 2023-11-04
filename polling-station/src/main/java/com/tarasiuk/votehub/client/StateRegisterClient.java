package com.tarasiuk.votehub.client;

import com.tarasiuk.votehub.data.CitizenEligibilityResult;
import com.tarasiuk.votehub.util.data.RSAKey;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "state-register-service", url = "${application.state-register-service.url}")
public interface StateRegisterClient {

    @GetMapping("/citizen/validate/{passportId}")
    CitizenEligibilityResult isEligibleToVote(@PathVariable Integer passportId);

    @GetMapping("/citizen/exists/{passportId}")
    Boolean existsByPassportId(@PathVariable Integer passportId);

    @GetMapping("/key/publicKey/{passportId}")
    RSAKey getPublicKey(@PathVariable Integer passportId);

}
