package com.hh.ota.rsasign;

public class signTest {
    static String filePath = "/home/jack_xie/ota-update/test.zip";
    static String pubKeyPath = "/home/jack_xie/ota-update/public.pem";
    static String priKeyPath = "/home/jack_xie/ota-update/private.pem";
    static String sigPath = "/home/jack_xie/ota-update/test.sig.bin";

    static String infile = "/home/jack_xie/ota-update/test.zip";
    static String cerPath = "/home/jack_xie/ota-update/ca.crt";
    static String digPath = "/home/jack_xie/ota-update/test.sig.bin";
    static String outfile = "/home/jack_xie/ota-update/append.zip";


    public static void main(String[] args) {
//        SIGNTEST.INSTANCE.generate_keys();
        String sign = "";
        SIGNTEST.INSTANCE.sign(filePath, priKeyPath, sigPath, sign);
//        SIGNTEST.INSTANCE.verify(filePath, pubKeyPath, sigPath);
//        SIGNTEST.INSTANCE.print_hex(sign);

//        SIGNTEST.INSTANCE.append_header(infile,cerPath,sigPath,outfile);


    }
}
