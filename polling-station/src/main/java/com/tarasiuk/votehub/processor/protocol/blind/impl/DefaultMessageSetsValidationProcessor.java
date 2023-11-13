package com.tarasiuk.votehub.processor.protocol.blind.impl;

import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessage;
import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessageSet;
import com.tarasiuk.votehub.exception.ValidationProcessingException;
import com.tarasiuk.votehub.processor.protocol.blind.AbstractMessageSetsValidationProcessor;
import com.tarasiuk.votehub.service.CandidateStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class DefaultMessageSetsValidationProcessor extends AbstractMessageSetsValidationProcessor {

    private final CandidateStatisticsService candidateStatisticsService;

    @Override
    protected void validateFirstMessageSet(BlindProtocolMessageSet selectedMessageSet, Integer passportId) {
        List<BlindProtocolMessage> messageSet = selectedMessageSet.messageSet();
        List<String> allCandidates = new ArrayList<>(candidateStatisticsService.getAllCandidatesNames());

        // validate if message set size == candidates quantity
        validate(allCandidates.size() == messageSet.size(), bool -> bool, String.format("Error! Message set is not valid! It should have %s candidates specified!", allCandidates.size()));

        for (BlindProtocolMessage message : messageSet) {
            // validate correct passportId
            validate(passportId.compareTo(message.passportId()) == 0, bool -> bool, "Error! The passportId in the request is different from the passportId in the message set!");
            // validate if message set has all candidates specified
            validate(allCandidates.contains(message.candidateValue()), bool -> bool, "Error! Candidates specified in the message set is not valid!");
            // remove after matching
            allCandidates.remove(message.candidateValue());
        }

    }

    @Override
    protected void validateIfAllSetsAreTheSame(BlindProtocolMessageSet validatedMessageSet, List<BlindProtocolMessageSet> messageSets) {
        for (BlindProtocolMessageSet messageSet : messageSets) {
            validate(hasSameSize(validatedMessageSet, messageSet), bool -> bool, "Error! Message sets are not the same! Message quantity is different!");
            validate(hasSameMessages(validatedMessageSet, messageSet), bool -> bool, "Error! Message sets are not the same! Messages content is different!");
        }
    }

    private boolean hasSameSize(BlindProtocolMessageSet validatedMessageSet, BlindProtocolMessageSet messageSet) {
        return validatedMessageSet.messageSet().size() == messageSet.messageSet().size();
    }

    private boolean hasSameMessages(BlindProtocolMessageSet set1, BlindProtocolMessageSet set2) {
        List<BlindProtocolMessage> list1 = set1.messageSet();
        List<BlindProtocolMessage> list2 = set2.messageSet();
        return IntStream.range(0, list1.size()).allMatch(i -> list1.get(i).equals(list2.get(i)));
    }

    private <T> void validate(T value, Predicate<T> predicate, String errorMsg) {
        if (!predicate.test(value)) {
            throw new ValidationProcessingException(errorMsg);
        }
    }

}
