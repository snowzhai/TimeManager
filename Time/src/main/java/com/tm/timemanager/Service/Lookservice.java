package com.tm.timemanager.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private DBOpenHelperdao dao;
    private SimpleDateFormat hourmin;
    private int gettime;
    private String currentApp;

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
        if (!isRegisterReceiver) {
            isRegisterReceiver = true;
            InfoReceive infoReceive = new InfoReceive();
            IntentFilter filter = new IntentFilter();//过滤器
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            Log.i("哈哈", "注册屏幕解锁、加锁广播接收者...");
            registerReceiver(infoReceive, filter);
        }

        new Thread(new Runnable() {

            private String pkgName;
            private Cursor getapptotal;
            String currentApp = "1";

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
                   /* runpackagename="1";
                    Log.i("哈哈1",runpackagename);*/

                    /*List<ActivityManager.RunningTaskInfo> runningTasks = ams.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    runningTaskInfo.g
                    UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");*/


               /*     final List<ActivityManager.RunningTaskInfo> taskInfo = ams.getRunningTasks(1);
                    final ComponentName componentName = taskInfo.get(0).topActivity;
                    final String[] activePackages = new String[1];
                    activePackages[0] = componentName.getPackageName();
                    if (!activePackages[0].equals(null)){
                        Log.i("哈哈2",activePackages[0]);
                    }*/


//                   String name= new DetectCalendarLaunchRunnable();
                   /* if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // intentionally using string value as Context.USAGE_STATS_SERVICE was
                    // strangely only added in API 22 (LOLLIPOP_MR1)
                    @SuppressWarnings("WrongConstant")
                    UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
                    long time = System.currentTimeMillis();
                    List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                    if (appList != null && appList.size() > 0) {
                        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                        }
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {
                            currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                            Log.i("哈哈1",currentApp);
                        }
                    }
                }else {
                    List<ActivityManager.RunningAppProcessInfo> tasks = ams.getRunningAppProcesses();
                    currentApp = tasks.get(1).processName;
                    Log.i("哈哈2",currentApp);
                }
//                beforpackagename=currentApp;
                    Log.i("哈哈3",currentApp);*/


                    /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        // intentionally using string value as Context.USAGE_STATS_SERVICE was
                        // strangely only added in API 22 (LOLLIPOP_MR1)
                        @SuppressWarnings("WrongConstant")
                        UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
                        long time = System.currentTimeMillis();
                        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                time - 1000 * 1000, time);
                        if (appList != null && appList.size() > 0) {
                            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                            for (UsageStats usageStats : appList) {
                                mySortedMap.put(usageStats.getLastTimeUsed(),
                                        usageStats);
                            }
                            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                                currentApp = mySortedMap.get(
                                        mySortedMap.lastKey()).getPackageName();
                            }
                        }
                    } else {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningAppProcessInfo> tasks = am
                                .getRunningAppProcesses();
                        currentApp = tasks.get(0).processName;
                    }
                    Log.i("哈哈4",currentApp);
*/

                  /*  ComponentName componentName2 = ams.getRunningTasks(1).get(0).topActivity;
                    Log.d("哈哈5", "package:"+componentName2.getPackageName());
                    Log.d("哈哈5", "class:"+componentName2.getClassName());*/


//                    List<ActivityManager.RunningServiceInfo> list = new ArrayList<ActivityManager.RunningServiceInfo>();
                 /*   List<ActivityManager.RunningServiceInfo> runningServices = ams.getRunningServices(1);
                    String process = runningServices.get(1).process;
                    if (!process.equals(null)){
                        Log.i("啊哈哈process",process);
                    }*/

                    /*for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()){
                        if(runningAppProcessInfo.pid == pid){
                            return processInfo.processName;
                        }
                    }
*/
                   /* pkgName = ams.getRunningTasks(1).get(0).topActivity.getPackageName();
                    if (!pkgName.equals(null)){
                        Log.i("哈哈7",pkgName);
                    }*/

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
                                if (getapptotal.getCount() == 0) {
                                    dao.insertapptotal(beforpackagename, appname, runningtime, 1, icon);
                                } else {
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
                            Log.i("我靠，你使用" + beforpackagename, "到时间了");
                        }

                    }
                    MyApplication.unlocktime += 1;
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

  /*  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getUsageStatsForegroundActivityName() {


        UsageStatsManager mUsageStatsManager = (UsageStatsManager) MyApplication.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 1000 * 60;

        // result
        String topActivity = null;

        // We get usage stats for the last minute
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);

        // Sort the stats by the last time used
        if (stats != null) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                topActivity = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                Log.i("topActivity: ", topActivity);
            }
        }
        if (topActivity != null)
            return topActivity;
        else
            return geta.ACTIVITY_NOT_FOUND;

    }*/

  /*  public class DetectCalendarLaunchRunnable implements Runnable {

        @Override
        public void run() {
            String[] activePackages;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                activePackages = getActivePackages();
            } else {
                activePackages = getActivePackagesCompat();
            }
            if (activePackages != null) {
                for (String activePackage : activePackages) {
                    if (activePackage.equals("com.google.android.calendar")) {
                        //Calendar app is launched, do something
                    }
                }
            }
//            mHandler.postDelayed(this, 1000);
        }

        String[] getActivePackagesCompat() {
            final List<ActivityManager.RunningTaskInfo> taskInfo = ams.getRunningTasks(1);
            final ComponentName componentName = taskInfo.get(0).topActivity;
            final String[] activePackages = new String[1];
            activePackages[0] = componentName.getPackageName();
            return activePackages;
        }

        String[] getActivePackages() {
            final Set<String> activePackages = new HashSet<String>();
            final List<ActivityManager.RunningAppProcessInfo> processInfos = ams.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    activePackages.addAll(Arrays.asList(processInfo.pkgList));
                }
            }
            return activePackages.toArray(new String[activePackages.size()]);
        }
    }*/
}
