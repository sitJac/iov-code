import static com.hh.ota.transfer.MultiUpload.*;

public class testBigUpload {
    public static void main(String[] args) {
        String filePath = "/home/jack_xie/files/aoyun/zip/old.zip";
        String uploadCatalog = "otatest/testfiles/";
        String downloadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
        String returnString = MultiUploadBigFile(filePath, uploadCatalog, downloadUrl);
        System.out.println("return string is " + returnString);
    }
}
