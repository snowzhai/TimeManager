package com.tm.timemanager.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.WindowManager;

/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class Application extends android.app.Application {

    public static SharedPreferences config;

    @Override
    public void onCreate() {
        super.onCreate();

        config = getSharedPreferences("config", MODE_PRIVATE);
    }

    // 保存配置参数
    public static void setConfigValue(String key, String value) {
        SharedPreferences.Editor editor = config.edit();
        editor.putString(key, value);
        editor.apply();
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
}
