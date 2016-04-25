package com.tm.timemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CHENQIAO on 2016/4/11.
 */
public class PrefUtils {

    public static boolean getBoolean(Context ctx,String key, boolean defaultValue){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    public static  void setBoolean(Context ctx,String key,boolean value){

        SharedPreferences sharedPreferences = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
         sharedPreferences.edit().putBoolean(key,value).commit();
    }
}
