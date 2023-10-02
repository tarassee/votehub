package com.tarasiuk.votehub.service;

import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.data.CitizenEligibilityResult;

import java.util.Set;

public interface CitizenValidationService {

    CitizenEligibilityResult isCitizenEligibleToVote(CitizenModel citizenModel, Set<String> eligibilityFilters);

}
