package com.hh.ota.encrypt;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * 计算文件sha256值
 */
public class GetFileSHA256 {

    public static byte[] getFileSHA256(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        int bufferSize = 16384;
        byte [] buffer = new byte[bufferSize];
        int sizeRead = -1;
        while ((sizeRead = in.read(buffer)) != -1) {
            digest.update(buffer, 0, sizeRead);
        }
        in.close();
        byte [] hash = null;
        hash = new byte[digest.getDigestLength()];
        hash = digest.digest();
        return hash;
    }

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i]);
            if (hex.length() == 1) {
                sb.append("0");
            } else if (hex.length() == 8) {
                hex = hex.substring(6);
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }
}