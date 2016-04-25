package com.tm.timemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "app.db", null, version);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table apptotal (id integer primary key autoincrement,packname varchar(40),appname varchar(20),totaltime long,totalcount int,icon BLOB  );");
        db.execSQL("create table appdaily (id integer primary key autoincrement,date varchar(10),packname varchar(40),appname varchar(20),starttime long,runtime int,clickcount int );");
        db.execSQL("create table appevent (id integer primary key autoincrement,date varchar(10),starttime long,lock int ,type int);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
