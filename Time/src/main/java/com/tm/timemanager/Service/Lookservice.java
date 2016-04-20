package com.tm.timemanager.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tm.timemanager.dao.DBOpenHelperdao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhai on 2016/4/19.
 */
public class Lookservice extends Service {
    private ActivityManager ams;
    private String packagename = "com.android.zhai";
    private ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
    private List<ActivityManager.RunningAppProcessInfo> runningServices;
    private String runningname = "1";
    private long starttime;
    private int runningtime;
    private DateFormat dateFormatday;
    private String yearmouthday;
    private String todayhours;
    private PackageManager packageManager;
    private Drawable icon;
    private ApplicationInfo applicationInfo;
    private String appname;
    private DBOpenHelperdao dao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dateFormatday = new SimpleDateFormat("yyyyMMdd");
        //得到数据库的操作助手
        dao = new DBOpenHelperdao(getApplication());
        starttime=0;
        runningtime=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //发送广播
                   /* Intent intent = new Intent();
                    intent.setAction("com.tm.timemanager.changeinfo");*/
                    //得到一个运行APP的管理者
                    runningServices = ams.getRunningAppProcesses();
                    //得到一个运行包的管理者
                    packageManager = getApplication().getPackageManager();
                    //得到最近刚打开的应用
                    runningAppProcessInfo = runningServices.get(0);
                    //得到它的名字
                    packagename = runningAppProcessInfo.processName;
                    //如果正在运行的现在将要运行的不是同一个就进来
                    if (!runningname.equals(packagename)) {

                        //得到这个名字的信息  从这里面拿icon appname
                        try {
                            applicationInfo = packageManager.getApplicationInfo(packagename, 0);
                            icon = packageManager.getApplicationIcon(packagename);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        starttime = new Date().getTime();               //开始的时间
                        yearmouthday =dateFormatday.format(starttime);//得到这个时间的日期
                        todayhours = String.valueOf(new Date().getHours());//得到这个时间的所属小时
                        appname = packagename;                      //防止没有appname
                        appname = (String)applicationInfo.loadLabel(packageManager);

//                    Log.i("哈哈", packagename +"---"+appname +"----" + runningtime + "---" + starttime + "---" + yearmouthday + "---" + todayhours);

                        //如果runningname不为空的话就进来
                        if (!runningname.equals("1")) {
                            Log.i("哈哈", packagename + "--" + appname + "--"+"上个应用运行时间："+runningtime + "--"+"开始时间："+ starttime + "---" + yearmouthday + "---" + todayhours);
                            dao.insertBlackNumber(yearmouthday,packagename,appname,starttime,runningtime,1);
                        }

                        //如果应用是系统的应用就不计时
                        if (!packagename.startsWith("com.android.")) {
                            runningtime = 0;
                        }
                        runningname = packagename;
                    }
                    //如果正在运行的和将要运行的是同一个就计时
                    if (runningname.equals(packagename)) {
                        runningtime = runningtime + 1;

                    }


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


}
