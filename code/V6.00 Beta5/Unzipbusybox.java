package com.lack006.hosts_l;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class Unzipbusybox {

    public static void Unzipbusybox(Context context, InputStream is) {
        try {
            String DATABASE_FILENAME6 = "busybox";
            File file = context.getFilesDir();
            final String DATABASE_PATH = file.getAbsolutePath();
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME6;
            File dir = new File(DATABASE_PATH);

            if (!dir.exists())
                dir.mkdir();

            if (!(new File(databaseFilename)).exists()) {


                FileOutputStream fos = new FileOutputStream(
                        databaseFilename);
                byte[] buffer = new byte[8192];
                int count;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();

            }


        } catch (Exception ignored) {
        }

    }

}
