package com.hh.ota.diff;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface HDiff extends Library{
    HDiff INSTANCE = (HDiff) Native.load("hidiff", HDiff.class);

    void hh_ota_diff(String oldFile, String newFile, String outDiffFile, long size);

}
