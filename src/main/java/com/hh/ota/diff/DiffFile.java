package com.hh.ota.diff;

import com.hh.ota.cache.LocalFile;
import com.hh.ota.classuntils.OBSBucket;
import com.hh.ota.classuntils.dstFileObject;
import com.hh.ota.encrypt.EncryptException;

import java.io.*;
import java.security.NoSuchAlgorithmException;

import static com.hh.ota.cache.LocalFile.deleteFile;
import static com.hh.ota.cache.LocalFile.getFileSize;
import static com.hh.ota.encrypt.GetFileSHA256.getFileSHA256;
import static com.hh.ota.encrypt.GetFileSHA256.toHex;
import static com.hh.ota.transfer.MultiUpload.MultiUploadBigFile;
import static com.hh.ota.transfer.SaveUrlAs.saveUrlAs;
import static com.hh.ota.transfer.UploadFileTo.uploadFileToObs;

public class DiffFile {
    /**
     * 差分
     * @param oldFile 原始文件
     * @param newFile 目标文件
     * @param diffFile 差分文件
     * @throws FileNotFoundException oldFile或newFile不存在
     */
    public static void diff(String oldFile, String newFile, String diffFile) throws FileNotFoundException {
        LocalFile.requireFileExist(oldFile);
        LocalFile.requireFileExist(newFile);
        HDiff.INSTANCE.hh_ota_diff(oldFile, newFile, diffFile, 1024*1024*1024*1024*128);
    }

    /**
     * diff local old zip file and new zip file from obs, and upload diff file to obs
     * @param obs to upload or download filr form obs
     * @param oldFilePath ola file's local path
     * @param newFilePath new file's local path
     * @return dstFileObject
     * @throws IOException
     */
    public static dstFileObject diffFile(OBSBucket obs, String oldFilePath, String newFilePath) throws IOException, EncryptException, NoSuchAlgorithmException {
        String LOCAL_FOLDER = obs.obsPath;
        if(LOCAL_FOLDER == null || LOCAL_FOLDER == "") {
            LOCAL_FOLDER = "/tmp/ota_tmp/";
        }
        //download new file and old file to local folder
        String newFileName = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
        String newFileLocalPath = LOCAL_FOLDER + newFileName;
        saveUrlAs(newFilePath, newFileLocalPath, "GET");
        String oldFileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
        String oldFileLocalPath = LOCAL_FOLDER + oldFileName;
        saveUrlAs(oldFilePath, oldFileLocalPath, "GET");

        //diff file
        obs.obsPath = LOCAL_FOLDER + "diff_" + newFileName;
        try {
            diff(oldFileLocalPath, newFileLocalPath, obs.obsPath);//差分
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload file to obs
        String downloadUrl = MultiUploadBigFile(obs.obsPath, obs.uploadKey, obs.downloadUrl);

        //return dstFileObject
        dstFileObject retObj = new dstFileObject();
        retObj.ecuName = newFileName.substring(0, newFileName.lastIndexOf("."));
        retObj.sign = String.valueOf(toHex(getFileSHA256(obs.obsPath)));
        System.out.println("obs Path:" + obs.obsPath);
        retObj.size = getFileSize(new File(obs.obsPath));
        //拼接到文件名
        retObj.url = downloadUrl;

        //delete local cache folder
        File directory = new File(LOCAL_FOLDER);
        deleteFile(directory);
        return retObj;
    }
}
