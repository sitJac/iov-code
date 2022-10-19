package com.hh.ota.classuntils;

public class dstFileObject {
    public String ecuName;
    public String url;
    public String sign;                 //sign == 1:sign, sign == 0:sha256 value
    public double size;                 //use in diff and patch interface
    public double oldSize;              //size before zip (only use in OTAPackage)
    public double newSize;              //size after aip  (only use in OTAPackage)
}
