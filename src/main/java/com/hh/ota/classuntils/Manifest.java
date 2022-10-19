package com.hh.ota.classuntils;

import java.util.List;

public class Manifest {
    public String updateObj;                    // 升级对象文件名称
    public int packType;                        // 升级包类型：1→整包   2→差分包
    public String srcVer;                          // 升级原版本，标识差分关系
    public String dstVer;                       // 升级目标版本，即本次提交的软件版本号
    public int packNum;                         // 升级文件的个数
    public List<packInformation> packInfos ;    // 升级文件的列表，列表中从上到下为要升级的顺序

    public class packInformation {
        public String updateObj;                // 升级对象名称
        public int packType;                    // 升级包类型
        public String srcVer;                   // 升级原版本
        public String dstVer;                   // 升级目标版本
        public String fileName;                 // 升级文件名称
        public String fileSign;                 // 升级文件签名
        public long   fileSize;                 // 升级文件长度 单位Byte
    }

    public List<packInformation> getPackInfos() {
        return this.packInfos;
    }

    public void setPackInfos(List<packInformation> packInfos) {
        this.packInfos = packInfos;
    }
}