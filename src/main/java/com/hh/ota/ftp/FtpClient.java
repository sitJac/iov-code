package com.hh.ota.ftp;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

public class FtpClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(FtpClient.class);
    /**
     * FTP服务地址
     */
    private static String FTP_IP = "10.131.175.73";
    /**
     * FTP端口号
     */
    private static Integer FTP_PORT = 21112;
    /**
     * FTP用户名
     */
    private static String FTP_USER = "admin";
    /**
     * FTP密码
     */
    private static String FTP_PASSWORD = "EP7fXKdniOvRfl3p";
    /**
     * FTP根路径
     */
    private static String FTP_PATH = "/";
    /**
     * 映射盘符
     */
    private static String FTP_DRIVELETTER;
    private static FTPClient ftpClient;

    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = FtpClient.class.getClassLoader().getResourceAsStream("ftp-config.properties");
            properties.load(inputStream);
            FTP_IP = properties.getProperty("FTP_IP");
            FTP_PORT = Integer.valueOf(properties.getProperty("FTP_PORT"));
            FTP_USER = properties.getProperty("FTP_USER");
            FTP_PASSWORD = properties.getProperty("FTP_PASSWORD");
            FTP_PATH = properties.getProperty("FTP_PATH");
            FTP_DRIVELETTER = properties.getProperty("FTP_DRIVELETTER");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FTPClient getFTPConnection() {
        ftpClient = new FTPClient();
        try {
            //连接
            ftpClient.connect(FtpClient.FTP_IP, FtpClient.FTP_PORT);
            //设置编码
            ftpClient.setControlEncoding("UTF-8");
            //设置传输文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //登录
            ftpClient.login(FtpClient.FTP_USER, FtpClient.FTP_PASSWORD);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
//                LOGGER.info("==============未连接到FTP，用户名或密码错误=================");
                //拒绝连接
                ftpClient.disconnect();
            } else {
//                LOGGER.info("==============连接到FTP成功=================");
            }
        } catch (SocketException e) {
            e.printStackTrace();
//            LOGGER.info("==============FTP的IP地址错误==============");
        } catch (IOException e) {
            e.printStackTrace();
//            LOGGER.info("==============FTP的端口错误==============");
        }
        return ftpClient;
    }

    public static void closeConnect() {
        LOGGER.warn("关闭ftp服务器");
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


