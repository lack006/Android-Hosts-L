package com.lack006.hosts_l.Delay;

import android.support.design.widget.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lack006 on 2016/9/17.
 * AndroidHosts-LV7
 */
public class DelayHelper {
    public static void snackBarShowDelay(final Snackbar snackbar) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                snackbar.show();
            }
        };
        timer.schedule(task, 500);
    }


}
