import com.hh.ota.cache.LocalFile;
import com.hh.ota.diff.HDiff;
import com.hh.ota.patch.HPatch;

import java.io.FileNotFoundException;

public class testLocalDiffPatch {

    public static void diff(String oldFile, String newFile, String diffFile) throws FileNotFoundException {
        LocalFile.requireFileExist(oldFile);
        LocalFile.requireFileExist(newFile);
        HDiff.INSTANCE.hh_ota_diff(oldFile, newFile, diffFile, 1024*1024*128);
    }

    public static void patch(String diffPath, String oldPath, String outNewPath) throws FileNotFoundException {
        LocalFile.requireFileExist(oldPath);
        LocalFile.requireFileExist(diffPath);
        HPatch.INSTANCE.hh_ota_patch(diffPath, oldPath, outNewPath);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String oldFile = "/home/jack_xie/files/aoyun/6C/MPU.PRG";
        String newFile = "/home/jack_xie/files/aoyun/6D/MPU.PRG";
        String diffFile = "/home/jack_xie/files/aoyun/MPU_diff.PRG";
        String patchFile = "/home/jack_xie/files/aoyun/6D/MPU_patch.PRG";
        try {
//            diff(oldFile, newFile, diffFile);//差分
            patch(diffFile, oldFile, patchFile);//还原
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("FUNCTION TIME:" + (endTime - startTime) / 1000 + "s");
    }
}
