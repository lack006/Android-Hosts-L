package com.lack006.hosts_l;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.lack006.hosts_l.About.AboutHelper;
import com.lack006.hosts_l.About.MarketHelper;
import com.lack006.hosts_l.Backup.BackupHostsTaskHelper;
import com.lack006.hosts_l.Backup.RestoreHostsTaskHelper;
import com.lack006.hosts_l.BatteryOptimizations.IsIgnoringBatteryOptimizations;
import com.lack006.hosts_l.ChangeIp.ChangeIpTaskHelper;
import com.lack006.hosts_l.CheckVersion.CheckRootTaskHelper;
import com.lack006.hosts_l.CheckVersion.CheckVersionTaskHelper;
import com.lack006.hosts_l.CleanCache.CleanCacheHelper;
import com.lack006.hosts_l.Consistent.Consistent;
import com.lack006.hosts_l.DNS.ChangeDNSTaskHelper;
import com.lack006.hosts_l.Dialog.HostsChooseHelper;
import com.lack006.hosts_l.Download.DownloadClashHelper;
import com.lack006.hosts_l.GoogleLocation.GoogleLocationReportTaskHelper;
import com.lack006.hosts_l.Helper.ObjectAnimatorHelper;
import com.lack006.hosts_l.ProtectMode.ProtectModeHelper;
import com.lack006.hosts_l.Reboot.RebootHelper;
import com.lack006.hosts_l.RemoveMark.RemoveMarkTaskHelper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Context mContext = null;
    private Toolbar mToolbar = null;
    private FloatingActionButton mRefreshBtn = null;
    private Snackbar mSnackBar = null;
    private DrawerLayout mDrawerLayout = null;
    private CoordinatorLayout mCoordinatorLayout = null;

    private Map<String, CheckBox> mCheckBoxMap = null;

    private Button mButtonAD = null;
    private Button mButtonRE = null;
    private Button mButtonAR = null;

    private CheckBox mCheckBoxHosts = null;
    private CheckBox mCheckBoxVersion = null;
    private CheckBox mCheckBoxBackUp = null;
    private CheckBox mCheckBoxBusybox = null;
    private CheckBox mCheckBoxMark = null;
    private CheckBox mCheckBoxLocal = null;
    private CheckBox mCheckBoxDNS = null;
    private CheckBox mCheckBoxProtect = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanCacheHelper cleanCacheHelper = new CleanCacheHelper();
        cleanCacheHelper.cleanCache(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        showView();
        showDrawer();
        CheckRootTaskHelper checkRoot = new CheckRootTaskHelper();
        checkRoot.checkRoot(mContext, mSnackBar, mCoordinatorLayout, mCheckBoxMap, mDrawerLayout);


    }

    private void showView() {
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);


        mRefreshBtn = findViewById(R.id.refresh);
        mRefreshBtn.setOnClickListener(this);

        mButtonAD = findViewById(R.id.button_ad);
        mButtonRE = findViewById(R.id.button_re);
        mButtonAR = findViewById(R.id.button_ar);

        mButtonAD.setOnClickListener(this);
        mButtonRE.setOnClickListener(this);
        mButtonAR.setOnClickListener(this);

        mCheckBoxHosts = findViewById(R.id.checkBox_hosts);
        mCheckBoxHosts.setClickable(false);
        mCheckBoxVersion = findViewById(R.id.checkBox_version);
        mCheckBoxVersion.setClickable(false);
        mCheckBoxBackUp = findViewById(R.id.checkBox_backup);
        mCheckBoxBackUp.setClickable(false);
        mCheckBoxBusybox = findViewById(R.id.checkBox_busybox);
        mCheckBoxBusybox.setClickable(false);
        mCheckBoxMark = findViewById(R.id.checkBox_mark);
        mCheckBoxMark.setClickable(false);
        mCheckBoxLocal = findViewById(R.id.checkBox_local);
        mCheckBoxLocal.setClickable(false);
        mCheckBoxDNS = findViewById(R.id.checkBox_dns);
        mCheckBoxDNS.setClickable(false);
        mCheckBoxProtect = findViewById(R.id.checkBox_protect);
        mCheckBoxProtect.setClickable(false);

        mCheckBoxMap = new HashMap<>();
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_HOSTS, mCheckBoxHosts);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_VERSION, mCheckBoxVersion);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_BACKUP, mCheckBoxBackUp);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_BUSYBOX, mCheckBoxBusybox);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_MARK, mCheckBoxMark);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_LOCAL, mCheckBoxLocal);
        mCheckBoxMap.put(Consistent.KEY_CHECKBOX_DNS, mCheckBoxDNS);
        mCheckBoxMap.put(Consistent.KEY_PROTECT_MODE, mCheckBoxProtect);


    }

    private void showDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.finish();

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_backup) {
            BackupHostsTaskHelper backupHostsTaskHelper = new BackupHostsTaskHelper();
            backupHostsTaskHelper.backupHostsHelper(mContext, mCheckBoxBackUp, mCheckBoxHosts);

        } else if (id == R.id.nav_rotate) {
            RestoreHostsTaskHelper restoreHostsTaskHelper = new RestoreHostsTaskHelper();
            restoreHostsTaskHelper.restoreHosts(mContext, mCheckBoxHosts);

        } else if (id == R.id.nav_auto_change_dns) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IsIgnoringBatteryOptimizations isIgnoringBatteryOptimizations = new IsIgnoringBatteryOptimizations();
                if (isIgnoringBatteryOptimizations.isIgnoringBatteryOptimizations(mContext, Consistent.TYPE_DNS)) {
                    ChangeDNSTaskHelper changeDNSTaskHelper = new ChangeDNSTaskHelper();
                    changeDNSTaskHelper.changeDNSHelper(mContext, mCheckBoxDNS);
                }
            } else {
                ChangeDNSTaskHelper changeDNSTaskHelper = new ChangeDNSTaskHelper();
                changeDNSTaskHelper.changeDNSHelper(mContext, mCheckBoxDNS);
            }

        } else if (id == R.id.nav_remove_mark) {
            RemoveMarkTaskHelper removeMarkTaskHelper = new RemoveMarkTaskHelper();
            removeMarkTaskHelper.removeMark(mContext, mCheckBoxMark);

        } else if (id == R.id.nav_enable_google_location) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IsIgnoringBatteryOptimizations isIgnoringBatteryOptimizations = new IsIgnoringBatteryOptimizations();
                if (isIgnoringBatteryOptimizations.isIgnoringBatteryOptimizations(mContext, Consistent.TYPE_GOOGLE_LOCAL)) {
                    requestPhonePermission();
                }
            } else {
                requestPhonePermission();
            }


        } else if (id == R.id.nav_change_ip) {
            ChangeIpTaskHelper changeIpTaskHelper = new ChangeIpTaskHelper();
            changeIpTaskHelper.changeIpHelper(mContext);

        } else if (id == R.id.nav_reboot) {
            RebootHelper rebootHelper = new RebootHelper();
            rebootHelper.reboot(mContext);
        } else if (id == R.id.nav_log) {
            AboutHelper aboutHelper = new AboutHelper();
            aboutHelper.about(mContext);

        } else if (id == R.id.nav_update) {
            MarketHelper marketHelper = new MarketHelper();
            marketHelper.openApplicationMarket(mContext);

        } else if (id == R.id.nav_exit) {
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == mButtonAD) {
            DownloadClashHelper downloadClashHelper = new DownloadClashHelper();
            downloadClashHelper.downloadClash(mContext, mCheckBoxHosts, false);

        } else if (view == mButtonRE) {
            HostsChooseHelper hostsChooseHelper = new HostsChooseHelper();
            hostsChooseHelper.reHostsChoose(mContext, mCheckBoxHosts);

        } else if (view == mButtonAR) {
            DownloadClashHelper downloadClashHelper = new DownloadClashHelper();
            downloadClashHelper.downloadClash(mContext, mCheckBoxHosts, true);

        } else if (view == mRefreshBtn) {
            CleanCacheHelper cacheHelper = new CleanCacheHelper();
            cacheHelper.cleanVersion(mContext);
            ObjectAnimatorHelper.ShowView(view, Consistent.DURATION_TIME, Consistent.ONE_TIME);
            CheckVersionTaskHelper checkVersionTaskHelper = new CheckVersionTaskHelper();
            checkVersionTaskHelper.checkVersion(mContext, mSnackBar, mCoordinatorLayout, mCheckBoxMap, mDrawerLayout);
        }
    }


    private void requestPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, Consistent.PHONE_PERMISSIONS_REQUEST);
        } else {
            GoogleLocationReportTaskHelper googleLocationReportTaskHelper = new GoogleLocationReportTaskHelper();
            googleLocationReportTaskHelper.enableChoose(mContext, mCheckBoxLocal);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Consistent.PHONE_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0) || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.no_phone_permission, Toast.LENGTH_LONG).show();
                } else {
                    GoogleLocationReportTaskHelper googleLocationReportTaskHelper = new GoogleLocationReportTaskHelper();
                    googleLocationReportTaskHelper.enableChoose(mContext, mCheckBoxLocal);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.help_title);
            LayoutInflater factory = LayoutInflater.from(mContext);
            View layout = factory.inflate(R.layout.about_dialog_layout, null);

            TextView text = layout.findViewById(R.id.text);
            text.setMovementMethod(LinkMovementMethod.getInstance());
            text.setText(R.string.help_msg);
            builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setView(layout);
            builder.show();
            return true;
        } else if (id == R.id.action_protect) {
            ProtectModeHelper protectModeHelper = new ProtectModeHelper();
            protectModeHelper.protectModeDialog(mContext, mCheckBoxProtect);
        }


        return super.onOptionsItemSelected(item);
    }

}
