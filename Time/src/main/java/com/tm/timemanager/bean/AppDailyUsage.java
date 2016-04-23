package com.tm.timemanager.bean;

/**
 * Created by CHENQIAO on 2016/4/22.
 */
public class AppDailyUsage {
    String packname;
    String appname;
    int runtime;
    String date;
    long starttime;
    int clickcount;

    public AppDailyUsage(String packname, String appname, int runtime, String date, long starttime, int clickcount) {
        this.packname = packname;
        this.appname = appname;
        this.runtime = runtime;
        this.date = date;
        this.starttime = starttime;
        this.clickcount = clickcount;
    }

    public AppDailyUsage(String packname) {
        this.packname = packname;
    }

    public AppDailyUsage() {
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getClickcount() {
        return clickcount;
    }

    public void setClickcount(int clickcount) {
        this.clickcount = clickcount;
    }

    @Override
    public String toString() {
        return "AppDailyUsage{" +
                "packname='" + packname + '\'' +
                ", appname='" + appname + '\'' +
                ", runtime=" + runtime +
                ", date='" + date + '\'' +
                ", clickcount=" + clickcount +
                '}';
    }
}
