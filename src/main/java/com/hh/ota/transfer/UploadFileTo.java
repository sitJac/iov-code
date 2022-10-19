package com.hh.ota.transfer;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadFileTo {
    public static String uploadFileToObs(String downloadUrl, String uploadUrl, String uploadToken, String uploadCatalog, String localFilePath) throws IOException {
        String uploadName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + "_" + localFilePath.substring(localFilePath.lastIndexOf("/") + 1);
        String uploadKey = uploadCatalog + uploadName;
        System.out.println("UploadKey:" + uploadKey);
        HttpPost httpPost = new HttpPost(uploadUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("token", uploadToken);
        builder.addTextBody("key",uploadKey);
        builder.addBinaryBody("file", new File(localFilePath));
        httpPost.setEntity(builder.build());
        HttpClients.createDefault().execute(httpPost);
        System.out.println("upload success!");
        return downloadUrl + '/' + uploadKey;
    }
}
