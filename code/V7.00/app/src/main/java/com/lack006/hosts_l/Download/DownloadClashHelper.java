package com.lack006.hosts_l.Download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;

import com.lack006.hosts_l.Clash.GetClashHelper;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Dialog.HostsChooseHelper;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.lack006.hosts_l.Consistent.Consistent.TIME_OUT;

/**
 * Created by lack on 2016/12/3.
 * AndroidHosts-LV7
 */

public class DownloadClashHelper {
    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private CheckBox mCheckBox = null;
    private Map<String[], String[]> mClashMap = new HashMap<>();
    private boolean mIsAR = false;

    public void downloadClash(Context context, CheckBox checkBox, boolean isAR) {
        mContext = context;
        mCheckBox = checkBox;
        mIsAR = isAR;

        CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
        cleanCacheHelper.cleanClash(mContext);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.download_clash));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        DownloadClashTask downloadClashTask = new DownloadClashTask();
        downloadClashTask.execute();

    }

    private class DownloadClashTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {


            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            URL url;
            try {

                url = new URL(mContext.getString(R.string.clash_url));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(TIME_OUT);
                con.setRequestProperty("Accept-Encoding", "identity");
                InputStream in = con.getInputStream();
                File fileOut = new File(CACHE_PATH + Consistent.CLASH_FILE);
                FileOutputStream out = new FileOutputStream(fileOut);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                in.close();
                out.close();
                GetClashHelper getClashHelper = new GetClashHelper();

                mClashMap = getClashHelper.getClash(mContext);


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            HostsChooseHelper hostsChooseHelper = new HostsChooseHelper();
            hostsChooseHelper.adHostsChoose(mContext, mClashMap, mCheckBox, mIsAR);
            mProgressDialog.cancel();

        }
    }
}
