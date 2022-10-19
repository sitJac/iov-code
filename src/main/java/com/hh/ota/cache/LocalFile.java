package com.hh.ota.cache;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class LocalFile {
    //解决double小文件精度缺失问题
    public static double getFileSize(File file) {
        long length = file.length();
        BigDecimal bigLength = ((new BigDecimal(length)).divide(new BigDecimal(1024))).divide(new BigDecimal(1024));
        double result = (bigLength).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(result == 0.00) {
            result = 0.01;
        }
        return result;
    }

    public static double getFolderSize(File folder) {
        long size = FileUtils.sizeOfDirectory(folder);
        BigDecimal bigSize = ((new BigDecimal(size)).divide(new BigDecimal(1024))).divide(new BigDecimal(1024));
        double result = (bigSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if(result == 0.00) {
            result = 0.01;
        }
        return result;
    }

     //校验文件是否存在，如果不存在则抛出异常
    public static void requireFileExist(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
    }

    //将字符串写入文件
    public static void StringToFile(String str, String dir) throws IOException {
        File file = new File(dir);
        FileWriter writer;
        writer = new FileWriter(dir);
        writer.write(str);
        writer.flush();
        writer.close();
    }

    //删除本地缓存目录
    public static void deleteFile(File file) {
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

}
