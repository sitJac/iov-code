import com.hh.ota.ftp.FtpUtils;

import java.io.ByteArrayInputStream;

public class testFTP {

    public static void main(String[] args) {
        String str = "ota_test";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes());
        FtpUtils.uploadFile("/ota_test", "/home/jack_xie/ota-update/target/ota-update-1.5-SNAPSHOT.jar", inputStream);
    }
}
