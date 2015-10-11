package com.lack006.hosts_l;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lack006 on 2015/10/10.
 */
public class Afterapply {
    static TextView AD1;
    static TextView RE1;
    static TextView AR1;
    static Button btn_AD1;
    static Button btn_RE1;
    static Button btn_AR1;
    static String AD_V;
    static String RE_V;
    static String AR_V;
    static Context context1;

    public static void After_apply(final Context context, Button btn_AD, Button btn_RE, Button btn_AR) {


        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;


        try {
            FileInputStream inputStream = new FileInputStream(
                    "/data/data/com.lack006.hosts_l/files/check");
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

                AD_V = m.group();


                break;
            }
            n = q.matcher(new String(b));// 获得匹配
            while (n.find()) { // 注意这里，是while不是if

                RE_V = n.group();


                break;
            }
            o = r.matcher(new String(b));// 获得匹配
            while (o.find()) { // 注意这里，是while不是if

                AR_V = o.group();


                break;
            }

            After_setversion();


        } catch (Exception ignored) {

        }
    }


    private static void After_setversion() {
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


            Matcher m, n, o;
            m = p.matcher(new String(b));// 获得匹配


            while (m.find()) { // 注意这里，是while不是if

                String xxx = m.group();
                if (!(AD_V.equals(xxx))) {
                    btn_AD1.setText(R.string.AD_new);
                    btn_RE1.setText(R.string.RE);
                    btn_AR1.setText(R.string.AR);

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
                    btn_AD1.setText(R.string.AD);
                    btn_AR1.setText(R.string.AR);
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
                    btn_AD1.setText(R.string.AD);
                    btn_RE1.setText(R.string.RE);
                    a = 2;
                } else {
                    a = 5;
                }


                Log.i("handler", String.valueOf(a));


                break;
            }
            Log.i("handler", String.valueOf(a));
            if (a == 0) {
                btn_AD1.setText(R.string.AD_new);
                btn_RE1.setText(R.string.RE_new);
                btn_AR1.setText(R.string.AR_new);
            } else if (a == 2) {
                a = 0;
            } else if (a == 3) {
                btn_AD1.setText(R.string.AD_is_new);
                btn_RE1.setText(R.string.RE);
                btn_AR1.setText(R.string.AR);
                Toast.makeText(context1, R.string.Apply_complete, Toast.LENGTH_SHORT).show();
                a = 0;
            } else if (a == 4) {
                btn_AD1.setText(R.string.AD);
                btn_RE1.setText(R.string.RE_is_new);
                btn_AR1.setText(R.string.AR);
                Toast.makeText(context1, R.string.Apply_complete, Toast.LENGTH_SHORT).show();
                a = 0;
            } else {
                btn_AD1.setText(R.string.AD);
                btn_RE1.setText(R.string.RE);
                btn_AR1.setText(R.string.AR_is_new);
                Toast.makeText(context1, R.string.Apply_complete, Toast.LENGTH_SHORT).show();
                a = 0;
            }


        } catch (Exception ignored) {

        }
    }
}


