package com.hh.ota.patch;

import com.hh.ota.diff.HDiff;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface HPatch extends Library {
    HPatch INSTANCE = (HPatch) Native.load("hidiff", HPatch.class);

    void hh_ota_patch(String diffPath, String oldPath, String outNewPath);

}