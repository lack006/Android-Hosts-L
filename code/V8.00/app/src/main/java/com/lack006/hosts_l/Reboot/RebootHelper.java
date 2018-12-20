package com.lack006.hosts_l.Reboot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
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
        /**
         *  正常重启命令 有时失效 暂时取消
         */
//        dialog.setPositiveButton(R.string.reboot, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                mIsHotReboot = false;
//                reboot();
//            }
//        });
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


        suShellReturnHelper.run(new String[]{
                ConsistentCommands.HOT_REBOOT
        });

    }
}
