package com.lack006.hosts_l.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;

import com.lack006.hosts_l.Consistent;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lack on 2016/12/3.
 * AndroidHosts-LV7
 */

class InsertClashTaskHelper {

    private Context mContext = null;
    private String[] mHosts = null;
    private boolean[] mCheck = null;
    private ProgressDialog mProgressDialog = null;
    private CheckBox mCheckBox = null;

    void insertClashHelper(Context context, String[] hosts, boolean[] check, CheckBox checkBox) {
        mContext = context;
        mHosts = hosts;
        mCheck = check;
        mCheckBox = checkBox;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.insert_clash));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        InsertTask insertTask = new InsertTask();
        insertTask.execute();

    }

    private class InsertTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            for (int i = 0; i < mCheck.length; i++) {
                if (mCheck[i]) {
                    try {
                        String tmp = mHosts[i];
                        File file = mContext.getCacheDir();
                        final String CACHE_PATH = file.getAbsolutePath();
                        FileOutputStream fos = new FileOutputStream(CACHE_PATH + Consistent.HOSTS_FILE, true);
                        fos.write(tmp.getBytes());
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();
            ApplyHostsTaskHelper applyHostsTaskHelper = new ApplyHostsTaskHelper();
            applyHostsTaskHelper.applyHostsHelper(mContext, mCheckBox);
        }


    }
}
