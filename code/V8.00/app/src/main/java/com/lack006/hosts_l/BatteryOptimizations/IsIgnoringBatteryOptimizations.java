package com.lack006.hosts_l.BatteryOptimizations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.R;

/**
 * Android Hosts-L V8
 * Created by lack006 on 2018/1/23.
 */

public class IsIgnoringBatteryOptimizations {
    public boolean isIgnoringBatteryOptimizations(Context context,int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!(pm.isIgnoringBatteryOptimizations(packageName))) {
                switch(type){
                    case Consistent.TYPE_DNS:{
                        Toast.makeText(context, R.string.dns_ask_battery_permission, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Consistent.TYPE_GOOGLE_LOCAL:{
                        Toast.makeText(context, R.string.google_ask_battery_permission, Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                //在MainActivity里面加一个接受看看
                //或者试试看 在 MainActivity里 的 requestPermission 能不能用
                ((Activity)context).startActivityForResult(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse(Consistent.PACKAGE + context.getPackageName())),Consistent.BATTARY_PERMISSIONS_REQUEST);
                //context.startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse(Consistent.PACKAGE + context.getPackageName())));
            }
            return pm.isIgnoringBatteryOptimizations(packageName);
        }
        return true;
    }
    public boolean checkIsIgnoringBatteryOptimizations(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        }
        return true;
    }

}
