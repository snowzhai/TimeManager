package com.tm.timemanager.bean;

/**
 * Created by CHENQIAO on 2016/4/24.
 */
public class AppTotalUsage {
//    while (cursor.moveToNext()) {
//            int id = cursor.getInt(0);
//            String packname = cursor.getString(1);
//            String appname = cursor.getString(2);
//            long totaltime = cursor.getLong(3);
//            int totalcount = cursor.getInt(4);
//            byte[] icon = cursor.getBlob(5);


    String packname;
    String appname;
    long totaltime;
    int totalcount;

    public AppTotalUsage(String packname, String appname, long totaltime, int totalcount) {
        this.packname = packname;
        this.appname = appname;
        this.totaltime = totaltime;
        this.totalcount = totalcount;
    }

    public AppTotalUsage(String packname) {
        this.packname = packname;
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

    public long getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(long totaltime) {
        this.totaltime = totaltime;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }


    @Override
    public String toString() {
        return "AppTotalUsage{" +
                "packname='" + packname + '\'' +
                ", appname='" + appname + '\'' +
                ", totaltime=" + totaltime +
                ", totalcount=" + totalcount +
                '}';
    }
}
