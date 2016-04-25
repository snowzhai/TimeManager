package com.tm.timemanager.pager;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tm.timemanager.R;
import com.tm.timemanager.Utils.DateUtil;
import com.tm.timemanager.application.MyApplication;
import com.tm.timemanager.dao.DBOpenHelperdao;
import com.tm.timemanager.view.RingView;
import com.tm.timemanager.view.RoundProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.tm.timemanager.view.RoundProgressBar;

/**
 * Created by CHENQIAO on 2016/4/20.
 */
public class HomePager extends BasePager {

    private TextView tv_main_hour;
    private TextView tv_main_h;
    private TextView tv_main_minute;
    private TextView tv_main_m;
    private LinearLayout rl_main_content;
    public View mView;
    private int i;
    private RoundProgressBar roundProgressBar;
    private RingView ringView;
    private int totalRuntime;

    public HomePager(Activity activity) {
        super(activity);
        initView();
    }


    public void initView(){

        mView = View.inflate(mActivity, R.layout.pager_home, null);

//        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) mLogin.getLayoutParams();
//        linearParams.height = 200;
//        mlogin.setLayoutParams(linearParams);

        int screenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
        int screenHeight =mActivity.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高

        rl_main_content = (LinearLayout) mView.findViewById(R.id.rl_main_content);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rl_main_content.getLayoutParams();
        i = (screenHeight > screenWidth) ? screenWidth : screenHeight;
        params.height = i;
        params.width = i;
        rl_main_content.setLayoutParams(params);

        tv_main_hour = (TextView) mView.findViewById(R.id.tv_main_hour);
        tv_main_h = (TextView) mView.findViewById(R.id.tv_main_h);
        tv_main_minute = (TextView) mView.findViewById(R.id.tv_main_minute);
        tv_main_m = (TextView) mView.findViewById(R.id.tv_main_m);


        setPhoneUsageTime();
        setFont();


        ValueAnimator animator1 = ValueAnimator.ofInt(0, Integer.parseInt(tv_main_hour.getText().toString()));
        ValueAnimator animator2 = ValueAnimator.ofInt(0, Integer.parseInt(tv_main_minute.getText().toString()));

        animator1.setDuration(1000);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                tv_main_hour.setText((animatedValue + ""));
            }
        });
        animator1.start();

        animator2.setDuration(1000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                tv_main_minute.setText((animatedValue + ""));
            }
        });

        animator2.start();

//        roundProgressBar = (RoundProgressBar) mView.findViewById(R.id.roundProgressBar);
//        int width = MyApplication.getPhoneWidth(mActivity);
//        int height = MyApplication.getPhoneHeight(mActivity);
//        i = (height > width) ? width : height;

//        roundProgressBar.setCricleProgressColor(0xffB0F44B);
//        roundProgressBar.setProgress(80);

        ringView = (RingView) mView.findViewById(R.id.ringview);
        DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(mActivity);
        Cursor cursor = dbOpenHelperdao.getappdaily(DateUtil.getDate());
        while (cursor.moveToNext()){

           int  runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
            totalRuntime = totalRuntime+runtime;
        }

        float i = (float) (360- (totalRuntime / (24 * 60 * 60.0)) * 360);

        Log.i("totalRuntime",i+"");

        ringView.setProgress(i);


    }

    //设置显示字体样式大小
    private void setFont() {
        tv_main_hour.setTextSize(TypedValue.COMPLEX_UNIT_DIP,i/12);
        tv_main_minute.setTextSize(TypedValue.COMPLEX_UNIT_DIP,i/12);
        tv_main_h.setTextSize(TypedValue.COMPLEX_UNIT_DIP,i/18);
        tv_main_m.setTextSize(TypedValue.COMPLEX_UNIT_DIP,i/18);
        Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/FjallaOne-Regular.ttf");
        tv_main_hour.setTypeface(tf);
        tv_main_h.setTypeface(tf);
        tv_main_minute.setTypeface(tf);
        tv_main_m.setTypeface(tf);
    }


    //获取手机使用时间
    private void setPhoneUsageTime() {

        DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(mActivity);
        long getappeventtotalday = dbOpenHelperdao.getappeventtotalday(DateUtil.getDate());

        Log.i("phoneUsageTime",getappeventtotalday+"");


        String s = DateUtil.formatTime(getappeventtotalday);
        String hour = s.substring(0, 2);
        String minutes = s.substring(3, 5);

        Log.i("phoneUsageTime",hour+"时"+minutes+"分");
        tv_main_hour.setText(hour);
        tv_main_minute.setText(minutes);
    }
}
