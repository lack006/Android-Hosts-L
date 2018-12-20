package com.lack006.hosts_l.RemoveMark;

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

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.util.List;

/**
 * Created by lack on 2016/12/18.
 * AndroidHosts-LV7
 */


public class RemoveMarkTaskHelper {
    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private AlertDialog.Builder mDialog = null;
    private ProgressDialog mProgressDialogSearch = null;
    private List<String> mTypeResult = null;
    private String mCustomServer = "";
    private boolean mUseHttps = false;
    private boolean mEnableServer = false;
    private boolean is712 = false;


    public void removeMark(Context context, CheckBox checkBox) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            beforeLollipop();
        } else {

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


    }

    private class GetMarkInfoTask extends AsyncTask<Object, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {

            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mTypeResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.GET_MARK_ENABLE,
                    ConsistentCommands.GET_MARK_URL,
                    ConsistentCommands.GET_HTTPS,
                    ConsistentCommands.GET_MARK_HTTP_URL,
                    ConsistentCommands.GET_MARK_HTTPS_URL,
                    ConsistentCommands.GET_MARK_MODE
            });
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mProgressDialogSearch.cancel();
            mDialog = new AlertDialog.Builder(mContext);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //5.0-6.0
                betweenLollipop2Nougat();
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                //7.0-<7.1
                atNougat();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                //>7.1.1-8.1.0-9
                atNougat_MR1ToOreo27();
            } else {
                noSupport();
            }
        }
    }


    private void beforeLollipop() {
        mDialog.setMessage(R.string.no_need_remove_mark);
        mDialog.setNegativeButton(R.string.next_step, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        mDialog.show();
    }

    private void betweenLollipop2Nougat() {
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.remove_mark_dialog_layout, null);
        final TextInputLayout textInputLayout = textEntryView.findViewById(R.id.server_edt_layout);
        final TextInputEditText urlEditText = textEntryView.findViewById(R.id.remove_edt);
        final TextView serverTxv = textEntryView.findViewById(R.id.captive_portal_server_txv);
        final Switch httpsSwitch = textEntryView.findViewById(R.id.https_switch);
        final Switch checkSwitch = textEntryView.findViewById(R.id.check_switch);

        if (mTypeResult.get(Consistent.GET_MARK_URL).equals(Consistent.NO_SUPPORT)) {
            serverTxv.setText(R.string.default_server);
        } else {
            serverTxv.setText(mTypeResult.get(Consistent.GET_MARK_URL));
        }

        urlEditText.setText(Consistent.DEFAULT_SERVER);
        urlEditText.setSelection(urlEditText.getText().length());


        httpsSwitch.setChecked(false);
        httpsSwitch.setClickable(false);
        httpsSwitch.setText(R.string.https_disable);

        checkSwitch.setChecked(Consistent.ENABLE.equals(mTypeResult.get(Consistent.GET_MARK_ENABLE)));

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
                SetDefaultServerBetweenLollipop2NougatTask setDefaultServerBetweenLollipop2NougatTask = new SetDefaultServerBetweenLollipop2NougatTask();
                setDefaultServerBetweenLollipop2NougatTask.setDefaultServerBetweenLollipop2Nougat(mContext, mCheckBox);

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
                    mEnableServer = checkSwitch.isChecked();
                    if (!mEnableServer) {
                        SetCustomServerBetweenLollipop2NougatTask setCustomServerBetweenLollipop2NougatTask = new SetCustomServerBetweenLollipop2NougatTask();
                        setCustomServerBetweenLollipop2NougatTask.setCustomerServer(mContext, mCheckBox, mEnableServer, mCustomServer);
                        alertDialog.cancel();
                    } else {
                        if (Consistent.DEFAULT_SERVER.equals(mCustomServer) && httpsSwitch.isChecked()) {
                            textInputLayout.setError(mContext.getString(R.string.default_no_support_https));
                        } else if (mCustomServer.equals("")) {
                            textInputLayout.setError(mContext.getString(R.string.no_customer_server));
                        } else {
                            SetCustomServerBetweenLollipop2NougatTask setCustomServerBetweenLollipop2NougatTask = new SetCustomServerBetweenLollipop2NougatTask();
                            setCustomServerBetweenLollipop2NougatTask.setCustomerServer(mContext, mCheckBox, mEnableServer, mCustomServer);

                            alertDialog.cancel();

                        }
                    }


                }

            });

    }

    private void atNougat() {


        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.remove_mark_dialog_layout, null);
        final TextInputLayout textInputLayout = textEntryView.findViewById(R.id.server_edt_layout);
        final TextInputEditText urlEditText = textEntryView.findViewById(R.id.remove_edt);
        final TextView serverTxv = textEntryView.findViewById(R.id.captive_portal_server_txv);
        final Switch httpsSwitch = textEntryView.findViewById(R.id.https_switch);
        final Switch checkSwitch = textEntryView.findViewById(R.id.check_switch);

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

        checkSwitch.setChecked(Consistent.ENABLE.equals(mTypeResult.get(Consistent.GET_MARK_ENABLE)));

        if (mTypeResult.get(Consistent.GET_MARK_URL).equals(Consistent.NO_SUPPORT)) {
            serverTxv.setText(R.string.default_server);
        } else {
            serverTxv.setText(mTypeResult.get(Consistent.GET_MARK_URL));
        }


        mUseHttps = !(mTypeResult.get(Consistent.GET_HTTPS_ENABLE).equals(Consistent.DISABLE));
        httpsSwitch.setClickable(true);
        if (mUseHttps) {
            httpsSwitch.setChecked(true);
        } else {
            httpsSwitch.setChecked(false);
        }

        urlEditText.setText(Consistent.DEFAULT_SERVER);
        urlEditText.setSelection(urlEditText.getText().length());


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
                SetDefaultServerBetweenLollipop2NougatTask setDefaultServerBetweenLollipopAndNougatTask = new SetDefaultServerBetweenLollipop2NougatTask();
                setDefaultServerBetweenLollipopAndNougatTask.setDefaultServerBetweenLollipop2Nougat(mContext, mCheckBox);

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
                    mEnableServer = checkSwitch.isChecked();
                    mUseHttps = httpsSwitch.isChecked();
                    if (!mEnableServer) {
                        SetCustomServerAtNougatTask setCustomServerAtNougatTask = new SetCustomServerAtNougatTask();
                        setCustomServerAtNougatTask.setCustomerServer(mContext, mCheckBox, mEnableServer, mCustomServer, mUseHttps);
                        alertDialog.cancel();
                    } else {
                       if (mCustomServer.equals("")) {
                            textInputLayout.setError(mContext.getString(R.string.no_customer_server));
                        } else {
                            SetCustomServerAtNougatTask setCustomServerAtNougatTask = new SetCustomServerAtNougatTask();
                            setCustomServerAtNougatTask.setCustomerServer(mContext, mCheckBox, mEnableServer, mCustomServer, mUseHttps);

                            alertDialog.cancel();

                        }
                    }


                }

            });
    }


    private void atNougat_MR1ToOreo27() {


        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.remove_mark_dialog_layout, null);
        final TextInputLayout textInputLayout = textEntryView.findViewById(R.id.server_edt_layout);
        final TextInputEditText urlEditText = textEntryView.findViewById(R.id.remove_edt);
        final TextView serverTxv = textEntryView.findViewById(R.id.captive_portal_server_txv);
        final Switch httpsSwitch = textEntryView.findViewById(R.id.https_switch);
        final Switch checkSwitch = textEntryView.findViewById(R.id.check_switch);
        httpsSwitch.setEnabled(Consistent.OFF);
        checkSwitch.setEnabled(Consistent.OFF);

                if (Consistent.NO_SUPPORT.equals(mTypeResult.get(Consistent.GET_HTTPS_SERVER))) {
                    serverTxv.setText(R.string.default_server);
                } else {
                    serverTxv.setText(mTypeResult.get(Consistent.GET_HTTPS_SERVER));
                }

            urlEditText.setText(Consistent.DEFAULT_SERVER_711);
            urlEditText.setSelection(urlEditText.getText().length());

        mDialog.setView(textEntryView);
        mDialog.setCancelable(false);
        mDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

                       if (mCustomServer.equals("")) {
                            textInputLayout.setError(mContext.getString(R.string.no_customer_server));
                        } else {
                            SetCustomServerAfterNougatTask setCustomServerAfterNougatTask = new SetCustomServerAfterNougatTask();
                            setCustomServerAfterNougatTask.setCustomerServer(mContext, mCheckBox, mEnableServer, mCustomServer, mUseHttps);

                            alertDialog.cancel();

                        }



                }

            });
    }

    private void noSupport() {
        mDialog.setMessage(R.string.no_support_remove_mark);
        mDialog.setNegativeButton(R.string.next_step, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        mDialog.show();
    }


}
