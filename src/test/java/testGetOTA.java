import com.hh.ota.classuntils.OBSBucket;
import com.hh.ota.classuntils.dstFileObject;
import com.hh.ota.classuntils.srcFileObject;
import com.hh.ota.classuntils.updateObject;
import com.hh.ota.encrypt.EncryptException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hh.ota.zip.GetOTAPackage.getOTAPacket;

public class testGetOTA {

    public static void test() throws IOException, EncryptException {
        OBSBucket obs = new OBSBucket();
        obs.uploadUrl = "https://tsp-pro-vod.obs.cn-north-1.myhuaweicloud.com";
        obs.uploadToken = "LQDBAF0OLJ4WNWOWCUGT:et7DyWUSyaG3z/hFULdqZtZfOH4=:eyJleHBpcmF0aW9uIjoiMjAyMi0xMC0yNlQwNTo1MzowOC4zOTRaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLXZvZCJ9XX0=";
        obs.uploadKey = "test1019/OTA";
        obs.downloadUrl = "https://tsp-pro-vod.obs.cn-north-1.myhuaweicloud.com";
        obs.obsPath = "/home/jack_xie/files/ota_tmp/";

        List<srcFileObject> files = new ArrayList<srcFileObject>();

        srcFileObject file1 = new srcFileObject();
        file1.isEmbed = 0; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
        file1.isDemarcate = 0;
        file1.objList = new ArrayList<updateObject>();
        updateObject app1_1 = new updateObject();
        app1_1.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/ABC-PEI-TEST.hex";
        file1.objList.add(app1_1);
        files.add(file1);

        srcFileObject file2 = new srcFileObject();
        file2.isEmbed = 0; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
        file2.isDemarcate = 0;
        file2.objList = new ArrayList<updateObject>();
        updateObject app2_1 = new updateObject();
        app2_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111797195/VX1_TBOX_AG35_01642271X10H.zip";
        file2.objList.add(app2_1);
        files.add(file2);

        srcFileObject file3 = new srcFileObject();
        file3.isEmbed = 0; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
        file3.isDemarcate = 0;
        file3.objList = new ArrayList<updateObject>();
        updateObject app3_1 = new updateObject();
        app3_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111771245/VX1_TBOX_MCU_01642271X10H.s19";
        file3.objList.add(app3_1);
        files.add(file3);

        srcFileObject file4 = new srcFileObject();
        file4.isEmbed = 0; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
        file4.isDemarcate = 0;
        file4.objList = new ArrayList<updateObject>();
        updateObject app4_1 = new updateObject();
        app4_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111778356/VX1_TBOX_BLE_01642271X10H.bin";
        file4.objList.add(app4_1);
        files.add(file4);

        srcFileObject file5 = new srcFileObject();
        file5.isEmbed = 0; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
        file5.isDemarcate = 0;
        file5.objList = new ArrayList<updateObject>();
        updateObject app5_1 = new updateObject();
        app5_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2021-04-25/VX1_TBOX_BEACON_01541149X104_1619349562852.bin";
        file5.objList.add(app5_1);
        files.add(file5);

        dstFileObject dstFile = getOTAPacket(obs, "ecuName", "mainVersion", files, 1, "HONDA", 1);
        System.out.println("Return downloadUrl：" + dstFile.url);
        System.out.println("Folder size before zip:" + dstFile.oldSize + "MB");
        System.out.println("Folder size after zip:" + dstFile.newSize + "MB");

    }

