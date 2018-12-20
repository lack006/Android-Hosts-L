package com.lack006.hosts_l.GoogleLocation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by lack006 on 2017/12/7.
 */

public class GoogleLocationEnableReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        enableGoogleLocalService(context);
        Intent startIntent = new Intent(context, GoogleLocationEnableService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(startIntent);
//        } else {
            context.startService(startIntent);
//        }


    }

    private void enableGoogleLocalService(Context context) {
        ComponentName receiver;
        PackageManager pm = context.getPackageManager();
        receiver = new ComponentName(context, GoogleLocationEnableService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
