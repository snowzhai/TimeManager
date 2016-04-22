package com.tm.timemanager.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CHENQIAO on 2016/4/22.
 */
public class DataUtil {

    public static String getDate(){

        //获取当前时间，得到当天数据库cursor信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        Log.i("当前时间", date);
        return date;

    }


}
