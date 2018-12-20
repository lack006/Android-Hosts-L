package com.lack006.hosts_l.CheckVersion;

import android.content.Context;

import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.ProtectMode.ProtectModeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lack006.hosts_l.Consistent.Consistent.KEY_AD;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_AR;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_LOCAL_HOSTS_KIND;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_LOCAL_HOSTS_VERSION;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_OTHER;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_RE;
import static com.lack006.hosts_l.Consistent.Consistent.KEY_VERSION;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_AD;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_AR;
import static com.lack006.hosts_l.Consistent.Consistent.LOCAL_RE;
import static com.lack006.hosts_l.Consistent.Consistent.REGEX_AD;
import static com.lack006.hosts_l.Consistent.Consistent.REGEX_AR;
import static com.lack006.hosts_l.Consistent.Consistent.REGEX_OTHER;
import static com.lack006.hosts_l.Consistent.Consistent.REGEX_RE;
import static com.lack006.hosts_l.Consistent.Consistent.REGEX_VERSION;
import static com.lack006.hosts_l.Consistent.Consistent.SYSTEM_ETC_HOSTS;

/**
 * Created by lack on 2016/12/2.
 * AndroidHosts-LV7
 */

public class GetVersionHelper {
    private Map<String, String> mTextMap = new HashMap<>();

    public Map<String, String> getVersion(Context context) {

        File file = context.getCacheDir();
        final String CACHE_PATH = file.getAbsolutePath();
        try {

            BufferedReader in;
            in = new BufferedReader(new FileReader(CACHE_PATH + Consistent.VERSION_FILE));
            String s;
            Pattern patternAD = Pattern.compile(REGEX_AD);
            Pattern patternRE = Pattern.compile(REGEX_RE);
            Pattern patternAR = Pattern.compile(REGEX_AR);
            Pattern patternVERSION = Pattern.compile(REGEX_VERSION);
            Pattern patternOTHER = Pattern.compile(REGEX_OTHER);

            while ((s = in.readLine()) != null) {

                Matcher matcherAD = patternAD.matcher(s);
                Matcher matcherRE = patternRE.matcher(s);
                Matcher matcherAR = patternAR.matcher(s);
                Matcher matcherOTHER = patternOTHER.matcher(s);
                Matcher matcherVERSION = patternVERSION.matcher(s);

                if (matcherAD.find()) {
                    mTextMap.put(KEY_AD, matcherAD.group());
                } else if (matcherRE.find()) {
                    mTextMap.put(KEY_RE, matcherRE.group());
                } else if (matcherAR.find()) {
                    mTextMap.put(KEY_AR, matcherAR.group());
                } else if (matcherOTHER.find()) {
                    mTextMap.put(KEY_OTHER, matcherOTHER.group());
                } else if (matcherVERSION.find()) {
                    mTextMap.put(KEY_VERSION, matcherVERSION.group());
                }
            }
            in.close();

        } catch (Exception ignored) {

        }
        getLocalVersion(context);


        return mTextMap;
    }

    private void getLocalVersion(Context context) {

        try {
            ProtectModeHelper protectModeHelper = new ProtectModeHelper();
            protectModeHelper.prepareRead(context);
            BufferedReader in;
            in = new BufferedReader(new FileReader(SYSTEM_ETC_HOSTS));
            String s;
            Pattern patternAD = Pattern.compile(REGEX_AD);
            Pattern patternRE = Pattern.compile(REGEX_RE);
            Pattern patternAR = Pattern.compile(REGEX_AR);
            s = in.readLine();
            if (s != null) {

                Matcher matcherAD = patternAD.matcher(s);
                Matcher matcherRE = patternRE.matcher(s);
                Matcher matcherAR = patternAR.matcher(s);

                if (matcherAD.find()) {
                    mTextMap.put(KEY_LOCAL_HOSTS_VERSION, matcherAD.group());
                    mTextMap.put(KEY_LOCAL_HOSTS_KIND, LOCAL_AD);
                } else if (matcherRE.find()) {
                    mTextMap.put(KEY_LOCAL_HOSTS_VERSION, matcherRE.group());
                    mTextMap.put(KEY_LOCAL_HOSTS_KIND, LOCAL_RE);
                } else if (matcherAR.find()) {
                    mTextMap.put(KEY_LOCAL_HOSTS_VERSION, matcherAR.group());
                    mTextMap.put(KEY_LOCAL_HOSTS_KIND, LOCAL_AR);
                } else {
                    mTextMap.put(KEY_LOCAL_HOSTS_VERSION, "");
                    mTextMap.put(KEY_LOCAL_HOSTS_KIND, "");
                }


            }
            in.close();

            protectModeHelper.applyProtectMode(context);

        } catch (Exception ignored) {

        }

    }


}




