package com.tm.timemanager.fragment;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tm.timemanager.R;
import java.util.ArrayList;
/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class LeftMenuFragment extends BaseFragment {


    private ListView lv_list;
    private ArrayList<String> tv_menuList;
    private ArrayList<Integer> iv_menuList;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_leftmenu,null);
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



        lv_list.setAdapter(new leftMenuAdapter());

    }

    class leftMenuAdapter extends BaseAdapter{

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
