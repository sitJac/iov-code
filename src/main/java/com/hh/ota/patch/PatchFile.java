package com.hh.ota.patch;

import com.hh.ota.cache.LocalFile;
import com.hh.ota.classuntils.OBSBucket;
import com.hh.ota.classuntils.dstFileObject;
import com.hh.ota.diff.DiffFile;
import com.hh.ota.encrypt.EncryptException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.hh.ota.cache.LocalFile.deleteFile;
import static com.hh.ota.cache.LocalFile.getFileSize;
import static com.hh.ota.encrypt.GetFileSHA256.getFileSHA256;
import static com.hh.ota.encrypt.GetFileSHA256.toHex;
import static com.hh.ota.transfer.MultiUpload.MultiUploadBigFile;
import static com.hh.ota.transfer.SaveUrlAs.saveUrlAs;
import static com.hh.ota.transfer.UploadFileTo.uploadFileToObs;

public class PatchFile {
    /**
     * 还原
     * @param oldPath 原始文件地址
     * @param diffPath 差分文件地址
     * @param outNewPath 还原新生成文件地址
     * @throws FileNotFoundException oldFile或patchFile不存在
     */
    public static void patch(String diffPath, String oldPath, String outNewPath) throws FileNotFoundException {
        LocalFile.requireFileExist(oldPath);
        LocalFile.requireFileExist(diffPath);
        HPatch.INSTANCE.hh_ota_patch(diffPath, oldPath, outNewPath);
    }

    /**
     * patch local old file and diff file from obs, and upload patch file to obs
     * @param obs
     * @param oldFilePatch
     * @param diffFilePath
     * @return
     * @throws IOException
     */
    public static dstFileObject patchFile(OBSBucket obs, String oldFilePatch, String diffFilePath) throws IOException, EncryptException, NoSuchAlgorithmException {
        String LOCAL_FOLDER = obs.obsPath;
        if(LOCAL_FOLDER == null || LOCAL_FOLDER == "") {
            LOCAL_FOLDER = "/tmp/ota_tmp/";
        }
        //download old file and diff file to local folder
        String oldFileName = oldFilePatch.substring(oldFilePatch.lastIndexOf("/")+1);
        String oldFileLocalPath = LOCAL_FOLDER + oldFileName;
        saveUrlAs(oldFilePatch, oldFileLocalPath, "GET");
        String diffFileName = diffFilePath.substring(diffFilePath.lastIndexOf("/")+1);
        String diffFileLocalPath = LOCAL_FOLDER + diffFileName;
        saveUrlAs(diffFilePath, diffFileLocalPath, "GET");

        //patch file
        obs.obsPath = LOCAL_FOLDER + "patch_" + oldFileName;
        try {
            patch(diffFileLocalPath, oldFileLocalPath, obs.obsPath);//差分
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //upload file to obs
        String uploadKey = MultiUploadBigFile(obs.obsPath, obs.uploadKey, obs.downloadUrl);

        //return dstFileObject
        dstFileObject retObj = new dstFileObject();
        retObj.ecuName = oldFileName.substring(0, oldFileName.lastIndexOf("."));
        retObj.sign = String.valueOf(toHex(getFileSHA256(obs.obsPath)));
//        retObj.sign = signature.sign("***HONDA***", 1, obs.obsPath);  //how to transfer model and enviroment to generate signature
        retObj.size = getFileSize(new File(obs.obsPath));
        retObj.url = obs.downloadUrl + "/" + uploadKey;

        //delete local cache folder
        File directory = new File(LOCAL_FOLDER);
        deleteFile(directory);

        return retObj;
    }
}
