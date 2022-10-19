package com.hh.ota.encrypt;

import com.hh.ota.encrypt.binary.Hex;
import com.hh.ota.encrypt.binary.Base64;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    private static final String ENCODING_CODE = "UTF-8";
    private static final String SECURE_RANDOM = "SHA1PRNG";
    private static final String AES_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * aes解密
     *
     * @param content 要解密的密文
     * @return 解密后的明文
     * @throws EncryptException 自定义异常
     */
    public static String decrypt(String content,String key) throws EncryptException {
        try {
            String ivStr = content.substring(0, 32);
            String message = content.substring(32);

            SecretKeySpec sKeySpec = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
            Cipher cipher = Cipher.getInstance(AES_PADDING);
            IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(ivStr.toCharArray()));
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(message));
            return new String(original, ENCODING_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-1006", e);
        }
    }

    /**
     * aes加密
     *
     * @param content 要加密的密文
     * @return 加密后的密文
     * @throws EncryptException 自定义异常
     */
    public static String encrypt(String content,String key) throws EncryptException {
        try {
            Cipher cipher = Cipher.getInstance(AES_PADDING);
            SecretKeySpec sKeySpec = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
            IvParameterSpec iv = getSecretIv(16);
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING_CODE));
            return new String(Hex.encodeHex(iv.getIV())).concat(Base64.encodeBase64String(encrypted));
        } catch (Exception e) {
            throw new EncryptException("-1007", e);
        }
    }

    private static IvParameterSpec getSecretIv(int length) {
        byte[] ivBytes = new byte[length];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM);
            secureRandom.nextBytes(ivBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(ivBytes);
    }

    public static String getAESSecureKey() {
        String aesKey = "";

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            aesKey = Hex.encodeHexString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }

        return aesKey;
    }

    public static void keytoFile(String key, String keyPath) throws IOException {
        FileWriter writer;
        String dirStr = "/tmp/ota_tmp";
        File directory = new File(dirStr);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        try {
            writer = new FileWriter(keyPath);
            writer.write(key);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate aes key return and write into file
     * @param model vehicle model year
     * @param env   vehicle environment
     * @return string of aes key
     */
    public static String genKey(String model, int env) throws IOException {
        String key = "";
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = ModelEnv.randomModelEnv(model, env);
            keyGenerator.init(256, random);
            SecretKey secretKey = keyGenerator.generateKey();
            key = Hex.encodeHexString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }
        keytoFile(key, "/home/jack_xie/ota_sdk/src/ota_mgr/verify/aes_key_enc_dec/key.txt");
        return key;
    }
}