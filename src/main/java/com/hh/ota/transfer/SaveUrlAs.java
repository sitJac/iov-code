package com.hh.ota.transfer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.String;

public class SaveUrlAs {
    /**
     * @ download certain file from url
     * @param url url of file
     * @param filePath local path of file
     * @param method method of download: POST or GET
     * @return File
     */
    public static File saveUrlAs(String url, String filePath, String method){
        System.out.println("Start save file to------->" + filePath);
        long startTime = System.currentTimeMillis();
        //创建不同的文件夹目录
        String dirStr = filePath.substring(0, filePath.lastIndexOf("/"));
        File directory = new File(dirStr);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(filePath);
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("catch erro！！");
            return null;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Finish save file, time:" + (endTime - startTime) + "ms");
        return file;
    }
}
