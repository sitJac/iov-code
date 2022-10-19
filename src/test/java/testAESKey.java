import com.hh.ota.encrypt.EncryptException;

import java.io.IOException;

import static com.hh.ota.encrypt.AesUtil.decrypt;
import static com.hh.ota.encrypt.AesUtil.encrypt;
import static com.hh.ota.encrypt.AesUtil.genKey;

public class testAESKey {
    public static void main(String args[]) throws IOException {
        String key = genKey("HONDA", 1);
        System.out.println("Key:" + key);
        String content = "1234567890123456";
        try {
            String encrypt = encrypt(content, key);
            System.out.println("Encrypt:" + encrypt);
            String decrypt = decrypt(encrypt, key);
            System.out.println("Decrypt:" + decrypt);
        } catch (EncryptException e) {
            e.printStackTrace();
        }
    }
}
