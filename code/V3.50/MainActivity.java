package com.lack006.hosts_l;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private final String DATABASE_FILENAME2 = "jttqbcsmwxhxy";
    private final String DATABASE_FILENAME6 = "busybox";
    //////////////////////////////////////////////////////////
    /////////////////////busybox安裝路徑/////////////////////
    /////////////////////////////////////////////////////////
    /*

    private final String Busybox_xbin = "/system/xbin/busybox";
    private final String Busybox_bin = "/system/bin/busybox";

    */

    //////////////////////////////////////////////////////////
    /////////////////////busybox安裝路徑/////////////////////
    /////////////////////////////////////////////////////////


    private TextView resultTextView;
    private TextView version;
    boolean isExit;
    int have = 0;
    int datapath1 = 0;
    int jiajia =0;
    int umeng = 0;
    int mybus = 0;
    int check = 0;
    int whitch = 0;
    long fileSize;
    int downLoadFileSize;
    private ProgressDialog mpDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        resultTextView = (TextView) this.findViewById(R.id.textView4);
        version = (TextView) this.findViewById(R.id.textView1);
        String command[] = {"su", "-c", "ls", "/data"};
        Shell shell = new Shell();
        String text = shell.sendShellCommand(command);
        if ((text.indexOf("app") > -1) || (text.indexOf("anr") > -1) || (text.indexOf("user") > -1) || (text.indexOf("data") > -1)) {
            Toast.makeText(MainActivity.this, "检测到root", Toast.LENGTH_SHORT).show();
            String apkRoot = "chmod 777 " + getPackageCodePath(); // SD卡分区路径，也可能是mmcblk1随系统版本定，当前程序路径请用getPackageCodePath();
            RootCmd(apkRoot);
            if (!isNetworkConnected()) {
                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("提示").setMessage("本程序需要联网运行，请检查网络连接后再次尝试。").setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        System.exit(0);
                    }
                }).show();
            } else {
                Context context = MainActivity.this;// 首先，在Activity里获取context
                File file = context.getFilesDir();
                final String DATABASE_PATH = file.getAbsolutePath();
                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                pb.directory(new File("/"));// 设置shell的当前目录。
                try {
                    Process proc = pb.start();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                    out.println("su");
                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                /*    out.println("toolbox mount -o rw,remount /dev/block/mtdblock3 /system");
                    out.println("busybox mount -o rw,remount /dev/block/mtdblock3 /system");
                    out.println("mount -o remount,rw /system");
                    out.println("toolbox mount -o remount,rw /system");
                    out.println("busybox mount -o remount,rw /system");
                    out.println("busybox --install /system/xbin");
                */
                    String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME6;
                    File dir = new File(DATABASE_PATH);
                    if (!dir.exists()) dir.mkdir();
                    if (!(new File(databaseFilename)).exists()) {
                        InputStream is = getResources().openRawResource(R.raw.lack006);
                        FileOutputStream fos = new FileOutputStream(databaseFilename);
                        byte[] buffer = new byte[8192];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                    }
                    out.println("su");
                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                    out.println("busybox --install /system/xbin");
                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/check");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/check");
                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                    //out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");
                } catch (Exception e) {
                }
                //out.println("rm /data/data/com.lack006.hosts_l/files/jttqbclxylxy");
                whitch = 3;
                File file1 = new File(DATABASE_PATH);
                if (!file1.exists()) {
                    file1.mkdir();
                }
                mpDialog = new ProgressDialog(MainActivity.this);
                mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
                mpDialog.setTitle("检测中");// 设置标题 29
                mpDialog.setMessage("正在检测更新，请稍候。。。");
                mpDialog.setIndeterminate(false);// 设置进度条是否为不明确 false 就是不设置为不明确
                mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
                mpDialog.incrementProgressBy(0); // 增加和减少进度，这个属性必须的
                mpDialog.setButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check = 1;
                        dialog.cancel();
                        new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("提示").setMessage("检测更新已取消。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                            }
                        }).show();
                    }
                });
                mpDialog.show();
                Log.e("downloadPath", DATABASE_PATH);

                new Thread() {
                    public void run() {
                        String apkUrl = "https://raw.githubusercontent.com/lack006/Android-Hosts-L/master/update1.txt";
                        URL url = null;
                        try {
                            check = 0;
                            url = new URL(apkUrl);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestProperty("Accept-Encoding", "identity");
                            InputStream in = con.getInputStream();
                            fileSize = con.getContentLength();
                            File fileOut = new File(DATABASE_PATH + "/check");
                            FileOutputStream out = new FileOutputStream(fileOut);
                            byte[] bytes = new byte[1024];
                            downLoadFileSize = 0;
                            sendMsg(0);
                            int c;
                            while ((c = in.read(bytes)) != -1) {
                                out.write(bytes, 0, c);
                                downLoadFileSize += c;
                                sendMsg(1);// 更新进度条
                                if (check == 1) {
                                    break;
                                }
                            }
                            in.close();
                            out.close();
                        } catch (Exception e) { //TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        sendMsg(2);// 下载完成
                        try {
                        } catch (Exception e) {// TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        } else {
            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("提示").setMessage("本软件必须运行在已root的设备上，本软件未能取得root权限，请检查您的设备是否已经获得root权限后再尝试。\n" +
                    "（本软件暂时不支持魅族系统自带的root权限管理模式,请尝试安装super SU 获取完整的root权限。）").setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    finish();
                }
            }).show();
        }
        try {
            FileInputStream inputStream = new FileInputStream("/system/etc/hosts");
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            Pattern p;
            p = Pattern.compile("\\w{2} \\w{3,7} \\w{2} \\d{4}-\\d{2}-\\d{2}");
            Matcher m;
            m = p.matcher(new String(b));// 获得匹配
            while (m.find()) { // 注意这里，是while不是if
                String xxx = m.group();
                version.setText(xxx);
                break;
            }
        } catch (Exception e) {
        }

        //////////////////////////////////////////////////////
        //////////////////////////////////////////////////////
         /*//////////////////busybox安裝////////////////////*/
        /////////////////////////////////////////////////////
        /*
        final File box1 = new File(Busybox_xbin);
        final File box2 = new File(Busybox_bin);
        if (!box1.exists()) {
            if (!box2.exists()) {
                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("提示").setMessage("软件未检测到您的系统中已安装Busybox，为了本软件能够顺利运行，建议您安装Buxybox！按下“安装”将尝试为您安装软件内置的Busybox。").setPositiveButton("安装", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Context context = MainActivity.this;// 首先，在Activity里获取context
                        File file = context.getFilesDir();
                        final String DATABASE_PATH = file.getAbsolutePath();
                        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME6;
                        File dir = new File(DATABASE_PATH);
                        if (!dir.exists()) dir.mkdir();
                        if (!(new File(databaseFilename)).exists()) {
                            InputStream is = getResources().openRawResource(R.raw.lack006);
                            try {
                                FileOutputStream fos = new FileOutputStream(databaseFilename);
                                byte[] buffer = new byte[8192];
                                int count = 0;
                                while ((count = is.read(buffer)) > 0) {
                                    fos.write(buffer, 0, count);
                                }
                                fos.close();
                                is.close();
                            } catch (Exception e) {
                            }
                        }
                        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                        pb.directory(new File("/"));// 设置shell的当前目录。
                        try {
                            Process proc = pb.start();

                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                            out.println("su");
                            out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                            out.println("toolbox mount -o remount,rw /system");
                            out.println("busybox mount -o remount,rw /system");
                            out.println("busybox mount -o rw,remount yassf2 /system/");
                            out.println("busybox chmod 777 /system");
                            out.println("busybox chmod 777 /system/xbin");
                            out.println("busybox cat /data/data/com.lack006.hosts_l/files/busybox>/system/xbin/busybox");
                            try {
                                Thread.currentThread().sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            out.println("busybox chmod 755 /system/xbin/busybox");
                            out.println("busybox chmod 755 /system");
                            out.println("busybox chmod 755 /system/xbin");
                            //out.println("busybox --install /system/xbin");
                            out.println("toolbox mount -o remount,rw /system");
                            out.println("busybox mount -o remount,rw /system");
                            out.println("toolbox mount -o rw,remount yassf2 /system/");
                            out.println("toolbox chmod 777 /system");
                            out.println("toolbox chmod 777 /system/xbin");
                            out.println("toolbox cat /data/data/com.lack006.hosts_l/files/busybox>/system/xbin/busybox");
                            try {
                                Thread.currentThread().sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            out.println("toolbox chmod 755 /system/xbin/busybox");
                            out.println("toolbox chmod 755 /system");
                            out.println("toolbox chmod 755 /system/xbin");
                            //out.println("busybox --install /system/xbin");
                            out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                            out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                        } catch (Exception e) {
                        }
                        try {
                            Thread.currentThread().sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!box1.exists()) {
                            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装失败").setMessage("建议用户自行百度手动安装Busybox。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            }).show();
                        } else if (box1.exists()) {
                            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装成功").setMessage("软件内置的Busybox已安装成功，若依旧应用失败，建议用户自行百度手动安装Busybox。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {


                                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                    pb.directory(new File("/"));// 设置shell的当前目录。
                                    try {
                                        Process proc = pb.start();

                                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                                        out.println("su");

                                        out.println("export PATH=/system/xbin:$PATH");
                                        out.println("busybox --install /system/xbin");
                                        //out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                                        //out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                                    } catch (Exception e) {
                                    }




                                }
                            }).show();
                        }
                    }
                }).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
            }
        }



        ///////////////////////////////////////////////////////
        //////////////////////////////////////////////////////
        /*//////////////////busybox安裝////////////////////*/
        /////////////////////////////////////////////////////
        final Button mButton1 = (Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(new OnClickListener() {
            Context context = MainActivity.this;// 首先，在Activity里获取context
            File file = context.getFilesDir();
            String DATABASE_PATH = file.getAbsolutePath();
            final String[] arrayFruit = new String[]{"勾选后使用data路径（在提示应用失败时可以尝试）", "勾选屏蔽 au.umeng域名\n" + "(可能会影响一些软件更新如：bilibili客户端)", "勾选屏蔽 0591.mygolbs.com\n" + "（会导致掌上公交福州区域无法正常使用）","勾选屏蔽 api.mofang.com\n"+"(会导致无法使用“扑家汉化组”汉化游戏中的“加加”模块）"};
            final boolean[] arrayFruitSelected = new boolean[]{false, false, false, false};

            public void onClick(View v) {
                umeng = 0;
                mybus = 0;
                datapath1 = 0;
                jiajia =0;
                whitch = 1;
                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("请选择").setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        arrayFruitSelected[which] = isChecked;
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < arrayFruitSelected.length; i++) {
                            if (arrayFruitSelected[i] == true) {
                                if (i == 0) {
                                    datapath1 = 1;
                                }
                                if (i == 1) {
                                    umeng = 1;
                                }
                                if (i == 2) {
                                    mybus = 1;
                                }
                                if (i == 3){
                                    jiajia = 1;

                                }
                                stringBuilder.append(arrayFruit[i] + "、");
                            }
                        }
                        //   Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                        File file1 = new File(DATABASE_PATH);
                        if (!file1.exists()) {
                            file1.mkdir();
                        }
                        mpDialog = new ProgressDialog(MainActivity.this);
                        mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
                        mpDialog.setTitle("下载中");// 设置标题 29
                        //  mpDialog.setIcon(R.drawable.icon);//设置图标
                        mpDialog.setMessage("Android Hosts-L\n正在下载中，请稍候。。。");
                        mpDialog.setIndeterminate(false);// 设置进度条是否为不明确 false 就是不设置为不明确
                        mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
                        mpDialog.setProgress(20);
                        mpDialog.incrementProgressBy(0); // 增加和减少进度，这个属性必须的
                        mpDialog.setButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check = 1;
                                dialog.cancel();
                                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("提示").setMessage("下载已取消。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                    }
                                }).show();
                            }
                        });
                        mpDialog.show();
                        Log.e("downloadPath", DATABASE_PATH);

                        new Thread() {
                            public void run() {
                                String apkUrl = "https://raw.githubusercontent.com/lack006/Android-Hosts-L/master/hosts.txt";
                                URL url = null;
                                try {
                                    check = 0;
                                    url = new URL(apkUrl);
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
                                        if (check == 1) {
                                            break;
                                        }
                                    }
                                    if (check == 0) {
                                        if (whitch == 1) {
                                            if (umeng == 1) {
                                                String temp = "\n#umeng start" + "\n127.0.0.1 au.umeng.co\n" + "127.0.0.1  au.umeng.com\n" + "#umeng end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                out.write(temp.getBytes());
                                                fos.close();//流要及时关闭
                                            }
                                            if (mybus == 1) {
                                                String temp = "\n#mybus start" + "\n127.0.0.1 0591.mygolbs.com\n" + "#mybus end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                out.write(temp.getBytes());
                                            }
                                            if (jiajia == 1){
                                                String temp = "\n#jiajia start" + "\n127.0.0.1 api.mofang.com\n" +  "127.0.0.1  spapi.i-moible.co.jp\n" +"#jiajia end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                out.write(temp.getBytes());

                                            }
                                        }
                                    }
                                    in.close();
                                    out.close();
                                } catch (Exception e) {
                                    //TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                sendMsg(2);// 下载完成
                                try {
                                } catch (Exception e) { //TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }).show();
            }
        });
        final Button instruction_book = (Button) findViewById(R.id.instruction_book);
        instruction_book.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("使用说明").setMessage("软件名称：Android Hosts-L\n\n" + "\t\t\t本软件通过修改Android设备中的hosts文件，" + "将被墙域名解析到“正确”的地址，将广告域名“劫持”到空白的地址，" + "以达到重定向（翻墙）和屏蔽软件联网广告的目的。（原理可自行百度）\n\n" + "\t\t\t软件中有（重定向），（去广告+重定向），系统原版三种模式，三选一即可。\n\n" + "\t\t\t软件上方会显示服务器上的hosts版本和目前系统中的hosts版本，可以根据显示的版本号进行更新。\n\n" + "\t\t\t软件仅供学习之用，请勿用于商业用途。").setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
            }
        });
        final Button mButton6 = (Button) findViewById(R.id.button4);
        mButton6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });
        final Button mButton2 = (Button) findViewById(R.id.button2);
        mButton2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Context context = MainActivity.this;// 首先，在Activity里获取context
                File file = context.getFilesDir();
                String DATABASE_PATH = file.getAbsolutePath();// 此处返回的路劲为/data/data/包/files，其中的包就是我们建立的主Activity所在的包 我们可以看到这个路径也是在data文件夹下 程序本身是可以对自己的私有文件进行操作 程序中很多私有的数据会写入到私有文件路径下，这也是android为什么对data数据做保护的原因之一
                Toast.makeText(MainActivity.this, "开始还原", Toast.LENGTH_SHORT).show();
                mButton2.setEnabled(false);
                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                pb.directory(new File("/"));// 设置shell的当前目录。
                try {
                    String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME2;
                    File dir = new File(DATABASE_PATH);
                    if (!dir.exists()) dir.mkdir();
                    if (!(new File(databaseFilename)).exists()) {
                        InputStream is = getResources().openRawResource(R.raw.jttqbcsmwxhxy);
                        FileOutputStream fos = new FileOutputStream(databaseFilename);
                        byte[] buffer = new byte[8192];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.close();
                        is.close();
                    } //////////////////////////////////
                    try {
                        databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME6;
                        if (!dir.exists()) dir.mkdir();
                        if (!(new File(databaseFilename)).exists()) {
                            InputStream is = getResources().openRawResource(R.raw.lack006);
                            FileOutputStream fos = new FileOutputStream(databaseFilename);
                            byte[] buffer = new byte[8192];
                            int count = 0;
                            while ((count = is.read(buffer)) > 0) {
                                fos.write(buffer, 0, count);
                            }
                            fos.close();
                            is.close();
                        }
                    } catch (Exception e) {
                    } //////////////////////////////////
                    Process proc = pb.start(); //获取输入流，可以通过它获取SHELL的输出。 BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream())); BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream())); 获取输出流，可以通过它向SHELL发送命令。
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                    out.println("su");
                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                    out.println("busybox mount -o rw,remount yassf2 /system/");
                    out.println("toolbox mount -o remount,rw /system");
                    out.println("busybox mount -o remount,rw /system");
                    out.println("busybox chmod 777 /system");
                    out.println("busybox chmod 777 /system/etc");
                    out.println("busybox chmod 777 /system/etc/hosts");
                 //   out.println("su -c 'rm -fr /system/etc/hosts'");
                    out.println("busybox cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.println("busybox chmod 644 /system/etc/hosts");
                    out.println("busybox chmod 755 /system");
                    out.println("busybox chmod 755 /system/etc");
                    out.println("toolbox mount -o rw,remount yassf2 /system/");
                    out.println("toolbox chmod 777 /system");
                    out.println("toolbox chmod 777 /system/etc");
                    out.println("toolbox chmod 777 /system/etc/hosts");

                    out.println("toolbox cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
               //     out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
                    out.println("toolbox chmod 644 /system/etc/hosts");
                    out.println("toolbox chmod 755 /system");
                    out.println("toolbox chmod 755 /system/etc");
                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy");
                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy");
                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                    mButton2.setEnabled(true);
                    new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("恢复完成").setMessage("Android的原版hosts已经成功应用到您的系统中，请关闭并重新打开移动数据/无线网络或者重启设备使文件生效（清空DNS缓存），感谢您的支持！！！").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
                    version.setText("本地 hosts 未应用或系统默认hosts");
                } catch (Exception e) {
                    System.out.println("exception:" + e);
                    new AlertDialog.Builder(MainActivity.this).setTitle("错误报告").setMessage("无法复制！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
                }
            }
        });
        final Button mButtonMKER = (Button) findViewById(R.id.button6);
        mButtonMKER.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
             //   String url = "http://www.baidu.com"; // web address
                Uri uri = Uri.parse("http://t.cn/Rv7Rr1c");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);


            }
        });
        final Button mButton3 = (Button) findViewById(R.id.button3);
        mButton3.setOnClickListener(new OnClickListener() {
            Context context = MainActivity.this;// 首先，在Activity里获取context
            File file = context.getFilesDir();
            String DATABASE_PATH = file.getAbsolutePath();
            final String[] arrayFruit = new String[]{"勾选后使用data路径（在提示应用失败时可以尝试）"};
            final boolean[] arrayFruitSelected = new boolean[]{false};

            public void onClick(View v) {
                datapath1 = 0;
                whitch = 2; ///////////////////////////////////////////
                new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("请选择").setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        arrayFruitSelected[which] = isChecked;
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < arrayFruitSelected.length; i++) {
                                    if (arrayFruitSelected[i] == true) {
                                        if (i == 0) {
                                            datapath1 = 1;
                                        }
                                        stringBuilder.append(arrayFruit[i] + "、");
                                    }
                                } ////////////////////////////////////////////
                                File file1 = new File(DATABASE_PATH);
                                if (!file1.exists()) {
                                    file1.mkdir();
                                }
                                mpDialog = new ProgressDialog(MainActivity.this);
                                mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
                                mpDialog.setTitle("下载中");// 设置标题 29
                                //mpDialog.setIcon(R.drawable.icon);//设置图标
                                mpDialog.setMessage("Android Hosts-L（重定向）\n正在下载中，请稍候。。。");
                                mpDialog.setIndeterminate(false);// 设置进度条是否为不明确 false 就是不设置为不明确
                                mpDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
                                // mpDialog.setProgress(20);
                                mpDialog.incrementProgressBy(0); // 增加和减少进度，这个属性必须的
                                mpDialog.setButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        check = 1;

                                        dialog.cancel();

                                        new AlertDialog.Builder(MainActivity.this)
                                                .setCancelable(false)
                                                .setTitle("提示")
                                                .setMessage("下载已取消。")
                                                .setPositiveButton("确定",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialoginterface,
                                                                    int i) {
                                                            }
                                                        }
                                                ).show();

                                    }
                                });
                                mpDialog.show();

                                Log.e("downloadPath", DATABASE_PATH);
                                // Looper.prepare();
                                new Thread() {
                                    public void run() {
                                        String apkUrl = "https://raw.githubusercontent.com/lack006/Android-Hosts-L/master/hosts3.txt";

                                        URL url = null;
                                        try {
                                            check = 0;

                                            url = new URL(apkUrl);

                                            HttpURLConnection con = (HttpURLConnection) url
                                                    .openConnection();
                                            con.setRequestProperty("Accept-Encoding",
                                                    "identity");
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
                                                if (check == 1) {

                                                    break;

                                                }
                                            }

                                            in.close();
                                            out.close();

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        sendMsg(2);// 下载完成
                                        try {


                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        }
                ).show();


            }
        });


    }

    public boolean RootCmd(String cmd) {

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
            System.out.println("exception:" + e);
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle("错误报告")
                    .setMessage("无法获取Root权限！！！")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    finish();
                                }
                            }
                    ).show();

            return false;
        } finally {
            try {
                if (os != null) {

                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                System.out.println("exception:" + e);
                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setTitle("错误报告")
                        .setMessage("无法获取Root权限！！！")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface,
                                            int i) {
                                        finish();
                                    }
                                }
                        ).show();

            }
        }
        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };

    /**
     * A placeholder fragment containing a simple view.
     */

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
            pb.directory(new File("/"));// 设置shell的当前目录。
            Log.i("handler",
                    "55555555555555555555" + String.valueOf(downLoadFileSize)
                            + ""
            );
            if (!Thread.currentThread().isInterrupted()) {
                Log.i("msg what", String.valueOf(msg.what));
                switch (msg.what) {
                    case 0:
                        // mpDialog.setMax(fileSize);

                        // mpDialog.setMax(fileSize);

                        break;
                    case 1:

                    {
                        long result = downLoadFileSize * 100 / fileSize;
                        int a = (int) result;
                        // String a=result;

                        mpDialog.setProgress(a);
                        System.out.println("result:" + (-result));
                        System.out
                                .println("downLoadFileSize:" + (downLoadFileSize));
                        System.out.println("fileSize:" + (fileSize));
                    }

                    break;
                    case 2:
                        System.out.println("check:" + (check));
                        System.out.println("whitch" + (whitch));
                        if (whitch == 3) {
                            if (check == 1) {
                                check = 0;

                                Process proc;
                                try {
                                    proc = pb.start();

                                    // 获取输入流，可以通过它获取SHELL的输出。
                                    // BufferedReader in = new BufferedReader(new
                                    // InputStreamReader(proc.getInputStream()));
                                    // BufferedReader err = new BufferedReader(new
                                    // InputStreamReader(proc.getErrorStream()));
                                    // 获取输出流，可以通过它向SHELL发送命令。
                                    PrintWriter out = new PrintWriter(
                                            new BufferedWriter(
                                                    new OutputStreamWriter(proc
                                                            .getOutputStream())
                                            ),
                                            true
                                    );

                                    Context context = MainActivity.this;// 首先，在Activity里获取context
                                    File file = context.getFilesDir();
                                    final String DATABASE_PATH = file.getAbsolutePath();

                                    try {


                                        String databaseFilename = DATABASE_PATH + "/"
                                                + DATABASE_FILENAME6;
                                        File dir = new File(DATABASE_PATH);

                                        if (!dir.exists())
                                            dir.mkdir();

                                        if (!(new File(databaseFilename)).exists()) {

                                            InputStream is = getResources().openRawResource(
                                                    R.raw.lack006);
                                            FileOutputStream fos = new FileOutputStream(
                                                    databaseFilename);
                                            byte[] buffer = new byte[8192];
                                            int count = 0;

                                            while ((count = is.read(buffer)) > 0) {
                                                fos.write(buffer, 0, count);
                                            }
                                            fos.close();
                                            is.close();
                                        }


                                        //	out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");
                                    } catch (Exception e) {
                                    }


                                    out.println("su");
                                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");


                                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/check");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");

                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/check");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");


                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                break;
                            } else {
                                whitch = 0;
                                try {
                                    FileInputStream inputStream = new FileInputStream(
                                            "/data/data/com.lack006.hosts_l/files/check");
                                    byte[] b = new byte[inputStream.available()];
                                    inputStream.read(b);
                                    resultTextView.setText(new String(b));
                                    mpDialog.cancel();

                                } catch (Exception e) {
                                    // Toast.makeText(Checkupdate.this, "读取失败",
                                    // Toast.LENGTH_SHORT).show();
                                }

                                Process proc;
                                try {
                                    Context context = MainActivity.this;// 首先，在Activity里获取context
                                    File file = context.getFilesDir();
                                    final String DATABASE_PATH = file.getAbsolutePath();
                                    proc = pb.start();

                                    // 获取输入流，可以通过它获取SHELL的输出。
                                    // BufferedReader in = new BufferedReader(new
                                    // InputStreamReader(proc.getInputStream()));
                                    // BufferedReader err = new BufferedReader(new
                                    // InputStreamReader(proc.getErrorStream()));
                                    // 获取输出流，可以通过它向SHELL发送命令。
                                    PrintWriter out = new PrintWriter(
                                            new BufferedWriter(
                                                    new OutputStreamWriter(proc
                                                            .getOutputStream())
                                            ),
                                            true
                                    );


                                    try {


                                        String databaseFilename = DATABASE_PATH + "/"
                                                + DATABASE_FILENAME6;
                                        File dir = new File(DATABASE_PATH);

                                        if (!dir.exists())
                                            dir.mkdir();

                                        if (!(new File(databaseFilename)).exists()) {

                                            InputStream is = getResources().openRawResource(
                                                    R.raw.lack006);
                                            FileOutputStream fos = new FileOutputStream(
                                                    databaseFilename);
                                            byte[] buffer = new byte[8192];
                                            int count = 0;

                                            while ((count = is.read(buffer)) > 0) {
                                                fos.write(buffer, 0, count);
                                            }
                                            fos.close();
                                            is.close();
                                        }


                                        //	out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");
                                    } catch (Exception e) {
                                    }


                                    out.println("su");
                                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/check");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/check");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");


                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                break;

                            }

                        }
                        if (check == 1) {

                            Context context = MainActivity.this;// 首先，在Activity里获取context
                            File file = context.getFilesDir();
                            final String DATABASE_PATH = file.getAbsolutePath();
                            check = 0;

                            Process proc;
                            try {


                                proc = pb.start();

                                // 获取输入流，可以通过它获取SHELL的输出。
                                // BufferedReader in = new BufferedReader(new
                                // InputStreamReader(proc.getInputStream()));
                                // BufferedReader err = new BufferedReader(new
                                // InputStreamReader(proc.getErrorStream()));
                                // 获取输出流，可以通过它向SHELL发送命令。
                                PrintWriter out = new PrintWriter(
                                        new BufferedWriter(new OutputStreamWriter(
                                                proc.getOutputStream())), true
                                );

                                try {


                                    String databaseFilename = DATABASE_PATH + "/"
                                            + DATABASE_FILENAME6;
                                    File dir = new File(DATABASE_PATH);

                                    if (!dir.exists())
                                        dir.mkdir();

                                    if (!(new File(databaseFilename)).exists()) {

                                        InputStream is = getResources().openRawResource(
                                                R.raw.lack006);
                                        FileOutputStream fos = new FileOutputStream(
                                                databaseFilename);
                                        byte[] buffer = new byte[8192];
                                        int count = 0;

                                        while ((count = is.read(buffer)) > 0) {
                                            fos.write(buffer, 0, count);
                                        }
                                        fos.close();
                                        is.close();
                                    }


                                    //	out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");
                                } catch (Exception e) {
                                }


                                out.println("su");
                                out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                                out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                                out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
                                out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                                out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
                                out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");


                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            mpDialog.setMessage("文件下载完成");
                            Context context = MainActivity.this;// 首先，在Activity里获取context
                            File file = context.getFilesDir();
                            final String DATABASE_PATH = file.getAbsolutePath();

                            Process proc;
                            try {
                                proc = pb.start();

                                // 获取输入流，可以通过它获取SHELL的输出。
                                // BufferedReader in = new BufferedReader(new
                                // InputStreamReader(proc.getInputStream()));
                                // BufferedReader err = new BufferedReader(new
                                // InputStreamReader(proc.getErrorStream()));
                                // 获取输出流，可以通过它向SHELL发送命令。
                                PrintWriter out = new PrintWriter(
                                        new BufferedWriter(new OutputStreamWriter(
                                                proc.getOutputStream())), true
                                );


                                try {


                                    String databaseFilename = DATABASE_PATH + "/"
                                            + DATABASE_FILENAME6;
                                    File dir = new File(DATABASE_PATH);

                                    if (!dir.exists())
                                        dir.mkdir();

                                    if (!(new File(databaseFilename)).exists()) {

                                        InputStream is = getResources().openRawResource(
                                                R.raw.lack006);
                                        FileOutputStream fos = new FileOutputStream(
                                                databaseFilename);
                                        byte[] buffer = new byte[8192];
                                        int count = 0;

                                        while ((count = is.read(buffer)) > 0) {
                                            fos.write(buffer, 0, count);
                                        }
                                        fos.close();
                                        is.close();
                                    }


                                    //	out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");
                                } catch (Exception e) {
                                }

                                if (datapath1 == 1) {
                                    out.println("su");
                                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                                    out.println("busybox mount -o rw,remount yassf2 /system/");
                                    out.println("toolbox mount -o remount,rw /system");
                                    out.println("busybox mount -o remount,rw /system");
                                    out.println("busybox chmod 777 /system");
                                    out.println("busybox chmod 777 /system/etc");
                                    out.println("busybox chmod 777 /data/hosts");
                                   // out.println("su -c 'rm -fr /data/hosts'");
                                    out.println("busybox cp /data/data/com.lack006.hosts_l/files/hosts /data/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /data/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("busybox chmod 644 /data/hosts");
                                    out.println("busybox rm /system/etc/hosts");
                                    out.println("busybox ln -s /data/hosts /system/etc/hosts");
                                    out.println("busybox chmod 755 /system");
                                    out.println("busybox chmod 755 /system/etc");


                                   out.println("toolbox mount -o rw,remount yassf2 /system/");
                                    out.println("toolbox mount -o remount,rw /system");
                                    out.println("busybox mount -o remount,rw /system");
                                    out.println("toolbox chmod 777 /system");
                                    out.println("toolbox chmod 777 /system/etc");
                                    out.println("toolbox chmod 777 /data/hosts");
                                    out.println("toolbox rm /data/hosts");
                                    out.println("toolbox cp /data/data/com.lack006.hosts_l/files/hosts /data/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("toolbox chmod 644 /data/hosts");

                                    out.println("toolbox ln -s /data/hosts /system/etc/hosts");
                                    out.println("toolbox chmod 755 /system");
                                    out.println("toolbox chmod 755 /system/etc");


                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                                } else if (datapath1 == 0) {


                                    out.println("su");
                                    out.println("toolbox chmod 755 /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                                   out.println("busybox mount -o rw,remount yassf2 /system/");
                                    out.println("toolbox mount -o remount,rw /system");
                                    out.println("busybox mount -o remount,rw /system");
                                    out.println("busybox chmod 777 /system");
                                    out.println("busybox chmod 777 /system/etc");
                                    out.println("busybox chmod 777 /system/etc/hosts");
                                  //  out.println("su -c 'rm -fr /system/etc/hosts'");
                                    out.println("busybox cp /data/data/com.lack006.hosts_l/files/hosts /system/etc/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("busybox chmod 644 /system/etc/hosts");
                                    out.println("busybox chmod 755 /system");
                                    out.println("busybox chmod 755 /system/etc");


                                    out.println("toolbox mount -o rw,remount yassf2 /system/");
                                    out.println("toolbox chmod 777 /system");
                                    out.println("toolbox chmod 777 /system/etc");
                                    out.println("toolbox chmod 777 /system/etc/hosts");

                                    out.println("toolbox cp /data/data/com.lack006.hosts_l/files/hosts /system/etc/hosts");
                                    try {
                                        Thread.currentThread().sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    out.println("toolbox chmod 644 /system/etc/hosts");
                                    out.println("toolbox chmod 755 /system");
                                    out.println("toolbox chmod 755 /system/etc");

                                    out.println("busybox chmod 777 /data/hosts");
                                    out.println("su -c 'rm -fr /data/hosts'");
                                    out.println("toolbox chmod 777 /data/hosts");
                                    out.println("su -c 'rm -fr /data/hosts'");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/hosts");
                                    out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/hosts");
                                    out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                                }

                                //	out.println("cp /system/etc/hosts /data/data/com.lack006.hosts_l/files/temp");
                                //	out.println("chmod 777 /data/data/com.lack006.hosts_l/files/temp");

                                mpDialog.cancel();

                                if (whitch == 1) {
//                                    if(umeng==1)
//                                    {
//
//                                        String temp="Hello world!\n";
//                                        FileOutputStream fos = new FileOutputStream("D:\\my.txt",true);//true表示在文件末尾追加
//                                        fos.write(temp.getBytes());
//                                        fos.close();//流要及时关闭
//
//                                    }
                                    have = 0;
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setCancelable(false)
                                            .setTitle("下载完成")
                                            .setMessage(
                                                    "请点击确定继续。")
                                            .setPositiveButton(
                                                    "确定",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialoginterface,
                                                                int i) {
                                                            // super.handleMessage(msg);


                                                            try {


                                                                FileInputStream inputStream = new FileInputStream(
                                                                        "/system/etc/hosts");
                                                                byte[] b = new byte[inputStream
                                                                        .available()];
                                                                inputStream.read(b);
                                                                Pattern p;
                                                                p = Pattern
                                                                        .compile("\\w{2} \\w{3,7} \\w{2} \\d{4}-\\d{2}-\\d{2}");
                                                                Matcher m;
                                                                m = p.matcher(new String(
                                                                        b));// 获得匹配
                                                                while (m.find()) { // 注意这里，是while不是if
                                                                    String xxx = m
                                                                            .group();
                                                                    version.setText(xxx);
                                                                    have = 1;
                                                                    break;

                                                                }


                                                            } catch (Exception e) {

                                                            }
                                                            if (have == 1) {

                                                                new AlertDialog.Builder(MainActivity.this)
                                                                        .setCancelable(false)
                                                                        .setTitle("应用完成")
                                                                        .setMessage(
                                                                                "Android Hosts-L（重定向、去广告）已经成功应用到您的系统中，请关闭并重新打开移动数据/无线网络或者重启设备使文件生效（清空DNS缓存），感谢您的支持！！！\n\n" +
                                                                                        "(建议使用 SDcard 清理类软件清理广告程序的广告缓存以达到去广告的效果)")
                                                                        .setPositiveButton(
                                                                                "我知道了",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialoginterface,
                                                                                            int i) {


                                                                                    }
                                                                                }
                                                                        ).show();
                                                            } else {
                                                                new AlertDialog.Builder(MainActivity.this)
                                                                        .setCancelable(false)
                                                                        .setTitle("应用失败")
                                                                        .setMessage(
                                                                                "软件应用hosts失败，请尝试使用data路径模式。")
                                                                        .setPositiveButton(
                                                                                "我知道了",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialoginterface,
                                                                                            int i) {
                                                                                        version.setText("本地 hosts 未应用或系统默认hosts");


                                                                                    }
                                                                                }
                                                                        ).show();
                                                            }


                                                        }
                                                    }
                                            ).show();


                                }


                                if (whitch == 2) {
                                    have = 0;

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setCancelable(false)
                                            .setTitle("下载完成")
                                            .setMessage(
                                                    "请点击确定继续。")
                                            .setPositiveButton(
                                                    "确定",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialoginterface,
                                                                int i) {


                                                            try {


                                                                FileInputStream inputStream = new FileInputStream(
                                                                        "/system/etc/hosts");
                                                                byte[] b = new byte[inputStream
                                                                        .available()];
                                                                inputStream.read(b);
                                                                Pattern p;
                                                                p = Pattern
                                                                        .compile("\\w{2} \\w{3,7} \\w{2} \\d{4}-\\d{2}-\\d{2}");
                                                                Matcher m;
                                                                m = p.matcher(new String(
                                                                        b));// 获得匹配
                                                                while (m.find()) { // 注意这里，是while不是if
                                                                    String xxx = m
                                                                            .group();
                                                                    version.setText(xxx);
                                                                    have = 1;
                                                                    break;

                                                                }


                                                            } catch (Exception e) {

                                                            }

                                                            if (have == 1) {
                                                                new AlertDialog.Builder(MainActivity.this)
                                                                        .setCancelable(false)
                                                                        .setTitle("应用完成")
                                                                        .setMessage(
                                                                                "Android Hosts-L（重定向）已经成功应用到您的系统中，请关闭并重新打开移动数据/无线网络或者重启设备使文件生效（清空DNS缓存），感谢您的支持！！！")
                                                                        .setPositiveButton(
                                                                                "我知道了",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialoginterface,
                                                                                            int i) {


                                                                                    }
                                                                                }
                                                                        ).show();
                                                            } else {
                                                                new AlertDialog.Builder(MainActivity.this)
                                                                        .setCancelable(false)
                                                                        .setTitle("应用失败")
                                                                        .setMessage(
                                                                                "软件应用hosts失败，请尝试使用data路径模式。")
                                                                        .setPositiveButton(
                                                                                "我知道了",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(
                                                                                            DialogInterface dialoginterface,
                                                                                            int i) {

                                                                                        version.setText("本地 hosts 未应用或系统默认hosts");
                                                                                    }
                                                                                }
                                                                        ).show();


                                                            }


                                                        }
                                                    }
                                            ).show();


                                }

                                // ///////////////////////////////////////////////////////////
                                // ///////////////////////////////////////////////////////////

                                // ///////////////////////////////////////////////////////////
                                // ///////////////////////////////////////////////////////////

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            break;
                        }
                        break;
                    case -1:
                        String error = msg.getData().getString("error");
                        mpDialog.setMessage(error);

                        break;

                    default:
                        break;
                }
            }

            super.handleMessage(msg);

        }
    };

    private void sendMsg(int flag) {
        Log.e("haha", "dsfsdfdsfd");
        Message msg = new Message();
        msg.what = flag;

        handler.sendMessage(msg);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle("关于")
                    .setMessage(
                            "版本：V3.50\n" +
                                    "作者：lack006\n" +
                                    "去广告hosts文件来自：lack006\n\n" +
                               //     "重定向hosts文件来自：Imouto.host(已授权) \n\n" +
                                    "软件更新日志：\n" +
                                    "V3.50：重启项目。\n" +
                                    "V3.21：增加一项屏蔽选择。\n" +
                                    "V3.20：去除安装/检测Busybox提示。更完整的提示内容。\n" +
                                    "V3.18：增加挂载系统目录的代码冗余量。\n" +
                                    "V3.17：修复代码。\n" +
                                    "V3.15+：经测试修复cm11 4.4.4原生系统。\n" +
                                    "V3.14：加入强制删除命令，尝试修复4.4以上系统问题。\n" +
                                    "V3.13：提高兼容性。\n" +
                                    "V3.12：再次尝试兼容4.4以上系统。\n" +
                                    "V3.11：增加智能识别是否安装Busybox功能，增加本地安装Busybox功能。\n" +
                                    "V3.10：增加软件说明。智能检测应用hosts情况。增加高级应用模式，尝试修复某些机型应用失败等问题。\n" +
                                    "V3.01：尝试修复某些机型退出按钮BUG。\n" +
                                    "V3.00：新增可以选择是否屏蔽与软件功能有冲突的广告域名。\n" +
                                    "V2.21：提高软件兼容性。\n" +
                                    "V2.20：程序自带命令集，减少调用系统内部命令，提高兼容性，基本无视系统指令集是否完整。\n" +
                                    "V2.12：修复BUG。\n" +
                                    "V2.11：实验性修复失败，修复代码。\n" +
                                    "V2.10：优化下载hosts方式，更亲切的下载界面，实验性修复hosts应用方式，减少权限需求。\n" +
                                    "V2.02：增加显示本地hosts版本号。\n" +
                                    "V2.01：优化提示。\n" +
                                    "V2.00：软件更新为云更新版本。\n" +
                                    "V1.08：增加检测root功能。\n" +
                                    "V1.07：修复MIUI系统无法应用的问题。\n" +
                                    "V1.06：增加应用原版Imouto.host。\n" +
                                    "V1.05：优化对系统的读写。\n" +
                                    "V1.04：增加软件声明，优化断网提示。\n" +
                                    "V1.03：增加断网提示。\n" +
                                    "V1.02：增加关于与提示窗口。"
                    )
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                }
                            }
                    ).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
