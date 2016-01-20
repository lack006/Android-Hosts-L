package com.lack006.hosts_l;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

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


        try {
            com.lack006.hosts_l.rootcommands.Shell rootShell = null;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();


            SimpleCommand command = new SimpleCommand("reboot");
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }

    }

    public static void Reboot_quit() {

        try {
            com.lack006.hosts_l.rootcommands.Shell rootShell = null;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();


            SimpleCommand command = new SimpleCommand("killall system_server");
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }


    }
}
