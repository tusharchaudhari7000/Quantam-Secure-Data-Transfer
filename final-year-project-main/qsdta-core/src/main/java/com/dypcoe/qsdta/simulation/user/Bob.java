package com.dypcoe.qsdta.simulation.user;

import com.dypcoe.qsdta.simulation.model.QubitPhoton;
import com.dypcoe.qsdta.simulation.utility.singleton.CryptoUtilityRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bob {
    private final List<String> bases;

    public Bob(int numPhotons) {
        this.bases = new ArrayList<>();
        simulateBases(numPhotons);
    }

    private void simulateBases(int numPhotons) {
        for (int i = 0; i < numPhotons; i++) {
            this.bases.add(CryptoUtilityRandom.nextBoolean() ? "Z" : "X");
        }
    }

    public List<Integer> measurePhotons(List<QubitPhoton> photons) {
        List<Integer> measurements = new ArrayList<>();

        for (int i = 0; i < photons.size(); i++) {
            QubitPhoton photon = photons.get(i);
            String bobBasis = this.bases.get(i);

            if (bobBasis.equals(photon.getBasis())) {
                measurements.add(photon.getState());
            } else {
                measurements.add(CryptoUtilityRandom.nextBoolean() ? 0 : 1);
            }
        }

        return measurements;
    }

    public List<String> getBases() {
        return Collections.unmodifiableList(this.bases);
    }
}
