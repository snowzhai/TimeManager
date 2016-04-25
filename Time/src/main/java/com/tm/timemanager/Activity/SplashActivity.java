package com.tm.timemanager.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.tm.timemanager.R;
import com.tm.timemanager.Utils.PrefUtils;

public class SplashActivity extends Activity {
    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);

        startAnim();
    }

    private void startAnim() {

        //开屏动画
        AnimationSet animationSet = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f,1,0.6f,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);


        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //判断是不是第一次打开应用，第一次打开进入引导页
                boolean jump_to_user_guide = PrefUtils.getBoolean(SplashActivity.this, "jump_to_user_guide", true);
                if(jump_to_user_guide){
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        rl_splash.startAnimation(animationSet);
    }
}
