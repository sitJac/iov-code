import java.io.File;

import static com.hh.ota.transfer.SaveUrlAs.saveUrlAs;

public class testSaveUrlAs {
    public static void main(String[] args)
    {
        saveUrlAs("https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/ABC-PEI-TEST.hex", "/home/jack_xie/files/folder1019/ABC-PEI-TEST.hex", "GET");
//        saveUrlAs("https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/idcm/ota/package/integration/20220829test/old.zip", "/tmp/ota_tmp/old.zip", "GET");
    }
}
