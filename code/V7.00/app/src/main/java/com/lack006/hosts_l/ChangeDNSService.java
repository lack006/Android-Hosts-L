package com.lack006.hosts_l;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by lack on 2016/12/10.
 * AndroidHosts-LV7
 */

public class ChangeDNSService extends Service {
    private ChangeDNSReceiver mChangeDNSReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Consistent.NETWORK_CONNECT_CHANGE_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        mChangeDNSReceiver = new ChangeDNSReceiver();
        registerReceiver(mChangeDNSReceiver, intentFilter);


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Consistent.NETWORK_CONNECT_CHANGE_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);

        try {
            unregisterReceiver(mChangeDNSReceiver);
        } catch (Exception ignored) {

        }

        super.onDestroy();
    }
}
