package com.tm.timemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by chenkui on 16/4/20.
 */
// 这是一个JavaBean
public class AppInfo {

    private Drawable appIcon; // 应用图标
    private String appName; // 应用名称
    private boolean isUserApp; // 是否为用户APP，否则为系统应用

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setUserApp(boolean userApp) {
        isUserApp = userApp;
    }

    @Override
    // 重写toString方法
    public String toString() {
//        return super.toString();
        return "AppInfo [appName=" + appName + ", isUserApp=" + isUserApp + "]";
    }
}
