package com.tm.timemanager.bean;

import android.graphics.Bitmap;

/**
 * Created by chenkui on 16/4/20.
 */
// 这是一个JavaBean
public class AppInfo {

    private Bitmap appIcon; // 应用图标
    private String appName; // 应用名称
    private String packageName; // 应用包名

    public Bitmap getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Bitmap appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    // 重写toString方法
    public String toString() {
//        return super.toString();
        return "AppInfo [appName=" + appName + ", packageName=" + packageName + "]";
    }
}
