package com.hh.ota.aesenc;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface EncFile extends Library {
    EncFile INSTANCE = (EncFile) Native.load("aes", EncFile.class);

    void aesFileEnc(String fileIn, String fileOut, String key);

}
