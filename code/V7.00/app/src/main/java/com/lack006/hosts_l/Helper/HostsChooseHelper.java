package com.lack006.hosts_l.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;

import com.lack006.hosts_l.R;

import java.util.Map;

/**
 * Created by lack on 2016/12/3.
 * AndroidHosts-LV7
 */

public class HostsChooseHelper {
    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private String[] mText;
    private String[] mHosts;
    private boolean[] mCheck;
    private boolean mIsAR = false;
    private String[] mReText = null;
    private boolean[] mReCheck = null;

    void adHostsChoose(Context context, Map<String[], String[]> clashMap, CheckBox checkBox, final boolean isAR) {
        mContext = context;
        mCheckBox = checkBox;
        mIsAR = isAR;


        for (Map.Entry<String[], String[]> entry : clashMap.entrySet()) {

            int size = entry.getKey().length;
            mText = new String[size];
            mHosts = new String[size];
            mCheck = new boolean[size];
            mHosts = entry.getValue();
            mText = entry.getKey();

        }

        new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle(mContext.getString(R.string.clash_title))
                .setMultiChoiceItems(mText, mCheck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mCheck[which] = isChecked;
                    }
                }).setPositiveButton(mContext.getString(R.string.next_step), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                if (mIsAR) {
                    reHostsChoose(mContext, mCheckBox);
                } else {
                    DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                    downloadTaskHelper.download(mContext, mContext.getString(R.string.ad_url), mHosts, mCheck, mCheckBox);
                }
            }
        })
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();


    }

    public void reHostsChoose(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mReText = new String[]{mContext.getString(R.string.google_map_only)};
        mReCheck = new boolean[]{false};

        new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle(mContext.getString(R.string.choose_re_title))
                .setMultiChoiceItems(mReText, mReCheck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mReCheck[which] = isChecked;
                    }
                }).setPositiveButton(mContext.getString(R.string.next_step), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                if (mIsAR) {

                    if (mReCheck[0]) {
                        DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                        downloadTaskHelper.download(mContext, mContext.getString(R.string.ar_map_url), mHosts, mCheck, mCheckBox);
                    } else {
                        DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                        downloadTaskHelper.download(mContext, mContext.getString(R.string.ar_url), mHosts, mCheck, mCheckBox);
                    }

                } else {
                    if (mReCheck[0]) {
                        DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                        downloadTaskHelper.download(mContext, mContext.getString(R.string.re_map_url), null, null, mCheckBox);
                    } else {
                        DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                        downloadTaskHelper.download(mContext, mContext.getString(R.string.re_url), null, null, mCheckBox);
                    }
                }
            }
        })
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();

    }
}
