package com.lack006.hosts_l.Download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.CheckBox;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Hosts.ApplyHostsTaskHelper;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Clash.InsertClashTaskHelper;
import com.lack006.hosts_l.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.lack006.hosts_l.Consistent.Consistent.CONNECTED;
import static com.lack006.hosts_l.Consistent.Consistent.DOWNLOAD_CANCEL;
import static com.lack006.hosts_l.Consistent.Consistent.TIME_OUT;

/**
 * Created by lack006 on 2016/10/3.
 * AndroidHosts-LV7
 */

public class DownloadTaskHelper {
    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private CheckBox mCheckBox = null;
    private String[] mHosts = null;
    private boolean[] mCheck = null;
    private boolean mStopFlag = false;

    private String mUrl = null;


    public void download(Context context, String url, String[] hosts, boolean[] check, CheckBox checkBox) {

        mContext = context;
        mHosts = hosts;
        mCheck = check;
        mCheckBox = checkBox;
        mUrl = url;

        CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
        cleanCacheHelper.cleanHosts(mContext);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.connecting));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.setProgress(Consistent.DOWNLOAD_CANCEL);
                mStopFlag = true;

            }
        });
        mProgressDialog.show();

        DownloadHostsTask downloadHostsTask = new DownloadHostsTask();
        downloadHostsTask.execute();


    }

    private class DownloadHostsTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {


            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            URL url;
            long fileSize;
            int downloadSize = 0;
            try {

                url = new URL(mUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(TIME_OUT);
                con.setRequestProperty("Accept-Encoding", "identity");
                InputStream in = con.getInputStream();
                File fileOut = new File(CACHE_PATH + Consistent.HOSTS_FILE);
                FileOutputStream out = new FileOutputStream(fileOut);
                byte[] bytes = new byte[4096];
                int len;
                fileSize = con.getContentLength();
                publishProgress(Consistent.CONNECTED);
                while ((len = in.read(bytes)) != -1) {
                    if (mStopFlag) {
                        con.disconnect();
                        break;
                    }
                    out.write(bytes, 0, len);
                    downloadSize += len;
//                    publishProgress((int) (downloadSize * 100 / fileSize));
                    publishProgress(downloadSize );

                }
                in.close();
                out.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (!mStopFlag) {
                if (mHosts != null) {
                    InsertClashTaskHelper insertClashTaskHelper = new InsertClashTaskHelper();
                    insertClashTaskHelper.insertClashHelper(mContext, mHosts, mCheck, mCheckBox);
                } else {

                    ApplyHostsTaskHelper applyHostsTaskHelper = new ApplyHostsTaskHelper();
                    applyHostsTaskHelper.applyHostsHelper(mContext, mCheckBox);
                }
                mProgressDialog.cancel();

            }else {
                CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
                cleanCacheHelper.cleanHosts(mContext);
            }



        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == CONNECTED) {
                mProgressDialog.setMessage(mContext.getString(R.string.downloading));
            } else if (values[0] == DOWNLOAD_CANCEL) {
                mProgressDialog.cancel();
            } else {
//                mProgressDialog.setProgress(values[0]);
                mProgressDialog.setMessage(mContext.getString(R.string.downloading_progress, values[0]));
            }
        }
    }


}
