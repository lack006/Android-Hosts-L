package com.lack006.hosts_l.DNS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lack on 2016/12/10.
 * AndroidHosts-LV7
 */

public class ChangeDNSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mActiveNetInfo = connectivityManager.getActiveNetworkInfo();
        if (mActiveNetInfo != null) {
            Intent startIntent = new Intent(context, DNSShellService.class);
            context.startService(startIntent);
        }


    }


}

