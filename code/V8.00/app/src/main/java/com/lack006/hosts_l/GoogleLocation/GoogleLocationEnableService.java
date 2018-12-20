package com.lack006.hosts_l.GoogleLocation;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.lack006.hosts_l.Consistent.Consistent;

/**
 * Created by lack006 on 2017/12/7.
 */

public class GoogleLocationEnableService extends Service {
    private GoogleLocationEnableReceiver mGoogleLocationEnableReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Consistent.SIM_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        mGoogleLocationEnableReceiver = new GoogleLocationEnableReceiver();
        registerReceiver(mGoogleLocationEnableReceiver, intentFilter);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, false)) {
            new GoogleLocationEnableTaskHelper.GoogleLocationEnableShell().execute();

        }

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
        intentFilter.addAction(Consistent.SIM_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);

        try {
            unregisterReceiver(mGoogleLocationEnableReceiver);
        } catch (Exception ignored) {

        }


        super.onDestroy();
    }
}
