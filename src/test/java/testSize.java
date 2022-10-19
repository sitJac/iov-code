import java.io.File;

import static com.hh.ota.cache.LocalFile.*;

public class testSize {
    public static void main(String[] args) {
        String folder = "/home/jack_xie/files";
        String file = "/home/jack_xie/files/images.zip";

        double folderSize = getFolderSize(new File(folder));
        double fileSize = getFileSize(new File(file));

        System.out.println("Folder size:" + folderSize +"MB");
        System.out.println("File size:" + fileSize + "MB");

    }
}
