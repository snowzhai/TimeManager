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
}
