package com.lack006.hosts_l.Clash;

import android.content.Context;

import com.lack006.hosts_l.Consistent.Consistent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static com.lack006.hosts_l.Consistent.Consistent.CLASH_SPLIT;

/**
 * Created by lack on 2016/12/3.
 * AndroidHosts-LV7
 */

public class GetClashHelper {
    public Map<String[], String[]> getClash(Context context) {
        Map<String[], String[]> clashMap = new HashMap<>();
        String[] text;
        String[] hosts;
        String arrayString[];
        File file = context.getCacheDir();
        final String CACHE_PATH = file.getAbsolutePath();
        try {

            BufferedReader in;
            in = new BufferedReader(new FileReader(CACHE_PATH + Consistent.CLASH_FILE));
            String lineText;
            int count = 0;
            while ((in.readLine()) != null) {
                count++;
            }
            text = new String[count];
            hosts = new String[count];

            count = 0;
            in.close();
            in = new BufferedReader(new FileReader(CACHE_PATH + Consistent.CLASH_FILE));
            while ((lineText = in.readLine()) != null) {

                arrayString = lineText.split(CLASH_SPLIT);
                text[count] = arrayString[0].replace(";", "\n");
                hosts[count] = arrayString[1].replace(";", "\n");
                count++;


            }
            in.close();
            clashMap.put(text, hosts);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return clashMap;

    }
}

