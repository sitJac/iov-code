import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.hh.ota.encrypt.SHA256withRSA.generateKeyBytes;

public class testManifestFile {
    public static class Manifest {
        public String updateObj;                    // 升级对象文件名称
        public int packType;                        // 升级包类型：1→整包   2→差分包
        public String srcVer;                          // 升级原版本，标识差分关系
        public String dstVer;                       // 升级目标版本，即本次提交的软件版本号
        public int packNum;                         // 升级文件的个数
        public List<packInformation> packInfos ;    // 升级文件的列表，列表中从上到下为要升级的顺序

        class packInformation {
            public String updateObj;                // 升级对象名称
            public int packType;                    // 升级包类型
            public String srcVer;                   // 升级原版本
            public String dstVer;                   // 升级目标版本
            public String fileName;                 // 升级文件名称
            public String fileSign;                 // 升级文件签名
            public long   fileSize;                 // 升级文件长度 单位Byte
        }

        public List<packInformation> getPackInfos() {
            return this.packInfos;
        }

        public void setPackInfos(List<packInformation> packInfos) {
            this.packInfos = packInfos;
        }
    }

    //输入参数为json字符串和文件路径
    private boolean transformToJsonFile(String jsonString, String filePath){
        boolean flag = true;
        try{
            File file = new File(filePath);
            if(!file.getParentFile().exists()){
                //若父目录不存在则创建父目录
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                //若文件存在，则删除旧文件
                file.delete();
            }
            file.createNewFile();
            //将格式化后的字符串写入文件
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public static void StringToFile(String str, String dir) throws IOException {
        File file = new File(dir);
        FileWriter writer;
        writer = new FileWriter(dir);
        writer.write(str);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        Manifest mf = new Manifest();
        mf.updateObj = "TBOX";
        mf.packType = 1;
        mf.srcVer = "FHD1539MA-02-CN-20161025000000";
        mf.dstVer = "FHD1539MA-02-CN-20161025000001";
        mf.packNum = 3;

        List<Manifest.packInformation> pis = new ArrayList<>();

        Manifest.packInformation pi1 = mf.new packInformation();
        pi1.updateObj = "BootLoader";
        pi1.packType = 1;
        pi1.srcVer = "Boot-02-CN-2020071600000";
        pi1.dstVer = "Boot-02-CN-2020071600001";
        pi1.fileName = "bootloader.zip";
        pi1.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi1.fileSize = (long)1024;
        pis.add(pi1);

        Manifest.packInformation pi2 = mf.new packInformation();
        pi2.updateObj = "MCU";
        pi2.packType = 1;
        pi2.srcVer = "MCU-02-CN-20200716000000";
        pi2.dstVer = "MCU-02-CN-20200716000001";
        pi2.fileName = "MCU.zip";
        pi2.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi2.fileSize = (long)1024;
        pis.add(pi2);

        Manifest.packInformation pi3 = mf.new packInformation();
        pi3.updateObj = "MPU";
        pi3.packType = 1;
        pi3.srcVer = "MPU-02-CN-20200716000000";
        pi3.dstVer = "MPU-02-CN-20200716000001";
        pi3.fileName = "MPU.zip";
        pi3.fileSign = "844c8f7da414a3bb3974e5c022627ed44dfd9c6e3ef079ce1d059b5937e9a633";
        pi3.fileSize = (long)1024;
        pis.add(pi3);

        mf.setPackInfos(pis);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        System.out.println(gson.toJson(mf));
        String jsonString = gson.toJson(mf);
        StringToFile(jsonString, "/home/jack_xie/files/ota_tmp/manifest.json");
    }
}
