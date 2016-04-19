package com.tm.timemanager.application;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class Application {


    public static int getPhoneWidth(Context context){
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowmanager.getDefaultDisplay().getWidth();

        return width;

    }
    public static int getPhoneHeight(Context context){
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = windowmanager.getDefaultDisplay().getHeight();
        return height;
    }
}
