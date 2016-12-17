package com.lack006.hosts_l.GoogleLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.DNS.ChangeDNSService;

/**
 * Created by lack on 2016/12/9.
 * AndroidHosts-LV7
 */

public class GoogleLocationEnableReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, false)) {
            GoogleLocationReportTaskHelper googleLocationReportTaskHelper = new GoogleLocationReportTaskHelper();
            googleLocationReportTaskHelper.enableGoogleLocalShell();
        }
        if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, false)) {
            Intent startIntent = new Intent(context, ChangeDNSService.class);
            context.startService(startIntent);
        }

    }
}
