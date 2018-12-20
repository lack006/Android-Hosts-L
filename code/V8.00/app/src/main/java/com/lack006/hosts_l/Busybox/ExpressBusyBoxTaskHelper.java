package com.lack006.hosts_l.Busybox;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.widget.CheckBox;

import com.lack006.hosts_l.CheckVersion.CheckVersionTaskHelper;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by lack on 2016/12/1.
 * AndroidHosts-LV7
 */

public class ExpressBusyBoxTaskHelper {
    private Context mContext = null;
    private Snackbar mSnackBar = null;
    private CoordinatorLayout mCoordinatorLayout = null;
    private DrawerLayout mDrawerLayout = null;
    private Map<String, CheckBox> mCheckBoxMap = null;
    private ProgressDialog mProgressDialog = null;

    public void expressBusyBox(Context context, Snackbar snackbar, CoordinatorLayout coordinatorLayout, Map<String, CheckBox> checkBoxMap, DrawerLayout drawerLayout) {
        mContext = context;
        mSnackBar = snackbar;
        mCoordinatorLayout = coordinatorLayout;
        mDrawerLayout = drawerLayout;
        mCheckBoxMap = checkBoxMap;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.express_busybox_doing));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        ExpressBusyBoxTask expressBusyBoxTask = new ExpressBusyBoxTask();
        expressBusyBoxTask.execute();
    }

    private class ExpressBusyBoxTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {
            InputStream is = mContext.getResources().openRawResource(
                    R.raw.lack006);
            try {
                File file = mContext.getCacheDir();
                final String CACHE_PATH = file.getAbsolutePath();
                String databaseFilename = CACHE_PATH + Consistent.BUSYBOX_FILE;
                File dir = new File(CACHE_PATH);

                if (!dir.exists())
                    dir.mkdir();

                if (!(new File(databaseFilename)).exists()) {


                    FileOutputStream fos = new FileOutputStream(
                            databaseFilename);
                    byte[] buffer = new byte[8192];
                    int count;

                    while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();

                }


            } catch (Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();
            CheckVersionTaskHelper checkVersionTaskHelper = new CheckVersionTaskHelper();
            checkVersionTaskHelper.checkVersion(mContext, mSnackBar, mCoordinatorLayout, mCheckBoxMap, mDrawerLayout);
        }


    }
}
