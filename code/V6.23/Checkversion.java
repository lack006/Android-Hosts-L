package com.lack006.hosts_l;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class Checkversion {
    static TextView AD1;
    static TextView RE1;
    static TextView AR1;
    static Button btn_AD1;
    static Button btn_RE1;
    static Button btn_AR1;
    static String AD_V;
    static String RE_V;
    static String AR_V;
    static int VERSION;
    private static Context context1;


    public static void Checkversion(final ProgressDialog mpDialog, final Context context, final TextView AD, final TextView RE, final TextView AR, Button btn_AD, Button btn_RE, Button btn_AR) {

        AD1 = AD;
        RE1 = RE;
        AR1 = AR;
        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;

        File file = context.getFilesDir();
        final String DATABASE_PATH = file.getAbsolutePath();
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpDialog.setMessage(context.getString(R.string.Initialization));
        mpDialog.setIndeterminate(true);// 设置进度条是否为不明确 false 就是不设置为不明确
        mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
        mpDialog.show();
        new Thread() {
            public void run() {
                String apkUrl = context.getString(R.string.URL_CHECK);

                URL url;
                try {


                    url = new URL(apkUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Accept-Encoding", "identity");
                    InputStream in = con.getInputStream();

                    File fileOut = new File(DATABASE_PATH + "/check");
                    FileOutputStream out = new FileOutputStream(fileOut);
                    byte[] bytes = new byte[1024];


                    int c;
                    while ((c = in.read(bytes)) != -1) {
                        out.write(bytes, 0, c);


                    }
                    in.close();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mpDialog.cancel();// 下载完成
                sendMsg(2, AD, RE, AR);// 下载完成


            }
        }.start();

    }

    private static void sendMsg(int flag, TextView AD, TextView RE, TextView AR) {
        Log.e("haha", "dsfsdfdsfd");
        Message msg = new Message();
        msg.what = flag;

        handler.sendMessage(msg);

    }

    public static final Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {

            if (!Thread.currentThread().isInterrupted()) {
                Log.i("msg what", String.valueOf(msg.what));
                switch (msg.what) {
                    case 0:


                        break;
                    case 1:


                        break;
                    case 2:


                        try {
                            FileInputStream inputStream = new FileInputStream(
                                    "/data/data/com.lack006.hosts_l/files/check");
                            byte[] b = new byte[inputStream.available()];
                            inputStream.read(b);
                            Pattern p, q, r, s;
                            p = Pattern.compile("V20\\d{10}AD");
                            q = Pattern.compile("V20\\d{10}RE");
                            r = Pattern.compile("V20\\d{10}AR");
                            s = Pattern.compile("520614\\d{2}");
                            Log.i("handler", "a");

                            Matcher l, m, n, o;
                            l = s.matcher(new String(b));// 获得匹配


                            while (l.find()) { // 注意这里，是while不是if

                                String xxx = l.group();
                                int xxxx = Integer.parseInt(xxx);
                                VERSION = Integer.parseInt(context1.getString(R.string.Software_vresion));
                                if (xxxx > VERSION) {

                                    new AlertDialog.Builder(context1)
                                            .setCancelable(false)
                                            .setTitle(R.string.newer_software)
                                            .setMessage(R.string.update_software)
                                            .setPositiveButton(R.string.Software_update,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialoginterface, int i) {
                                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context1.getString(R.string.UpdateURL)));
                                                            context1.startActivity(browserIntent);
                                                        }
                                                    }
                                            )
                                            .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {


                                                }
                                            }).show();

                                }


                                break;
                            }
                            m = p.matcher(new String(b));// 获得匹配


                            while (m.find()) { // 注意这里，是while不是if

                                String xxx = m.group();
                                AD_V = xxx;
                                AD1.setText(R.string.Enable);
                                Log.i("handler", xxx);


                                break;
                            }
                            n = q.matcher(new String(b));// 获得匹配
                            while (n.find()) { // 注意这里，是while不是if

                                String xxx = n.group();
                                RE_V = xxx;
                                RE1.setText(R.string.Enable);
                                Log.i("handler", xxx);


                                break;
                            }
                            o = r.matcher(new String(b));// 获得匹配
                            while (o.find()) { // 注意这里，是while不是if

                                String xxx = o.group();
                                AR1.setText(R.string.Enable);
                                AR_V = xxx;
                                Log.i("handler", xxx);


                                break;
                            }

                            setversion();


                        } catch (Exception ignored) {

                        }


                        break;
                    case -1:


                        break;

                    default:
                        break;
                }
            }

            super.handleMessage(msg);

        }

        private void setversion() {
            try {
                int a = 0;//0未应用  2已应用但不是最新 3去广告为最新 4重定向为最新 5广告+重定向为最新

                FileInputStream inputStream = new FileInputStream(
                        "/system/etc/hosts");
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                Pattern p, q, r;
                p = Pattern.compile("V20\\d{10}AD");
                q = Pattern.compile("V20\\d{10}RE");
                r = Pattern.compile("V20\\d{10}AR");
                Log.i("handler", "a");

                Matcher m, n, o;
                m = p.matcher(new String(b));// 获得匹配


                while (m.find()) { // 注意这里，是while不是if

                    String xxx = m.group();
                    if (!(AD_V.equals(xxx))) {
                        btn_AD1.setText(R.string.AD_new);

                        a = 2;
                    } else {
                        a = 3;
                    }
                    Log.i("handler", String.valueOf(a));


                    break;
                }
                n = q.matcher(new String(b));// 获得匹配
                while (n.find()) { // 注意这里，是while不是if

                    String xxx = n.group();
                    if (!(RE_V.equals(xxx))) {
                        btn_RE1.setText(R.string.RE_new);
                        a = 2;
                    } else {
                        a = 4;
                    }
                    Log.i("handler", String.valueOf(a));


                    break;
                }
                o = r.matcher(new String(b));// 获得匹配
                while (o.find()) { // 注意这里，是while不是if

                    String xxx = o.group();
                    if (!(AR_V.equals(xxx))) {
                        btn_AR1.setText(R.string.AR_new);
                        a = 2;
                    } else {
                        a = 5;
                    }


                    Log.i("handler", String.valueOf(a));


                    break;
                }
                if (a == 0) {
                    btn_AD1.setText(R.string.AD_new);
                    btn_RE1.setText(R.string.RE_new);
                    btn_AR1.setText(R.string.AR_new);
                } else if (a == 2) {
                    a = 0;
                } else if (a == 3) {
                    btn_AD1.setText(R.string.AD_is_new);
                    a = 0;
                } else if (a == 4) {
                    btn_RE1.setText(R.string.RE_is_new);
                    a = 0;
                } else {
                    btn_AR1.setText(R.string.AR_is_new);
                    a = 0;
                }


            } catch (Exception ignored) {

            }
        }
    };


}
