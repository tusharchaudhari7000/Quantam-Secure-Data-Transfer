package com.dypcoe.qsdta.simulation.reconsiliation;

import com.dypcoe.qsdta.exception.simulation.SimulationException;

import java.util.ArrayList;
import java.util.List;

public class KeyReconciliation {
    public List<Integer> reconcileKeys(
            List<String> aliceBases,
            List<String> bobBases,
            List<Integer> aliceKey,
            List<Integer> bobMeasurements) throws SimulationException {
        List<Integer> sharedKey = new ArrayList<>();
        List<Integer> bitIndexes = new ArrayList<>();

        for(int i = 0; i < aliceBases.size(); i++) {
            if(aliceBases.get(i).equals(bobBases.get(i))) {
                sharedKey.add(aliceKey.get(i));
                bitIndexes.add(i);
            }
        }

        detectEavesDropping(bitIndexes, aliceKey, bobMeasurements);

        return  sharedKey;
    }

    private void detectEavesDropping(List<Integer> bitIndex, List<Integer> aliceKey, List<Integer> bobKey) throws SimulationException {
        for (int i : bitIndex) {
            if(!aliceKey.get(i).equals(bobKey.get(i))) {
                throw new SimulationException("Eve's dropping detected, discarding key...", new Throwable());
            }
        }
    }
}
