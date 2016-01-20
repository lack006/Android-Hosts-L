package com.lack006.hosts_l;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.lack006.hosts_l.rootcommands.Shell;
import com.lack006.hosts_l.rootcommands.Toolbox;
import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

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


    public static void Copy_backup2hosts(Context context, Button btn_AD, Button btn_RE, Button btn_AR) {
        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;

        String privateDir = context.getFilesDir().getAbsolutePath();
        String privateFile = privateDir + File.separator + Constants.BACKUP_FILENAME;
        String target = Constants.ANDROID_SYSTEM_ETC_HOSTS;
        try {
            Shell rootShell = null;
            rootShell = Shell.startRootShell();
            Toolbox tb = new Toolbox(rootShell);
            tb.remount(Constants.ANDROID_SYSTEM_ETC_HOSTS, "RW");
            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + Constants.ANDROID_SYSTEM_ETC_HOSTS);
            rootShell.add(command).waitForFinish();
            if (!tb.copyFile(privateFile, target, false, false)) {
                Toast.makeText(context, R.string.Apply_fail, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, R.string.Restore_complete, Toast.LENGTH_SHORT).show();
            }

            command = new SimpleCommand(Constants.COMMAND_CHOWN + " " + target,
                    Constants.COMMAND_CHMOD_644 + " " + target);
            rootShell.add(command).waitForFinish();
            tb.remount(target, "RO");



            sendMsg(2);


        } catch (Exception ignored) {
        }

    }

    public static void Copy_hosts2backup(Context context) {
        String privateDir = context.getFilesDir().getAbsolutePath();
        String privateFile = privateDir + File.separator + Constants.BACKUP_FILENAME;
        String target = Constants.ANDROID_SYSTEM_ETC_HOSTS;
        try {
            Shell rootShell = null;
            rootShell = Shell.startRootShell();
            Toolbox tb = new Toolbox(rootShell);
            tb.remount(Constants.ANDROID_SYSTEM_ETC_HOSTS, "RW");
            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + privateDir);
            rootShell.add(command).waitForFinish();
            if (!tb.copyFile( target,privateFile, false, false)) {
                Toast.makeText(context, R.string.Backup_fail, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, R.string.Backup_complete, Toast.LENGTH_SHORT).show();
            }

            command = new SimpleCommand(Constants.COMMAND_CHOWN + " " + target,
                    Constants.COMMAND_CHMOD_644 + " " + target);
            rootShell.add(command).waitForFinish();
            tb.remount(target, "RO");


        } catch (Exception ignored) {
        }
    }


    public static void Copy_download2hosts(final Context context, Button btn_AD, Button btn_RE, Button btn_AR) {
        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;
        String privateDir = context.getFilesDir().getAbsolutePath();
        String privateFile = privateDir + File.separator + Constants.HOSTS_FILENAME;
        String target = Constants.ANDROID_SYSTEM_ETC_HOSTS;
        try {
            Shell rootShell = null;
            rootShell = Shell.startRootShell();
            Toolbox tb = new Toolbox(rootShell);
            tb.remount(Constants.ANDROID_SYSTEM_ETC_HOSTS, "RW");
            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + Constants.ANDROID_SYSTEM_ETC_HOSTS);
            rootShell.add(command).waitForFinish();
            if (!tb.copyFile(privateFile, target, false, false)) {
                Toast.makeText(context, R.string.Apply_fail, Toast.LENGTH_SHORT).show();
            }

            command = new SimpleCommand(Constants.COMMAND_CHOWN + " " + target,
                    Constants.COMMAND_CHMOD_644 + " " + target);
            rootShell.add(command).waitForFinish();
            tb.remount(target, "RO");
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
