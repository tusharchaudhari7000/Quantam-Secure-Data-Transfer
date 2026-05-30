package com.dypcoe.qsdta.simulation;

import com.dypcoe.qsdta.exception.simulation.CryptoUtilityException;
import com.dypcoe.qsdta.exception.simulation.SimulationException;
import com.dypcoe.qsdta.simulation.reconsiliation.KeyReconciliation;
import com.dypcoe.qsdta.simulation.user.Alice;
import com.dypcoe.qsdta.simulation.user.Bob;
import com.dypcoe.qsdta.simulation.utility.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class QuantumKeyDistribution {
    private static volatile QuantumKeyDistribution instance;
    private static final Logger logger = LoggerFactory.getLogger(QuantumKeyDistribution.class);

    private QuantumKeyDistribution() {};

    public static QuantumKeyDistribution getInstance() {
        if(instance == null) {
            synchronized (QuantumKeyDistribution.class) {
                logger.info("BB84 QKD Instantiated");
                instance = new QuantumKeyDistribution();
            }
        }

        return instance;
    }

    public byte[] establishConnectionKey(int NUM_PHOTONS) throws SimulationException, CryptoUtilityException {
        CryptoUtil cryptoUtil = CryptoUtil.getInstance();

        Alice alice = new Alice(NUM_PHOTONS);
        Bob bob = new Bob(NUM_PHOTONS);
        KeyReconciliation reconciliation = new KeyReconciliation();

        List<Integer> sharedKey =
                reconciliation.reconcileKeys(
                        alice.getBases(),
                        bob.getBases(),
                        alice.getKey(),
                        bob.measurePhotons(alice.getPhotons())
                );

        try{
            byte[] quantumKey = cryptoUtil.binaryToBytes(sharedKey);

            try{
                if(quantumKey.length >= 32)
                    return Arrays.copyOf(quantumKey, 32);
                else if(quantumKey.length >= 24)
                    return Arrays.copyOf(quantumKey, 24);
                else if(quantumKey.length >= 16)
                    return Arrays.copyOf(quantumKey, 16);
            } catch (Exception e) {
                logger.error("Short quantum key detected");
                throw new SimulationException("Short quantum key detected", e);
            }

        } catch (Exception e) {
            logger.error("Failed to convert bits into bytes");
            throw new CryptoUtilityException("Failed to convert bits into bytes", e);
        }

        return new byte[0];
    }
}
