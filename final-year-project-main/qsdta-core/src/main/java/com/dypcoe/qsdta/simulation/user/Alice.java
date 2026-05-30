package com.dypcoe.qsdta.simulation.user;

import com.dypcoe.qsdta.simulation.model.QubitPhoton;
import com.dypcoe.qsdta.simulation.utility.singleton.CryptoUtilityRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Alice {
    private final List<QubitPhoton> photons;
    private final List<String> bases;
    private final List<Integer> key;

    public Alice(int numPhotons) {
        photons = new ArrayList<>();
        bases = new ArrayList<>();
        key = new ArrayList<>();
        simulatePhotons(numPhotons);
    }

    private void simulatePhotons(int numPhotons) {
        for (int i = 0; i < numPhotons; i++) {
            String basis = CryptoUtilityRandom.nextBoolean() ? "Z" : "X";
            int state = (CryptoUtilityRandom.nextBoolean() ? 1 : 0);

            this.bases.add(basis);
            this.key.add(state);

            photons.add(new QubitPhoton(state, basis));
        }
    }

    public List<QubitPhoton> getPhotons() {
        return Collections.unmodifiableList(this.photons);
    }

    public List<String> getBases() {
        return Collections.unmodifiableList(this.bases);
    }

    public List<Integer> getKey() {
        return Collections.unmodifiableList(this.key);
    }
}
