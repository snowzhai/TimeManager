package com.tm.timemanager.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tm.timemanager.R;
import com.tm.timemanager.Utils.PrefUtils;

import java.util.ArrayList;


public class GuideActivity extends Activity {

    private ViewPager vp_guide_show;
    private ArrayList<MyPageInfo> myPageInfos;
    private Button bt_guide_enter;
    private LinearLayout ll_guide_indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        vp_guide_show = (ViewPager) findViewById(R.id.vp_guide_show);
        bt_guide_enter = (Button) findViewById(R.id.bt_guide_enter);
        ll_guide_indicator = (LinearLayout) findViewById(R.id.ll_guide_indicator);
        final View redpoint_guide_indicator = findViewById(R.id.redpoint_guide_indicator);

        myPageInfos = new ArrayList<>();
        final int[] imgid = new int[]{R.drawable.guide4,R.drawable.guide2,R.drawable.guide3,R.drawable.guide1};

        for(int i = 0;i<imgid.length;i++){

            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgid[i]);
            MyPageInfo myPageInfo = new MyPageInfo(imageView,"page"+i);
            myPageInfos.add(myPageInfo);


            View view =  new View(this);
            view.setBackgroundResource(R.drawable.greypoint);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            if(i!=0) layoutParams.leftMargin = 20;

            view.setLayoutParams(layoutParams);
            ll_guide_indicator.addView(view);

        }

        vp_guide_show.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgid.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                MyPageInfo myPageInfo = (MyPageInfo) object;
                return view == myPageInfo.iv;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                MyPageInfo myPageInfo = myPageInfos.get(position);
                container.addView(myPageInfo.iv);
                return myPageInfo;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

                MyPageInfo myPageInfo = (MyPageInfo) object;
                container.removeView(myPageInfo.iv);
            }

        });

        vp_guide_show.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) redpoint_guide_indicator.getLayoutParams();
                layoutParams.leftMargin= position*40+  (int)(positionOffset*40);
                redpoint_guide_indicator.setLayoutParams(layoutParams);

            }

            @Override
            public void onPageSelected(int position) {
                if(position == imgid.length-1){
                    bt_guide_enter.setVisibility(View.VISIBLE);

                }else {
                    bt_guide_enter.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void enterHome(View view) {

        //如果展示过新手引导，则直接进入主页面
        PrefUtils.setBoolean(GuideActivity.this,"jump_to_user_guide",false);
        startActivity(new Intent(GuideActivity.this,HomeActivity.class));
        finish();
    }

    class MyPageInfo {
        public MyPageInfo(ImageView iv, String pageTitle) {
            this.iv = iv;
            this.pageTitle = pageTitle;
        }

        ImageView iv;
        String pageTitle;

    }
}