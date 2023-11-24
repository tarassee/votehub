package com.tarasiuk.votehub.processor.protocol.blind;

import com.tarasiuk.votehub.data.protocol.blind.mask.BlindProtocolMessageSet;
import com.tarasiuk.votehub.processor.Processor;
import com.tarasiuk.votehub.util.RandomSelectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This abstract class contains vote processing validation
 * that is a part of bling signature protocol
 */
public abstract class AbstractMessageSetsValidationProcessor implements Processor<Map.Entry<Integer, List<BlindProtocolMessageSet>>> {

    @Override
    public void process(Map.Entry<Integer, List<BlindProtocolMessageSet>> item) {
        Integer passportId = item.getKey();
        List<BlindProtocolMessageSet> messageSets = new ArrayList<>(item.getValue());

        BlindProtocolMessageSet selectedMessageSet = RandomSelectionUtil.selectOneRandomly(messageSets);
        messageSets.remove(selectedMessageSet);

        validateFirstMessageSet(selectedMessageSet, passportId);
        validateIfAllSetsAreTheSame(selectedMessageSet, messageSets);
    }

    protected abstract void validateFirstMessageSet(BlindProtocolMessageSet selectedMessageSet, Integer passportId);

    protected abstract void validateIfAllSetsAreTheSame(BlindProtocolMessageSet selectedMessageSet, List<BlindProtocolMessageSet> messageSets);

}
