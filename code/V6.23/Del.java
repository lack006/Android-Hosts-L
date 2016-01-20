package com.lack006.hosts_l;

import android.content.Context;

import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

import java.io.File;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class Del {
    public static void Deloldfile(Context context) {


        try {
            String privateDir = context.getFilesDir().getAbsolutePath();

            String privateFile_check = privateDir + File.separator + Constants.CHECK_FILENAME;
            String privateFile_busybox = privateDir + File.separator + Constants.BUSYBOX_FILENAME;
            String privateFile_hosts = privateDir + File.separator + Constants.HOSTS_FILENAME;
            com.lack006.hosts_l.rootcommands.Shell rootShell;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();

            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + privateFile_check);
            rootShell.add(command).waitForFinish();
            command = new SimpleCommand(Constants.COMMAND_RM + " " + privateFile_busybox);
            rootShell.add(command).waitForFinish();
            command = new SimpleCommand(Constants.COMMAND_RM + " " + privateFile_hosts);
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }

    }

    public static void Delversion(Context context) {
        String privateDir = context.getFilesDir().getAbsolutePath();


        String privateFile_check = privateDir + File.separator + Constants.CHECK_FILENAME;
        try {
            com.lack006.hosts_l.rootcommands.Shell rootShell = null;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();


            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + privateFile_check);
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }

    }

    public static void Delhosts(Context context) {
        String privateDir = context.getFilesDir().getAbsolutePath();


        String privateFile_hosts = privateDir + File.separator + Constants.HOSTS_FILENAME;
        try {
            com.lack006.hosts_l.rootcommands.Shell rootShell = null;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();


            SimpleCommand command = new SimpleCommand(Constants.COMMAND_RM + " " + privateFile_hosts);
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }
    }

}
