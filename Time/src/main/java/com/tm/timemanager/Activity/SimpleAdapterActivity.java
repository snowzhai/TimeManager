package com.tm.timemanager.Activity;

import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.tm.timemanager.R;
import com.tm.timemanager.Utils.DateUtil;
import com.tm.timemanager.dao.DBOpenHelperdao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapterActivity extends ListActivity {

    private List<Map<String, Object>> list;
    private SimpleDateFormat hourmin;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.activity_main, new String[]{"tv_sjz_time", "image_sjz_icon", "tv_sjz_appname", "tv_sjz_long"}, new int[]{R.id.tv_sjz_time, R.id.image_sjz_icon, R.id.tv_sjz_appname, R.id.tv_sjz_long});
        setListAdapter(adapter);
    }

    private List<Map<String, Object>> getData() {
        //map.put(参数名字,参数值)
        list = new ArrayList<>();
        inputitem();
        return list;
    }

    private void inputitem() {
        hourmin = new SimpleDateFormat("HH:mm:ss");
        String date = DateUtil.getDate();
        Cursor cursor;
        DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(this);
        cursor = dbOpenHelperdao.getappdaily(date);



        while (cursor.moveToNext()) {

            String packname = cursor.getString(2);

            Log.i("呵呵额呵呵呵", packname + "");
            long starttime = cursor.getLong(4);

            int runtime = cursor.getInt(5);
            String format = hourmin.format(starttime);

            PackageManager pm = this.getPackageManager();
            String name = null;
            try {
                name = pm.getApplicationLabel(pm.getApplicationInfo(packname, PackageManager.GET_META_DATA)).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            Map<String, Object> map = new HashMap<>();
            map.put("tv_sjz_time", format);
            map.put("image_sjz_icon", R.drawable.timeline_green);
            map.put("tv_sjz_appname", name);
            map.put("tv_sjz_long", runtime + "秒");
            list.add(map);

        }

    }


}