package com.hh.ota.rsasign;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SIGNTEST extends Library {
    SIGNTEST INSTANCE = (SIGNTEST) Native.load("sign", SIGNTEST.class);

    int sign(String filePath, String priKeyPath, String signPath, String signRet);
    int verify(String filePath, String pubKeyPath, String signPath);

    int generate_keys();

    void print_hex(String md, int len);

    void append_header(String infile, String cer_path, String sig_path, String outfile);

}
