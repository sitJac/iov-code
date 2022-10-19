import com.hh.ota.classuntils.OBSBucket;
import com.hh.ota.classuntils.dstFileObject;
import com.hh.ota.diff.DiffFile;
import com.hh.ota.encrypt.EncryptException;
import com.hh.ota.patch.PatchFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.hh.ota.diff.DiffFile.diffFile;
import static com.hh.ota.patch.PatchFile.patchFile;

public class testDiffPatch {
    public static void testDiff() throws IOException, EncryptException, NoSuchAlgorithmException {
        OBSBucket obs = new OBSBucket();

        obs.uploadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
        obs.uploadToken = "LQDBAF0OLJ4WNWOWCUGT:UfiAP6eNrHWnVC8JOeB4o4Ucy9Y=:eyJleHBpcmF0aW9uIjoiMjAyMi0wOS0xNFQwMToyMjozMi4yNTdaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLW1ycyJ9XX0=";
        obs.uploadKey = "otatest/20221010/";
        obs.downloadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
        obs.obsPath = "/tmp/ota_tmp1/";

//        String newFilePath = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-04-13/1649849044903/VX1_IDCM_ARM_la_system_01105122X10F.img";
//        String oldFilePath = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2021-12-24/VX1_IDCM_ARM_la_system_01105110X10D_1640313647591.img";

        String newFilePath = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com/otatest/testfiles/d3eb600ab2b5416c8f00b61dc69cb017_new.zip";
        String oldFilePath = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com/otatest/testfiles/6cca11669b1f400b9bec2ad17605f011_old.zip";

        dstFileObject dstFile = diffFile(obs, oldFilePath, newFilePath);
        System.out.println("return downloadUrl:" + dstFile.url);
        System.out.println("return size:" + dstFile.size + "MB");
    }

    public static void testPatch() throws IOException, EncryptException, NoSuchAlgorithmException {
        OBSBucket obs = new OBSBucket();
        obs.uploadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
        obs.uploadToken = "LQDBAF0OLJ4WNWOWCUGT:UfiAP6eNrHWnVC8JOeB4o4Ucy9Y=:eyJleHBpcmF0aW9uIjoiMjAyMi0wOS0xNFQwMToyMjozMi4yNTdaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLW1ycyJ9XX0=";
        obs.uploadKey = "otatest/20221010/";
        obs.downloadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
//        obs.obsPath = "/tmp/ota_tmp1/";

        String oldFilePatch = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com/otatest/testfiles/6cca11669b1f400b9bec2ad17605f011_old.zip";
        String diffFilePath = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com/otatest/20221010/7ad35d14b57f4d288ff51ee3c3d8e8d7_diff_d3eb600ab2b5416c8f00b61dc69cb017_new.zip";
        dstFileObject dstFile = patchFile(obs, oldFilePatch, diffFilePath);
        System.out.println("return downloadUrl:" + dstFile.url);
        System.out.println("return size:" + dstFile.size + "MB");
    }
    
    public static void main(String[] args) throws IOException, EncryptException, NoSuchAlgorithmException {
        long startTime = System.currentTimeMillis();

//        testDiff();
        testPatch();

        long endTime = System.currentTimeMillis();

        System.out.println("MAIN FUNCTION RUN TIME：" + (endTime - startTime) + "ms");
    }
}
