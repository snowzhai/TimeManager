package com.tm.timemanager.bean;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.tm.timemanager.application.MyApplication;
import com.tm.timemanager.dao.DBOpenHelperdao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenkui on 16/4/20.
 * 该类提供一个获取所有App信息的方法
 */
public class AppInfos {

    public static List<AppInfo> getAppInfos(Context context) {
        // App信息对象集合
        List<AppInfo> appInfos = new ArrayList<>();
        // 从数据库获得每个应用的应用信息（DB——>AppInfo）
        DBOpenHelperdao dao = new DBOpenHelperdao(context);
        // 调用dao的getapptotal方法
        Cursor cursor = dao.getapptotal();

        // 查询结果
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AppInfo appInfo = new AppInfo(); // new一个AppInfo
                int id = cursor.getInt(0);
                String packname = cursor.getString(1);
                String appname = cursor.getString(2);
//                long totaltime = cursor.getLong(3);
//                int totalcount = cursor.getInt(4);
                byte[] icon = cursor.getBlob(5);
                int timing = MyApplication.gettime(packname);

                Bitmap bitmap = dao.getimagefrom(icon);
                appInfo.setAppIcon(bitmap); // 获取应用图标
                appInfo.setAppName(appname); // 获取应用名称
                appInfo.setPackageName(packname); // 获取应用包名
                appInfo.setTiming(timing); // 应用计时类型
                // 将设置完成的App信息对象添加至集合
                appInfos.add(appInfo);
            }
            cursor.close();
        }
        return appInfos; // 返回该App信息对象集合
    }
}
