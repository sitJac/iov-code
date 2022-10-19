package com.hh.ota.encrypt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
 * @author LinDingQiang
 * @time 2020/9/4 14:54
 * @email dingqiang.l@verifone.cn
 */
public class SHA256withRSA {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 还原公钥
     *
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
     *
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
     * 签名
     *
     * @param privateKey 私钥
     * @param plainText  明文
     * @return 签名后的签名串
     */
    public static String sign(String privateKey, String plainText) throws EncryptException {
        try {
            PrivateKey restorePrivateKey = restorePrivateKey(privateKey);
            Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            sign.initSign(restorePrivateKey);
            sign.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] signByte = sign.sign();
            return Base64.encodeBase64String(signByte);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10003", e);
        }
    }

    /**
     * 验签
     *
     * @param publicKey  公钥
     * @param plainText  明文
     * @param signedText 签名
     */
    public static boolean verifySign(String publicKey, String plainText, String signedText) throws EncryptException {
        try {
            PublicKey publicTypeKey = restorePublicKey(publicKey);
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicTypeKey);
            verifySign.update(plainText.getBytes(StandardCharsets.UTF_8));
            return verifySign.verify(Base64.decodeBase64(signedText.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new EncryptException("-10002", e);
        }
    }


    /**
     * 生成密钥对
     *
     * @return
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
            System.out.println("签名验证失败");

        }
        return null;
    }

    public static void main(String[] args) throws EncryptException {
//        Map<String, String> map = generateKeyBytes();
//        String public_key = map.get("PUBLIC_KEY");
//        String private_key = map.get("PRIVATE_KEY");
//        System.out.println("公钥：" + public_key);
//        System.out.println("私钥：" + private_key);
        String plainText = "1234";

        JsonObject busDataJson = new JsonObject();
        busDataJson.addProperty("plainText", plainText);
        String signInfo = SHA256withRSA.sign(Encrypt.PRIVATE_KEY, plainText);
        busDataJson.addProperty("signInfo", signInfo);
        System.out.println("签名信息：" + signInfo);
        /*
        1、客户端和服务端双方约定好公私钥对，即公钥PUBLIC_KEY，私钥PRIVATE_KEY
        2、客户端使用私钥PRIVATE_KEY对数据进行签名
        3、客户端生成一个随机密钥KEY，并且使用公钥PUBLIC_KEY对其RSA加密得到加密后的密钥ENCRYPT_KEY
        4、客户端使用加密后的密钥ENCRYPT_KEY对数据（包括签名信息）进行AES加密得到加密数据
        5、将加密后的数据以及加密后的密钥发送给服务端
         */

        String aesKey = AesUtil.getAESSecureKey();
        System.out.println("密钥明文：" + aesKey);

        String encryptedAesKey = RsaUtil.encrypt(aesKey, Encrypt.PUBLIC_KEY);
        System.out.println("密钥密文：" + encryptedAesKey);

        String encrypt = AesUtil.encrypt(busDataJson.toString(), aesKey);
        System.out.println("加密数据：" + encrypt);

        JsonObject root = new JsonObject();
        root.addProperty("encryptKey", encryptedAesKey);
        root.addProperty("busData", encrypt);
        System.out.println("发送数据："+root.toString());

        System.out.println("======================以下是解密=======================");
/*

        1、服务端用私钥PRIVATE_KEY对加密后的密钥进行RSA解密，得到解密后的密钥DECRPTY_KEY
        2、服务端使用解密后的密钥DECRPTY_KEY对加密数据进行解密
        3、服务端利用SHA256withRSA算法，用DECRPTY_KEY对解密后的签名信息验签
        4、客户端解密同理

 */

        String encryptKey = root.get("encryptKey").getAsString();
        String decryptKey = RsaUtil.decrypt(encryptKey, Encrypt.PRIVATE_KEY);
        System.out.println("解密后的密钥：" + decryptKey);

        String busData = root.get("busData").getAsString();
        String decryptBusData = AesUtil.decrypt(busData, decryptKey);

        JsonObject respDataJsonObject = JsonParser.parseString(decryptBusData).getAsJsonObject();
        System.out.println("解密后的数据：" + respDataJsonObject.toString());

        String decryptPlaitText = respDataJsonObject.get("plainText").getAsString();
        System.out.println(decryptPlaitText);
        String decryptSignInfo = respDataJsonObject.get("signInfo").getAsString();
        System.out.println(decryptSignInfo);
        boolean verifySign = SHA256withRSA.verifySign(Encrypt.PUBLIC_KEY, decryptPlaitText, decryptSignInfo);
        System.out.println("是否验签通过：" + verifySign);

    }
}
