package com.tm.timemanager.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tm.timemanager.application.MyApplication;
import com.tm.timemanager.dao.DBOpenHelperdao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by snow on 2016/4/19.
 */
//广播接收者  当收到屏幕加锁 解锁 的广播的时候执行
public class InfoReceive extends BroadcastReceiver {

    private DBOpenHelperdao db;
    private DateFormat dateFormatday;
    private String yearmouthday;
    private long time;
    private long starttime;
    private long endtime;
    private long totaltime;
    private String action;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DBOpenHelperdao(context);
        dateFormatday = new SimpleDateFormat("yyyyMMdd");
        time = new Date().getTime();
        action = intent.getAction();
        yearmouthday =dateFormatday.format(time);
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("哈哈", "屏幕解锁广播...");
            db.insertappevent(yearmouthday,time,1,0);
            starttime=time;
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("哈哈", "屏幕加锁广播...");
            MyApplication.unlocktime=0;
            db.insertappevent(yearmouthday,time,0,0);
            endtime=time;
            totaltime=endtime-starttime;
            if (starttime!=0){
                db.insertappevent(yearmouthday,totaltime,0,1);

            }

        }
    }
}
