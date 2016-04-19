package com.tm.timemanager.zhai;

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
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

   public static List<UsageStats> bt_test_getmessage11(Context context){
       UsageStatsManager usm = getUsageStatsManager(context);
       Calendar calendar = Calendar.getInstance();
       long endTime = calendar.getTimeInMillis();
       calendar.add(Calendar.YEAR, -1);
       long startTime = calendar.getTimeInMillis();

       DateFormat dateFormat = new SimpleDateFormat("EE-MM-dd-yyyy");
       Log.d("哈哈", "Range start:" + dateFormat.format(startTime) );
       Log.d("哈哈", "Range end:" + dateFormat.format(endTime));
       List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
       return usageStatsList;
   }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
    public static void printUsageStats(List<UsageStats> usageStatsList) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        for (UsageStats u : usageStatsList) {
            Log.d("啊哈哈", "Pkg: " + u.getPackageName() + "\t     " + "ForegroundTime: "
                    + DateUtils.formatElapsedTime(u.getTotalTimeInForeground() / 1000) + "   lasttimeuser:" + DateUtils.formatSameDayTime(u.getLastTimeUsed(),
                    System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM)
                    + "times" + u.getClass().getDeclaredField("mLaunchCount").getInt(u));
        }
    }
}
