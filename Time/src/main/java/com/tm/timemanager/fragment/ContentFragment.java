package com.tm.timemanager.fragment;


import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.Utils.DateUtil;
import com.tm.timemanager.Utils.FontUtils;
import com.tm.timemanager.bean.AppDailyUsage;
import com.tm.timemanager.dao.DBOpenHelperdao;
import com.tm.timemanager.pager.HomePager;
import com.tm.timemanager.pager.PiePager;
import com.tm.timemanager.pager.TotalPiepager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class ContentFragment extends BaseFragment {

    private View view;
    private ViewPager vp_main;
    private ArrayList<View> pagers;
    private CirclePageIndicator cpi_main;
    int mPosition;
    private MyPagerAdapter myPagerAdapter;
    private TextView tv_main_appuagecount;
    private TextView tv_main_appuagecount_;
    private TextView tv_main_appusagetime;
    private TextView tv_main_appusagetime_;
    private TextView tv_main_phoneusagecount;
    private TextView tv_main_phoneusagecount_;
        private ListView lv_piepager_applist;
    private ArrayList<AppDailyUsage> appDailyUsagesList;

    @Override
    public View initViews() {
        view = View.inflate(mActivity, R.layout.fragment_content, null);
        ImageButton ibn_menu = (ImageButton) view.findViewById(R.id.ibn_menu);
        cpi_main = (CirclePageIndicator) view.findViewById(R.id.cpi_main);
        ibn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity mainActivity = (HomeActivity)mActivity;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });

        vp_main = (ViewPager) view.findViewById(R.id.vp_main);
        cpi_main = (CirclePageIndicator) view.findViewById(R.id.cpi_main);
        tv_main_appuagecount = (TextView) view.findViewById(R.id.tv_main_appuagecount);
        tv_main_appuagecount_ = (TextView) view.findViewById(R.id.tv_main_appuagecount_);
        tv_main_appusagetime = (TextView) view.findViewById(R.id.tv_main_appusagetime);
        tv_main_appusagetime_ = (TextView) view.findViewById(R.id.tv_main_appusagetime_);
        tv_main_phoneusagecount = (TextView) view.findViewById(R.id.tv_main_phoneusagecount);
        tv_main_phoneusagecount_ = (TextView) view.findViewById(R.id.tv_main_phoneusagecount_);

        return view;
    }


    @Override
    public void initData() {

        pagers = new ArrayList<>();


        pagers.add(new PiePager(mActivity).mView);
        pagers.add(new HomePager(mActivity).mView);
        pagers.add(new TotalPiepager(mActivity).mView);
        myPagerAdapter = new MyPagerAdapter();
        vp_main.setAdapter(myPagerAdapter);
        vp_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("vp_main", position + "--");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置viewpager默认位置为1
        vp_main.setCurrentItem(1);

        int screenWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vp_main.getLayoutParams();
        int i = (screenHeight > screenWidth) ? screenWidth : screenHeight;
        params.height = i;
        params.width = i;
        vp_main.setLayoutParams(params);

        cpi_main.setViewPager(vp_main);
        cpi_main.setSnap(true);
        cpi_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                 mPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cpi_main.onPageSelected(1);


        setDetailStatistics();



    }



    class MyPagerAdapter extends PagerAdapter {


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


                    View view = pagers.get(position);
                    container.addView(view);
                    ContentFragment.this.view = view;
                     return view;

            }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pagers.get(position));
        }

    }


    /*//给lisiview设置数据
    private void setListData() {

        int totalTime = 0;
        int totalCount = 0;

        //从数据库读取数据
        String date = DateUtil.getDate();
        appDailyUsagesList = new ArrayList<>();
        ArrayList<String> packNameList = new ArrayList<>();
        Cursor cursor = new DBOpenHelperdao(mActivity).getappdaily(date);
        while (cursor.moveToNext()) {
            String packname = cursor.getString(cursor.getColumnIndex("packname"));

            //将数据库按照packagename分类，运行时间相加

            if (!packNameList.contains(packname)) {
                packNameList.add(packname);

                String appname = cursor.getString(cursor.getColumnIndex("appname"));
                int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                int starttime = cursor.getInt(cursor.getColumnIndex("starttime"));
                int clickcount = cursor.getInt(cursor.getColumnIndex("clickcount"));
                AppDailyUsage appDailyUsage = new AppDailyUsage(packname, appname, runtime, date, starttime, clickcount);

                totalTime = totalTime + runtime;
                totalCount = totalCount + clickcount;
                appDailyUsagesList.add(appDailyUsage);
//                Log.i("appUsageList1", appDailyUsage.toString());
            } else {
                for (AppDailyUsage usage : appDailyUsagesList) {
                    if (packname.equals(usage.getPackname())) {
                        int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                        int count = cursor.getInt(cursor.getColumnIndex("clickcount"));
                        int totalruntime = usage.getRuntime() + runtime;
                        usage.setRuntime(totalruntime);
                        int totalclick = usage.getClickcount() + count;
                        usage.setClickcount(totalclick);
                        totalTime = totalTime + runtime;
                        totalCount = totalCount + count;
//                        Log.i("appUsageList2",usage.toString());
                    }
                }
            }
        }

        lv_piepager_applist = (ListView) view.findViewById(R.id.lv_piepager_applist);
        lv_piepager_applist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return appDailyUsagesList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = null;
                ViewHolder viewHolder=null;
                if (convertView == null){
                    view = View.inflate(mActivity, R.layout.item_piepager_applist, null);
                    viewHolder =     new ViewHolder();
                    viewHolder.appicon = (ImageView) view.findViewById(R.id.iv_piepagerlist_icon);
                    viewHolder.appname = (TextView) view.findViewById(R.id.tv_piepagerlist_appname);
                    viewHolder.appusagetime = (TextView) view.findViewById(R.id.tv_piepagerlist_appusagetime);
                    viewHolder.appusagecount = (TextView) view.findViewById(R.id.tv_piepagerlist_appusagecount);
                    view.setTag(viewHolder);

                }else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }

                AppDailyUsage usage = appDailyUsagesList.get(position);
                viewHolder.appicon.setImageResource(R.mipmap.ic_launcher);
                viewHolder.appname.setText(usage.getAppname());
                viewHolder.appusagetime.setText(usage.getRuntime()+"");
                viewHolder.appusagecount.setText(usage.getClickcount()+"");
                return view;
            }
            class ViewHolder{
                ImageView appicon;
                TextView appname;
                TextView appusagetime;
                TextView appusagecount;
            }
        });
    }*/



    private void setDetailStatistics() {
        int phoneDailyUsageCount = 0;
        DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(mActivity);

        //手机使用次数
        Cursor cursor = dbOpenHelperdao.getappevent(DateUtil.getDate());
        while(cursor.moveToNext()){
            phoneDailyUsageCount++;
        }
//        phoneDailyUsageCount=cursor.getCount();
        Log.i("homeDailyUsageCount",phoneDailyUsageCount+"");
        tv_main_phoneusagecount.setText(phoneDailyUsageCount/2+"");


        int totalTime = 0;
        int totalCount = 0;

        //从数据库读取数据
        String date = DateUtil.getDate();
        appDailyUsagesList = new ArrayList<>();
        ArrayList<String> packNameList = new ArrayList<>();
         cursor = new DBOpenHelperdao(mActivity).getappdaily(date);
        while (cursor.moveToNext()) {
            String packname = cursor.getString(cursor.getColumnIndex("packname"));

            //将数据库按照packagename分类，运行时间相加

            if (!packNameList.contains(packname)) {
                packNameList.add(packname);

                String appname = cursor.getString(cursor.getColumnIndex("appname"));
                int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                int starttime = cursor.getInt(cursor.getColumnIndex("starttime"));
                int clickcount = cursor.getInt(cursor.getColumnIndex("clickcount"));
                AppDailyUsage appDailyUsage = new AppDailyUsage(packname, appname, runtime, date, starttime, clickcount);

                totalTime = totalTime + runtime;
                totalCount = totalCount + clickcount;
                appDailyUsagesList.add(appDailyUsage);
                Log.i("appUsageList1", appDailyUsage.toString());
            } else {
                for (AppDailyUsage usage : appDailyUsagesList) {
                    if (packname.equals(usage.getPackname())) {
                        int runtime = cursor.getInt(cursor.getColumnIndex("runtime"));
                        int count = cursor.getInt(cursor.getColumnIndex("clickcount"));
                        int totalruntime = usage.getRuntime() + runtime;
                        usage.setRuntime(totalruntime);
                        int totalclick = usage.getClickcount() + count;
                        usage.setClickcount(totalclick);
                        totalTime = totalTime + runtime;
                        totalCount = totalCount + count;
                        Log.i("appUsageList2",usage.toString());
                    }
                }
            }
        }

//         tv_main_appuagecount
//         tv_main_appuagecount_
//         tv_main_appusagetime
//         tv_main_appusagetime_
//         tv_main_phoneusagecount
//         tv_main_phoneusagecount_

        FontUtils.setFont(mActivity,tv_main_appuagecount,20);
        FontUtils.setFont(mActivity,tv_main_appuagecount_,20);
        FontUtils.setFont(mActivity,tv_main_appusagetime,20);
        FontUtils.setFont(mActivity,tv_main_appusagetime_,20);
        FontUtils.setFont(mActivity,tv_main_phoneusagecount,20);
        FontUtils.setFont(mActivity,tv_main_phoneusagecount_,20);


        tv_main_appuagecount.setText(appDailyUsagesList.size()+"");
        Log.i("homeappDailyUsagesList",appDailyUsagesList.size()+"");
        tv_main_appusagetime.setText(totalCount+"");
        Log.i("hometotalCount",totalCount+"");
    }


}
