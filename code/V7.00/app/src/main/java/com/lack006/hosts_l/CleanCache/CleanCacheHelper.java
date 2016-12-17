package com.lack006.hosts_l.CleanCache;

import android.content.Context;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.io.File;
import java.util.List;

import static com.lack006.hosts_l.Consistent.Consistent.TEMP_FILE;
import static com.lack006.hosts_l.Consistent.Consistent.VERSION_FILE;

/**
 * Created by lack on 2016/12/2.
 * AndroidHosts-LV7
 */

public class CleanCacheHelper {

   public void cleanCache(Context context) {
        String privateDir = context.getCacheDir().getAbsolutePath();
        File file = new File(privateDir);
        deleteAll(file, context);


    }

    public void cleanChange(Context context) {
        String privateDir = context.getCacheDir().getAbsolutePath() + TEMP_FILE;
        File file = new File(privateDir);
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.cache_del_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cleanVersion(Context context) {
        String privateDir = context.getCacheDir().getAbsolutePath() + VERSION_FILE;
        File file = new File(privateDir);
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.version_del_failed), Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void cleanClash(Context context) {
        String privateDir = context.getCacheDir().getAbsolutePath() + Consistent.CLASH_FILE;
        File file = new File(privateDir);
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.clash_del_failed), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void cleanHosts(Context context) {
        String privateDir = context.getCacheDir().getAbsolutePath() + Consistent.HOSTS_FILE;
        File file = new File(privateDir);
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.hosts_temp_del_failed), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void cleanBackup(Context context) {
        String CACHE_PATH = context.getFilesDir().getAbsolutePath();
        String privateDir = CACHE_PATH + Consistent.BACKUP_FILE;
        File file = new File(privateDir);
        if (file.exists()) {
            if (!context.deleteFile(Consistent.BACKUP_FILE_NAME_ONLY)) {
                SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
                List<String> shell = suShellReturnHelper.run(new String[]{
                        ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                        ConsistentCommands.SET_BUSYBOX_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                        ConsistentCommands.RM + privateDir,
                        ConsistentCommands.SET_BUSYBOX_600
                });
                if (shell.size() != 0) {
                    Toast.makeText(context, context.getString(R.string.backup_del_failed), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    private static void deleteAll(File file, Context context) {


        if (file.isFile() || file.list().length == 0) {
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.cache_del_failed), Toast.LENGTH_SHORT).show();
            }
        } else {
            for (File f : file.listFiles()) {
                deleteAll(f, context); // 递归删除每一个文件

            }
            if (!file.delete()) {
                Toast.makeText(context, context.getString(R.string.cache_del_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
