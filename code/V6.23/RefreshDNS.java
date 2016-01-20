package com.lack006.hosts_l;

import com.lack006.hosts_l.rootcommands.Shell;
import com.lack006.hosts_l.rootcommands.command.SimpleCommand;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class RefreshDNS {
    public static void RefreshDNS() {

        try {
            Shell rootShell = null;
            rootShell = Shell.startRootShell();
            SimpleCommand command = new SimpleCommand(Constants.Refresh_DNS_1);
            rootShell.add(command).waitForFinish();
            command = new SimpleCommand(Constants.Refresh_DNS_2);
            rootShell.add(command).waitForFinish();


        } catch (Exception ignored) {
        }
    }
}
