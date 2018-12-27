package com.lack006.hosts_l.Consistent;

/**
 * Created by lack006 on 2016/10/3.
 * AndroidHosts-LV7
 */

public class Consistent {

    public final static boolean PLAY_VERSION = true;

    public final static int SOFTWARE_VERSION = 52061481;

    public final static int DURATION_TIME = 500;

    public final static int ONE_TIME = 1;

    public final static int CONNECTED = -1;

    public final static int DOWNLOAD_CANCEL = -2;

    public final static int MIX_HOSTS = -3;

    public final static int CUSTOM_ERROR = -4;

    public final static int MIXING = -5;

    public final static int TIME_OUT = 3000;

    public final static int PHONE_PERMISSIONS_REQUEST = 6;

    public final static int BATTERY_PERMISSIONS_REQUEST = 7;

    public final static int GET_MARK_ENABLE = 0;

    public final static int GET_MARK_URL = 1;

    public final static int GET_HTTPS_ENABLE = 2;

    public final static int GET_HTTP_SERVER = 3;

    public final static int GET_HTTPS_SERVER = 4;

    public final static int GET_MARK_MODE = 5;

    public final static int TYPE_DNS = 0;

    public final static int TYPE_GOOGLE_LOCAL = 1;

    public final static int BATTARY_PERMISSIONS_REQUEST = 66;

    public final static boolean ON = true;

    public final static boolean OFF = false;

    public final static String ENABLE = "1";

    public final static String DISABLE = "0";

    public final static String VERSION_FILE = "/version";

    public final static String CLASH_FILE = "/clash";

    public final static String HOSTS_FILE = "/hosts";

    public final static String AD_FILE = "/ad";

    public final static String RE_FILE = "/re";

    public final static String BUSYBOX_FILE = "/busybox";

    public final static String BACKUP_FILE = "/backup";

    public final static String BACKUP_FILE_NAME_ONLY = "backup";

    public final static String DEFAULT_HOSTS = "/default";

    public final static String TEMP_FILE = "/tempHosts";

    public final static String SYSTEM_ETC_HOSTS = "/system/etc/hosts";

    public final static String CLASH_SPLIT = ":";

    public final static String REGEX_AD = "V20\\d{10}AD";

    public final static String REGEX_RE = "V20\\d{10}RE";

    public final static String REGEX_AR = "V20\\d{10}AR";

    public final static String REGEX_OTHER = "[\\u4e00-\\u9fa5]*：.*";

    public final static String REGEX_VERSION = "520614\\d{2}";

    public final static String REGEX_IPV4 = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";

    //   public final static String REGEX_IPV4 = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]|\\d{1,2}:\\d{1,4}|\\d{1,2}:[1-5]\\d{4}|\\d{1,2}:6[0-5][0-5][0-3][0-5]|1\\d\\d:\\d{1,4}|1\\d\\d:[1-5]\\d{4}|1\\d\\d:6[0-5][0-5][0-3][0-5]|2[0-4]\\d:\\d{1,4}|2[0-4]\\d:[1-5]\\d{4}|2[0-4]\\d:6[0-5][0-5][0-3][0-5]|25[0-5]:\\d{1,4}|25[0-5]:[1-5]\\d{4}|25[0-5]:6[0-5][0-5][0-3][0-5])$";


    public final static String KEY_AD = "AD";

    public final static String KEY_RE = "RE";

    public final static String KEY_AR = "AR";

    public final static String KEY_OTHER = "OTHER";

    public final static String KEY_VERSION = "VERSION";

    public final static String KEY_LOCAL_HOSTS_KIND = "LOCAL_HOSTS_KIND";

    public final static String KEY_LOCAL_HOSTS_VERSION = "LOCAL_HOSTS_VERSION";

    public final static String LOCAL_AD = "LOCAL_AD";

    public final static String LOCAL_RE = "LOCAL_RE";

    public final static String LOCAL_AR = "LOCAL_AR";

    public final static String KEY_CHECKBOX_HOSTS = "CHECKBOX_HOSTS";

    public final static String KEY_CHECKBOX_VERSION = "CHECKBOX_VERSION";

    public final static String KEY_CHECKBOX_BACKUP = "CHECKBOX_BACKUP";

    public final static String KEY_CHECKBOX_BUSYBOX = "CHECKBOX_BUSYBOX";

    public final static String KEY_CHECKBOX_MARK = "CHECKBOX_MARK";

    public final static String KEY_CHECKBOX_LOCAL = "CHECKBOX_LOCAL";

    public final static String KEY_CHECKBOX_DNS = "CHECKBOX_DNS";

    public final static String KEY_PROTECT_MODE = "CHECKBOX_PROTECT";

    public final static String DEFAULT_SERVER = "www.google.cn";

    public final static String DEFAULT_SERVER_711 = "https://www.google.cn/create_204.php";

    public final static String NO_SUPPORT = "null";

    public final static String GOOGLE_URL = "clients1.google.com";

    public final static String SYSTEM_RW_ERROR = "Read-only file system";

    public final static String SHARED_PREFERENCES_FILE = "hosts-l";

    public final static String SHARED_PREFERENCES_ENABLE_LOCAL = "google_local";

    public final static String SHARED_PREFERENCES_ENABLE_DNS = "change_dns";

    public final static String SHARED_PREFERENCES_ENABLE_DNS_1 = "dns1";

    public final static String SHARED_PREFERENCES_ENABLE_DNS_2 = "dns2";

    public final static String SHARED_PREFERENCES_KEEP_SCREEN_ON = "screen";

    public final static String SHARED_PREFERENCES_FIRST_TIME = "first";

    public final static String COMMANDS_DNS_1 = ".dns1 ";

    public final static String COMMANDS_DNS_2 = ".dns2 ";

    public final static String NETWORK_CONNECT_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public final static String SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";

    public final static String LOCAL_HOST = "127.0.0.1";

    public final static String PROJECT_URL = "https://github.com/lack006/Android-Hosts-L/tree/master/apk";

    public final static String MARKET_URI = "market://details?id=";

    public final static String SHARED_PREFERENCES_CUSTOM_URL = "url";
    public final static String SHARED_PREFERENCES_CLASH_HISTORY = "clash_history";

    public final static String HASH_TAG = "#";

    public final static String SET_KITKAT = "0.0.0.0";

    public final static String PROTECTED = "-rw-------";

    public final static String SHARED_PREFERENCES_PROTECT_MODE = "protect";

    public final static String PACKAGE = "package:";


}
