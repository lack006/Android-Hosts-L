package com.lack006.hosts_l;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.lack006.hosts_l.Copy.Copy_download2hosts;
import static com.lack006.hosts_l.Del.Delhosts;

/**
 * Created by Lack006 on 2015/10/10.
 */
public class Downloadhosts {
    static int a = 0;
    static long fileSize;
    static int downLoadFileSize;
    static ProgressDialog mpDialog1;
    static Button btn_AD1;
    static Button btn_RE1;
    static Button btn_AR1;
    static Context context1;


    public static void Download(final ProgressDialog mpDialog, final Context context, final String hosts_url, Button btn_AD, Button btn_RE, Button btn_AR, final int umeng, final int mybus, final int jiajia, final int tieba) {
        btn_AD1 = btn_AD;
        btn_RE1 = btn_RE;
        btn_AR1 = btn_AR;
        context1 = context;
        mpDialog1 = mpDialog;
        File file = context.getFilesDir();
        final String DATABASE_PATH = file.getAbsolutePath();
        mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
        mpDialog.setTitle(R.string.Downloading);// 设置标题 29
        //  mpDialog.setIcon(R.drawable.icon);//设置图标
        mpDialog.setMessage(context.getString(R.string.Android_Hosts_L_Downloading));
        mpDialog.setIndeterminate(false);// 设置进度条是否为不明确 false 就是不设置为不明确
        mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
        mpDialog.setProgress(20);
        mpDialog.incrementProgressBy(0); // 增加和减少进度，这个属性必须的
        mpDialog.setButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                a = 1;
            }
        });
        mpDialog.show();

        new Thread() {
            public void run() {


                URL url;
                try {


                    url = new URL(hosts_url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Accept-Encoding", "identity");
                    InputStream in = con.getInputStream();
                    fileSize = con.getContentLength();
                    File fileOut = new File(DATABASE_PATH + "/hosts");
                    FileOutputStream out = new FileOutputStream(fileOut);
                    byte[] bytes = new byte[1024];


                    downLoadFileSize = 0;
                    sendMsg(0);
                    int c;
                    while ((c = in.read(bytes)) != -1) {
                        out.write(bytes, 0, c);
                        downLoadFileSize += c;
                        sendMsg(1);// 更新进度条
                        if (a == 1) {

                            break;
                        }
                    }

                    ///////////////////////
                    if (umeng == 1) {
                        String temp = (context.getString(R.string.URL_umeng));
                        FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                        fos.write(temp.getBytes());
                        Log.i("handler", "1111111111111111111111111111111111111111111111111111" +
                                        String.valueOf(umeng) + temp

                        );
                        fos.close();//流要及时关闭
                    }
                    if (mybus == 1) {
                        String temp = (context.getString(R.string.URL_mybus));
                        FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                        fos.write(temp.getBytes());
                        Log.i("handler",
                                String.valueOf(mybus) + temp

                        );
                        fos.close();//流要及时关闭
                    }
                    if (jiajia == 1) {
                        String temp = (context.getString(R.string.URL_jiajia));
                        FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                        fos.write(temp.getBytes());
                        fos.close();//流要及时关闭

                    }
                    if (tieba == 1) {
                        String temp = (context.getString(R.string.URL_tieba));
                        FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                        fos.write(temp.getBytes());
                        fos.close();//流要及时关闭

                    }
                    //////////////////////
                    in.close();
                    out.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                mpDialog.cancel();// 下载完成
                if (a == 1) {
                    sendMsg(-1);// 下载完成
                    a = 0;
                } else {
                    sendMsg(2);
                }


            }
        }.start();

    }

    private static void sendMsg(int flag) {
        Log.e("haha", "dsfsdfdsfd");
        Message msg = new Message();
        msg.what = flag;

        handler.sendMessage(msg);

    }

    public static final Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {

            if (!Thread.currentThread().isInterrupted()) {
                Log.i("msg what", String.valueOf(msg.what));
                switch (msg.what) {
                    case 0:


                        break;
                    case 1:


                    {
                        long result = downLoadFileSize * 100 / fileSize;
                        int z = (int) result;
                        // String a=result;


                        mpDialog1.setProgress(z);
                        System.out.println("result:" + (-result));
                        System.out
                                .println("downLoadFileSize:" + (downLoadFileSize));
                        System.out.println("fileSize:" + (fileSize));
                    }


                    break;
                    case 2:

                            Copy_download2hosts(context1, btn_AD1, btn_RE1, btn_AR1);




                        break;
                    case -1:
                        Delhosts();

                        break;

                    default:
                        break;
                }
            }

            super.handleMessage(msg);

        }


    };


}
