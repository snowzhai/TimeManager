package com.tm.timemanager.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by CHENQIAO on 2016/4/22.
 */
public class FontUtils {

    //设置显示字体样式大小
    public static void setFont(Context context, TextView textView,int i) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,i);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/FjallaOne-Regular.ttf");
        textView.setTypeface(tf);

    }
}
