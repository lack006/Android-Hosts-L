package com.lack006.hosts_l;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class RefreshDNS {
    public static void RefreshDNS() {

        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("ndc resolver flushdefaultif");
            out.println("ndc resolver flushif wlan0");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();


        } catch (Exception ignored) {
        }

    }
}
