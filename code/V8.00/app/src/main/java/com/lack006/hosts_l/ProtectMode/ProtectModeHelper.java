package com.lack006.hosts_l.ProtectMode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.lack006.hosts_l.CheckVersion.GetVersionHelper;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.io.File;
import java.util.List;

import static com.lack006.hosts_l.Consistent.Consistent.SYSTEM_ETC_HOSTS;

/**
 * Created by lack on 2017/1/9.
 * AndroidHosts-LV7
 */

public class ProtectModeHelper {
    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private CheckBox mCheckBox = null;

    public void protectModeDialog(final Context context, CheckBox checkBox) {
        mCheckBox = checkBox;
        mContext = context;
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.protect_mode);
        LayoutInflater factory = LayoutInflater.from(context);
        View layout = factory.inflate(R.layout.about_dialog_layout, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setText(R.string.protect_mode_message);
        dialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setPositiveButton(R.string.protect_on, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Consistent.SHARED_PREFERENCES_PROTECT_MODE, Consistent.ON);
                editor.apply();
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(context.getString(R.string.protect_hint));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                RefreshTask refreshTask = new RefreshTask();
                refreshTask.execute(context);
            }
        });
        dialog.setNegativeButton(R.string.protect_off, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Consistent.SHARED_PREFERENCES_PROTECT_MODE, Consistent.OFF);
                editor.apply();
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(context.getString(R.string.protect_hint));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                RefreshTask refreshTask = new RefreshTask();
                refreshTask.execute(context);
            }
        });
        dialog.setView(layout);
        dialog.show();
    }

    public void prepareRead(Context context) {
        List<String> permissionResult;
        File file = context.getCacheDir();
        String CACHE_PATH = file.getAbsolutePath();
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        permissionResult = suShellReturnHelper.run(new String[]{
                ConsistentCommands.GET_PERMISSION + SYSTEM_ETC_HOSTS
        });
        if (permissionResult.size() != 0) {
            if (permissionResult.get(0).contains(Consistent.PROTECTED)) {
                suShellReturnHelper.run(new String[]{
                        ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                        ConsistentCommands.SET_RW_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                        ConsistentCommands.RW_SYSTEM,
                        ConsistentCommands.DISABLE_PROTECT + Consistent.SYSTEM_ETC_HOSTS,
                        ConsistentCommands.RO_SYSTEM,
                        ConsistentCommands.SET_RW_600 + CACHE_PATH + Consistent.BUSYBOX_FILE
                });
            }
        }
    }

    public void applyProtectMode(Context context) {
        boolean isProtectMode;
        File file = context.getCacheDir();
        String CACHE_PATH = file.getAbsolutePath();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        isProtectMode = sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_PROTECT_MODE, false);
        if (isProtectMode) {
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_BUSYBOX_PATH + CACHE_PATH,
                    ConsistentCommands.SET_RW_777 + CACHE_PATH + Consistent.BUSYBOX_FILE,
                    ConsistentCommands.RW_SYSTEM,
                    ConsistentCommands.ENABLE_PROTECT + Consistent.SYSTEM_ETC_HOSTS,
                    ConsistentCommands.RO_SYSTEM,
                    ConsistentCommands.SET_RW_600 + CACHE_PATH + Consistent.BUSYBOX_FILE
            });
        }
    }

    private class RefreshTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            GetVersionHelper getVersionHelper = new GetVersionHelper();
            getVersionHelper.getVersion((Context) objects[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();
            Toast.makeText(mContext,mContext.getString(R.string.protect_finish),Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
            boolean protect = sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_PROTECT_MODE, false);
            mCheckBox.setChecked(protect);
            if(protect){
                mCheckBox.setText(R.string.protect_on_checkbox);
            }else{
                mCheckBox.setText(R.string.protect_off_checkbox);
            }

        }


    }
}
