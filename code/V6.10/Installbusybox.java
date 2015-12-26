package com.lack006.hosts_l;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lack006.hosts_l.rootcommands.Toolbox;
import com.lack006.hosts_l.rootcommands.command.SimpleCommand;
import com.lack006.hosts_l.rootcommands.util.Log;

import java.io.File;

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


                    String privateDir = context.getFilesDir().getAbsolutePath();
                    String privateFile = privateDir + File.separator + Constants.BUSYBOX_FILENAME;
                    String target = Constants.ANDROID_SYSTEM_XBIN;
                    Log.e(privateFile + " to ", target);
                    try {
                        com.lack006.hosts_l.rootcommands.Shell rootShell = null;
                        rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();
                        Toolbox tb = new Toolbox(rootShell);
                        tb.remount(Constants.ANDROID_SYSTEM_XBIN, "RW");
                        if (!tb.copyFile(privateFile, target, false, false)) {
                            new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_failure).setMessage(R.string.Failure_tip).setPositiveButton(R.string.AD_Continue, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            }).show();
                        } else {
                            SimpleCommand command = new SimpleCommand(Constants.COMMAND_CHOWN + " " + target,
                                    Constants.COMMAND_CHMOD_755 + " " + target);
                            rootShell.add(command).waitForFinish();
                            command = new SimpleCommand("busybox --install /system/xbin");
                            rootShell.add(command).waitForFinish();
                            tb.remount(target, "RO");
                            new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_complete).setMessage(R.string.Complete_tip).setPositiveButton((R.string.AD_Continue), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {

                                }
                            }).show();

                        }


                    } catch (Exception ignored) {
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


                    String privateDir = context.getFilesDir().getAbsolutePath();
                    String privateFile = privateDir + File.separator + Constants.BUSYBOX_FILENAME;
                    String target = Constants.ANDROID_SYSTEM_XBIN;
                    Log.e(privateFile + " to ", target);
                    try {
                        com.lack006.hosts_l.rootcommands.Shell rootShell = null;
                        rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();
                        Toolbox tb = new Toolbox(rootShell);
                        tb.remount(Constants.ANDROID_SYSTEM_XBIN, "RW");
                        if (!tb.copyFile(privateFile, target, false, false)) {
                            new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_failure).setMessage(R.string.Failure_tip).setPositiveButton(R.string.AD_Continue, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            }).show();
                        } else {
                            SimpleCommand command = new SimpleCommand(Constants.COMMAND_CHOWN + " " + target,
                                    Constants.COMMAND_CHMOD_755 + " " + target);
                            rootShell.add(command).waitForFinish();
                            command = new SimpleCommand("busybox --install /system/xbin");
                            rootShell.add(command).waitForFinish();
                            tb.remount(target, "RO");
                            new AlertDialog.Builder(context).setCancelable(false).setTitle(R.string.Install_complete).setMessage(R.string.Complete_tip).setPositiveButton((R.string.AD_Continue), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {

                                }
                            }).show();

                        }


                    } catch (Exception ignored) {
                    }


                }
            });

            builder.create().show();
        }

    }
}
