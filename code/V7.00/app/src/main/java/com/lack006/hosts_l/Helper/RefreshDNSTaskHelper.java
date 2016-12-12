package com.lack006.hosts_l.Helper;

/**
 * Created by lack on 2016/11/19.
 * AndroidHosts-LV7
 * 新版本 Android 系统失效
 */

public class RefreshDNSTaskHelper {


//    public static class RefreshDNSTask extends AsyncTask<Object, Integer, Boolean> {
//        private ProgressDialog mProgressDialog = null;
//        private Context mContext = null;
//        private List<String> mCmdResult = null;

//        void refreshDNS(Context context) {
//            mProgressDialog = new ProgressDialog(mContext);
//            mProgressDialog.setMessage(getString(R.string.refreshing_dns));
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.show();
//            mRefreshDNSTask = new RefreshDNSTaskHelper.RefreshDNSTask();
//            mRefreshDNSTask.execute(mContext, mProgressDialog);
//        }
//
//
//        @Override
//        protected Boolean doInBackground(Object... objects) {
//            mContext = (Context) objects[0];
//            mProgressDialog = (ProgressDialog) objects[1];
//
//            mCmdResult = Shell.SU.run(new String[]{
//                    ConsistentCommands.REFRESH_DNS_COMMAND_1,
//                    ConsistentCommands.REFRESH_DNS_COMMAND_2
//            });
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            StringBuilder errorLog = (new StringBuilder());
//            mProgressDialog.cancel();
//            if (mCmdResult != null) {
//                for (String line : mCmdResult) {
//                    errorLog.append(line).append((char) 10);
//                }
//                Toast.makeText(mContext, mContext.getString(R.string.refresh_dns_error) + errorLog.toString(), Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(mContext, R.string.refresh_dns_success, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//
//    }
}
