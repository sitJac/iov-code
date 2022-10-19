import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.hh.ota.encrypt.GetFileSHA256.getFileSHA256;
import static com.hh.ota.encrypt.GetFileSHA256.toHex;

public class testGetSHA256 {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String filePath = "/tmp/Symbols-R3099894049694786004.ttf";
        byte[] hash = getFileSHA256(filePath);
        System.out.println(toHex(hash));
    }
}
