package com.lack006.hosts_l.DNS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lack on 2016/12/9.
 * AndroidHosts-LV7
 */

public class ChangeDNSTaskHelper {
    private Context mContext = null;
    private CheckBox mCheckBox = null;
    private ProgressDialog mProgressDialog = null;
    private String mFirstDNS = null;
    private String mSecondDNS = null;
    private boolean mIsRestore = false;

    public void changeDNSHelper(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.apply_dns));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
        mDialog.setCancelable(false);
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.change_dns_dialog_layout, null);
        final TextInputLayout firstDNSLayout = (TextInputLayout) textEntryView.findViewById(R.id.first_dns_edt_layout);
        final TextInputLayout secondDNSLayout = (TextInputLayout) textEntryView.findViewById(R.id.second_dns_edt_layout);
        final TextInputEditText firstDNSEdt = (TextInputEditText) textEntryView.findViewById(R.id.first_dns_edt);
        final TextInputEditText secondDNSEdt = (TextInputEditText) textEntryView.findViewById(R.id.second_dns_edt);
        mDialog.setView(textEntryView);
        mDialog.setNeutralButton(R.string.auto_change_dns_disable, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mIsRestore = true;
                mProgressDialog.show();
                ChangeDNSTask changeDNSTask = new ChangeDNSTask();
                changeDNSTask.execute();
            }
        });
        mDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        mDialog.setPositiveButton(R.string.auto_change_dns_enable, null);
        final AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        if (null != alertDialog.getButton(AlertDialog.BUTTON_POSITIVE))
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean errorFlag = false;
                    mFirstDNS = firstDNSEdt.getText().toString();
                    mSecondDNS = secondDNSEdt.getText().toString();

                    Pattern pattern = Pattern.compile(Consistent.REGEX_IPV4);
                    Matcher matcher = pattern.matcher(mFirstDNS);
                    if (!matcher.find()) {
                        errorFlag = true;
                        firstDNSLayout.setError(mContext.getString(R.string.wrong_dns));
                    }
                    if (!errorFlag) {
                        matcher = pattern.matcher(mSecondDNS);
                        if (matcher.find()) {
                            alertDialog.cancel();
                            mProgressDialog.show();
                            ChangeDNSTask changeDNSTask = new ChangeDNSTask();
                            changeDNSTask.execute();

                        } else {
                            secondDNSLayout.setError(mContext.getString(R.string.wrong_dns));
                        }
                    }


                }

            });
    }

    private class ChangeDNSTask extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            if (!mIsRestore) {
                setDNSSharedPreference(true);
                Intent startIntent = new Intent(mContext, ChangeDNSService.class);
                mContext.startService(startIntent);
            } else {
                setDNSSharedPreference(false);
                Intent stopIntent = new Intent(mContext, ChangeDNSService.class);
                mContext.stopService(stopIntent);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.cancel();
            if (!mIsRestore) {
                Toast.makeText(mContext, mContext.getString(R.string.change_dns_enable), Toast.LENGTH_SHORT).show();
                mCheckBox.setChecked(true);
                mCheckBox.setText(R.string.dns_enable);

            } else {
                Toast.makeText(mContext, mContext.getString(R.string.change_dns_disable), Toast.LENGTH_SHORT).show();
                mCheckBox.setChecked(false);
                mCheckBox.setText(R.string.dns_disable);
            }


        }


    }

    public void setDNSSharedPreference(boolean enable) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, ContextWrapper.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (enable) {

            editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, Consistent.ON);
            editor.putString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_1, mFirstDNS);
            editor.putString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_2, mSecondDNS);

        } else {
            editor.putBoolean(Consistent.SHARED_PREFERENCES_ENABLE_DNS, Consistent.OFF);
        }
        editor.apply();

    }

    public String getLocalNetworkInterfaceName() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                    .getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses(); enumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = enumeration.nextElement();
                    // 如果不是回环地址
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && !inetAddress.isAnyLocalAddress()
                            && !inetAddress.isMCGlobal()
                            && !inetAddress.isMCLinkLocal()
                            && !inetAddress.isMCNodeLocal()
                            && !inetAddress.isMCOrgLocal()
                            && !inetAddress.isMCSiteLocal()
                            && !inetAddress.isMulticastAddress()) {
                        return networkInterface.getName();

                    }
                }
            }
        } catch (SocketException ignored) {

        }
        return null;
    }

    public void changeDNSShell(String networkInterfaceName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String dns1 = sharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_1, context.getString(R.string.dns_one));
        String dns2 = sharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_2, context.getString(R.string.dns_114));
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.CHANGE_DNS_ONE + dns1,
                ConsistentCommands.CHANGE_DNS_TWO + dns2,
                ConsistentCommands.CHANGE_NETWORK_INTERFACE_DNS + networkInterfaceName + Consistent.COMMANDS_DNS_1 + dns1,
                ConsistentCommands.CHANGE_NETWORK_INTERFACE_DNS + networkInterfaceName + Consistent.COMMANDS_DNS_2 + dns2,

        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REFRESH_DNS_COMMAND_1_BEFORE_LOLLIPOP,
                    ConsistentCommands.REFRESH_DNS_COMMAND_2_BEFORE_LOLLIPOP + networkInterfaceName

            });
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REFRESH_DNS_COMMAND_LOLLIPOP + networkInterfaceName

            });
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REFRESH_DNS_COMMAND_AFTER_LOLLIPOP + networkInterfaceName

            });
        }

    }


}


