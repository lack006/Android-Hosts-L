package com.lack006.hosts_l.Consistent;

/**
 * Created by lack on 2016/11/19.
 * AndroidHosts-LV7
 */

public class ConsistentCommands {

    /**
     * reboot
     */

    public final static String REBOOT = "reboot";

    public final static String HOT_REBOOT = "killall system_server";

    /**
     * use Busybox（需要先释放BusyBox）
     */
    public final static String TEST_BUSYBOX = "busybox";

    public final static String SET_BUSYBOX_PATH = "export PATH=$PATH:";

    public final static String SET_BUSYBOX_777 = "chmod 777 ";

    public final static String SET_BUSYBOX_600 = "chmod 600 ";

    /**
     * refresh DNS commands
     */

    public final static String REFRESH_DNS_COMMAND_1_BEFORE_LOLLIPOP = "ndc resolver flushdefaultif";

    public final static String REFRESH_DNS_COMMAND_2_BEFORE_LOLLIPOP = "ndc resolver flushif ";

    public final static String REFRESH_DNS_COMMAND_LOLLIPOP = "ndc resolver flushnet ";

    public final static String REFRESH_DNS_COMMAND_AFTER_LOLLIPOP = "ndc resolver clearnetdns ";


    /**
     * remove mark commands
     */

    public final static String GET_IS_ENABLE = "settings get global captive_portal_detection_enabled";

    public final static String GET_HTTPS = "settings get global captive_portal_use_https";

    public final static String SET_SERVER_ENABLE = "settings put global captive_portal_detection_enabled 1";

    public final static String SET_SERVER_DISABLE = "settings put global captive_portal_detection_enabled 0";

    public final static String SET_DISABLE_HTTPS = "settings put global captive_portal_use_https 0";

    public final static String SET_ENABLE_HTTPS = "settings put global captive_portal_use_https 1";

    public final static String GET_MARK_URL = "settings get global captive_portal_server";

    public final static String SET_REMOVE_MARK_COMMAND = "settings put global captive_portal_server ";

    public final static String SET_DEFAULT_MARK_COMMAND = "settings delete global captive_portal_server";

    /**
     * apply hosts(需要先调用use Busybox)
     */

    public final static String RW_SYSTEM = "busybox mount -o rw,remount /system";

    public final static String RW_SYSTEM_DEFAULT = "mount -o rw,remount /system";

    public final static String RM_HOSTS = "busybox rm -rf /system/etc/hosts";

    public final static String COPY = "busybox cp ";

    public final static String SET_HOSTS_644 = "busybox chmod 644 /system/etc/hosts";

    public final static String RO_SYSTEM = "busybox mount -o ro,remount /system";

    public final static String RO_SYSTEM_DEFAULT = "mount -o rw,remount /system";

    public final static String RM = "busybox rm -rf ";

    /**
     * apply google location report
     */

    public final static String CHANGE_SIM = "setprop gsm.sim.operator.numeric 310030";

    public final static String CHANGE_COUNTRY = "setprop gsm.sim.operator.iso-country us";

    public final static String CLEAR_PLAY_SERVER = "pm clear com.google.android.gms";

    public final static String CLEAR_MAP = "pm clear com.google.android.apps.maps";

    /**
     * change DNS
     */

    public final static String CHANGE_DNS_ONE = "setprop net.dns1 ";

    public final static String CHANGE_DNS_TWO = "setprop net.dns2 ";

    public final static String CHANGE_NETWORK_INTERFACE_DNS = "setprop net.";


}

