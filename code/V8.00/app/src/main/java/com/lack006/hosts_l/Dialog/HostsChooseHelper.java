package com.lack006.hosts_l.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Download.CustomDownloadTaskHelper;
import com.lack006.hosts_l.Download.DownloadTaskHelper;
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
    private String mCustomUrl = null;

    public void adHostsChoose(Context context, Map<String[], String[]> clashMap, CheckBox checkBox, final boolean isAR) {
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

        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View reChooseView = factory.inflate(R.layout.re_choose_dialog_layout, null);
        final TextInputLayout textInputLayout = (TextInputLayout) reChooseView.findViewById(R.id.custom_edt_layout);
        final TextInputEditText textInputEditText = (TextInputEditText) reChooseView.findViewById(R.id.custom_edit);
        final Switch defaultSwitch = (Switch) reChooseView.findViewById(R.id.default_switch);
        final Switch customSwitch = (Switch) reChooseView.findViewById(R.id.custom_switch);
        final CheckBox mapCheckBox = (CheckBox) reChooseView.findViewById(R.id.map_checkbox);
        final CheckBox keepScreenOnCheckBox = (CheckBox) reChooseView.findViewById(R.id.keep_screen_on_checkbox);

        defaultSwitch.setChecked(true);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        if (!mIsAR) {
            keepScreenOnCheckBox.setVisibility(View.GONE);
        } else {
            if (sharedPreferences.getBoolean(Consistent.SHARED_PREFERENCES_KEEP_SCREEN_ON, false)) {
                keepScreenOnCheckBox.setChecked(true);
            }
        }

        mCustomUrl = sharedPreferences.getString(Consistent.SHARED_PREFERENCES_CUSTOM_URL, "");
        if (!mCustomUrl.equals("")) {
            textInputEditText.setText(mCustomUrl);
            customSwitch.setChecked(true);
            defaultSwitch.setChecked(false);
            textInputEditText.requestFocus();
        }

        defaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (defaultSwitch.isChecked()) {
                    customSwitch.setChecked(false);
                } else {
                    customSwitch.setChecked(true);
                    mapCheckBox.setChecked(false);
                }
            }
        });
        customSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (customSwitch.isChecked()) {
                    defaultSwitch.setChecked(false);
                    mapCheckBox.setChecked(false);
                } else {
                    defaultSwitch.setChecked(true);
                }
            }
        });

        mapCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mapCheckBox.isChecked()) {
                    defaultSwitch.setChecked(true);
                    customSwitch.setChecked(false);
                }
            }
        });
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (defaultSwitch.isChecked()) {
                    defaultSwitch.setChecked(false);
                    mapCheckBox.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(false);
        dialog.setTitle(mContext.getString(R.string.choose_re_title));
        dialog.setView(reChooseView);
        dialog.setView(reChooseView).setPositiveButton(mContext.getString(R.string.next_step), null);
        dialog.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        if (null != alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsAR) {
                        if (defaultSwitch.isChecked()) {
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Consistent.SHARED_PREFERENCES_CUSTOM_URL, "");
                            editor.apply();
                            if (mapCheckBox.isChecked()) {
                                DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                                downloadTaskHelper.download(mContext, mContext.getString(R.string.ar_map_url), mHosts, mCheck, mCheckBox);
                            } else {
                                DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                                downloadTaskHelper.download(mContext, mContext.getString(R.string.ar_url), mHosts, mCheck, mCheckBox);
                            }
                            alertDialog.cancel();
                        } else {
                            mCustomUrl = textInputEditText.getText().toString();
                            if (mCustomUrl.replace(" ", "").equals("")) {
                                textInputLayout.setError(mContext.getString(R.string.wrong_custom_url));
                            } else {

                                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                boolean keepScreeOn = keepScreenOnCheckBox.isChecked();
                                if (keepScreeOn) {
                                    editor.putBoolean(Consistent.SHARED_PREFERENCES_KEEP_SCREEN_ON, true);
                                }

                                editor.putString(Consistent.SHARED_PREFERENCES_CUSTOM_URL, mCustomUrl);
                                editor.putBoolean(Consistent.SHARED_PREFERENCES_KEEP_SCREEN_ON, keepScreeOn);
                                editor.apply();
                                CustomDownloadTaskHelper customDownloadTaskHelper = new CustomDownloadTaskHelper();
                                customDownloadTaskHelper.download(mContext, mCustomUrl, mHosts, mCheck, mCheckBox, keepScreeOn);
                                alertDialog.cancel();
                            }
                        }

                    } else {
                        if (defaultSwitch.isChecked()) {
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Consistent.SHARED_PREFERENCES_CUSTOM_URL, "");
                            editor.apply();
                            if (mapCheckBox.isChecked()) {
                                DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                                downloadTaskHelper.download(mContext, mContext.getString(R.string.re_map_url), null, null, mCheckBox);
                            } else {
                                DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                                downloadTaskHelper.download(mContext, mContext.getString(R.string.re_url), null, null, mCheckBox);
                            }
                            alertDialog.cancel();
                        } else {
                            mCustomUrl = textInputEditText.getText().toString();
                            if (mCustomUrl.replace(" ", "").equals("")) {
                                textInputLayout.setError(mContext.getString(R.string.wrong_custom_url));
                            } else {
                                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Consistent.SHARED_PREFERENCES_CUSTOM_URL, mCustomUrl);
                                editor.apply();
                                DownloadTaskHelper downloadTaskHelper = new DownloadTaskHelper();
                                downloadTaskHelper.download(mContext, mCustomUrl, null, null, mCheckBox);
                                alertDialog.cancel();
                            }
                        }

                    }
                }


            });
        }


    }
}
