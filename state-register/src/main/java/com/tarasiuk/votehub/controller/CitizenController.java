package com.tarasiuk.votehub.controller;

import com.tarasiuk.votehub.data.CitizenEligibilityResult;
import com.tarasiuk.votehub.exception.NotFoundException;
import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.service.CitizenService;
import com.tarasiuk.votehub.service.CitizenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.tarasiuk.votehub.constant.StateRegisterConstant.CommonError.NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID;
import static com.tarasiuk.votehub.constant.StateRegisterConstant.EligibilityFilter.NO_FILTER;

@RequiredArgsConstructor
@RequestMapping("/citizen")
@RestController
public class CitizenController {

    private final CitizenService citizenService;
    private final CitizenValidationService citizenValidationService;

    @GetMapping("/validate/{passportId}")
    public ResponseEntity<CitizenEligibilityResult> isCitizenEligibleToVote(@PathVariable Integer passportId,
                                                                            @RequestParam(defaultValue = NO_FILTER) Set<String> eligibilityFilters) {
        if (!citizenService.existsByPassportId(passportId)) {
            throw new NotFoundException(String.format(NO_CITIZEN_INFORMATION_FOUND_FOR_PASSPORT_ID, passportId));
        }

        CitizenModel citizenModel = citizenService.getByPassportId(passportId);
        return ResponseEntity.ok(citizenValidationService.isCitizenEligibleToVote(citizenModel, eligibilityFilters));
    }

    @GetMapping("/exists/{passportId}")
    public ResponseEntity<Boolean> citizenExists(@PathVariable Integer passportId) {
        return ResponseEntity.ok(citizenService.existsByPassportId(passportId));
    }

}
