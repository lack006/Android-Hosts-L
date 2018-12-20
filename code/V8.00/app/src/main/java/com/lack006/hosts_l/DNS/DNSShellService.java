package com.lack006.hosts_l.DNS;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/11.
 * AndroidHosts-LV7
 */

public class DNSShellService extends Service {
    private Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        DNSShellTask dnsShellTask = new DNSShellTask();
        dnsShellTask.execute();
        mContext = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class DNSShellTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {
            ChangeDNSTaskHelper changeDNSTaskHelper = new ChangeDNSTaskHelper();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String netDev = changeDNSTaskHelper.getLocalNetworkInterfaceName();
                String netId = changeDNSTaskHelper.getNetId(mContext);
                if (null != netDev && null != netId) {
                    changeDNSTaskHelper.changeDNSShellLollipop(netDev, netId, mContext);
                }
            } else {
                String netDev = changeDNSTaskHelper.getLocalNetworkInterfaceName();
                if (null != netDev) {
                    changeDNSTaskHelper.changeDNSShellKitkat(netDev, mContext);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            stopSelf();


        }


    }
}
