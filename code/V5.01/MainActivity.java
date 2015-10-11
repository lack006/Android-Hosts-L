package com.lack006.hosts_l;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private ListView Buttonlistview;
    private final String DATABASE_FILENAME2 = "jttqbcsmwxhxy";
    private final String DATABASE_FILENAME6 = "busybox";
    private TextView resultTextView;
    private TextView version;
    private TextView versiontitle;
    private TextView ADversion;
    private TextView REversion;
    private TextView AD_REversion;
    private final String Busybox_xbin = "/system/xbin/busybox";
    private final String Busybox_bin = "/system/bin/busybox";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);





        ADversion = (TextView) this.findViewById(R.id.button2); //网络版本
       REversion = (TextView) this.findViewById(R.id.button3); //网络版本
        AD_REversion = (TextView) this.findViewById(R.id.button4); //网络版本
        version = (TextView) this.findViewById(R.id.textView4);//本地版本
        versiontitle = (TextView) this.findViewById(R.id.Title1);//本地版本





        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);




        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
            drawerArrow, R.string.drawer_open,
            R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        String[] values = new String[]{
            "   刷新DNS缓存",
            "   备份\\还原hosts文件",
            "   恢复系统默认hosts文件",
            "   重启设备",
            "   安装软件内置Busybox",
            "   软件更新",
            "   关于软件",
            "   lack006-CN（翾寰之星）",
            "   退出软件"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Context context = MainActivity.this;// 首先，在Activity里获取context
                        ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                        pb.directory(new File("/"));// 设置shell的当前目录。
                        try {
                            Process proc = pb.start();
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                            out.println("su");
                            out.println("export PATH=/data/data/com.lack006.hosts_l/files:/system/xbin:$PATH");
                            out.println("ndc resolver flushdefaultif");
                            out.println("ndc resolver flushif wlan0");
                            Toast.makeText(MainActivity.this, "DNS缓存刷新完毕！", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 1:
                        Context context1 = MainActivity.this;// 首先，在Activity里获取context
                        File file = context1.getFilesDir();
                        final String DATABASE_PATH = file.getAbsolutePath();
                        String databaseFilename = DATABASE_PATH + "/" + "backup";

                        if (!(new File(databaseFilename)).exists()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            // builder.setIcon(R.drawable.icon);
                            builder.setTitle("提示");
                            builder.setMessage("未检测到备份的hosts文件，请选择备份。");
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder.setNeutralButton("备份", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

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

                                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                    pb.directory(new File("/"));// 设置shell的当前目录。
                                    try {
                                        Process proc = pb.start();
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
                                        //  out.println("su -c 'rm -fr /system/etc/hosts'");
                                        out.println("busybox cp  /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    //    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
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

                                        out.println("toolbox cp /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        out.println("toolbox chmod 644 /system/etc/hosts");
                                        out.println("toolbox chmod 755 /system");
                                        out.println("toolbox chmod 755 /system/etc");



                                        out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");

                                        out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");

                                        Toast.makeText(MainActivity.this, "备份完毕！", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                    }


                                }
                            });

                            builder.create().show();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            // builder.setIcon(R.drawable.icon);
                            builder.setTitle("提示");
                            builder.setMessage("检测到备份的hosts文件，请选择。");
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder.setNegativeButton("重新备份", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

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

                                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                    pb.directory(new File("/"));// 设置shell的当前目录。
                                    try {
                                        Process proc = pb.start();
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
                                        out.println("busybox rm /data/data/com.lack006.hosts_l/files/backup");


                                        //  out.println("su -c 'rm -fr /system/etc/hosts'");
                                        out.println("busybox cp  /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
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
                                        out.println("toolbox rm /data/data/com.lack006.hosts_l/files/backup");
                                        out.println("toolbox cp /system/etc/hosts /data/data/com.lack006.hosts_l/files/backup");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        out.println("toolbox chmod 644 /system/etc/hosts");
                                        out.println("toolbox chmod 755 /system");
                                        out.println("toolbox chmod 755 /system/etc");



                                        out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");

                                        out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");

                                        Toast.makeText(MainActivity.this, "备份完毕！", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                    }


                                }
                            });
                            builder.setNeutralButton("还原", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

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

                                    ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                    pb.directory(new File("/"));// 设置shell的当前目录。
                                    try {
                                        Process proc = pb.start();
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
                                        //  out.println("su -c 'rm -fr /system/etc/hosts'");
                                        out.println("busybox cp /data/data/com.lack006.hosts_l/files/backup /system/etc/hosts");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //    out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
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

                                        out.println("toolbox cp /data/data/com.lack006.hosts_l/files/backup /system/etc/hosts");
                                        try {
                                            Thread.currentThread().sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        out.println("toolbox chmod 644 /system/etc/hosts");
                                        out.println("toolbox chmod 755 /system");
                                        out.println("toolbox chmod 755 /system/etc");



                                        out.println("busybox rm /data/data/com.lack006.hosts_l/files/busybox");

                                        out.println("toolbox rm /data/data/com.lack006.hosts_l/files/busybox");
                                        out.println("ndc resolver flushdefaultif");
                                        out.println("ndc resolver flushif wlan0");


                                        new AlertDialog.Builder(MainActivity.this)
                                                .setCancelable(false)
                                                .setTitle("还原完成")

                                                .setPositiveButton(
                                                        "轻触以刷新",
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
                                                                        version.setBackgroundColor(Color.parseColor("#FF000AFF"));
                                                                        versiontitle.setBackgroundColor(Color.parseColor("#FF000AFF"));
                                                                        have = 1;
                                                                        break;

                                                                    }
                                                                    if(have==0)
                                                                    {




                                                                            version.setText("@string/local_or_none");
                                                                            version.setBackgroundColor(Color.parseColor("#ffff0000"));
                                                                            versiontitle.setBackgroundColor(Color.parseColor("#ffff0000"));


                                                                    }


                                                                } catch (Exception e) {

                                                                }




                                                            }
                                                        }
                                                ).show();


                                        Toast.makeText(MainActivity.this, "还原完毕！", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                    }


                                }
                            });

                            builder.create().show();

                        }




                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 2:
                        context = MainActivity.this;// 首先，在Activity里获取context
                         file = context.getFilesDir();
                       String DATABASE_PATH9 = file.getAbsolutePath();// 此处返回的路劲为/data/data/包/files，其中的包就是我们建立的主Activity所在的包 我们可以看到这个路径也是在data文件夹下 程序本身是可以对自己的私有文件进行操作 程序中很多私有的数据会写入到私有文件路径下，这也是android为什么对data数据做保护的原因之一
                        Toast.makeText(MainActivity.this, "开始还原", Toast.LENGTH_SHORT).show();

                         pb = new ProcessBuilder("/system/bin/sh");
                        pb.directory(new File("/"));// 设置shell的当前目录。
                        try {
                             databaseFilename = DATABASE_PATH9 + "/" + DATABASE_FILENAME2;
                            File dir = new File(DATABASE_PATH9);
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
                                databaseFilename = DATABASE_PATH9 + "/" + DATABASE_FILENAME6;
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
                            out.println("ndc resolver flushdefaultif");
                            out.println("ndc resolver flushif wlan0");

                            new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("恢复完成").setMessage("Android的原版hosts已经成功应用到您的系统中，感谢您的支持！！！").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            }).show();
                            version.setText("未应用或系统默认hosts文件");
                            version.setBackgroundColor(Color.parseColor("#ffff0000"));
                            versiontitle.setBackgroundColor(Color.parseColor("#ffff0000"));
                        } catch (Exception e) {
                            System.out.println("exception:" + e);
                            new AlertDialog.Builder(MainActivity.this).setTitle("错误报告").setMessage("无法复制！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                }
                            }).show();
                        }
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setTitle("重启");

                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                        builder.setNeutralButton("热重启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Context context = MainActivity.this;// 首先，在Activity里获取context
                                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                pb.directory(new File("/"));// 设置shell的当前目录。
                                try {
                                    Process proc = pb.start();
                                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                                    out.println("su");

                                    out.println("killall system_server");

                                    Toast.makeText(MainActivity.this, "热重启！", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                }




                            }
                        });
                        builder.setNegativeButton("正常重启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Context context = MainActivity.this;// 首先，在Activity里获取context
                                ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
                                pb.directory(new File("/"));// 设置shell的当前目录。
                                try {
                                    Process proc = pb.start();
                                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                                    out.println("su");

                                    out.println("reboot");

                                    Toast.makeText(MainActivity.this, "正常重启！", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                }

                            }
                        });
                        builder.create().show();
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 4:
                        final File box1 = new File(Busybox_xbin);
                        if (!box1.exists()) {
                            builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setTitle("安装Busybox");
                            builder.setMessage("未检测到系统中有Busybox，按下“安装”将尝试为您安装软件内置的Busybox。");
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder.setNeutralButton("安装", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

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
                                        out.println("busybox --install /system/xbin");
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
                                        out.println("busybox --install /system/xbin");
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
                                        new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装失败").setMessage("建议用户自行手动安装Busybox。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialoginterface, int i) {
                                            }
                                        }).show();
                                    } else if (box1.exists()) {
                                        new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装成功").setMessage("软件内置的Busybox已安装成功。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
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
                            });

                            builder.create().show();
                        }
                        else{
                            builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setTitle("安装Busybox");
                            builder.setMessage("检测到系统中有Busybox，按下“安装”将尝试为您安装软件内置的Busybox。");
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                            builder.setNeutralButton("安装", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

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
                                        out.println("busybox --install /system/xbin");
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
                                        out.println("busybox --install /system/xbin");
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
                                        new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装失败").setMessage("建议用户自行手动安装Busybox。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialoginterface, int i) {
                                            }
                                        }).show();
                                    } else if (box1.exists()) {
                                        new AlertDialog.Builder(MainActivity.this).setCancelable(false).setTitle("尝试安装成功").setMessage("软件内置的Busybox已安装成功。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
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
                            });

                            builder.create().show();
                        }

                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 5:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://t.cn/Rv7Rr1c"));
                        startActivity(browserIntent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 6:
                        new AlertDialog.Builder(MainActivity.this)
                                .setCancelable(false)
                                .setTitle("关于")
                                .setMessage(
                                        "版本：V5.01 \n" +
                                                "作者：lack006\n\n" +

                                                //     "重定向hosts文件来自：Imouto.host(已授权) \n\n" +
                                                "软件更新日志：\n" +
                                                "V5.01：修复V5.00两个版本冲突域名无法应用的问题。\n"+
                                                "V5.00：更新软件内置Busybox版本为1.23.2。\n" +
                                                "V5.00 Beta：修改界面，增加功能，自动刷新Dns缓存，新增独立应用去广告模式，" +
                                                "增加侧滑栏，包含：刷新Dns缓存、备份\\还原hosts文件、重启设备、安装软件内置Busybox等功能。\n" +
                                                "更新软件内置Busybox版本为1.23.1。\n" +
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
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 7:
                         browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/lack006"));
                        startActivity(browserIntent);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                    case 8:
                        System.exit(0);
                        mDrawerLayout.closeDrawer(mDrawerList);
                        break;
                }

            }
        });
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
                        int count;
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
                        String apkUrl = "https://raw.githubusercontent.com/lack006/Android-Hosts-L/master/checkversion";
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
                version.setBackgroundColor(Color.parseColor("#FF000AFF"));
                versiontitle.setBackgroundColor(Color.parseColor("#FF000AFF"));
                break;
            }
        } catch (Exception e) {
        }





        final Button mButton1 = (Button) findViewById(R.id.button2);
        mButton1.setOnClickListener(new View.OnClickListener() {
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
                                if (i == 3) {
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
                                String apkUrl = "https://raw.githubusercontent.com/lack006/Android-Hosts-L/master/hosts4.txt";
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
                                            in.close();
                                            out.close();
                                            break;
                                        }
                                    }
                                    if (check == 0) {
         //                                if (whitch == 1) {
                                            if (umeng == 1) {
                                                String temp = "\n#umeng start" + "\n127.0.0.1 au.umeng.co\n" + "127.0.0.1  au.umeng.com\n" + "#umeng end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                Log.i("handler", "1111111111111111111111111111111111111111111111111111" +
                                                                String.valueOf(umeng) + temp

                                                );
                                                fos.close();//流要及时关闭
                                            }
                                            if (mybus == 1) {
                                                String temp = "\n#mybus start" + "\n127.0.0.1 0591.mygolbs.com\n" + "#mybus end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                Log.i("handler",
                                                        String.valueOf(mybus) + temp

                                                );
                                                fos.close();//流要及时关闭
                                            }
                                            if (jiajia == 1) {
                                                String temp = "\n#jiajia start" + "\n127.0.0.1 api.mofang.com\n" + "127.0.0.1  spapi.i-moible.co.jp\n" + "#jiajia end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                fos.close();//流要及时关闭

                                            }
                                        }
          //                          }
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
        final Button mButton3 = (Button) findViewById(R.id.button3);
        mButton3.setOnClickListener(new View.OnClickListener() {
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



        final Button mButton2 = (Button) findViewById(R.id.button4);
        mButton2.setOnClickListener(new View.OnClickListener() {
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
                whitch = 4;
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
          //                              if (whitch == 1) {
                                            if (umeng == 1) {
                                                String temp = "\n#umeng start" + "\n127.0.0.1 au.umeng.co\n" + "127.0.0.1  au.umeng.com\n" + "#umeng end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                fos.close();//流要及时关闭
                                            }
                                            if (mybus == 1) {
                                                String temp = "\n#mybus start" + "\n127.0.0.1 0591.mygolbs.com\n" + "#mybus end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                fos.close();//流要及时关闭
                                            }
                                            if (jiajia == 1){
                                                String temp = "\n#jiajia start" + "\n127.0.0.1 api.mofang.com\n" +  "127.0.0.1  spapi.i-moible.co.jp\n" +"#jiajia end";
                                                FileOutputStream fos = new FileOutputStream(DATABASE_PATH + "/hosts", true);//true表示在文件末尾追加
                                                fos.write(temp.getBytes());
                                                fos.close();//流要及时关闭

                                            }
                                        }
              //                      }
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






    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList))
            mDrawerLayout.closeDrawer(mDrawerList);
        else
            exit();
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
                                    Pattern p,q,r;
                                    p = Pattern.compile("去广告 \\d{4}-\\d{2}-\\d{2}");
                                    q = Pattern.compile("重定向 \\d{4}-\\d{2}-\\d{2}");
                                    r = Pattern.compile("去广告加重定向 \\d{4}-\\d{2}-\\d{2}");

                                    Matcher m,n,o;
                                    m = p.matcher(new String(b));// 获得匹配



                                    while (m.find()) { // 注意这里，是while不是if

                                        String xxx = m.group();
                                        ADversion.setText(xxx+"\n（轻触以更新）");


                                            break;
                                    }
                                    n = q.matcher(new String(b));// 获得匹配
                                    while (n.find()) { // 注意这里，是while不是if

                                        String xxx = n.group();
                                        REversion.setText(xxx+"\n（轻触以更新）");


                                        break;
                                    }
                                    o = r.matcher(new String(b));// 获得匹配
                                    while (o.find()) { // 注意这里，是while不是if

                                        String xxx = o.group();
                                        AD_REversion.setText(xxx+"\n（轻触以更新）");


                                        break;
                                    }

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
                                 //   out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /data/hosts");
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
                                    out.println("ndc resolver flushdefaultif");
                                    out.println("ndc resolver flushif wlan0");
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
                         //           out.println("\\cp /data/data/com.lack006.hosts_l/files/jttqbcsmwxhxy /system/etc/hosts");
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
                                    out.println("ndc resolver flushdefaultif");
                                    out.println("ndc resolver flushif wlan0");
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
                                                                    version.setBackgroundColor(Color.parseColor("#FF000AFF"));
                                                                    versiontitle.setBackgroundColor(Color.parseColor("#FF000AFF"));

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
                                                                                "Android Hosts-L（去广告）已经成功应用到您的系统中，感谢您的支持！！！\n\n" +
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
                                                                                        version.setBackgroundColor(Color.parseColor("#FFFF0000"));
                                                                                        versiontitle.setBackgroundColor(Color.parseColor("#ffff0000"));


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
                                                                    version.setBackgroundColor(Color.parseColor("#FF000AFF"));
                                                                    versiontitle.setBackgroundColor(Color.parseColor("#FF000AFF"));
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
                                                                                "Android Hosts-L（重定向）已经成功应用到您的系统中，感谢您的支持！！！")
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
                                                                                        version.setBackgroundColor(Color.parseColor("#ffff0000"));
                                                                                        versiontitle.setBackgroundColor(Color.parseColor("#ffff0000"));
                                                                                    }
                                                                                }
                                                                        ).show();


                                                            }


                                                        }
                                                    }
                                            ).show();


                                }

                                if (whitch == 4) {
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
                                                                    version.setBackgroundColor(Color.parseColor("#FF000AFF"));
                                                                    versiontitle.setBackgroundColor(Color.parseColor("#FF000AFF"));

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
                                                                                "Android Hosts-L（重定向、去广告）已经成功应用到您的系统中，感谢您的支持！！！\n\n" +
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
                                                                                        version.setBackgroundColor(Color.parseColor("#ffff0000"));
                                                                                        versiontitle.setBackgroundColor(Color.parseColor("#ffff0000"));


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






}
