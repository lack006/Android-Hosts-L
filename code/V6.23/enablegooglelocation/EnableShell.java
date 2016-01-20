package com.lack006.hosts_l.enablegooglelocation;

import android.content.Context;
import android.widget.Toast;

import com.lack006.hosts_l.Constants;
import com.lack006.hosts_l.R;
import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

/**
 * Created by Lack006 on 2016/1/20.
 */
public class EnableShell {
    public static void enableshell(Context context) {
        try {
            com.lack006.hosts_l.rootcommands.Shell rootShell;
            rootShell = com.lack006.hosts_l.rootcommands.Shell.startRootShell();
            SimpleCommand command = new SimpleCommand(Constants.Enable_Google_Loaction);
            rootShell.add(command).waitForFinish();
            Toast.makeText(context, R.string.Enable_Location, Toast.LENGTH_SHORT).show();


        } catch (Exception ignored) {
        }
    }
}
