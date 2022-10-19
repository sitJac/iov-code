import com.hh.ota.zip.MainZip;

import java.io.File;

public class testZip {
    public static void main(String[] args) {
        String LOCAL_FOLDER = "/tmp/ota_tmp/";
        String newFolder = LOCAL_FOLDER + "new/";
        File newDirectory = new File(newFolder);
        newDirectory.mkdirs();

        MainZip appZip = new MainZip();
        appZip.SOURCE_FOLDER = "/tmp/ota_tmp/old/3d95c274ab224c2da0b81405cafb0113";
        appZip.OUTPUT_ZIP_FILE = "/tmp/ota_tmp/new/3d95c274ab224c2da0b81405cafb0113.zip";
        appZip.generateFileList(new File(appZip.SOURCE_FOLDER));
        appZip.zipIt(appZip.OUTPUT_ZIP_FILE);
    }
}
