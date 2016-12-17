package com.lack006.hosts_l.Download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Clash.InsertClashTaskHelper;
import com.lack006.hosts_l.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.lack006.hosts_l.Consistent.Consistent.CONNECTED;
import static com.lack006.hosts_l.Consistent.Consistent.CUSTOM_ERROR;
import static com.lack006.hosts_l.Consistent.Consistent.DOWNLOAD_CANCEL;
import static com.lack006.hosts_l.Consistent.Consistent.MIX_HOSTS;
import static com.lack006.hosts_l.Consistent.Consistent.TIME_OUT;

/**
 * Created by lack on 2016/12/15.
 * AndroidHosts-LV7
 */

public class CustomDownloadTaskHelper {
    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private ProgressDialog mMixProgressDialog = null;
    private CheckBox mCheckBox = null;
    private String[] mHosts = null;
    private boolean[] mCheck = null;
    private boolean mStopFlag = false;
    private boolean mIsReError = false;
    private boolean mIsKeepScreenOn = false;

    private String mUrl = null;


    public void download(Context context, String url, String[] hosts, boolean[] check, CheckBox checkBox, boolean keepScreenOn) {

        mContext = context;
        mHosts = hosts;
        mCheck = check;
        mCheckBox = checkBox;
        mUrl = url;
        mIsKeepScreenOn = keepScreenOn;


        CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
        cleanCacheHelper.cleanHosts(mContext);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.connecting));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
            long downloadSize = 0;
            try {

                url = new URL(mContext.getString(R.string.ad_url));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(TIME_OUT);
                con.setRequestProperty("Accept-Encoding", "identity");
                InputStream in = con.getInputStream();
                File fileOut = new File(CACHE_PATH + Consistent.HOSTS_FILE);
                FileOutputStream out = new FileOutputStream(fileOut);
                byte[] bytes = new byte[1024];
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
                    publishProgress((int) (downloadSize * 100 / fileSize));

                }
                in.close();
                out.close();
                downloadSize = 0;
                publishProgress(0);
                if (!mStopFlag) {
                    mIsReError = true;
                    url = new URL(mUrl);
                    con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(TIME_OUT);
                    con.setRequestProperty("Accept-Encoding", "identity");
                    in = con.getInputStream();
                    fileOut = new File(CACHE_PATH + Consistent.RE_FILE);
                    out = new FileOutputStream(fileOut);
                    bytes = new byte[1024];

                    fileSize = con.getContentLength();
                    publishProgress(Consistent.CONNECTED);
                    while ((len = in.read(bytes)) != -1) {
                        if (mStopFlag) {
                            con.disconnect();
                            break;
                        }
                        out.write(bytes, 0, len);
                        downloadSize += len;
                        publishProgress((int) (downloadSize * 100 / fileSize));

                    }
                    in.close();
                    out.close();
                    publishProgress(Consistent.MIX_HOSTS);
                    mixHosts();


                }


            } catch (Exception e) {
                e.printStackTrace();
                mStopFlag = true;
                if (mIsReError) {
                    publishProgress(Consistent.CUSTOM_ERROR);
                } else {
                    publishProgress(Consistent.DOWNLOAD_CANCEL);
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (mIsKeepScreenOn) {
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                Toast.makeText(mContext, mContext.getString(R.string.keep_screen_on_hint_off), Toast.LENGTH_SHORT).show();
            }

            if (!mStopFlag) {

                mMixProgressDialog.cancel();

                InsertClashTaskHelper insertClashTaskHelper = new InsertClashTaskHelper();
                insertClashTaskHelper.insertClashHelper(mContext, mHosts, mCheck, mCheckBox);


            } else {
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
            } else if (values[0] == MIX_HOSTS) {
                if (mIsKeepScreenOn) {
                    ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    Toast.makeText(mContext, mContext.getString(R.string.keep_screen_on_hint_on), Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.cancel();
                mMixProgressDialog = new ProgressDialog(mContext);
                mMixProgressDialog.setMessage(mContext.getString(R.string.mix_hosts));
                mMixProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mMixProgressDialog.setIndeterminate(true);
                mMixProgressDialog.setCancelable(false);
                mMixProgressDialog.show();
            } else if (values[0] == CUSTOM_ERROR) {
                Toast.makeText(mContext, mContext.getString(R.string.custom_error), Toast.LENGTH_SHORT).show();
                mProgressDialog.cancel();
            } else {
                mProgressDialog.setProgress(values[0]);
            }
        }

        void mixHosts() {
            File file = mContext.getCacheDir();
            final String CACHE_PATH = file.getAbsolutePath();
            List<String> mReClashList = new ArrayList<>();
            try {
                BufferedReader adReader;
                adReader = new BufferedReader(new FileReader(CACHE_PATH + Consistent.HOSTS_FILE));

                String line;
                while ((line = adReader.readLine()) != null) {
                    if (!(line.contains(Consistent.HASH_TAG) || line.equals(""))) {
                        mReClashList.add(line.replace(Consistent.LOCAL_HOST + " ", ""));
                    }
                }
                adReader.close();

                BufferedReader reReader = new BufferedReader(new FileReader(CACHE_PATH + Consistent.RE_FILE));
                FileOutputStream fileOutputStream = new FileOutputStream(CACHE_PATH + Consistent.HOSTS_FILE, true);

                boolean flag = false;

                while ((line = reReader.readLine()) != null) {
                    for (String tmp : mReClashList) {
                        if (line.contains(tmp)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        fileOutputStream.write((line + "\n").getBytes());
                    } else {
                        flag = false;
                    }

                }
                fileOutputStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
