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
    public long insertBlackNumber(String packname,long starttime,int runtime,int clickcount){
        //当不符合条件的时候返回-1
        ContentValues cv = new ContentValues();
        cv.put("packname",packname);
        cv.put("starttime", starttime);
        cv.put("runtime", runtime);
        cv.put("clickcount", clickcount);
        long ret = db.insert("appdaily", null, cv);//blacknumber为表的名称
        //这里返回的是影响行数的行号 而不是影响的行数
        return ret;
    }
}
