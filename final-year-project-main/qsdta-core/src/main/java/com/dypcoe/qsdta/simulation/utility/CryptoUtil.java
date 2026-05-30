package com.dypcoe.qsdta.simulation.utility;

import com.dypcoe.qsdta.exception.simulation.CryptoUtilityException;
import com.dypcoe.qsdta.simulation.utility.implementation.ICipherData;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class CryptoUtil implements ICipherData {
    private static volatile CryptoUtil instance;

    private CryptoUtil() {
    };

    public static CryptoUtil getInstance() {
        if (instance == null) {
            synchronized (CryptoUtil.class) {
                instance = new CryptoUtil();
            }
        }

        return instance;
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] data) throws CryptoUtilityException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(data);

            return Base64.getEncoder().encode(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new CryptoUtilityException("Encryption Failure", e);
        }
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] data) throws CryptoUtilityException {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(data);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return cipher.doFinal(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | IllegalArgumentException e) {
            throw new CryptoUtilityException("Decryption failed", e);
        }
    }

    @Override
    public byte[] binaryToBytes(List<Integer> bitsKey) {
        int byteLength = (bitsKey.size() + 7) / 8;
        byte[] byteKey = new byte[byteLength];

        for (int i = 0; i < bitsKey.size(); i++) {
            if (bitsKey.get(i) == 1) {
                byteKey[i / 8] |= (byte) (1 << (7 - (i % 8)));
            }
        }

        return byteKey;
    }
}
