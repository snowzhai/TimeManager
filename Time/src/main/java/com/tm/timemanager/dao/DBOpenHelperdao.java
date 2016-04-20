package com.tm.timemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tm.timemanager.db.MyDBOpenHelper;

/**
 * Created by Administrator on 2016/4/20.
 */
public class DBOpenHelperdao  {

    private final MyDBOpenHelper helper;
    private final SQLiteDatabase db;

    public DBOpenHelperdao(Context context) {
        helper = new MyDBOpenHelper(context, null, null,1);
        db = helper.getReadableDatabase();
    }
    public long insertBlackNumber(String date,String packname,String appname,long starttime,int runtime,int clickcount){
        //当不符合条件的时候返回-1
        ContentValues cv = new ContentValues();
        cv.put("date",date);//日期
        cv.put("packname",packname);//包名
        cv.put("appname",appname);//app名
        cv.put("starttime", starttime);//开始时间
        cv.put("runtime", runtime);//运行时间
        cv.put("clickcount", clickcount);//点击次数
        long ret = db.insert("appdaily", null, cv);//blacknumber为表的名称
        //这里返回的是影响行数的行号 而不是影响的行数
        return ret;
    }
}
