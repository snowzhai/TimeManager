package com.tm.timemanager.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by snow on 2016/4/19.
 */
public class InfoReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String aPackagename = intent.getStringExtra("package");//得到intent中名字为package的内容
        Log.i("哈哈", "onReceive---" + aPackagename);
    }

}
