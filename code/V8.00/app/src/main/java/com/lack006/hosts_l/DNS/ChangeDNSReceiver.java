package com.lack006.hosts_l.DNS;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.lack006.hosts_l.BatteryOptimizations.IsIgnoringBatteryOptimizations;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.R;

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
            enableChangeDnsService(context);
            Intent startIntent = new Intent(context, DNSShellService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IsIgnoringBatteryOptimizations isIgnoringBatteryOptimizations = new IsIgnoringBatteryOptimizations();
                if (isIgnoringBatteryOptimizations.checkIsIgnoringBatteryOptimizations(context)) {
                    context.startService(startIntent);
                } else {
                    Toast.makeText(context, R.string.dns_reopen_battery_permission, Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, Consistent.OFF);
                    editor.apply();
                }
            } else {
                context.startService(startIntent);
            }
        }


    }

    private void enableChangeDnsService(Context context) {
        ComponentName receiver;
        PackageManager pm = context.getPackageManager();
        receiver = new ComponentName(context, DNSShellService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


}

