package com.lack006.hosts_l.enablegooglelocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lack006.hosts_l.R;

/**
 * Created by Lack006 on 2016/1/20.
 */
public class EnableGoogleLocation extends BroadcastReceiver {
    public EnableGoogleLocation() {
    }


    public void onReceive(Context context, Intent intent) {
        SharedPreferences mPreferences;
        mPreferences = context.getSharedPreferences(context.getString(R.string.Config_file),
                ContextWrapper.MODE_PRIVATE);
        int Z = mPreferences.getInt(context.getString(R.string.Config_file_AutoStart),0);
        if (Z==1){
            EnableShell.enableshell(context);
        }

    }
}
