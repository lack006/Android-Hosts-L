package com.lack006.hosts_l.GoogleLocation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.lack006.hosts_l.BatteryOptimizations.IsIgnoringBatteryOptimizations;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.DNS.ChangeDNSService;
import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/9.
 * AndroidHosts-LV7
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNeedBootStart = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, false)) {
            enableRecReceiver(context);
            Intent startIntent = new Intent(context, ChangeDNSService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IsIgnoringBatteryOptimizations isIgnoringBatteryOptimizations = new IsIgnoringBatteryOptimizations();
                if (isIgnoringBatteryOptimizations.checkIsIgnoringBatteryOptimizations(context)) {
                    context.startService(startIntent);
                    isNeedBootStart = true;
                }else{
                    Toast.makeText(context, R.string.dns_reopen_battery_permission, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, Consistent.OFF);
                    editor.apply();

                }

            } else {
                context.startService(startIntent);
                isNeedBootStart = true;
            }

        }
        if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, false)) {
            enableRecReceiver(context);
            Intent startIntent = new Intent(context, GoogleLocationEnableService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IsIgnoringBatteryOptimizations isIgnoringBatteryOptimizations = new IsIgnoringBatteryOptimizations();
                if (isIgnoringBatteryOptimizations.checkIsIgnoringBatteryOptimizations(context)) {
                    context.startService(startIntent);
                    isNeedBootStart = true;
                }else{
                    Toast.makeText(context, R.string.google_reopen_battery_permission, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, Consistent.OFF);
                    editor.apply();

                }

            } else {
                context.startService(startIntent);
                isNeedBootStart = true;
            }
        }
        if (!isNeedBootStart) {
            disableRecReceiver(context);
        }
    }

    private void enableRecReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, this.getClass());
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void disableRecReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, this.getClass());
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

}

