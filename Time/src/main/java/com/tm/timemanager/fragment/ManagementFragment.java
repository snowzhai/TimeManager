package com.tm.timemanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.application.Application;
import com.tm.timemanager.bean.AppInfo;
import com.tm.timemanager.bean.AppInfos;

import java.util.ArrayList;
import java.util.List;

public class ManagementFragment extends BaseFragment {

    // 全局变量
    private ListView listView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private int style_choice;

    @Override
    public View initViews() {
        // 将布局文件填充成一个View对象
        View view = View.inflate(mActivity, R.layout.fragment_management, null);
        // 应用管理界面的的ListView————
        listView = (ListView) view.findViewById(R.id.app_list_view);
        ImageButton ibn_menu = (ImageButton) view.findViewById(R.id.management_left_menu);

        // 左侧边栏点击侦听
        ibn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity mainActivity = (HomeActivity) mActivity;
                SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        initMyData();
    }

    // Handler对象
    Handler handler = new Handler() {
        // 此方法在主线程中被调用，刷新UI
        public void handleMessage(Message msg) {
            AppManagerAdapter adapter = new AppManagerAdapter();
            listView.setAdapter(adapter); // 展示所有App信息
        }
    };

    // 初始化数据
    private void initMyData() {
        // 子线程负责耗时操作
        new Thread() {

            @Override
            public void run() {
                // 调用AppInfos的getAppInfos方法获取所有App信息
                appInfos = AppInfos.getAppInfos(mActivity);
                // 将这个App信息对象集合拆分为用户App和系统App的信息对象集合
                // 用户App的信息对象集合
                userAppInfos = new ArrayList<>();
                // 系统App的信息对象集合
                systemAppInfos = new ArrayList<>();
                // 拆分集合
                for (AppInfo appInfo : appInfos) { // 增强for循环迭代
                    if (appInfo.isUserApp()) {
                        userAppInfos.add(appInfo); // 将用户App添加至对应集合
                    } else {
                        systemAppInfos.add(appInfo); // 将系统App添加至对应集合
                    }
                }
                // 将一个空消息（仅通知刷新）发送至主线程的消息队列
                // 主线程的handlerMessage方法将会处理这条消息
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    // 定义一个内部类继承自BaseAdapter
    // 这是ListView的数据适配器
    // （实现应用信息加载并展示）
    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfos.size();
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
        // 由系统调用，来获取一个View对象，作为ListView的条目
        public View getView(int position, View view, ViewGroup viewGroup) {
            // 根据条目位置i获取AppInfo对象
            final AppInfo info = appInfos.get(position); // ————～～～
            // 使用ViewHolder优化————
            ViewHolder holder;
            View v;
            // 判断条目是否有缓存
            if (view == null) {
                holder = new ViewHolder();
                // 没有缓存时，才会将布局文件填充成一个View对象（然后加载数据）
                v = View.inflate(mActivity, R.layout.item_app_info, null);
                // 通过资源id查找组件
                holder.appIcon = (ImageView) v.findViewById(R.id.iv_app_icon);
                holder.appName = (TextView) v.findViewById(R.id.tv_app_name);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
                v = view;
            }
            // 获取数据并显示至对应组件
            holder.appIcon.setBackground(info.getAppIcon());
            holder.appName.setText(info.getAppName());

            // 设置点击侦听（点击操作）
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.i("应用计时", "弹出对话框");
                    showChooseDialog(); // 弹出对话框
                }
            });

            return v;
        }
    }

    // ViewHolder
    private static class ViewHolder {
        ImageView appIcon;
        TextView appName;
    }

    // 可选的类型—————————————————————————————————————————
    final String[] styles = new String[]{"正常", "目标", "忽略"};
    final String[] targets = new String[]{"1hour", "2hour", "3hour", "4hour", "5hour", "6hour"};

    // 弹出选择对话框
    private void showChooseDialog() {
        // ————————
        final SharedPreferences.Editor editor = Application.config.edit();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        // 设置图标和标题
        builder.setIcon(R.drawable.management); // 图标
        builder.setTitle("计时类型"); // 标题
        // 获取timing_style
        int style = Application.config.getInt("timing_style", 0);

        View dialog_view = View.inflate(mActivity, R.layout.dialog_management, null);
        builder.setView(dialog_view);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            // 提交选择内容（确认）
            public void onClick(DialogInterface dialogInterface, int i) {
                // 保存address_style（提交）
                editor.apply();
                // 刷新Item当前状态（此处i不是用户选择，choice才是）
//                si_set_style.setStatus(styles[style_choice]);
//                dialogInterface.dismiss(); // 遣散
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            // 返回设置中心（取消）
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // 遣散
            }
        }).show();


//        // 定义为单选框并设置点击侦听（选择类型）
//        builder.setSingleChoiceItems(styles, style, new DialogInterface.OnClickListener() {
//
//            @Override
//            // 选择内容（单选）
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // 保存timing_style（未提交）
//                editor.putInt("address_style", i);
//                // 记录用户选择
//                style_choice = i;
//            }
//        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//            @Override
//            // 提交选择内容（确认）
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // 保存address_style（提交）
//                editor.apply();
//                // 刷新Item当前状态（此处i不是用户选择，choice才是）
////                si_set_style.setStatus(styles[style_choice]);
////                dialogInterface.dismiss(); // 遣散
//            }
//        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            // 返回设置中心（取消）
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss(); // 遣散
//            }
//        }).show();
    }
}
