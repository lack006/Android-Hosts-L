package com.lack006.hosts_l.Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by lack on 2016/12/7.
 * AndroidHosts-LV7
 */

class CopyHelper {
    boolean copyFile(String oldPath, String newPath) {
        boolean completeFlag = false;
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                fs.flush();
                fs.close();
                inStream.close();
                completeFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completeFlag;

    }
}
