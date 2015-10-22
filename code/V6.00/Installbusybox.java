package com.lack006.hosts_l;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Lack006 on 2015/10/10.
 */
public class Installbusybox {
    private static final String Busybox_xbin = "/system/xbin/busybox";

    public static void Install(AlertDialog.Builder builder, final Context context) {


        final File box1 = new File(Busybox_xbin);
        if (!box1.exists()) {


            builder.setTitle(R.string.Install_busybox);
            builder.setMessage(R.string.NO_busybox);
            builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            builder.setNeutralButton(R.string.Install, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {


                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                    pb.directory(new File("/"));// 设置shell的当前目录。
                    try {
                        Process proc = pb.start();

                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                        out.println("su");
                        out.println("export PATH=/system/xbin:$PATH");
                        out.println("mount -o remount,rw /system");
                        out.println("mount -o rw,remount yassf2 /system/");

                        out.println("cp /data/data/com.lack006.hosts_l/files/busybox /system/xbin/busybox");
                        out.println("sleep 0.5");
                        out.println("chmod 755 /system/xbin/busybox");
                        out.println("busybox --install /system/xbin");
                        out.println("sleep 0.5");
                        out.println("exit");
                        out.close();
                        proc.waitFor();
                        proc.destroy();
                    } catch (Exception ignored) {
                    }

                    if (!box1.exists()) {
                        new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_failure).setMessage(R.string.Failure_tip).setPositiveButton(R.string.AD_Continue, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        }).show();
                    } else if (box1.exists()) {
                        new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_complete).setMessage(R.string.Complete_tip).setPositiveButton((R.string.AD_Continue), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {


                                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                pb.directory(new File("/"));// 设置shell的当前目录。
                                try {
                                    Process proc = pb.start();

                                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                                    out.println("su");

                                    out.println("export PATH=/system/xbin:$PATH");
                                    out.println("mount -o remount,rw /system");
                                    out.println("mount -o rw,remount yassf2 /system/");
                                    out.println("busybox --install /system/xbin");
                                    out.println("exit");
                                    out.close();
                                    proc.waitFor();
                                    proc.destroy();
                                } catch (Exception ignored) {
                                }


                            }
                        }).show();
                    }


                }
            });

            builder.create().show();
        } else {


            builder.setTitle(R.string.Install_busybox);
            builder.setMessage(R.string.Is_busybox);
            builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            builder.setNeutralButton(R.string.RE_Install, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {


                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                    pb.directory(new File("/"));// 设置shell的当前目录。
                    try {
                        Process proc = pb.start();

                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                        out.println("su");
                        out.println("export PATH=/system/xbin:$PATH");
                        out.println("mount -o remount,rw /system");
                        out.println("mount -o rw,remount yassf2 /system/");

                        out.println("cp /data/data/com.lack006.hosts_l/files/busybox /system/xbin/busybox");
                        out.println("sleep 0.5");
                        out.println("chmod 755 /system/xbin/busybox");
                        out.println("busybox --install /system/xbin");
                        out.println("sleep 0.5");
                        out.println("exit");
                        out.close();
                        proc.waitFor();
                        proc.destroy();
                    } catch (Exception ignored) {
                    }

                    if (!box1.exists()) {
                        new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_failure).setMessage(R.string.Failure_tip).setPositiveButton(R.string.AD_Continue, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        }).show();
                    } else if (box1.exists()) {
                        new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_complete).setMessage(R.string.Complete_tip).setPositiveButton((R.string.AD_Continue), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {


                                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                pb.directory(new File("/"));// 设置shell的当前目录。
                                try {
                                    Process proc = pb.start();

                                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                                    out.println("su");

                                    out.println("export PATH=/system/xbin:$PATH");
                                    out.println("mount -o remount,rw /system");
                                    out.println("mount -o rw,remount yassf2 /system/");
                                    out.println("busybox --install /system/xbin");
                                    out.println("exit");
                                    out.close();
                                    proc.waitFor();
                                    proc.destroy();
                                } catch (Exception ignored) {
                                }


                            }
                        }).show();
                    }


                }
            });

            builder.create().show();
        }

    }
}
