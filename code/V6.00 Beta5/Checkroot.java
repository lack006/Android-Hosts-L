package com.lack006.hosts_l;

import java.io.DataOutputStream;


/**
 * Created by Lack006 on 2015/10/8.
 */
public class Checkroot {

    public static boolean RootCmd(String cmd) {
        Process process = null;
        DataOutputStream os = null;
        try {

            process = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();


        } catch (Exception e) {


            return false;
        } finally {
            try {
                if (os != null) {

                    os.close();
                }
                assert process != null;
                process.destroy();
            } catch (Exception ignored) {


            }
        }
        return true;

    }
}
