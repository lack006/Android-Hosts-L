package com.lack006.hosts_l.Backup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Copy.CopyHelper;
import com.lack006.hosts_l.CheckVersion.GetVersionHelper;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.ProtectMode.ProtectModeHelper;
import com.lack006.hosts_l.R;

import java.io.File;
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

public class BackupHostsTaskHelper {

    private Context mContext = null;
    private CheckBox mCheckBoxBackup = null;
    private CheckBox mCheckBoxHosts = null;
    private ProgressDialog mProgressDialog = null;
    private List<String> mCmdResult = null;
    private Map<String, String> mVersionMap = new HashMap<>();
    private boolean mBackupFlag = false;
    private String mBackupFilePath = null;
    private String mCacheFilePath = null;
    private boolean mBackupSuccess = false;

    public void backupHostsHelper(Context context, CheckBox checkBoxBackup, CheckBox checkBoxHosts) {
        mContext = context;
        mCheckBoxBackup = checkBoxBackup;
        mCheckBoxHosts = checkBoxHosts;

        File busybox = mContext.getCacheDir();
        mCacheFilePath = busybox.getAbsolutePath();
        File file = mContext.getFilesDir();
        final String FILE_PATH = file.getAbsolutePath();
        mBackupFilePath = FILE_PATH + Consistent.BACKUP_FILE;
        file = new File(mBackupFilePath);


        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);


        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(R.string.backup_hosts);
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        if (file.exists()) {
            dialog.setMessage(R.string.backup_exists);
            dialog.setPositiveButton(R.string.backup, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBackupFlag = true;
                    mProgressDialog.setMessage(mContext.getString(R.string.doing_backup));
                    mProgressDialog.show();
                    CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
                    cleanCacheHelper.cleanBackup(mContext);
                    BackupTask backupTask = new BackupTask();
                    backupTask.execute();
                }
            });
            dialog.setNeutralButton(R.string.restore, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBackupFlag = false;
                    mProgressDialog.setMessage(mContext.getString(R.string.doing_restore));
                    mProgressDialog.show();
                    BackupTask backupTask = new BackupTask();
                    backupTask.execute();

                }
            });
        } else {
            dialog.setMessage(R.string.backup_no_exists);
            dialog.setPositiveButton(R.string.backup, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBackupFlag = true;
                    mProgressDialog.setMessage(mContext.getString(R.string.doing_backup));
                    mProgressDialog.show();
                    BackupTask backupTask = new BackupTask();
                    backupTask.execute();

                }
            });
        }
        dialog.show();

    }

    private class BackupTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {

            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            ProtectModeHelper protectModeHelper = new ProtectModeHelper();
            protectModeHelper.prepareRead(mContext);

            if (mBackupFlag) {
                CopyHelper copyHelper = new CopyHelper();
                mBackupSuccess = copyHelper.copyFile(Consistent.SYSTEM_ETC_HOSTS, mBackupFilePath);
            } else {
                String[] restoreCommands = new String[]{
                        ConsistentCommands.SET_BUSYBOX_PATH + mCacheFilePath,
                        ConsistentCommands.SET_RW_777 + mCacheFilePath + Consistent.BUSYBOX_FILE,
                        ConsistentCommands.RW_SYSTEM,
                        ConsistentCommands.RM_HOSTS,
                        ConsistentCommands.COPY + mBackupFilePath + " " + Consistent.SYSTEM_ETC_HOSTS,
                        ConsistentCommands.SET_HOSTS_644,
                        ConsistentCommands.RO_SYSTEM,
                        ConsistentCommands.SET_RW_600 + mCacheFilePath + Consistent.BUSYBOX_FILE
                };
                mCmdResult = suShellReturnHelper.run(restoreCommands);
                GetVersionHelper getVersionHelper = new GetVersionHelper();
                mVersionMap = getVersionHelper.getVersion(mContext);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            StringBuilder errorLog = (new StringBuilder());

            if (mBackupFlag) {
                if (!mBackupSuccess) {
                    Toast.makeText(mContext, mContext.getString(R.string.backup_copy_failed), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.backup_copy_success, Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean importantError = false;
                if (mCmdResult.size() != 0) {
                    for (String line : mCmdResult) {
                        if (line.contains(Consistent.SYSTEM_RW_ERROR)) {
                            errorLog.append(line).append((char) 10);
                            importantError = true;

                        }
                    }
                    if (importantError) {
                        Toast.makeText(mContext, mContext.getString(R.string.restore_failed) + errorLog.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, R.string.restore_success, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.restore_success, Toast.LENGTH_SHORT).show();
                }
            }

            if (!mBackupFlag) {
                String adVersion = mVersionMap.get(KEY_AD);
                String reVersion = mVersionMap.get(KEY_RE);
                String arVersion = mVersionMap.get(KEY_AR);
                String hostsVersion = mVersionMap.get(KEY_LOCAL_HOSTS_VERSION);
                String hostsKind = mVersionMap.get(KEY_LOCAL_HOSTS_KIND);


                if (LOCAL_AD.equals(hostsKind)) {
                    if ((hostsVersion.equals(adVersion))) {
                        mCheckBoxHosts.setChecked(true);
                        mCheckBoxHosts.setText(R.string.AD_hosts_last_version);
                    } else {
                        mCheckBoxHosts.setChecked(false);
                        mCheckBoxHosts.setText(R.string.AD_hosts_new_version);
                    }
                } else if (LOCAL_RE.equals(hostsKind)) {
                    if ((hostsVersion.equals(reVersion))) {
                        mCheckBoxHosts.setChecked(true);
                        mCheckBoxHosts.setText(R.string.RE_hosts_last_version);
                    } else {
                        mCheckBoxHosts.setChecked(false);
                        mCheckBoxHosts.setText(R.string.RE_hosts_new_version);
                    }
                } else if (LOCAL_AR.equals(hostsKind)) {
                    if ((hostsVersion.equals(arVersion))) {
                        mCheckBoxHosts.setChecked(true);
                        mCheckBoxHosts.setText(R.string.AR_hosts_last_version);
                    } else {
                        mCheckBoxHosts.setChecked(false);
                        mCheckBoxHosts.setText(R.string.AR_hosts_new_version);
                    }
                } else {
                    mCheckBoxHosts.setChecked(false);
                    mCheckBoxHosts.setText(R.string.hosts_none);
                }


            }
            File file = new File(mBackupFilePath);
            if (file.exists()) {
                mCheckBoxBackup.setChecked(true);
                mCheckBoxBackup.setText(R.string.backup_exists_checkbox);
            } else {
                mCheckBoxBackup.setChecked(false);
                mCheckBoxBackup.setText(R.string.backup_no_exists_checkbox);
            }

            mProgressDialog.cancel();


        }


    }
}
