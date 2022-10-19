package com.hh.ota.transfer;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiUpload {
    public static String MultiUploadBigFile(String localFilePath, String uploadCatalog, String downloadUrl){
        String endPoint = "obs.cn-east-2.myhuaweicloud.com";
        String ak = "TPAXR63I4QFDDOCJSENW";
        String sk = "jyVO7dzcG4mDislo7EXVrQ99PuV9nzfaASXIzI6r";
        final String bucketName = "xota-test-obs";

        // 创建ObsClient实例
        final ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        // UUID拼接name
        String uploadName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + "_" + localFilePath.substring(localFilePath.lastIndexOf("/") + 1);
        final String uploadKey = uploadCatalog + uploadName;

        // 初始化线程池
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final File largeFile = new File(localFilePath);

        // 初始化分段上传任务(ObsClient.initiateMultipartUpload)
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, uploadKey);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);

        // 逐个或并行上传段 (ObsClient.uploadPart)
        final String uploadId = result.getUploadId();
        System.out.println("\t"+ uploadId + "\n");

        // 每段上传100MB
        long partSize = 100 * 1024 * 1024L;
        long fileSize = largeFile.length();

        // 计算需要上传的段数
        long partCount = fileSize % partSize == 0 ? fileSize / partSize : fileSize / partSize + 1;
        System.out.println("count of upload parts:" + partCount);

        final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<PartEtag>());

        // 执行并发上传段
        for (int i = 0; i < partCount; i++) {
        // 分段在文件中的起始位置
        final long offset = i * partSize;
        // 分段大小
        final long currPartSize = (i + 1 == partCount) ? fileSize - offset : partSize;
        // 分段号
        final int partNumber = i + 1;
        executorService.execute(new Runnable()
        {
            @Override
            public void run()
            {
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setObjectKey(uploadKey);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setFile(largeFile);
                uploadPartRequest.setPartSize(currPartSize);
                uploadPartRequest.setOffset(offset);
                uploadPartRequest.setPartNumber(partNumber);

                UploadPartResult uploadPartResult;
                try
                {
                    uploadPartResult = obsClient.uploadPart(uploadPartRequest);
                    System.out.println("Part#" + partNumber + " done\n");
                    partEtags.add(new PartEtag(uploadPartResult.getEtag(), uploadPartResult.getPartNumber()));
                }
                catch (ObsException e)
                {
                    e.printStackTrace();
                }
            }
        });
        }

        // 等待上传完成
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
        try
        {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        }
        //合并段 (ObsClient.completeMultipartUpload)
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, uploadKey, uploadId, partEtags);
        obsClient.completeMultipartUpload(completeMultipartUploadRequest);
        return downloadUrl + '/' + uploadKey;
    }
}
