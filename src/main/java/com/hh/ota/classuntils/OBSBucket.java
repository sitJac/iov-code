package com.hh.ota.classuntils;

public class OBSBucket {
    public String  uploadUrl;
    public String  uploadToken;
    public String  uploadKey;      //obs catalog: eg:folder1/folder2/folder3/
    public String  obsPath;        //upload file's local path: point to file name eg:tmp/ota_tmp/ota.zip
    public String  downloadUrl;    //no need this param //拼接返回对象中的uploadUrl

    public void setOBSBucket(String uploadUrl, String uploadToken, String uploadKey) {
        this.uploadUrl = uploadUrl;
        this.uploadToken = uploadToken;
        this.uploadKey = uploadKey;
    }
}
