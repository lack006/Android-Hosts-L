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

import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.R;

import java.util.Map;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by lack006 on 2016/9/17.
 * AndroidHosts-LV7
 */
public class CheckRootTaskHelper {

    private Context mContext = null;
    private Snackbar mSnackBar = null;
    private CoordinatorLayout mCoordinatorLayout = null;
    private DrawerLayout mDrawerLayout = null;
    private Map<String, CheckBox> mCheckBoxMap = null;
    private ProgressDialog mProgressDialog = null;

    public void checkRoot(Context context, Snackbar snackbar, CoordinatorLayout coordinatorLayout, Map<String, CheckBox> checkBoxMap, DrawerLayout drawerLayout) {

        mContext = context;
        mSnackBar = snackbar;
        mCoordinatorLayout = coordinatorLayout;
        mDrawerLayout = drawerLayout;
        mCheckBoxMap = checkBoxMap;

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.check_root_doing));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        CheckRootTask checkRootTask = new CheckRootTask();
        checkRootTask.execute();
    }

    private class CheckRootTask extends AsyncTask<Object, Integer, Boolean> {
        private boolean mIsRooted = false;

        @Override
        protected Boolean doInBackground(Object... objects) {
            if (Shell.SU.available()) {
                mIsRooted = true;
                CleanCacheHelper cacheHelper = new CleanCacheHelper();
                cacheHelper.cleanCache(mContext);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {


            if (!mIsRooted) {
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.no_root_title)
                        .setMessage(R.string.no_root)
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
                CheckNetTaskHelper checkNet = new CheckNetTaskHelper();
                checkNet.checkNet(mContext, mSnackBar, mCoordinatorLayout, mCheckBoxMap, mDrawerLayout);
            }


        }


    }
}
