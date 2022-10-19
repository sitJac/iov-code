import com.hh.ota.encrypt.EncryptException;

import java.util.Map;

import static com.hh.ota.encrypt.SHA256withRSA.generateKeyBytes;
import static com.hh.ota.signature.SignatureMain.sign;
import static com.hh.ota.signature.SignatureMain.verifySign;

public class testSignature {
    public static void main(String[] args) throws EncryptException {
        Map<String, String> keyMap = generateKeyBytes();
        System.out.println("Public Key:" + keyMap.get("PUBLIC_KEY"));
        System.out.println("Private Key:" + keyMap.get("PRIVATE_KEY"));
        String sign = sign("HONDA", 1, "/home/jack_xie/cloud_api_java/test.zip");
        System.out.println("Signature:" + sign);
        boolean result = verifySign("HONDA", 1, "/home/jack_xie/cloud_api_java/test.zip", sign);
        System.out.println(result);
    }
}
