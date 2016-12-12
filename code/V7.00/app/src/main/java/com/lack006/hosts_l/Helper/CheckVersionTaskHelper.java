package com.lack006.hosts_l.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.ChangeDNSService;
import com.lack006.hosts_l.Consistent;
import com.lack006.hosts_l.ConsistentCommands;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lack006.hosts_l.Consistent.KEY_AD;
import static com.lack006.hosts_l.Consistent.KEY_AR;
import static com.lack006.hosts_l.Consistent.KEY_LOCAL_HOSTS_KIND;
import static com.lack006.hosts_l.Consistent.KEY_LOCAL_HOSTS_VERSION;
import static com.lack006.hosts_l.Consistent.KEY_OTHER;
import static com.lack006.hosts_l.Consistent.KEY_RE;
import static com.lack006.hosts_l.Consistent.KEY_VERSION;
import static com.lack006.hosts_l.Consistent.LOCAL_AD;
import static com.lack006.hosts_l.Consistent.LOCAL_AR;
import static com.lack006.hosts_l.Consistent.LOCAL_RE;
import static com.lack006.hosts_l.Consistent.SOFTWARE_VERSION;
import static com.lack006.hosts_l.Consistent.TIME_OUT;

/**
 * Created by lack006 on 2016/10/3.
 * AndroidHosts-LV7
 */

public class CheckVersionTaskHelper {

    private Context mContext = null;
    private Snackbar mSnackBar = null;
    private CoordinatorLayout mCoordinatorLayout = null;
    private DrawerLayout mDrawerLayout = null;
    private Map<String, CheckBox> mCheckBoxMap = null;
    private CheckBox mCheckBoxHosts = null;
    private CheckBox mCheckBoxVersion = null;
    private CheckBox mCheckBoxBackUp = null;
    private CheckBox mCheckBoxBusybox = null;
    private CheckBox mCheckBoxMark = null;
    private CheckBox mCheckBoxLocal = null;
    private CheckBox mCheckBoxDNS = null;
    private ProgressDialog mProgressDialog = null;

    private Map<String, String> mTextMap = new HashMap<>();

    private boolean mIsDNSEnable = false;

    private List<String> mMarkResult = null;
    private List<String> mSystemWriteResult = null;

    public void checkVersion(Context context, Snackbar snackbar, CoordinatorLayout coordinatorLayout, Map<String, CheckBox> checkBoxMap, DrawerLayout drawerLayout) {
        mContext = context;
        mSnackBar = snackbar;
        mCoordinatorLayout = coordinatorLayout;
        mDrawerLayout = drawerLayout;
        mCheckBoxMap = checkBoxMap;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        CheckVersionTask checkVersionTask = new CheckVersionTask();
        checkVersionTask.execute();
    }

    private class CheckVersionTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {

            mCheckBoxHosts = mCheckBoxMap.get(Consistent.KEY_CHECKBOX_HOSTS);
            mCheckBoxVersion = mCheckBoxMap.get(Consistent.KEY_CHECKBOX_VERSION);
            mCheckBoxBusybox = mCheckBoxMap.get(Consistent.KEY_CHECKBOX_BUSYBOX);
            mCheckBoxBackUp = mCheckBoxMap.get(Consistent.KEY_CHECKBOX_BACKUP);
            mCheckBoxMark = mCheckBoxMap.get((Consistent.KEY_CHECKBOX_MARK));
            mCheckBoxLocal = mCheckBoxMap.get((Consistent.KEY_CHECKBOX_LOCAL));
            mCheckBoxDNS = mCheckBoxMap.get((Consistent.KEY_CHECKBOX_DNS));


            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            URL url;
            try {

                url = new URL(mContext.getString(R.string.version_url));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(TIME_OUT);
                con.setRequestProperty("Accept-Encoding", "identity");
                InputStream in = con.getInputStream();
                File fileOut = new File(CACHE_PATH + Consistent.VERSION_FILE);
                FileOutputStream out = new FileOutputStream(fileOut);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                in.close();
                out.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mMarkResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.GET_MARK_URL
            });

            mSystemWriteResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                    ConsistentCommands.SET_BUSYBOX_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                    ConsistentCommands.RW_SYSTEM_DEFAULT,
                    ConsistentCommands.RW_SYSTEM,
                    ConsistentCommands.RO_SYSTEM_DEFAULT,
                    ConsistentCommands.RO_SYSTEM,
                    ConsistentCommands.SET_BUSYBOX_600 + CACHE_PATH + Consistent.BUSYBOX_FILE
            });


            GetVersionHelper getVersionHelper = new GetVersionHelper();

            mTextMap = getVersionHelper.getVersion(mContext);

            mIsDNSEnable = getDNS();

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();


            setHostsVersion();
            setSoftwareVersion();
            setBusyboxExpress();
            setBackup();
            setMark();
            setLocal();
            setDNS();
            setSnackBar();
            getWriteState();
            closeDrawer();
            mDrawerLayout.closeDrawer(GravityCompat.START);


        }

        private void setHostsVersion() {
            String adVersion = mTextMap.get(KEY_AD);
            String reVersion = mTextMap.get(KEY_RE);
            String arVersion = mTextMap.get(KEY_AR);
            String hostsVersion = mTextMap.get(KEY_LOCAL_HOSTS_VERSION);
            String hostsKind = mTextMap.get(KEY_LOCAL_HOSTS_KIND);


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

        private void setSoftwareVersion() {

            String software = mTextMap.get(KEY_VERSION);
            if (null != software) {

                if (Integer.parseInt(software) == SOFTWARE_VERSION) {
                    mCheckBoxVersion.setChecked(true);
                    mCheckBoxVersion.setText(R.string.software_last_version);
                } else {
                    mCheckBoxVersion.setChecked(true);
                    mCheckBoxVersion.setText(R.string.software_new_version);
                }
            }
        }

        private void setSnackBar() {
            String other = mTextMap.get(KEY_OTHER);
            mSnackBar = SnackBarHelper.showSnackBar(mSnackBar, mCoordinatorLayout, other);
        }

        private void closeDrawer() {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }

        private void setBusyboxExpress() {
            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            String busyboxFile = CACHE_PATH + Consistent.BUSYBOX_FILE;
            file = new File(busyboxFile);

            if (file.exists()) {
                mCheckBoxBusybox.setChecked(true);
                mCheckBoxBusybox.setText(R.string.busybox_expressed);
            } else {
                mCheckBoxBusybox.setChecked(false);
                mCheckBoxBusybox.setText(R.string.busybox_no_exists);
            }

        }

        private void setBackup() {
            File file = mContext.getFilesDir();
            final String FILE_PATH = file.getAbsolutePath();
            String backupFile = FILE_PATH + Consistent.BACKUP_FILE;
            file = new File(backupFile);
            if (file.exists()) {
                mCheckBoxBackUp.setChecked(true);
                mCheckBoxBackUp.setText(R.string.backup_exists_checkbox);
            } else {
                mCheckBoxBackUp.setChecked(false);
                mCheckBoxBackUp.setText(R.string.backup_no_exists_checkbox);
            }
        }

        private void setMark() {
            if (mMarkResult.get(0).equals(Consistent.NO_SUPPORT)) {
                mCheckBoxMark.setChecked(false);
                mCheckBoxMark.setText(R.string.mark_server_default);
            } else {
                mCheckBoxMark.setChecked(true);
                mCheckBoxMark.setText(R.string.mark_server_changed);
            }
        }

        private void getWriteState() {
            if (mSystemWriteResult.size() > 2) {
                Toast.makeText(mContext, mContext.getString(R.string.system_write_failed), Toast.LENGTH_LONG).show();
            }
        }

        private void setLocal() {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, false)) {
                mCheckBoxLocal.setChecked(true);
                mCheckBoxLocal.setText(R.string.local_enable);
            } else {
                mCheckBoxLocal.setChecked(false);
                mCheckBoxLocal.setText(R.string.local_disable);
            }


        }

        private void setDNS() {

            if (mIsDNSEnable) {
                mCheckBoxDNS.setChecked(true);
                mCheckBoxDNS.setText(R.string.dns_enable);
            } else {
                mCheckBoxDNS.setChecked(false);
                mCheckBoxDNS.setText(R.string.dns_disable);
            }
        }

        private boolean getDNS() {
            boolean isDNSEnable;
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
            isDNSEnable = sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, false);
            if (isDNSEnable) {
                Intent startIntent = new Intent(mContext, ChangeDNSService.class);
                mContext.startService(startIntent);
            }
            return isDNSEnable;
        }

    }


}
