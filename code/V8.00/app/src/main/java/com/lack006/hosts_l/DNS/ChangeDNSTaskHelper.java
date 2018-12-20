package com.lack006.hosts_l.DNS;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
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
import com.lack006.hosts_l.GoogleLocation.BootReceiver;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

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

    private SharedPreferences mSharedPreferences = null;

    public void changeDNSHelper(Context context, CheckBox checkBox) {
        mContext = context;
        mCheckBox = checkBox;
        mSharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, ContextWrapper.MODE_PRIVATE);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.apply_dns));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
        mDialog.setCancelable(false);
        final LayoutInflater factory = LayoutInflater.from(mContext);
        final View textEntryView = factory.inflate(R.layout.change_dns_dialog_layout, null);
        final TextInputLayout firstDNSLayout = textEntryView.findViewById(R.id.first_dns_edt_layout);
        final TextInputLayout secondDNSLayout = textEntryView.findViewById(R.id.second_dns_edt_layout);
        final TextInputEditText firstDNSEdt = textEntryView.findViewById(R.id.first_dns_edt);
        final TextInputEditText secondDNSEdt = textEntryView.findViewById(R.id.second_dns_edt);
        firstDNSEdt.setText(mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_1, context.getString(R.string.default_dns_1)));
        secondDNSEdt.setText(mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_2, context.getString(R.string.default_dns_2)));
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
                            enableChangeDnsService(mContext);
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
                disableChangeDnsService(mContext);
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
        mSharedPreferences = mContext.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, ContextWrapper.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
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
            return null;
        } catch (SocketException ignored) {
            return null;
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getNetId(Context context) {
        String netId;
        try {
            ConnectivityManager con = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = con.getActiveNetworkInfo();
            if (null != netInfo) {
                for (Network net : con.getAllNetworks()) {
                    NetworkInfo ni = con.getNetworkInfo(net);
                    if (ni != null && ni.toString().equals(netInfo.toString())) {
                        netId = net.toString();
                        return netId;
                    }
                }
            }

            return null;

        } catch (EmptyStackException ignored) {
            return null;
        }
    }


    public void changeDNSShellKitkat(String netDev, Context context) {
        mSharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String dns1 = mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_1, context.getString(R.string.default_dns_1));
        String dns2 = mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_2, context.getString(R.string.default_dns_2));
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        suShellReturnHelper.run(new String[]{
                ConsistentCommands.REFRESH_DNS_COMMAND_1_BEFORE_LOLLIPOP,
                ConsistentCommands.REFRESH_DNS_COMMAND_2_BEFORE_LOLLIPOP + netDev,
                ConsistentCommands.REFRESH_DNS_COMMAND_3_BEFORE_LOLLIPOP + netDev

        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_DNS_BEFORE_KITKAT + netDev + " " + dns1 + " " + dns2,
                    ConsistentCommands.SETPROP_DNS_ONE + dns1,
                    ConsistentCommands.SETPROP_DNS_TWO + dns2,
                    ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_1 + dns1,
                    ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_2 + dns2,

            });
        } else {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_DNS_KITKAT + netDev + " " + Consistent.LOCAL_HOST + " " + dns1 + " " + dns2,
                    ConsistentCommands.SETPROP_DNS_ONE + dns1,
                    ConsistentCommands.SETPROP_DNS_TWO + dns2,
                    ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_1 + dns1,
                    ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_2 + dns2,

            });
        }


    }

    public void changeDNSShellLollipop(String netDev, String netId, Context context) {
        mSharedPreferences = context.getSharedPreferences(Consistent.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String dns1 = mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_1, context.getString(R.string.default_dns_1));
        String dns2 = mSharedPreferences.getString(Consistent.SHARED_PREFERENCES_ENABLE_DNS_2, context.getString(R.string.default_dns_2));
        SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REFRESH_DNS_COMMAND_LOLLIPOP + netId

            });
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            suShellReturnHelper.run(new String[]{
                    ConsistentCommands.REFRESH_DNS_COMMAND_AFTER_LOLLIPOP + netId

            });
        }

        suShellReturnHelper.run(new String[]{
                ConsistentCommands.SET_DNS_LOLLIPOP + netId + " " + Consistent.LOCAL_HOST + " " + dns1 + " " + dns2,
                ConsistentCommands.SETPROP_DNS_ONE + dns1,
                ConsistentCommands.SETPROP_DNS_TWO + dns2,
                ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_1 + dns1,
                ConsistentCommands.SETPROP_NETWORK_INTERFACE_DNS + netDev + Consistent.COMMANDS_DNS_2 + dns2,

        });


    }


    private void enableChangeDnsService(Context context){
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        receiver = new ComponentName(context, ChangeDNSService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        receiver = new ComponentName(context, DNSShellService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    private void disableChangeDnsService(Context context){
        ComponentName receiver;
        PackageManager pm = context.getPackageManager();

        receiver = new ComponentName(context, ChangeDNSService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        receiver = new ComponentName(context, DNSShellService.class);
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


}


