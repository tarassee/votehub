package com.tarasiuk.votehub.service.impl;

import com.tarasiuk.votehub.data.CitizenEligibilityResult;
import com.tarasiuk.votehub.exception.NotFoundException;
import com.tarasiuk.votehub.model.CitizenModel;
import com.tarasiuk.votehub.service.CitizenValidationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static com.tarasiuk.votehub.constant.StateRegisterConstant.EligibilityFilter.*;
import static java.util.function.Predicate.not;

@Service
public class DefaultCitizenValidationService implements CitizenValidationService {

    private static final String UNKNOWN_ELIGIBILITY_FILTER = "Unknown eligibility filter '%s'";
    private static final CitizenEligibilityResult SUCCESS_RESULT = new CitizenEligibilityResult(true, null);
    private final Map<String, EligibilityFilter> eligibilityFiltersMap = new HashMap<>();

    public DefaultCitizenValidationService() {
        eligibilityFiltersMap.put(NO_FILTER,
                new EligibilityFilter(f -> true, ""));
        eligibilityFiltersMap.put(IS_CAPABLE,
                new EligibilityFilter(CitizenModel::isCapable, "Citizen is not capable"));
        eligibilityFiltersMap.put(IS_NOT_PRISONER,
                new EligibilityFilter(not(CitizenModel::isPrisoner), "Citizen is a prisoner"));
        eligibilityFiltersMap.put(IS_ADULT,
                new EligibilityFilter(citizen -> citizen.getAge() >= ADULT_AGE, "Citizen is not an adult"));
    }

    @Override
    public CitizenEligibilityResult isCitizenEligibleToVote(CitizenModel citizenModel, Set<String> eligibilityFilters) {
        return eligibilityFilters.stream()
                .map(this::retrieveFilter)
                .filter(filter -> !filter.predicate().test(citizenModel))
                .findFirst()
                .map(filter -> new CitizenEligibilityResult(false, filter.errorMessage()))
                .orElse(SUCCESS_RESULT);
    }

    private EligibilityFilter retrieveFilter(String filter) {
        return Optional.ofNullable(eligibilityFiltersMap.get(filter))
                .orElseThrow(() -> new NotFoundException(String.format(UNKNOWN_ELIGIBILITY_FILTER, filter)));
    }

    private record EligibilityFilter(Predicate<CitizenModel> predicate, String errorMessage) {
    }

}
