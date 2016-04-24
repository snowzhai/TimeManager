package com.tm.timemanager.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class MyApplication extends Application {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor setedit;
    public static int unlocktime;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("appsettime", MODE_PRIVATE);
        setedit = sp.edit();
    }

    public static int getPhoneWidth(Context context) {
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowmanager.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getPhoneHeight(Context context) {
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = windowmanager.getDefaultDisplay().getHeight();
        return height;
    }

    // 计时类型：
    // “正常”：settime默认为int值-1
    // “目标”：settime传一个用户自定义的时间值
    // “忽略”：settime传一个int值-2

    //zhai的数据库
    public static void setapptime(String packname, int settime) {
        //为软件计时 设计的SharedPreferences数据库  appsettime  文件为.xml文件
//        Log.i("啊啊啊",packname+settime);
        setedit.putInt(packname, settime);       //将包名传入  放入设置的时间数据
        setedit.commit();
    }

    //传入一个包名 得到它在SharedPreferences 中设置的时间
    public static int gettime(String packname) {
        int defauletime = sp.getInt(packname, -1);
        return defauletime;
    }
    //为通知栏设置的检测是开还是关的逻辑
    public static void setapptime(String packname, boolean a) {
        //为软件计时 设计的SharedPreferences数据库  appsettime  文件为.xml文件
//        Log.i("啊啊啊",packname+settime);
        setedit.putBoolean(packname, a);       //将包名传入  放入设置的时间数据
        setedit.commit();
    }

    public static boolean gettime(String packname,boolean a) {
        boolean aBoolean = sp.getBoolean(packname, a);
        return aBoolean;
    }
}