    public static void main(String[] args) throws IOException, EncryptException {

        test();
//        OBSBucket obs = new OBSBucket();
//        obs.uploadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
//        obs.uploadToken = "LQDBAF0OLJ4WNWOWCUGT:UfiAP6eNrHWnVC8JOeB4o4Ucy9Y=:eyJleHBpcmF0aW9uIjoiMjAyMi0wOS0xNFQwMToyMjozMi4yNTdaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLW1ycyJ9XX0=";
//        obs.uploadKey = "test1010/OTA/";
//        obs.downloadUrl = "https://xota-test-obs.obs.cn-east-2.myhuaweicloud.com";
//        obs.obsPath = "/home/jack_xie/files/ota_tmp/";

//        List<srcFileObject> files = new ArrayList<srcFileObject>();

//        //20220919_test
//        srcFileObject file1 = new srcFileObject();
//        file1.isEmbed = 1;
//        file1.objList = new ArrayList<updateObject>();
//        updateObject app1_1 = new updateObject();
//        app1_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111787113/VX1_TBOX_MPU_01642271X10H.tar.bz2";
//        file1.objList.add(app1_1);
//        updateObject app1_2 = new updateObject();
//        app1_2.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111771245/VX1_TBOX_MCU_01642271X10H.s19";
//        file1.objList.add(app1_2);
//        files.add(file1);
//
//        srcFileObject file2 = new srcFileObject();
//        file2.isEmbed = 1;
//        file2.objList = new ArrayList<updateObject>();
//        updateObject app2_1 = new updateObject();
//        app2_1.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111797195/VX1_TBOX_AG35_01642271X10H.zip";
//        file2.objList.add(app2_1);
//        updateObject app2_2 = new updateObject();
//        app2_2.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-07-18/1658111778356/VX1_TBOX_BLE_01642271X10H.bin";
//        file2.objList.add(app2_2);
//        files.add(file2);

//        srcFileObject file1 = new srcFileObject();
//        file1.isEmbed = 1; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
//        file1.objList = new ArrayList<updateObject>();
//        updateObject app1_1 = new updateObject();
//        app1_1.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/IDCM-APP1.hex";
//        file1.objList.add(app1_1);
//        updateObject app1_2 = new updateObject();
//        app1_2.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/IDCM-APP2.hex";
//        file1.objList.add(app1_2);
//        updateObject app1_3 = new updateObject();
//        app1_3.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/IDCM-APP3.hex";
//        file1.objList.add(app1_3);
//        updateObject app1_4 = new updateObject();
//        app1_4.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/IDCM-APP4.hex";
//        file1.objList.add(app1_4);
//        files.add(file1);

//        srcFileObject file2 = new srcFileObject();
//        file2.isEmbed = 0; //if srcFileObject.isEmbed == 0 then there will be only one updateObject
//        file2.objList = new ArrayList<updateObject>();
//        updateObject app2 = new updateObject();
//        app2.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/ota/diff/new.zip";
//        file2.objList.add(app2);
//        files.add(file2);

//        srcFileObject file3 = new srcFileObject();
//        file3.isEmbed = 0;
//        file3.objList = new ArrayList<updateObject>();
//        updateObject app3 = new updateObject();
//        app3.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/01010006A101.zip";
//        file3.objList.add(app3);
//        files.add(file3);

//        srcFileObject file4 = new srcFileObject();
//        file4.isEmbed = 0;
//        file4.objList = new ArrayList<updateObject>();
//        updateObject app4 = new updateObject();
//        app4.url = "https://vsp-pro-obs.obs.cn-east-2.myhuaweicloud.com:443/2022-04-13/1649849044903/VX1_IDCM_ARM_la_system_01105122X10F.img";
//        file4.objList.add(app4);
//        files.add(file4);

//        srcFileObject file5 = new srcFileObject();
//        file5.isEmbed = 1; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
//        file5.objList = new ArrayList<updateObject>();
//        updateObject app5_1 = new updateObject();
//        app5_1.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/BDCM-APP1.hex";
//        file5.objList.add(app5_1);
//        updateObject app5_2 = new updateObject();
//        app5_2.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/BDCM-APP2.hex";
//        file5.objList.add(app5_2);
//        updateObject app5_3 = new updateObject();
//        app5_3.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/BDCM-APP3.hex";
//        file5.objList.add(app5_3);
//        updateObject app5_4 = new updateObject();
//        app5_4.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/test0901/OTA/BDCM-APP4.hex";
//        file5.objList.add(app5_4);
//        files.add(file5);
//
//        dstFileObject dstFile = getOTAPacket(obs, "ecuName", "mainVersion", files, 1, "HONDA", 1);
//        System.out.println("Return downloadUrl：" + dstFile.url);
//        System.out.println("Folder size before zip:" + dstFile.oldSize + "MB");
//        System.out.println("Folder size after zip:" + dstFile.newSize + "MB");
//    }

//    public static void main(String[] args) throws IOException, EncryptException {
//        OBSBucket obs = new OBSBucket();
//        obs.uploadUrl = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com";
//        obs.uploadToken = "LQDBAF0OLJ4WNWOWCUGT:UfiAP6eNrHWnVC8JOeB4o4Ucy9Y=:eyJleHBpcmF0aW9uIjoiMjAyMi0wOS0xNFQwMToyMjozMi4yNTdaIiwiY29uZGl0aW9ucyI6W1sic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJidWNrZXQiOiJ0c3AtcHJvLW1ycyJ9XX0=";
//        obs.uploadKey = "test0907/OTA/";
//        obs.downloadUrl = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com";
//        obs.obsPath = "/tmp/ota_tmp/";
//
//        List<srcFileObject> files = new ArrayList<srcFileObject>();
//
//        srcFileObject file1 = new srcFileObject();
//        file1.isEmbed = 1; //if srcFileObject.isEmbed == 1 then there will be more than one updateObject
//        file1.objList = new ArrayList<updateObject>();
//        updateObject app1_1 = new updateObject();
//        app1_1.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/ota/diff/new.zip";
//        file1.objList.add(app1_1);
//        updateObject app1_2 = new updateObject();
//        app1_2.url = "https://tsp-pro-mrs.obs.cn-east-2.myhuaweicloud.com/ota/diff/old.zip";
//        file1.objList.add(app1_2);
//        files.add(file1);
//
//        dstFileObject dstFile = getOTAPacket(obs, "ecuName", "mainVersion", files, 1, "HONDA", 1);
//        System.out.println("Return downloadUrl：" + dstFile.url);
//        System.out.println("Signature:" + dstFile.sign);
//        System.out.println("Folder size before zip:" + dstFile.oldSize + "MB");
//        System.out.println("Folder size after zip:" + dstFile.newSize + "MB");
    }
}
