package com.tm.timemanager.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.application.MyApplication;
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
    private String runpackagename = "com.android.zhai";
    private ActivityManager.RunningAppProcessInfo runningAppProcessInfo;
    private List<ActivityManager.RunningAppProcessInfo> runningServices;
    private String beforpackagename = "1";
    private long starttime;
    private int runningtime;
    private int forheadtime;
    private DateFormat dateFormatday;
    private String yearmouthday;
    private String todayhours;
    private PackageManager packageManager;
    private Drawable icon;
    private ApplicationInfo applicationInfo;
    private String appname;
    private String apptotalname;
    private ApplicationInfo toasinfo;
    private DBOpenHelperdao dao;
    private SimpleDateFormat hourmin;
    private int gettime;
    private Notification notification;
    private NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        boolean isRegisterReceiver = false;
        dateFormatday = new SimpleDateFormat("yyyyMMdd");
        hourmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dao = new DBOpenHelperdao(getApplication());        //得到数据库的操作助手

        //注册广播接收者 用于接收加锁解锁的广播
        registerbrocad(isRegisterReceiver);

        new Thread(new Runnable() {

            private String pkgName;
            private Cursor getapptotal;

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
                    //得到最近刚打开的应用 最初的
                    runningAppProcessInfo = runningServices.get(0);
                    runpackagename = runningAppProcessInfo.processName;
                    //得到它的名字
                    //如果正在运行的现在将要运行的不是同一个就进来  runningname正在运行   packagename马上要打开
                    if (!beforpackagename.equals(runpackagename) && !beforpackagename.equals("1")) {

//                        Log.i("哈哈1",runpackagename+"--"+beforpackagename);
                        //得到这个名字的信息  从这里面拿icon appname
                        try {
                            applicationInfo = packageManager.getApplicationInfo(beforpackagename, 0);

                            icon = packageManager.getApplicationIcon(beforpackagename);

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        gettime = MyApplication.gettime(beforpackagename);//得到给软件设置的时间
                        appname = beforpackagename;                      //防止没有appname
                        appname = (String) applicationInfo.loadLabel(packageManager);
//                    Log.i("哈哈", packagename +"---"+appname +"----" + runningtime + "---" + starttime + "---" + yearmouthday + "---" + todayhours);
                        //如果runningname不为空的话就进来
                        if (!beforpackagename.equals("1")) {
                            Log.i("哈哈", beforpackagename + "--" + appname + "--" + "应用运行时间：" + runningtime + "--" + forheadtime + "--" + "开始时间：" + hourmin.format(starttime) + "---" + yearmouthday + "---" + todayhours);
                            //如果包名不是系统的应用  而且时间不为0的时候 就记录下来
                            if (!beforpackagename.startsWith("com.android.") && starttime != 0 && beforpackagename.indexOf("launcher") == -1) {
                                Log.i("哈哈数据库", beforpackagename + "--" + appname + "--" + "应用运行时间：" + runningtime + "--" + "开始时间：" + hourmin.format(starttime) + "---" + yearmouthday + "---" + todayhours);
                                dao.insertappdaily(yearmouthday, beforpackagename, appname, starttime, runningtime, 1);
                                //查询数据库
                                getapptotal = dao.getapptotalhava(beforpackagename);
                                //如果总的数据库中没有的话就加入  判断是否为空的方法是 Cursor.getCount()这么一个简单的函数，如果是0，表示Cursor为空；如果非0，则表示Cursor不为空。
                                if (getapptotal.getCount() == 0&&gettime!=-2) {
                                    dao.insertapptotal(beforpackagename, appname, runningtime, 1, icon);
                                } else if (getapptotal.getCount()!=0&&gettime!=-2){
                                    Log.i("哈哈哈", appname + "--" + runningtime);
                                    //如果总的数据库中有的话  就将使用时间  使用次数 在原来的基础上添加到里面
                                    dao.updatetotal(appname, runningtime, 1);

                                    //测试数据库操作函数是否正确
                                    Cursor getapptotal = dao.getapptotal();
                                    Cursor getappdaily = dao.getappdaily("20160421");
                                    Cursor getappdaily1 = dao.getappdaily();
                                    Cursor getappevent = dao.getappevent();
                                    Cursor getappevent1 = dao.getappevent("20160421");
                                    long getappeventtotalday = dao.getappeventtotalday("20160421");
                                    int count3 = getappevent.getCount();
                                    int count4 = getappevent1.getCount();
                                    int count2 = getappdaily1.getCount();
                                    int count1 = getappdaily.getCount();
                                    int count = getapptotal.getCount();
                                    Log.i("哈哈", count + "--" + count1 + "--" + count2 + "解锁所有-" + count3 + "-每天-" + count4 + "总的解锁时间" + getappeventtotalday);
                                }
                            }
                        }

                        starttime = new Date().getTime();               //开始的时间
                        yearmouthday = dateFormatday.format(starttime);//得到这个时间的日期
                        todayhours = String.valueOf(new Date().getHours());//得到这个时间的所属小时
                        //如果应用是系统的应用就不计时
                        if (!runpackagename.startsWith("com.android.")) {
                            runningtime = 0;
                        }
                        beforpackagename = runpackagename;
                    } else {
                        beforpackagename = runpackagename;
                    }
                    //如果正在运行的和将要运行的是同一个就计时
                    if (beforpackagename.equals(runpackagename)) {
                        runningtime = runningtime + 1;
                    }

                    //给软件计时的逻辑  如果数据库中有这个当前包名的软件 就有时间的减少的逻辑
                    gettime = MyApplication.gettime(beforpackagename);//得到给软件设置的时间
                    if (-1 != gettime) {
                        MyApplication.setapptime(beforpackagename, gettime - 1);
                        Log.i("我靠，你使用" + beforpackagename, gettime + "");
                        if (gettime == 0) {
                            MyApplication.setapptime(beforpackagename, -1);//如果计时的时间为0 则证明计时完毕 让它计时的时间为-1
                            try {
                                toasinfo = packageManager.getApplicationInfo(runpackagename, 0);
                                apptotalname = (String) toasinfo.loadLabel(packageManager);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            toast(apptotalname);
                            manager.notify(1, notification);
//                            Toast.makeText(getApplication(),)
                            Log.i("我靠，你使用" + beforpackagename, "到时间了");
                        }
                    }
                    MyApplication.unlocktime += 1;
                    //给通知栏刷新
                    sendBroadcast(new Intent("com.tm.timemanager.refresh"));
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
    //注册广播接收者
    private void registerbrocad(boolean isRegisterReceiver) {
        if (!isRegisterReceiver) {
            isRegisterReceiver = true;
            InfoReceive infoReceive = new InfoReceive();
            IntentFilter filter = new IntentFilter();//过滤器
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            Log.i("哈哈", "注册屏幕解锁、加锁广播接收者...");
            registerReceiver(infoReceive, filter);
        }
    }
    public void toast(String appname){
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.man, appname, System.currentTimeMillis());
        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent p1 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setLatestEventInfo(this,appname,"使用时间到了！",p1);
        long []a={0,1000,1000,1000};//控制手机振动
        notification.vibrate=a;

        //控制手机的LED灯闪烁
        notification.ledARGB= Color.RED;
        notification.ledOnMS=1000;
        notification.ledOffMS=1000;
        notification.flags=Notification.FLAG_SHOW_LIGHTS;
    }
    //给通知栏刷新
   /* public void sedbrocast(){
        Intent intent = new Intent();
        intent.setAction("com.tm.timemanager.refresh");
        sendStickyBroadcast(intent);
    }*/
}
