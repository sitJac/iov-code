package com.hh.ota.encrypt;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class ModelEnv {
    /**
     * @breif int to byte
     * @param i
     * @return byte[]
     */
    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    /**
     * @breif bytes add bytes, generate SecureRandom
     * @param byte1
     * @param byte2
     * @return SecureRandom
     */
    public static byte[] joinByteArray(byte[] byte1, byte[] byte2) {
        return ByteBuffer.allocate(byte1.length + byte2.length)
                .put(byte1)
                .put(byte2)
                .array();
    }
    /**
     * @breif use SecureRandom to generate random number
     * @param: model:vehicle model
     * @param: env:vehicle environment
     * @return random number
     */
    public static SecureRandom randomModelEnv(String model, int env) {
        byte[] seed1 = model.getBytes();
        byte[] seed2 = intToByte4(env);
        byte[] seed = joinByteArray(seed1, seed2);

        SecureRandom random = new SecureRandom(seed);

        return random;
    }
}

