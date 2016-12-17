package com.lack006.hosts_l.ChangeIp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.Consistent.ConsistentCommands;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Helper.SuShellReturnHelper;
import com.lack006.hosts_l.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lack006.hosts_l.Consistent.Consistent.SYSTEM_ETC_HOSTS;

/**
 * Created by lack on 2016/12/7.
 * AndroidHosts-LV7
 */

public class ChangeIpTaskHelper {
    private Context mContext = null;
    private ProgressDialog mReplaceProgressDialog = null;
    private AlertDialog.Builder mDialog = null;
    private List<String> mCopyResult = null;
    private String mIpAddress = null;
    private String mReplaceIpAddress = null;

    public void changeIpHelper(Context context) {
        mContext = context;
        GetInfoTask getInfoTask = new GetInfoTask();
        getInfoTask.execute();


    }

    private class GetInfoTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {

            CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
            cleanCacheHelper.cleanChange(mContext);
            mDialog = new AlertDialog.Builder(mContext);
            mDialog.setCancelable(false);

            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(SYSTEM_ETC_HOSTS));
                String firstLine = in.readLine();
                if (firstLine.contains(Consistent.KEY_AR) || firstLine.contains(Consistent.KEY_RE)) {
                    InetAddress inetAddress = InetAddress.getByName(Consistent.GOOGLE_URL);
                    mIpAddress = inetAddress.getHostAddress();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (null == mIpAddress) {
                mDialog.setMessage(R.string.change_ip_premise);
                mDialog.setNegativeButton(R.string.next_step, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mDialog.show();
            } else {


                final LayoutInflater factory = LayoutInflater.from(mContext);
                final View textEntryView = factory.inflate(R.layout.change_ip_dialog_layout, null);
                final TextInputLayout textInputLayout = (TextInputLayout) textEntryView.findViewById(R.id.ip_edt_layout);
                final EditText edt = (EditText) textEntryView.findViewById(R.id.ip_edt);
                edt.setText(mIpAddress);
                edt.setSelection(edt.getText().length());
                mDialog.setView(textEntryView);
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
                            mReplaceIpAddress = edt.getText().toString();

                            if (!mIpAddress.equals(mReplaceIpAddress)) {
                                if (Consistent.LOCAL_HOST.equals(mReplaceIpAddress)) {
                                    textInputLayout.setError(mContext.getString(R.string.no_zuo_no_die));
                                } else {
                                    Pattern pattern = Pattern.compile(Consistent.REGEX_IPV4);
                                    Matcher matcher = pattern.matcher(mReplaceIpAddress);
                                    if (matcher.find()) {
                                        alertDialog.cancel();
                                        mReplaceProgressDialog = new ProgressDialog(mContext);
                                        mReplaceProgressDialog.setMessage(mContext.getString(R.string.ip_replacing));
                                        mReplaceProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        mReplaceProgressDialog.setIndeterminate(true);
                                        mReplaceProgressDialog.setCancelable(false);
                                        mReplaceProgressDialog.show();
                                        ChangeIpTask changeIpTask = new ChangeIpTask();
                                        changeIpTask.execute();
                                    } else {
                                        textInputLayout.setError(mContext.getString(R.string.ip_input_error));
                                    }
                                }
                            } else {
                                textInputLayout.setError(mContext.getString(R.string.ip_input_same));
                            }

                        }

                    });
            }


        }


    }

    private class ChangeIpTask extends AsyncTask<Object, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Object... objects) {
            File file = mContext.getCacheDir();
            String cachePath = file.getAbsolutePath();
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(SYSTEM_ETC_HOSTS));
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(cachePath + Consistent.TEMP_FILE)));
                String line;
                while ((line = in.readLine()) != null) {
                    out.println(line.replace(mIpAddress, mReplaceIpAddress));
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SuShellReturnHelper suShellReturnHelper = new SuShellReturnHelper();
            mCopyResult = suShellReturnHelper.run(new String[]{
                    ConsistentCommands.SET_BUSYBOX_PATH + cachePath,
                    ConsistentCommands.SET_BUSYBOX_777 + cachePath + Consistent.BUSYBOX_FILE,
                    ConsistentCommands.RW_SYSTEM,
                    ConsistentCommands.RM_HOSTS,
                    ConsistentCommands.COPY + cachePath + Consistent.TEMP_FILE + " " + Consistent.SYSTEM_ETC_HOSTS,
                    ConsistentCommands.SET_HOSTS_644,
                    ConsistentCommands.RO_SYSTEM,
                    ConsistentCommands.SET_BUSYBOX_600 + cachePath + Consistent.BUSYBOX_FILE
            });


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            StringBuilder errorLog = (new StringBuilder());
            boolean importantError = false;
            if (mCopyResult.size() != 0) {
                for (String line : mCopyResult) {
                    if (line.contains(Consistent.SYSTEM_RW_ERROR)) {
                        errorLog.append(line).append((char) 10);
                        importantError = true;

                    }
                }
                if (importantError) {
                    Toast.makeText(mContext, mContext.getString(R.string.ip_replace_failed) + errorLog.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.ip_replace_complete, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, R.string.ip_replace_complete, Toast.LENGTH_SHORT).show();
            }
            mReplaceProgressDialog.cancel();
        }


    }
}
