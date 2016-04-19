package com.tm.timemanager.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
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
    private String packagename="com.android.zhai";
    private ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
    private List<ActivityManager.RunningAppProcessInfo> runningServices;
    private String runningname="1";
    private long starttime, runningtime;
    private DateFormat dateFormatday;
    private String yearmouthday;
    private String todayhours;
    private PackageManager packageManager;
    private Drawable icon;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        dateFormatday = new SimpleDateFormat("yyyyMMdd");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    //得到一个一个运行APP的管理者
                    runningServices = ams.getRunningAppProcesses();
                    packageManager = getApplication().getPackageManager();
                    //得到最近刚打开的应用
                    runningAppProcessInfo = runningServices.get(0);
                    //得到它的名字
                    packagename = runningAppProcessInfo.processName;
                    Intent intent = new Intent();
                    intent.setAction("com.tm.timemanager.changeinfo");

                    DBOpenHelperdao dao = new DBOpenHelperdao(getApplication());//得到数据库的操作助手
                    for (int i=0;i<10;i++){
                        dao.insertBlackNumber("haha",1111,1111,i);

                    }

                    if (!runningname.equals(packagename)){//如果正在运行的现在将要运行的不是同一个就进来

                        if (!runningname.equals("1")){//如果runningname不为“1”的话说明之前有·
                            if (!packagename.startsWith("com.android.")){
                                Log.i("哈哈哈哈哈哈", packagename+"----"+runningtime+"---"+starttime+"---"+yearmouthday +"---"+todayhours);
                            }

                        }
                        if (!packagename.startsWith("com.android."))//如果应用是系统的应用就不计时
                        {
                            runningname = packagename;
                            runningtime = 0;
                        }
                    }
                    if (runningname.equals(packagename)) {//如果正在运行的和将要运行的是同一个就计时
                        runningtime = runningtime + 1;
                        try {
                            icon = packageManager.getApplicationIcon(packagename);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        starttime = new Date().getTime();//开始的时间
                        yearmouthday = dateFormatday.format(starttime);
                        todayhours = String.valueOf(new Date().getHours());
                        Log.i("哈哈", packagename+"----"+runningtime+"---"+starttime+"---"+yearmouthday +"---"+todayhours);
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
