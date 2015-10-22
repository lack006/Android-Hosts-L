package com.lack006.hosts_l;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Lack006 on 2015/10/22.
 */
public class Checkbusybox {
    private static final String Busybox_xbin = "/system/xbin/busybox";
    public static void checkbusybox(final Context context) {
        final File box1 = new File(Busybox_xbin);
        if (!box1.exists()) {
            Toast.makeText(context, R.string.need_busybox, Toast.LENGTH_LONG).show();
        }


    }
}
