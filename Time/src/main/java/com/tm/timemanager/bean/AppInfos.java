package com.tm.timemanager.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

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
        // PackageManager能够读取应用清单文件的所有信息
        PackageManager packageManager = context.getPackageManager();
        // 获得（已安装的所有应用的）封装应用信息的对象 // int flags = 0
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 获得每个应用的应用信息（PackageInfo——>AppInfo）
        for (PackageInfo packageInfo : packageInfos) { // 增强for循环迭代
            AppInfo appInfo = new AppInfo();
            // 获取应用图标
            Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
            appInfo.setAppIcon(icon);
            // 获取应用名称
            String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appInfo.setAppName(name);

            // 获取当前应用的标记
            int flags = packageInfo.applicationInfo.flags;

            // 判断当前应用是否为用户App
            // 方法1————
//            if (sourceDir.startsWith("/system")) {
//                appInfo.setUserApp(false); // 为系统App
//            } else if (sourceDir.startsWith("/data")) {
//                appInfo.setUserApp(true); // 确实为用户App
//            }
            // 方法2————
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setUserApp(false); // 为系统App
            } else {
                appInfo.setUserApp(true); // 确实为用户App
            }

            // 将设置完成的App信息对象添加至集合
            appInfos.add(appInfo);
        }
        return appInfos; // 返回该App信息对象集合
    }
}
