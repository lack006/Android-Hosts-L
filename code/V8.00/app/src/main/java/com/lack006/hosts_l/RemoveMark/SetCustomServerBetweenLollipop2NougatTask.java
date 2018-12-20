package com.lack006.hosts_l.RemoveMark;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/18.
 * AndroidHosts-LV7
 */

class SetCustomServerBetweenLollipop2NougatTask {
    private Context mContext = null;
    private ProgressDialog mProgressDialogApply = null;
    private CheckBox mCheckBox = null;

    private boolean mEnable = false;
    private String mServer = null;

    void setCustomerServer(Context context, CheckBox checkBox, boolean enable, String server) {
        mContext = context;
        mCheckBox = checkBox;
        mEnable = enable;
        mServer = server;
        mProgressDialogApply = new ProgressDialog(mContext);
        mProgressDialogApply.setMessage(mContext.getString(R.string.Applying_Mark));
        mProgressDialogApply.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialogApply.setIndeterminate(true);
        mProgressDialogApply.setCancelable(false);
        mProgressDialogApply.show();

        SetCustomServerTask setCustomServerTask = new SetCustomServerTask();
        setCustomServerTask.execute();
    }

    private class SetCustomServerTask extends AsyncTask<Object, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {

            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            if (mEnable) {
                suShellReturnHelper.run(new String[]{
                        ConsistentCommands.SET_REMOVE_MARK_COMMAND + mServer,
                        ConsistentCommands.SET_SERVER_ENABLE
                });
            } else {
                suShellReturnHelper.run(new String[]{
                        ConsistentCommands.SET_SERVER_DISABLE
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mProgressDialogApply.cancel();
            Toast.makeText(mContext, R.string.remove_mark_success, Toast.LENGTH_SHORT).show();
            mCheckBox.setChecked(true);
            mCheckBox.setText(R.string.mark_server_changed);
        }
    }
}
