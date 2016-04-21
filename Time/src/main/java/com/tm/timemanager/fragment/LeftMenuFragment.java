package com.tm.timemanager.fragment;

import android.app.Instrumentation;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.application.Application;

import java.util.ArrayList;

/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class LeftMenuFragment extends BaseFragment {


    private ListView lv_list;
    private ArrayList<String> tv_menuList;
    private ArrayList<Integer> iv_menuList;
    private LeftMenuAdapter adapter;
    private HomeActivity homeActivity;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_leftmenu, null);
        lv_list = (ListView) view.findViewById(R.id.lv_menu_list);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        tv_menuList = new ArrayList<>();
        tv_menuList.add("Home");
        tv_menuList.add("Trend");
        tv_menuList.add("Manage");
        tv_menuList.add("Setting");

        iv_menuList = new ArrayList<>();
        iv_menuList.add(R.drawable.home);
        iv_menuList.add(R.drawable.statistic);
        iv_menuList.add(R.drawable.management);
        iv_menuList.add(R.drawable.setting);

        adapter = new LeftMenuAdapter(); // 全局的adapter
        lv_list.setAdapter(adapter); // 展示左侧边栏内容

        // 获得HomeActivity对象，也就是这里的mActivity
        homeActivity = (HomeActivity) mActivity;

        // 为左侧边栏设置条目的点击侦听
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // i==position, l==id
                adapter.notifyDataSetChanged(); // 适配器刷新（调用getView方法）
                homeActivity.replaceFragment(i); // 替换为新的Fragment

                // 获取屏幕宽高
                int phoneWidth = Application.getPhoneWidth(mActivity);
                int phoneHeight = Application.getPhoneHeight(mActivity);

                // 模拟点击事件
                final MotionEvent event_down = MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
                        phoneWidth - 10, phoneHeight / 2, 0);
                final MotionEvent event_up = MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
                        phoneWidth - 10, phoneHeight / 2, 0);

                // 子线程模拟点击操作
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Instrumentation instrumentation = new Instrumentation();
                            instrumentation.sendPointerSync(event_down);
                            instrumentation.sendPointerSync(event_up);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tv_menuList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.item_leftmenu, null);

            ImageView iv_menu_list = (ImageView) view.findViewById(R.id.iv_menu_list);
            TextView tv_menu_list = (TextView) view.findViewById(R.id.tv_menu_list);
            tv_menu_list.setText(tv_menuList.get(position));
            iv_menu_list.setImageResource(iv_menuList.get(position));
            Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/FjallaOne-Regular.ttf");
            tv_menu_list.setTypeface(tf);
            return view;
        }
    }
}
