package com.lack006.hosts_l.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lack006.hosts_l.ConsistentCommands;
import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/11.
 * AndroidHosts-LV7
 */

public class RebootHelper {
    private boolean mIsHotReboot = false;

    public void reboot(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.reboot_title);
        dialog.setMessage(R.string.reboot_msg);
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setPositiveButton(R.string.reboot, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mIsHotReboot = false;
                reboot();
            }
        });
        dialog.setNeutralButton(R.string.hot_reboot, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mIsHotReboot = true;
                reboot();
            }
        });

        dialog.create().show();
    }

    private void reboot() {
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();

        if (mIsHotReboot) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.HOT_REBOOT
            });
        } else {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REBOOT
            });
        }
    }
}
