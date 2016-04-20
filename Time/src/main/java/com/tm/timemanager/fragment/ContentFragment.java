package com.tm.timemanager.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.pager.BasePager;
import com.tm.timemanager.pager.HomePager;
import com.tm.timemanager.pager.PiePager;

import java.util.ArrayList;


/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class ContentFragment extends BaseFragment {

    private View view;
    private ViewPager vp_main;
    private ArrayList<BasePager> pagers;

    @Override
    public View initViews() {
        view = View.inflate(mActivity, R.layout.fragment_content, null);

//        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) mLogin.getLayoutParams();
//        linearParams.height = 200;
//        mlogin.setLayoutParams(linearParams);


        ImageButton ibn_menu = (ImageButton) view.findViewById(R.id.ibn_menu);
        ibn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity mainActivity = (HomeActivity)mActivity;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });


        vp_main = (ViewPager) view.findViewById(R.id.vp_main);

        return view;
    }


    @Override
    public void initData() {

        pagers = new ArrayList<>();


        pagers.add(new PiePager(mActivity));
        pagers.add(new HomePager(mActivity));

        vp_main.setAdapter(new MyPagerAdapter());
        vp_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("vp_main",position+"--");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_main.setCurrentItem(1);


    }

    class MyPagerAdapter extends PagerAdapter{

        private View view;

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            switch (position){
                case 0:
                    PiePager piePager = (PiePager) pagers.get(position);
                    container.addView(piePager.mView);
                    view = piePager.mView;
                    break;
                case 1:
                    HomePager homePager = (HomePager) pagers.get(position);
                    container.addView(homePager.mView);
                    view = homePager.mView;
                    break;
            }

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            container.removeView(view);
        }
    }



}