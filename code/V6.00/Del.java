package com.lack006.hosts_l;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Lack006 on 2015/10/8.
 */
public class Del {
    public static void Deloldfile() {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("rm /data/data/com.lack006.hosts_l/files/busybox");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/check");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/check");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();
        } catch (Exception ignored) {
        }
    }

    public static void Delversion() {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/check");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/check");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();
        } catch (Exception ignored) {
        }
    }

    public static void Delhosts() {
        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
        pb.directory(new File("/"));// 设置shell的当前目录。
        try {
            Process proc = pb.start();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println("su");
            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
            out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
            out.println("exit");
            out.close();
            proc.waitFor();
            proc.destroy();
        } catch (Exception ignored) {
        }
    }

}
