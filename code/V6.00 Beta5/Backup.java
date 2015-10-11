package com.lack006.hosts_l;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import java.io.File;

import static com.lack006.hosts_l.Copy.Copy_backup2hosts;
import static com.lack006.hosts_l.Copy.Copy_hosts2backup;

/**
 * Created by Lack006 on 2015/10/9.
 */
public class Backup {
    public static void Backuphosts(Context context, AlertDialog.Builder builder,Button btn_AD, Button btn_RE, Button btn_AR) {
        File file = context.getFilesDir();
        final String DATABASE_PATH = file.getAbsolutePath();
        String databaseFilename = DATABASE_PATH + "/" + "backup";
        if (!(new File(databaseFilename)).exists()) {
            IsBackup(context, builder);
        } else {
            NoBackup(context, builder,btn_AD,btn_RE,btn_AR);
        }
    }

    public static void IsBackup(final Context context, final AlertDialog.Builder builder) {
        builder.setTitle(R.string.no_backup);
        builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.setNeutralButton(R.string.go_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Copy_hosts2backup(context);


            }
        });

        builder.create().show();
    }

    public static void NoBackup(final Context context, AlertDialog.Builder builder, final Button btn_AD, final Button btn_RE, final Button btn_AR) {
        builder.setTitle(R.string.is_backup);
        builder.setMessage(R.string.Tip_backup);
        builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.setNegativeButton(R.string.Re_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Copy_hosts2backup(context);


            }
        });
        builder.setNeutralButton(R.string.Restore_backup, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Copy_backup2hosts(context,btn_AD,btn_RE,btn_AR);


            }
        });

        builder.create().show();

    }
}
