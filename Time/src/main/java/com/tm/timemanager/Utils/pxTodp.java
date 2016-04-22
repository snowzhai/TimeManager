package com.tm.timemanager.Utils;

import android.content.Context;

/**
 * Created by CHENQIAO on 2016/4/21.
 */
public class pxTodp {

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
