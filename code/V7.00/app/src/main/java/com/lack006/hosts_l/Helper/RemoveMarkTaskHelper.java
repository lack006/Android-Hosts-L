package com.lack006.hosts_l.Helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent;
import com.lack006.hosts_l.ConsistentCommands;
import com.lack006.hosts_l.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lack on 2016/12/4.
 * AndroidHosts-LV7
 */

public class RemoveMarkTaskHelper {

    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private AlertDialog.Builder mDialog = null;
    private ProgressDialog mProgressDialogSearch = null;
    private ProgressDialog mProgressDialogApply = null;
    private List<String> mTypeResult = null;
    private List<String> mCmdResult = null;
    private String mCustomServer = "";
    private boolean mUseHttps = false;
    private boolean mEnableServer = false;
    private boolean mIsSetDefault = false;
    private boolean mIsHttpsSupport = false;

    public void removeMark(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mProgressDialogSearch = new ProgressDialog(mContext);
        mProgressDialogSearch.setMessage(mContext.getString(R.string.getting_mark_info));
        mProgressDialogSearch.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialogSearch.setIndeterminate(true);
        mProgressDialogSearch.setCancelable(false);
        mProgressDialogSearch.show();
        GetMarkInfoTask getMarkInfoTask = new GetMarkInfoTask();
        getMarkInfoTask.execute();


    }

