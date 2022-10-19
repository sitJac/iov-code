package com.hh.ota.signature;

import com.hh.ota.encrypt.Encrypt;
import com.hh.ota.encrypt.EncryptException;
import com.hh.ota.encrypt.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jack Xie
 * @time 2022/8/4
 * @email sitjac2018@outlook.com
 */
public class SignatureMain{
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 还原公钥
     * @param publicKey 公钥串
     * @return PublicKey
     */
    public static PublicKey restorePublicKey(String publicKey) throws EncryptException {
        byte[] prikeyByte;
        PublicKey pubTypeKey = null;
        try {
            prikeyByte = Base64.decodeBase64(publicKey.getBytes(StandardCharsets.UTF_8));
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(prikeyByte);
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

            pubTypeKey = factory.generatePublic(x509EncodedKeySpec);
            return pubTypeKey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10000", e);
        }
    }

    /**
     * 还原私钥
     * @param privateKey 私钥串
     * @return PrivateKey
     */
    public static PrivateKey restorePrivateKey(String privateKey) throws EncryptException {
        byte[] prikeyByte;
        PrivateKey priTypeKey;
        try {
            prikeyByte = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.UTF_8));
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prikeyByte);
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            priTypeKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return priTypeKey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10001", e);
        }
    }

    /**
     * @brief generate signature via rsa algorithm
     * @param model: model of vehicle year
     * @param env: distinct environment
     * @param filePath: path of file that needs to generate rsa signature
     * @return signature value
     */
    public static String sign(String model, int env, String filePath) throws EncryptException {
        try {
//            SecureRandom random_model_env = model_env.random_model_env(model, env);
            PrivateKey restorePrivateKey = restorePrivateKey(Encrypt.PRIVATE_KEY);
            Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            sign.initSign(restorePrivateKey);
            sign.update(filePath.getBytes(StandardCharsets.UTF_8));
            byte[] signByte = sign.sign();
            return Base64.encodeBase64String(signByte);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10003", e);
        }
    }

    /**
     * @brief verify rsa signature
     * @param model: model of vehicle year
     * @param env: distinct environment
     * @param filePath: path of file that needs to verify rsa signature
     * @param sign: signature value
     * @return false if failed, otherwise success
     */
    public static boolean verifySign(String model, int env, String filePath, String sign) throws EncryptException {
        try {
            PublicKey publicTypeKey = restorePublicKey(Encrypt.PUBLIC_KEY);
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicTypeKey);
            verifySign.update(filePath.getBytes(StandardCharsets.UTF_8));
            return verifySign.verify(Base64.decodeBase64(sign.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10002", e);
        }
    }

    /**
     * 生成公钥和私钥
     * @return Map of Public Key and Private Key>
     */
    public static Map<String, String> generateKeyBytes() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("PUBLIC_KEY", Base64.encodeBase64String(publicKey.getEncoded()));
            keyMap.put("PRIVATE_KEY", Base64.encodeBase64String(privateKey.getEncoded()));
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Verify signature failed!");
        }
        return null;
    }
}
