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
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("哈哈", "屏幕解锁广播...");
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("哈哈", "屏幕加锁广播...");
        }
    }
}
