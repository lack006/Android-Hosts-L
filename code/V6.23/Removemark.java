package com.lack006.hosts_l;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

/**
 * Created by Lack006 on 2016/1/15.
 */
public class Removemark {
    public static void removemark(final Context context) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.Remove_mark_title)
                .setMessage(R.string.Remove_mark_issue)
                .setNegativeButton(R.string.Remove_mark_default, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            com.lack006.hosts_l.rootcommands.Shell rootShell;
                            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();
                            SimpleCommand command = new SimpleCommand(Constants.Default_mark);
                            rootShell.add(command).waitForFinish();
                            Toast.makeText(context, R.string.Remove_success, Toast.LENGTH_SHORT).show();


                        } catch (Exception ignored) {
                        }

                    }
                })
                .setNeutralButton(R.string.Remove_mark_change, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            com.lack006.hosts_l.rootcommands.Shell rootShell;
                            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();
                            SimpleCommand command = new SimpleCommand(Constants.Remove_mark);
                            rootShell.add(command).waitForFinish();
                            Toast.makeText(context, R.string.Remove_success, Toast.LENGTH_SHORT).show();

                        } catch (Exception ignored) {
                        }

                    }
                })
                .setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                })

                .create().show();


    }
}
