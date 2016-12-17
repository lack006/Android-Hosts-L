package com.lack006.hosts_l.Helper;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.lack006.hosts_l.Delay.DelayHelper;
import com.lack006.hosts_l.R;

/**
 * Created by lack006 on 2016/9/17.
 * AndroidHosts-LV7
 */
public class SnackBarHelper {
    public static Snackbar showSnackBar(Snackbar snackbar, View view, String text) {
        if (null != snackbar && snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        DelayHelper.snackBarShowDelay(snackbar);
        return snackbar;
    }

    public static Snackbar showSnackBar(Snackbar snackbar, View view, int resId) {
        if (null != snackbar && snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, resId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        DelayHelper.snackBarShowDelay(snackbar);
        return snackbar;
    }
}
