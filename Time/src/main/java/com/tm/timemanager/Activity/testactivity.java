package com.tm.timemanager.Activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;

import com.tm.timemanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    private static UsageStatsManager usm;
    private static long firstTimeStamp;
    private static long lastTimeUsed;
    private static long totalTimeInForeground;
    private static int totalclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            printUsageStats(bt_test_getmessage11(this));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        //判断权限是否打开 如果没打开就跳到打开页面
        if (!isNoSwitch()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

   public static List<UsageStats> bt_test_getmessage11(Context context){
       UsageStatsManager usageStatsManager = getUsageStatsManager(context);//获得检测程序管理者
       Calendar calendar = Calendar.getInstance();//得到日历
       long endTime = calendar.getTimeInMillis();//设定计时的终止的时间
//       calendar.add(Calendar.YEAR, -1);
       calendar.add(Calendar.DAY_OF_MONTH, -1);
       long startTime = calendar.getTimeInMillis();//设定计时的开始的时间

       DateFormat dateFormat = new SimpleDateFormat("EE-MM-dd-yyyy");//设置日期的格式
       Log.d("哈哈", "Range start:" + dateFormat.format(startTime) );
       Log.d("哈哈", "Range end:" + dateFormat.format(endTime));
       List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
       return usageStatsList;
   }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context) {
        usm = (UsageStatsManager) context.getSystemService("usagestats");//得到一个应用数据管理者
        return usm;
    }
    //打印函数
    public static void printUsageStats(List<UsageStats> usageStatsList) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        for (UsageStats u : usageStatsList) {

            String packageName = u.getPackageName();//得到包名
            //得到第一次打开的时间
            firstTimeStamp = u.getFirstTimeStamp();
            //得到最后一次打开的时间
            lastTimeUsed = u.getLastTimeUsed();
            //得到总共的打开的时间 单位s
            totalTimeInForeground = u.getTotalTimeInForeground()/1000;
            //得到总共点击的次数
            totalclick = u.getClass().getDeclaredField("mLaunchCount").getInt(u);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.i("哈哈","packageName---"+packageName+"firstTimeStamp---"+sdf.format(firstTimeStamp)+"lastTimeUsed---"+sdf.format(lastTimeUsed)+"totalTimeInForeground---"+ totalTimeInForeground);
            Log.i("哈哈","---------");
            Log.d("哈哈",
                    "包名: " + u.getPackageName()+"\t" +
                    "第一次使用的时间:" + DateUtils.formatSameDayTime(firstTimeStamp, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM) +
                    "最后一次使用的时间:" + DateUtils.formatSameDayTime(lastTimeUsed, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM)+
                    "打开的总时间："+ totalTimeInForeground +
                    "打开的次数：" + totalclick);
        }
    }

    //判断权限是否打开的函数
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usm.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }
    /* Log.d("啊哈哈", "包名: " + u.getPackageName() + "\t     " + "ForegroundTime: "
                    + DateUtils.formatElapsedTime(u.getTotalTimeInForeground() / 1000) + "   lasttimeuser:" + DateUtils.formatSameDayTime(u.getLastTimeUsed(),
                    System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM)
                    + "打开的次数：" + u.getClass().getDeclaredField("mLaunchCount").getInt(u));*/
/*
    public static void bt_test_getmessage() {
        Log.i("哈哈", "[getPkgUsageStats]");
        try {
            Class<?> cServiceManager = Class.forName("android.os.ServiceManager");
            Method mGetService = cServiceManager.getMethod("getService", java.lang.String.class);
            Object oRemoteService = mGetService.invoke(null, "usagestats");
            // IUsageStats oIUsageStats =
            // IUsageStats.Stub.asInterface(oRemoteService)
            Class<?> cStub = Class.forName("com.android.internal.app.IUsageStats$Stub");
            Method mUsageStatsService = cStub.getMethod("asInterface", android.os.IBinder.class);
            Object oIUsageStats = mUsageStatsService.invoke(null, oRemoteService);

            // PkgUsageStats[] oPkgUsageStatsArray =
            // mUsageStatsService.getAllPkgUsageStats();
            Class<?> cIUsageStatus = Class.forName("com.android.internal.app.IUsageStats");
            Method mGetAllPkgUsageStats = cIUsageStatus.getMethod("getAllPkgUsageStats", (Class[]) null);
            Object[] oPkgUsageStatsArray = (Object[]) mGetAllPkgUsageStats.invoke(oIUsageStats, (Object[]) null);
            Log.i("哈哈", "[getPkgUsageStats] oPkgUsageStatsArray = "+oPkgUsageStatsArray);

            Class<?> cPkgUsageStats = Class.forName("com.android.internal.os.PkgUsageStats");

            StringBuffer sb = new StringBuffer();
            sb.append("nerver used : ");
            for (Object pkgUsageStats : oPkgUsageStatsArray) {
                // get pkgUsageStats.packageName, pkgUsageStats.launchCount,
                // pkgUsageStats.usageTime
                String packageName = (String) cPkgUsageStats.getDeclaredField("packageName").get(pkgUsageStats);
                int launchCount = cPkgUsageStats.getDeclaredField("launchCount").getInt(pkgUsageStats);
                long usageTime = cPkgUsageStats.getDeclaredField("usageTime").getLong(pkgUsageStats);
                if (launchCount > 0)
                    Log.i("哈哈", "[getPkgUsageStats] "+ packageName + "  count: "
                            + launchCount + "  time:  " + usageTime);
                else {
                    sb.append(packageName + "; ");
                }
            }
            Log.i("哈哈", "[getPkgUsageStats] " + sb.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }*/

}
