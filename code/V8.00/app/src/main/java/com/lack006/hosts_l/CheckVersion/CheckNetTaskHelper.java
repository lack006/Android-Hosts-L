package com.lack006.hosts_l.CheckVersion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.widget.CheckBox;

import com.lack006.hosts_l.Busybox.ExpressBusyBoxTaskHelper;
import com.lack006.hosts_l.R;

import java.util.Map;

/**
 * Created by lack on 2016/11/12.
 * AndroidHosts-LV7
 */

class CheckNetTaskHelper {

    private Context mContext = null;
    private Snackbar mSnackBar = null;
    private CoordinatorLayout mCoordinatorLayout = null;
    private DrawerLayout mDrawerLayout = null;
    private Map<String, CheckBox> mCheckBoxMap = null;
    private ProgressDialog mProgressDialog = null;

    void checkNet(Context context, Snackbar snackbar, CoordinatorLayout coordinatorLayout, Map<String, CheckBox> checkBoxMap, DrawerLayout drawerLayout) {
        mContext = context;
        mSnackBar = snackbar;
        mCoordinatorLayout = coordinatorLayout;
        mDrawerLayout = drawerLayout;
        mCheckBoxMap = checkBoxMap;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.check_net_doing));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        CheckNetTask checkNetTask = new CheckNetTask();
        checkNetTask.execute();
    }

    private class CheckNetTask extends AsyncTask<Object, Integer, Boolean> {
        private boolean mIsConnect = false;

        @Override
        protected Boolean doInBackground(Object... objects) {

            if (NetHelper.isNetworkConnected(mContext)) {
                mIsConnect = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!mIsConnect) {

                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.no_net)
                        .setPositiveButton(R.string.exit,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        ((Activity) mContext).finish();
                                    }
                                }
                        )
                        .show();
            } else {
                mProgressDialog.cancel();
                ExpressBusyBoxTaskHelper expressBusyBoxTaskHelper = new ExpressBusyBoxTaskHelper();
                expressBusyBoxTaskHelper.expressBusyBox(mContext, mSnackBar, mCoordinatorLayout, mCheckBoxMap, mDrawerLayout);
            }


        }


    }
}
