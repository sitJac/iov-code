package com.hh.ota.zip;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hh.ota.classuntils.*;
import com.hh.ota.encrypt.EncryptException;
import com.hh.ota.signature.SignatureMain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Signature;
import java.util.*;

import static com.hh.ota.cache.LocalFile.*;
import static com.hh.ota.encrypt.SHA256withRSA.generateKeyBytes;
import static com.hh.ota.transfer.MultiUpload.MultiUploadBigFile;
import static com.hh.ota.transfer.SaveUrlAs.saveUrlAs;
import static com.hh.ota.transfer.UploadFileTo.uploadFileToObs;

public class GetOTAPackage {
    /**
     * generate OTA package
     * @param obs: Huawei Cloud OBS bucket
     * @param ecuName: ECU name String
     * @param mainVersion: ECU main version String
     * @param files: list of files that need to be compressed, List<srcFileObject>
     * @param sign: 1:need to signature   0:don't need signature
     * @param model: model of vehicle year
     * @param env: distinct environment
     * @return compressed dstFileObject
     */
    public static dstFileObject getOTAPacket(OBSBucket obs, String ecuName, String mainVersion, List<srcFileObject> files, int sign, String model, int env) throws IOException, EncryptException {
        int cnt = 0;
        //local cache folder
        String LOCAL_FOLDER = obs.obsPath;
        if(LOCAL_FOLDER == null || LOCAL_FOLDER == "") {
            LOCAL_FOLDER = "/tmp/ota_tmp/";
        }

        String MultiThread = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String newFolder = LOCAL_FOLDER + MultiThread + "/new/";
        File newDirectory = new File(newFolder);
        newDirectory.mkdirs();

        String oldFolder = LOCAL_FOLDER + MultiThread + "/old/";
        File oldDirectory = new File(oldFolder);
        newDirectory.mkdirs();

        //download hex files form obs server
        for (srcFileObject file : files) {
            if(file.isEmbed == 1) { //if embed, zip first
                String folderName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                String embedFolder = oldFolder + folderName;
                File directory = new File(embedFolder);
                directory.mkdirs();
                for (updateObject app : file.objList) {
                    String fileName = app.url.substring(app.url.lastIndexOf("/") + 1);     //download file name
                    String filePath = embedFolder + "/" + fileName;                              //download file path
                    if (null == saveUrlAs(app.url, filePath, "GET")) {
                        System.out.println("URL erro, the specified key does not exist, program terminated: " + app.url);
                        return null;
                    }
                    cnt++;
                }
                MainZip embedZip = new MainZip();
                embedZip.SOURCE_FOLDER = embedFolder;
                embedZip.OUTPUT_ZIP_FILE = newFolder + folderName + ".zip";
                embedZip.generateFileList(new File(embedZip.SOURCE_FOLDER));
                embedZip.zipIt(embedZip.OUTPUT_ZIP_FILE);
            }
            else {  //if ethernet, no need to zip, download directly
                String fileName = file.objList.get(0).url.substring(file.objList.get(0).url.lastIndexOf("/") + 1);
                String oldFilePath = oldFolder + fileName;
                String newFilePath = newFolder + fileName;
                if (null == saveUrlAs(file.objList.get(0).url, oldFilePath, "GET")) {
                    System.out.println("URL erro, the specified key does not exist, program terminated: " + file.objList.get(0).url);
                    return null;
                }
                if (null == saveUrlAs(file.objList.get(0).url, newFilePath , "GET")) {
                    System.out.println("URL erro, the specified key does not exist, program terminated: " + file.objList.get(0).url);
                    return null;
                }
                cnt++;
            }
        }

        //add manifest file to ota package
        String manifestDir = newFolder + "manifest.json";

        Manifest mf = new Manifest();
        mf.updateObj = "TBOX";
        mf.packType = 1;
        mf.srcVer = "FHD1539MA-02-CN-20161025000000";
        mf.dstVer = "FHD1539MA-02-CN-20161025000001";
        mf.packNum = 3;

        List<Manifest.packInformation> pis = new ArrayList<>();

        Manifest.packInformation pi1 = mf.new packInformation();
        pi1.updateObj = "BootLoader";
        pi1.packType = 1;
        pi1.srcVer = "Boot-02-CN-2020071600000";
        pi1.dstVer = "Boot-02-CN-2020071600001";
        pi1.fileName = "bootloader.zip";
        pi1.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi1.fileSize = (long)1024;
        pis.add(pi1);

        Manifest.packInformation pi2 = mf.new packInformation();
        pi2.updateObj = "MCU";
        pi2.packType = 1;
        pi2.srcVer = "MCU-02-CN-20200716000000";
        pi2.dstVer = "MCU-02-CN-20200716000001";
        pi2.fileName = "MCU.zip";
        pi2.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi2.fileSize = (long)1024;
        pis.add(pi2);

        Manifest.packInformation pi3 = mf.new packInformation();
        pi3.updateObj = "MPU";
        pi3.packType = 1;
        pi3.srcVer = "MPU-02-CN-20200716000000";
        pi3.dstVer = "MPU-02-CN-20200716000001";
        pi3.fileName = "MPU.zip";
        pi3.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi3.fileSize = (long)1024;
        pis.add(pi3);

        mf.setPackInfos(pis);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        System.out.println(gson.toJson(mf));
        String jsonString = gson.toJson(mf);
        StringToFile(jsonString, manifestDir);

        //add public key to ota package
        String pubKeyDir = newFolder + "pub.crt";
        Map<String, String> keyMap = generateKeyBytes();
        String pub = keyMap.get("PUBLIC_KEY");
        StringToFile(pub, pubKeyDir);

        //compress OTA Package
        MainZip appZip = new MainZip();
        appZip.SOURCE_FOLDER = LOCAL_FOLDER + MultiThread + "/new";
        appZip.OUTPUT_ZIP_FILE = LOCAL_FOLDER + MultiThread + "/OTA.zip";
        appZip.generateFileList(new File(appZip.SOURCE_FOLDER));
        appZip.zipIt(appZip.OUTPUT_ZIP_FILE);
        obs.obsPath = LOCAL_FOLDER + MultiThread + "/OTA.zip";

        //upload compressed files to obs server
        String uploadKey = MultiUploadBigFile(appZip.OUTPUT_ZIP_FILE, obs.uploadKey, obs.downloadUrl);
//        String uploadKey = uploadFileToObs(obs.downloadUrl, obs.uploadUrl, obs.uploadToken, obs.uploadKey, appZip.OUTPUT_ZIP_FILE);

        //return dstFileObject
        dstFileObject dstFile = new dstFileObject();
        dstFile.ecuName = ecuName;
        dstFile.url = uploadKey;
        dstFile.sign = SignatureMain.sign(model, env, obs.obsPath);
        dstFile.newSize = getFileSize(new File(appZip.OUTPUT_ZIP_FILE));
        dstFile.oldSize = getFolderSize(new File(oldFolder));

//        //delete local cache folder
//        String dirStr = LOCAL_FOLDER + MultiThread;
//        File directory = new File(dirStr);
//        deleteFile(directory);

        return dstFile;
    }
}
