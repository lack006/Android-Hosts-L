package com.lack006.hosts_l.Backup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.CheckVersion.GetVersionHelper;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lack006.hosts_l.Consistent.Consistent.KEY_AD;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_AR;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_LOCAL_HOSTS_KIND;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_LOCAL_HOSTS_VERSION;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_RE;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_AD;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_AR;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_RE;

/**
 * Created by lack on 2016/12/4.
 * AndroidHosts-LV7
 */

public class RestoreHostsTaskHelper {

    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private ProgressDialog mProgressDialog = null;
    private List<String> mCmdResult = null;
    private Map<String, String> mVersionMap = new HashMap<>();

    public void restoreHosts(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.restoring));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        RestoreHostsTask restoreHostsTask = new RestoreHostsTask();
        restoreHostsTask.execute();
    }

    private class RestoreHostsTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {
            InputStream is = mContext.getResources().openRawResource(
                    R.raw.jttqbcsmwxhxy);
            try {
                File file = mContext.getCacheDir();
                final String CACHE_PATH = file.getAbsolutePath();
                String databaseFilename = CACHE_PATH + Consistent.DEFAULT_HOSTS;
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
            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mCmdResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                    ConsistentCommands.SET_RW_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                    ConsistentCommands.RW_SYSTEM,
                    ConsistentCommands.RM_HOSTS,
                    ConsistentCommands.COPY + CACHE_PATH + Consistent.DEFAULT_HOSTS + " " + Consistent.SYSTEM_ETC_HOSTS,
                    ConsistentCommands.SET_HOSTS_644,
                    ConsistentCommands.RO_SYSTEM,
                    ConsistentCommands.SET_RW_600 + CACHE_PATH + Consistent.BUSYBOX_FILE
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
