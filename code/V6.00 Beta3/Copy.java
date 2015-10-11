package com.lack006.hosts_l;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class Copy {
    static Button btn_AD1;
    static Button btn_RE1;
    static Button btn_AR1;
    static Context context1;

    public static void Copy_backup2hosts(Context context) {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("busybox mount -o rw,remount yassf2 /system/");
            out.println("toolbox mount -o remount,rw /system");
            out.println("busybox mount -o remount,rw /system");
            out.println("busybox chmod 777 /system");
            out.println("busybox chmod 777 /system/etc");
            out.println("busybox chmod 777 /system/etc/hosts");
            out.println("busybox cp /data/data/com.lack006.hosts_l/files/backup /system/etc/hosts");
            out.println("sleep 0.5");
            out.println("busybox chmod 644 /system/etc/hosts");
            out.println("busybox chmod 755 /system");
            out.println("busybox chmod 755 /system/etc");
            out.println("toolbox mount -o rw,remount yassf2 /system/");
            out.println("toolbox chmod 777 /system");
            out.println("toolbox chmod 777 /system/etc");
            out.println("toolbox chmod 777 /system/etc/hosts");
            out.println("toolbox cp /data/data/com.lack006.hosts_l/files/backup /system/etc/hosts");
            out.println("sleep 0.5");
            out.println("toolbox chmod 644 /system/etc/hosts");
            out.println("toolbox chmod 755 /system");
            out.println("toolbox chmod 755 /system/etc");
            out.println("ndc resolver flushdefaultif");
            out.println("ndc resolver flushif wlan0");
            out.println("exit");
            out.close();
            proc.waitFor();
            Toast.makeText(context, R.string.Restore_complete, Toast.LENGTH_SHORT).show();
            proc.destroy();
            sendMsg(2);


        } catch (Exception ignored) {
        }

    }

    public static void Copy_hosts2backup(Context context) {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("busybox mount -o rw,remount yassf2 /system/");
            out.println("toolbox mount -o remount,rw /system");
            out.println("busybox mount -o remount,rw /system");
            out.println("busybox chmod 777 /system");
            out.println("busybox chmod 777 /system/etc");
            out.println("busybox chmod 777 /system/etc/hosts");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/backup");
            out.println("sleep 0.5");
            out.println("busybox cp  /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
            out.println("sleep 0.5");
            out.println("busybox chmod 644 /system/etc/hosts");
            out.println("busybox chmod 755 /system");
            out.println("busybox chmod 755 /system/etc");
            out.println("toolbox mount -o rw,remount yassf2 /system/");
            out.println("toolbox chmod 777 /system");
            out.println("toolbox chmod 777 /system/etc");
            out.println("toolbox chmod 777 /system/etc/hosts");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/backup");
            out.println("sleep 0.5");
            out.println("toolbox cp /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
            out.println("sleep 0.5");
            out.println("toolbox chmod 644 /system/etc/hosts");
            out.println("toolbox chmod 755 /system");
            out.println("toolbox chmod 755 /system/etc");
            out.println("exit");
            out.close();
            proc.waitFor();
            Toast.makeText(context, R.string.Backup_complete, Toast.LENGTH_SHORT).show();
            proc.destroy();


        } catch (Exception ignored) {
        }
    }

    public static void Copy_download2hosts(final Context context, Button btn_AD, Button btn_RE, Button btn_AR) {
        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("busybox mount -o rw,remount yassf2 /system/");
            out.println("toolbox mount -o remount,rw /system");
            out.println("busybox mount -o remount,rw /system");
            out.println("busybox chmod 777 /system");
            out.println("busybox chmod 777 /system/etc");
            out.println("busybox chmod 777 /system/etc/hosts");
            out.println("busybox cp /data/data/com.lack006.hosts_l/files/hosts /system/etc/hosts");
            out.println("sleep 0.5");
            out.println("busybox chmod 644 /system/etc/hosts");
            out.println("busybox chmod 755 /system");
            out.println("busybox chmod 755 /system/etc");
            out.println("toolbox mount -o rw,remount yassf2 /system/");
            out.println("toolbox chmod 777 /system");
            out.println("toolbox chmod 777 /system/etc");
            out.println("toolbox chmod 777 /system/etc/hosts");
            out.println("toolbox cp /data/data/com.lack006.hosts_l/files/hosts /system/etc/hosts");
            out.println("sleep 0.5");
            out.println("toolbox chmod 644 /system/etc/hosts");
            out.println("toolbox chmod 755 /system");
            out.println("toolbox chmod 755 /system/etc");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("ndc resolver flushdefaultif");
            out.println("ndc resolver flushif wlan0");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();

            sendMsg(2);


        } catch (Exception ignored) {
        }
    }

    private static void sendMsg(int flag) {
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
                        System.out.println("1111111111111111111111111111111111111111111111111111111");
                        Afterapply.After_apply(context1, btn_AD1, btn_RE1, btn_AR1);


                        break;
                    case -1:


                        break;

                    default:
                        break;
                }
            }

            super.handleMessage(msg);

        }


    };


}
