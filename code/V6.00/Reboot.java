package com.lack006.hosts_l;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Lack006 on 2015/10/9.
 */
public class Reboot {

    public static void Reboot_switch(final AlertDialog.Builder builder) {
        builder.setTitle(R.string.Reboot);
        builder.setMessage(R.string.Tip_reboot);
        builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.setNegativeButton(R.string.Reboot_simple, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Reboot();

            }
        });
        builder.setNeutralButton(R.string.Reboot_quick, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Reboot_quit();


            }
        });

        builder.create().show();
    }

    public static void Reboot() {

        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("reboot");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();


        } catch (Exception ignored) {
        }

    }

    public static void Reboot_quit() {

        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("killall system_server");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();


        } catch (Exception ignored) {
        }

    }
}
