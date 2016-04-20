package com.tm.timemanager.pager;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tm.timemanager.R;

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
        int i = (screenHeight > screenWidth) ? screenWidth : screenHeight;
        params.height = i;
        params.width = i;
        rl_main_content.setLayoutParams(params);

        tv_main_hour = (TextView) mView.findViewById(R.id.tv_main_hour);
        tv_main_h = (TextView) mView.findViewById(R.id.tv_main_h);
        tv_main_minute = (TextView) mView.findViewById(R.id.tv_main_minute);
        tv_main_m = (TextView) mView.findViewById(R.id.tv_main_m);


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

        Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/FjallaOne-Regular.ttf");
        tv_main_hour.setTypeface(tf);
        tv_main_h.setTypeface(tf);
        tv_main_minute.setTypeface(tf);
        tv_main_m.setTypeface(tf);



    }
}
