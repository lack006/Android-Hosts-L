package com.lack006.hosts_l.GoogleLocation;

import android.os.AsyncTask;

/**
 * Created by lack006 on 2017/12/7.
 */

public class GoogleLocationEnableTaskHelper {

    public static class GoogleLocationEnableShell extends AsyncTask<Object, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Object... objects) {
            GoogleLocationReportTaskHelper googleLocationReportTaskHelper = new GoogleLocationReportTaskHelper();
            googleLocationReportTaskHelper.enableGoogleLocalShell();

            return null;
        }
    }
}
