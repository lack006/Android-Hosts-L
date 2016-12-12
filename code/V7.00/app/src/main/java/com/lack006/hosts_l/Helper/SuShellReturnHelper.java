package com.lack006.hosts_l.Helper;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by lack on 2016/12/4.
 * AndroidHosts-LV7
 */

class SuShellReturnHelper {
    /**
     * @param commands shell commands
     * @return all shell output
     */
    List<String> run(String[] commands) {
        return Shell.run("su", commands, null, true);
    }

    List<String> run(List<String> listCmd) {
        int size = listCmd.size();
        String[] commands = new String[size];
        for (int i = 0; i < listCmd.size(); i++) {
            commands[i] = listCmd.get(i);
        }
        return Shell.run("su", commands, null, true);
    }
}
