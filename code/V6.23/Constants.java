

package com.lack006.hosts_l;

public class Constants {


    public static final String HOSTS_FILENAME = "hosts";
    public static final String BACKUP_FILENAME = "backup";
    public static final String CHECK_FILENAME = "check";
    public static final String BUSYBOX_FILENAME = "busybox";
    public static final String FILE_SEPERATOR = System.getProperty("file.separator", "/");

    public static final String COMMAND_CHOWN = "chown 0:0";
    public static final String COMMAND_CHMOD_644 = "chmod 644";

    public static final String COMMAND_CHMOD_755 = "chmod 755";

    public static final String COMMAND_RM = "rm -f";


    public static final String ANDROID_SYSTEM_PATH = System.getProperty("java.home", "/system");
    public static final String ANDROID_SYSTEM_ETC_HOSTS = ANDROID_SYSTEM_PATH + FILE_SEPERATOR
            + "etc" + FILE_SEPERATOR + HOSTS_FILENAME;

    public static final String ANDROID_SYSTEM_XBIN = ANDROID_SYSTEM_PATH + FILE_SEPERATOR
            + "xbin" + FILE_SEPERATOR + BUSYBOX_FILENAME;
    public static final String ANDROID_DATA_DATA_HOSTS = FILE_SEPERATOR + "data" + FILE_SEPERATOR
            + "data" + FILE_SEPERATOR + HOSTS_FILENAME;
    public static final String ANDROID_DATA_HOSTS = FILE_SEPERATOR
            + "data" + FILE_SEPERATOR + HOSTS_FILENAME;


    public static final String Refresh_DNS_1 = "ndc resolver flushdefaultif";
    public static final String Refresh_DNS_2 = "ndc resolver flushif wlan0";
    public static final String Remove_mark = "settings put global captive_portal_server 1.lack00006.applinzi.com";
    public static final String Default_mark = "settings delete global captive_portal_server";
    public static final String Enable_Google_Loaction = "setprop gsm.sim.operator.numeric 310030";
}
