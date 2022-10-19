import java.io.IOException;

import static com.hh.ota.transfer.UploadFileTo.uploadFileToObs;

public class testUploadFileTo {
    public static void main(String[] args) throws IOException {
        String uploadUrl = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com";
        String uploadToken = "LQDBAF0OLJ4WNWOWCUGT:Vg5363zKpVYVkOhOoJ+hpf7QgqU=:eyJleHBpcmF0aW9uIjoiMjAyMi0wOS0wMVQwOTozNDowMC41NzVaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLW1ycyJ9XX0=";
        String uploadCatalog = "idcm/ota/package/integration/20221010test/";
        String localFilePath = "/home/jack_xie/codes_backup/process_files/TBOX.zip";
        String downloadUrl = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com";
        String returnUrl = uploadFileToObs(downloadUrl, uploadUrl, uploadToken, uploadCatalog, localFilePath);
        System.out.println("returnUrl = " + returnUrl);
    }
}
