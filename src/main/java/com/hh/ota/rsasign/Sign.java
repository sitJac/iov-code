package com.hh.ota.rsasign;

import com.hh.ota.aesenc.EncFile;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Sign extends Library {
    EncFile INSTANCE = (EncFile) Native.load("aes", EncFile.class);
}
