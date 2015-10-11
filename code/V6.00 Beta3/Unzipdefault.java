package com.lack006.hosts_l;

import android.content.Context;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.lack006.hosts_l.Copy.Copy_download2hosts;

/**
 * Created by Lack006 on 2015/10/9.
 */
public class Unzipdefault {
    public static void UnzipDefault(Context context, InputStream is, Button btn_AD, Button btn_RE, Button btn_AR) {
        try {
            final String Default_hosts = "hosts";
            File file = context.getFilesDir();
            final String DATABASE_PATH = file.getAbsolutePath();
            String databaseFilename = DATABASE_PATH + "/" + Default_hosts;
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
            Copy_download2hosts(context, btn_AD, btn_RE, btn_AR);


        } catch (Exception ignored) {
        }

    }
}
