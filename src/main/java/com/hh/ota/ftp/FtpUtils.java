package com.hh.ota.ftp;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
public class FtpUtils {
    /**
     * 上传单个文件到远程
     *
     * @param ftpPath        路径
     * @param uploadFileName 文件名
     * @param input          输入流
     * @return
     */
    public static boolean uploadFile(String ftpPath, String uploadFileName, InputStream input) {
        boolean issuccess = false;
        FTPClient ftpClient = null;
        try {
            ftpClient = FtpClient.getFTPConnection();

            //切换到工作目录

            if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                ftpClient.changeWorkingDirectory("/");
                String[] dirs = ftpPath.split("/");
                for (String dir : dirs) {
                    ftpClient.makeDirectory(dir);
                    ftpClient.changeWorkingDirectory(dir);
                }
            }

            ftpClient.enterLocalPassiveMode();//工作模式被动
            //文件写入 远程文件名；输入流
            boolean storeFile = ftpClient.storeFile(uploadFileName, input);
            if (!storeFile) {
                return issuccess;
            }
            input.close();
            ftpClient.logout();
            issuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return issuccess;
    }

    private String inputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String oneLine = "";
        try {
            while ((oneLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(oneLine);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
