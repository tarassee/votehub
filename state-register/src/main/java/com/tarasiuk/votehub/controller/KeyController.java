package com.tarasiuk.votehub.controller;

import com.tarasiuk.votehub.exception.NotFoundException;
import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.service.CitizenService;
import com.tarasiuk.votehub.service.KeyGenerationService;
import com.tarasiuk.votehub.util.data.RSAKey;
import com.tarasiuk.votehub.util.data.RSAKeyPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.tarasiuk.votehub.constant.StateRegisterConstant.CommonError.NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID;
import static com.tarasiuk.votehub.constant.StateRegisterConstant.CommonError.NO_PUBLIC_KEY_FOUND_FOR_PASSPORT_ID;

@RequiredArgsConstructor
@RequestMapping("/key")
@RestController
public class KeyController {

    private final CitizenService citizenService;
    private final KeyGenerationService keyGenerationService;

    @PostMapping("/generate/{passportId}")
    public ResponseEntity<RSAKeyPair> generateKeys(@PathVariable Integer passportId) {
        if (!citizenService.existsByPassportId(passportId)) {
            throw new NotFoundException(String.format(NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID, passportId));
        }

        return ResponseEntity.ok(keyGenerationService.generateAndRegister(passportId));
    }

    @GetMapping("/publicKey/{passportId}")
    public ResponseEntity<RSAKey> getPublicKey(@PathVariable Integer passportId) {
        if (!citizenService.existsByPassportId(passportId)) {
            throw new NotFoundException(String.format(NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID, passportId));
        }

        // todo: refactor
        var key = Optional.ofNullable(citizenService.getByPassportId(passportId)).map(CitizenModel::getPublicKey)
                .orElseThrow(() -> new NotFoundException(String.format(NO_PUBLIC_KEY_FOUND_FOR_PASSPORT_ID, passportId)));

        return ResponseEntity.ok(new RSAKey(key.getPublicExponent(), key.getModulus()));
    }

}
