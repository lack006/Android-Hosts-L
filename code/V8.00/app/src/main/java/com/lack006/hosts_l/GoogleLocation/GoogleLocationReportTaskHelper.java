package com.lack006.hosts_l.GoogleLocation;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/8.
 * AndroidHosts-LV7
 */

public class GoogleLocationReportTaskHelper {
    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private CheckBox mCheckBox = null;
    private boolean[] mCheck = null;
    private boolean mIsRestore = false;

    public void enableChoose(Context context, CheckBox checkBox) {
        mContext = context;
        String[] choose = mContext.getResources().getStringArray(R.array.choose);
        mCheck = new boolean[choose.length];
        mCheckBox = checkBox;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.applying_local));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);


        new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle(mContext.getString(R.string.local_choose))
                .setMultiChoiceItems(choose, mCheck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mCheck[which] = isChecked;
                    }
                })
                .setPositiveButton(mContext.getString(R.string.set_change), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        mProgressDialog.show();
                        EnableGoogleLocationReportTask enableGoogleLocationReportTask = new EnableGoogleLocationReportTask();
                        enableGoogleLocationReportTask.execute();
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setNeutralButton(mContext.getString(R.string.set_default), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mProgressDialog.show();
                        mIsRestore = true;
                        EnableGoogleLocationReportTask enableGoogleLocationReportTask = new EnableGoogleLocationReportTask();
                        enableGoogleLocationReportTask.execute();
                    }
                }).show();
        Toast.makeText(mContext, mContext.getString(R.string.google_location_before_hint), Toast.LENGTH_LONG).show();

    }

    private class EnableGoogleLocationReportTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            if (!mIsRestore) {

                enableBootReceiver(mContext);
                final SharedPreferences mPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, ContextWrapper.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, Consistent.ON);
                editor.apply();

                enableGoogleLocalService(mContext);
                Intent startIntent = new Intent(mContext, GoogleLocationEnableService.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    mContext.startForegroundService(startIntent);
//                } else {
                    mContext.startService(startIntent);
//                }

                if (mCheck[0]) {
                    cleanPlayServerShell();
                }
                if (mCheck[1]) {
                    cleanMapShell();
                }
                if (mCheck[2]) {
                    rebootShell();
                }

            } else {
                final SharedPreferences mPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, ContextWrapper.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_LOCAL, Consistent.OFF);
                editor.apply();
                Intent endIntent = new Intent(mContext, GoogleLocationEnableService.class);
                mContext.stopService(endIntent);
                disableGoogleLocalService(mContext);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();
            if (!mIsRestore) {

                Toast.makeText(mContext, mContext.getString(R.string.google_location_hint), Toast.LENGTH_LONG).show();
//                Toast.makeText(mContext, mContext.getString(R.string.location_report_enable), Toast.LENGTH_SHORT).show();
                mCheckBox.setChecked(true);
                mCheckBox.setText(R.string.local_enable);

            } else {
                Toast.makeText(mContext, mContext.getString(R.string.location_report_disable), Toast.LENGTH_SHORT).show();
                mCheckBox.setChecked(false);
                mCheckBox.setText(R.string.local_disable);
            }

        }


    }

    public void enableGoogleLocalShell() {
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.CHANGE_SIM,
                ConsistentCommands.CHANGE_COUNTRY
        });
    }

    private void cleanPlayServerShell() {
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.CLEAR_PLAY_SERVER
        });
    }

    private void cleanMapShell() {
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.CLEAR_MAP
        });
    }

    private void rebootShell() {
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.HOT_REBOOT
        });
    }

    private void enableBootReceiver(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    private void enableGoogleLocalService(Context context) {
        ComponentName receiver;
        PackageManager pm = context.getPackageManager();
        receiver = new ComponentName(context, GoogleLocationEnableService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void disableGoogleLocalService(Context context) {
        ComponentName receiver;
        PackageManager pm = context.getPackageManager();
        receiver = new ComponentName(context, GoogleLocationEnableService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
