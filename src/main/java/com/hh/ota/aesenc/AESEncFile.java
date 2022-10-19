package com.hh.ota.aesenc;

public class AESEncFile {
    public static void main(String[] args) {
        String inFile = "/home/jack_xie/files/aoyun/zip/new.zip";
        String outFile = "/home/jack_xie/files/aoyun/zip/new.zip.enc";
        String key = "12345678876543211234567887654321";
        EncFile.INSTANCE.aesFileEnc(inFile, outFile, key);
    }
}
