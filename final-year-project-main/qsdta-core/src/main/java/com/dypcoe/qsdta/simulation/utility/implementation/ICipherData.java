package com.dypcoe.qsdta.simulation.utility.implementation;

import com.dypcoe.qsdta.exception.simulation.CryptoUtilityException;

public interface ICipherData extends IBitToByteManipulation {
    public byte[] encrypt(byte[] key, byte[] data) throws CryptoUtilityException;
    public byte[] decrypt(byte[] kye, byte[] data) throws CryptoUtilityException;
}
