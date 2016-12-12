package com.lack006.hosts_l.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent;
import com.lack006.hosts_l.ConsistentCommands;
import com.lack006.hosts_l.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lack006.hosts_l.Consistent.KEY_AD;
import static com.lack006.hosts_l.Consistent.KEY_AR;
import static com.lack006.hosts_l.Consistent.KEY_LOCAL_HOSTS_KIND;
import static com.lack006.hosts_l.Consistent.KEY_LOCAL_HOSTS_VERSION;
import static com.lack006.hosts_l.Consistent.KEY_RE;
import static com.lack006.hosts_l.Consistent.LOCAL_AD;
import static com.lack006.hosts_l.Consistent.LOCAL_AR;
import static com.lack006.hosts_l.Consistent.LOCAL_RE;

/**
 * Created by lack on 2016/12/3.
 * AndroidHosts-LV7
 */

class ApplyHostsTaskHelper {
    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private ProgressDialog mProgressDialog = null;
    private List<String> mCmdResult = null;
    private Map<String, String> mVersionMap = new HashMap<>();

    void applyHostsHelper(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.applying));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        ApplyHostsTask applyHostsTask = new ApplyHostsTask();
        applyHostsTask.execute();


    }

    private class ApplyHostsTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            File file = mContext.getCacheDir();
            String CACHE_PATH = file.getAbsolutePath();
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mCmdResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                    ConsistentCommands.SET_BUSYBOX_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                    ConsistentCommands.RW_SYSTEM,
                    ConsistentCommands.RM_HOSTS,
                    ConsistentCommands.COPY + CACHE_PATH + Consistent.HOSTS_FILE + " " + Consistent.SYSTEM_ETC_HOSTS,
                    ConsistentCommands.SET_HOSTS_644,
                    ConsistentCommands.RO_SYSTEM,
                    ConsistentCommands.SET_BUSYBOX_600 + CACHE_PATH + Consistent.BUSYBOX_FILE
            });
            GetVersionHelper getVersionHelper = new GetVersionHelper();
            mVersionMap = getVersionHelper.getVersion(mContext);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            StringBuilder errorLog = (new StringBuilder());
            boolean importantError = false;
            if (mCmdResult.size() != 0) {
                for (String line : mCmdResult) {
                    if (line.contains(Consistent.SYSTEM_RW_ERROR)) {
                        errorLog.append(line).append((char) 10);
                        importantError = true;

                    }
                }
                if (importantError) {
                    Toast.makeText(mContext, mContext.getString(R.string.apply_hosts_failed) + errorLog.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.apply_hosts_success, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.apply_hosts_success, Toast.LENGTH_SHORT).show();
            }
            String adVersion = mVersionMap.get(KEY_AD);
            String reVersion = mVersionMap.get(KEY_RE);
            String arVersion = mVersionMap.get(KEY_AR);
            String hostsVersion = mVersionMap.get(KEY_LOCAL_HOSTS_VERSION);
            String hostsKind = mVersionMap.get(KEY_LOCAL_HOSTS_KIND);


            if (LOCAL_AD.equals(hostsKind)) {
                if ((hostsVersion.equals(adVersion))) {
                    mCheckBox.setChecked(true);
                    mCheckBox.setText(R.string.AD_hosts_last_version);
                } else {
                    mCheckBox.setChecked(false);
                    mCheckBox.setText(R.string.AD_hosts_new_version);
                }
            } else if (LOCAL_RE.equals(hostsKind)) {
                if ((hostsVersion.equals(reVersion))) {
                    mCheckBox.setChecked(true);
                    mCheckBox.setText(R.string.RE_hosts_last_version);
                } else {
                    mCheckBox.setChecked(false);
                    mCheckBox.setText(R.string.RE_hosts_new_version);
                }
            } else if (LOCAL_AR.equals(hostsKind)) {
                if ((hostsVersion.equals(arVersion))) {
                    mCheckBox.setChecked(true);
                    mCheckBox.setText(R.string.AR_hosts_last_version);
                } else {
                    mCheckBox.setChecked(false);
                    mCheckBox.setText(R.string.AR_hosts_new_version);
                }
            } else {
                mCheckBox.setChecked(false);
                mCheckBox.setText(R.string.hosts_none);
            }

            mProgressDialog.cancel();


        }


    }
}
