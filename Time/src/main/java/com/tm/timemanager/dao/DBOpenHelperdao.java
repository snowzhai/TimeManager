package com.tm.timemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tm.timemanager.db.MyDBOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/20.
 */
public class DBOpenHelperdao {

    private final MyDBOpenHelper helper;
    private final SQLiteDatabase db;
    private ByteArrayOutputStream baos;
    private Bitmap bitmap;

    public DBOpenHelperdao(Context context) {
        helper = new MyDBOpenHelper(context, null, null, 1);
        db = helper.getReadableDatabase();
    }

    //app每天数据的插入
    public long insertappdaily(String date, String packname, String appname, long starttime, int runtime, int clickcount) {
        //当不符合条件的时候返回-1
        ContentValues cv = new ContentValues();
        cv.put("date", date);                //日期
        cv.put("packname", packname);        //包名
        cv.put("appname", appname);          //app名
        cv.put("starttime", starttime);     //开始时间
        cv.put("runtime", runtime);         //运行时间
        cv.put("clickcount", clickcount);   //点击次数
        long ret = db.insert("appdaily", null, cv);//blacknumber为表的名称
        //这里返回的是影响行数的行号 而不是影响的行数
        return ret;
    }

    //app总的情况的插入
    public long insertapptotal(String packname, String appname, int totaltime, int totalcount, Drawable icon) {
        //当不符合条件的时候返回-1
        ContentValues cv = new ContentValues();
        byte[] icontobyte = Icontobyte(icon);
//        Log.i("哈哈图片", icontobyte.length + "");
        cv.put("packname", packname);        //包名
        cv.put("appname", appname);          //app名
        cv.put("totaltime", totaltime);         //运行时间
        cv.put("totalcount", totalcount);   //点击次数
        cv.put("icon", icontobyte);         //图片
        long ret = db.insert("apptotal", null, cv);//blacknumber为表的名称
        //这里返回的是影响行数的行号 而不是影响的行数
        return ret;
    }
    //插入 加锁 解锁 事件
    public long insertappevent(String date,long starttime,int lock){
        ContentValues cv = new ContentValues();
        cv.put("date", date);        //包名
        cv.put("starttime", starttime);          //app名
        cv.put("lock", lock);         //运行时间
        long ret = db.insert("appevent", null, cv);//blacknumber为表的名称
        return ret;
    }
    //获得appdaily所有数据
    public Cursor getappdaily() {
        //所有数据的结果游标集
        Cursor cursor = db.rawQuery("select * from appdaily;", null);
      /*结果处理
       while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            String packname = cursor.getString(2);
            String appname = cursor.getString(3);
            long starttime = cursor.getLong(4);
            int clickcount = cursor.getInt(5);
        }*/
        return cursor;
    }

    //获得appdaily中某一天的所有信息
    public Cursor getappdaily(String date) {
        Cursor cursor = db.rawQuery("select * from appdaily where date='" + date + "';", null);
        return cursor;
    }

    //获得apptotal总的数据
    public Cursor getapptotal() {
        Cursor cursor = db.rawQuery("select * from apptotal;", null);
//      结果处理
       /*  while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String packname = cursor.getString(1);
            String appname = cursor.getString(2);
            long totaltime = cursor.getLong(3);
            int totalcount = cursor.getInt(4);
            byte[] icon = cursor.getBlob(5);


        //如何将icon设置到imageview中
          Bitmap bitmap = getimagefrom(icon);
          ImageView imageView = (ImageView) findViewById(R.id.zhai);
          imageView.setImageBitmap(bitmap);
        }*/
            return cursor;
        }
    //获得所有的解锁事件
    public Cursor getappevent() {
        Cursor cursor = db.rawQuery("select * from appevent;", null);
        return cursor;
    }
    //获得某一天的解锁事件
    public Cursor getappevent(String date) {
        Cursor cursor = db.rawQuery("select * from appevent where date='"+date+"';", null);
        return cursor;
    }
    //判断apptotal中有没有这个应用
    public Cursor getapptotalhava(String packname) {
        String[] columns = {"packname"};
        String[] whereargus = {packname};
        Cursor cursor = db.query("apptotal", columns, "packname=?", whereargus, null, null, null);
        return cursor;
    }

    //将apptotal中的数据动态改变
    public void updatetotal(String appname, int runningtime, int totalcount) {
        String update = "update apptotal set totaltime=totaltime+" + runningtime + " , totalcount=totalcount+" + totalcount + " where appname = '" + appname + "' ; ";
        Log.i("哈哈", appname);
        db.execSQL(update);
//        db.execSQL(update1);
    }
    //将图片转化成字节数组存到数据库中
    public byte[] Icontobyte(Drawable icon) {
        try {
            baos = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    //从数据库中将图片区出来
    public Bitmap getimagefrom(byte[] icon) {
        bitmap = BitmapFactory.decodeByteArray(icon, 0, icon.length);
        return bitmap;
    }
}