    private class GetMarkInfoTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {

            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mTypeResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.GET_IS_ENABLE,
                    ConsistentCommands.GET_HTTPS,
                    ConsistentCommands.GET_MARK_URL

            });


            return null;
        }


        @Override
        protected void onPostExecute(Boolean result) {


            mProgressDialogSearch.cancel();
            mDialog = new AlertDialog.Builder(mContext);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

                mDialog.setMessage(R.string.no_need_remove_mark);
                mDialog.setNegativeButton(R.string.next_step, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                mDialog.show();

            } else {

                mProgressDialogApply = new ProgressDialog(mContext);
                mProgressDialogApply.setMessage(mContext.getString(R.string.Applying_Mark));
                mProgressDialogApply.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialogApply.setIndeterminate(true);
                mProgressDialogApply.setCancelable(false);


                final LayoutInflater factory = LayoutInflater.from(mContext);
                final View textEntryView = factory.inflate(R.layout.remove_mark_dialog_layout, null);
                final TextInputLayout textInputLayout = (TextInputLayout) textEntryView.findViewById(R.id.server_edt_layout);
                final TextInputEditText urlEditText = (TextInputEditText) textEntryView.findViewById(R.id.remove_edt);
                final TextView serverTxv = (TextView) textEntryView.findViewById(R.id.captive_portal_server_txv);
                final Switch httpsSwitch = (Switch) textEntryView.findViewById(R.id.https_switch);
                final Switch checkSwitch = (Switch) textEntryView.findViewById(R.id.check_switch);

                if (mTypeResult.get(Consistent.GET_MARK_URL).equals(Consistent.NO_SUPPORT)) {
                    serverTxv.setText(R.string.default_server);
                } else {
                    serverTxv.setText(mTypeResult.get(Consistent.GET_MARK_URL));
                }

                urlEditText.setText(Consistent.DEFAULT_SERVER);
                urlEditText.setSelection(urlEditText.getText().length());

                final String httpsEnable = mTypeResult.get(Consistent.GET_HTTPS_ENABLE);
                mIsHttpsSupport = !httpsEnable.equals(Consistent.NO_SUPPORT);

                switch (httpsEnable) {
                    case Consistent.NO_SUPPORT:
                        httpsSwitch.setChecked(false);
                        httpsSwitch.setClickable(false);
                        httpsSwitch.setText(R.string.https_disable);
                        break;
                    case Consistent.ENABLE:
                        httpsSwitch.setClickable(true);
                        httpsSwitch.setChecked(true);
                        break;
                    default:
                        httpsSwitch.setClickable(true);
                        httpsSwitch.setChecked(false);
                        break;
                }
                checkSwitch.setChecked(Consistent.ENABLE.equals(mTypeResult.get(Consistent.GET_MARK_ENABLE)));

                httpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (httpsSwitch.isClickable()) {
                            if (b) {
                                httpsSwitch.setText(mContext.getString(R.string.use_https));
                            } else {
                                httpsSwitch.setText(mContext.getString(R.string.use_http));
                            }
                        }
                    }
                });

                checkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            checkSwitch.setText(mContext.getString(R.string.check_on));
                        } else {
                            checkSwitch.setText(mContext.getString(R.string.check_off));
                        }
                    }
                });

                mDialog.setView(textEntryView);
                mDialog.setCancelable(false);
                mDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mDialog.setNeutralButton(R.string.set_default_server, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mIsSetDefault = true;
                        ApplyServerTask applyServerTask = new ApplyServerTask();
                        applyServerTask.execute();
                        mProgressDialogApply.show();

                    }
                });
                mDialog.setPositiveButton(R.string.do_change, null);
                final AlertDialog alertDialog = mDialog.create();
                alertDialog.show();
                if (null != alertDialog.getButton(AlertDialog.BUTTON_POSITIVE))
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCustomServer = urlEditText.getText().toString();

                            if (Consistent.DEFAULT_SERVER.equals(mCustomServer) && httpsSwitch.isChecked()) {
                                textInputLayout.setError(mContext.getString(R.string.default_no_support_https));
                            } else if (mCustomServer.equals("")) {
                                textInputLayout.setError(mContext.getString(R.string.no_customer_server));
                            } else {
                                mIsSetDefault = false;
                                mCustomServer = urlEditText.getText().toString();
                                if (mIsHttpsSupport) {
                                    mUseHttps = httpsSwitch.isChecked();
                                }
                                mEnableServer = checkSwitch.isChecked();
                                ApplyServerTask applyServerTask = new ApplyServerTask();
                                applyServerTask.execute();

                                alertDialog.cancel();
                                mProgressDialogApply.show();
                            }
                        }

                    });


            }
        }


    }

    private class ApplyServerTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            List<String> cmdList = new ArrayList<>();
            if (mIsSetDefault) {
                if (mIsHttpsSupport) {
                    cmdList.add(ConsistentCommands.SET_ENABLE_HTTPS);
                }
                cmdList.add(ConsistentCommands.SET_DEFAULT_MARK_COMMAND);
                cmdList.add(ConsistentCommands.SET_SERVER_ENABLE);
            } else {
                if (!mEnableServer) {
                    cmdList.add(ConsistentCommands.SET_SERVER_DISABLE);
                } else {
                    cmdList.add(ConsistentCommands.SET_SERVER_ENABLE);
                    if (mIsHttpsSupport) {
                        if (mUseHttps) {
                            cmdList.add(ConsistentCommands.SET_ENABLE_HTTPS);
                        } else {
                            cmdList.add(ConsistentCommands.SET_DISABLE_HTTPS);
                        }
                    }
                    cmdList.add(ConsistentCommands.SET_REMOVE_MARK_COMMAND + mCustomServer);
                }
            }
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mCmdResult = suShellReturnHelper.run(cmdList);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            mProgressDialogApply.cancel();

            StringBuilder errorLog = (new StringBuilder());
            if (mIsSetDefault) {
                if (mCmdResult.size() != 1) {
                    for (String line : mCmdResult) {
                        errorLog.append(line).append((char) 10);
                    }
                    Toast.makeText(mContext, mContext.getString(R.string.remove_mark_error) + errorLog.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.remove_mark_success, Toast.LENGTH_SHORT).show();
                    mCheckBox.setChecked(false);
                    mCheckBox.setText(R.string.mark_server_default);
                }
            } else {
                if (mCmdResult.size() != 0) {
                    for (String line : mCmdResult) {
                        errorLog.append(line).append((char) 10);
                    }
                    Toast.makeText(mContext, mContext.getString(R.string.remove_mark_error) + errorLog.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.remove_mark_success, Toast.LENGTH_SHORT).show();
                    mCheckBox.setChecked(true);
                    mCheckBox.setText(R.string.mark_server_changed);
                }
            }
        }


    }

}
