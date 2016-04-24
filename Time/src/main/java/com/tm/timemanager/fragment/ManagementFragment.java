package com.tm.timemanager.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tm.timemanager.Activity.HomeActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.bean.AppInfo;
import com.tm.timemanager.bean.AppInfos;

import java.util.List;

public class ManagementFragment extends BaseFragment implements NumberPicker.OnValueChangeListener,
        NumberPicker.OnScrollListener, NumberPicker.Formatter {

    // 全局变量
    private ListView listView;
    private List<AppInfo> allAppInfos;

    private View dialog_view;
//    private ListView targetListView;
//    private TargetListAdapter tAdapter;

    private RadioGroup rg_buttons;
    private NumberPicker targetPicker1;
    private NumberPicker targetPicker2;
    private LinearLayout linearLayout;
    private RadioButton first;
    private RadioButton second;
    private RadioButton third;

    @Override
    public View initViews() {
        // 将布局文件填充成一个View对象
        View view = View.inflate(mActivity, R.layout.fragment_management, null);
        // 应用管理界面的的ListView————
        listView = (ListView) view.findViewById(R.id.app_list_view);
        // 对话框的View对象
        dialog_view = View.inflate(mActivity, R.layout.dialog_management, null);
        // 对话框RadioGroup
        rg_buttons = (RadioGroup) dialog_view.findViewById(R.id.rg_buttons);
        // 滚动条NumberPicker父控件
        linearLayout = (LinearLayout) dialog_view.findViewById(R.id.ll_picker_parent);
        // "目标"滚动条NumberPicker
        targetPicker1 = (NumberPicker) dialog_view.findViewById(R.id.target_picker1);
        targetPicker2 = (NumberPicker) dialog_view.findViewById(R.id.target_picker2);

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
        Log.i("呵呵", "数据初始化");
        initMyData();
    }

    // Handler对象
    Handler handler = new Handler() {
        // 此方法在主线程中被调用，刷新UI
        public void handleMessage(Message msg) {
            // new一个AppManagerAdapter
            AppManagerAdapter adapter = new AppManagerAdapter();
            listView.setAdapter(adapter);
            // new一个TargetListAdapter
//            tAdapter = new TargetListAdapter();
        }
    };

    // 初始化数据
    private void initMyData() {
        // 子线程负责耗时操作
        new Thread() {

            @Override
            public void run() {
                // 调用AppInfos的getAppInfos方法获取所有App信息
                allAppInfos = AppInfos.getAppInfos(mActivity);
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
            return allAppInfos.size();
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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            // 根据条目位置i获取AppInfo对象
            final AppInfo info = allAppInfos.get(position); // ————～～～
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
            holder.appIcon.setImageBitmap(info.getAppIcon());
            holder.appName.setText(info.getAppName());
            final String appPackageName = info.getPackageName();

            // 设置点击侦听（点击操作）
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.i("呵呵", "弹出对话框");
                    showChooseDialog(appPackageName);
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

    // ——————————————————————————

    // 弹出选择对话框
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showChooseDialog(String appPackageName) {

        // "目标"滚动条ListView
//        targetListView = (ListView) dialog_view.findViewById(R.id.target_list_view);

        // 初始化时间滚动条
        initPicker(targetPicker1, targetPicker2);

        // 对话框默认选择（根据本地数据库来决定）
        rg_buttons.check(R.id.rb_first);
        // 获得所有的RadioButton对象
        first = (RadioButton) dialog_view.findViewById(R.id.rb_first);
        second = (RadioButton) dialog_view.findViewById(R.id.rb_second);
        third = (RadioButton) dialog_view.findViewById(R.id.rb_third);
        // 进入对话框时先判断"目标"Button是否被选中
        if (second.isChecked()) {
            linearLayout.setVisibility(View.VISIBLE);
        }

        // 监听底部按钮的勾选状态————
        rg_buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // 根据该状态设置当前页面
                switch (checkedId) {
                    case R.id.rb_first:
                        linearLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_second:
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_third:
                        linearLayout.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        // ————————
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        // 展示滚动条
//        targetListView.setAdapter(tAdapter);
        // 设为对话框
        builder.setView(dialog_view);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                ((ViewGroup) dialog_view.getParent()).removeView(dialog_view);
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            // 提交选择内容（确认）
            public void onClick(DialogInterface dialogInterface, int i) {
                if (second.isChecked()) {
                    int value1 = targetPicker1.getValue();
                    int value2 = targetPicker2.getValue();
                    int targetValue = (value1 * 60 + value2) * 60;
                    Log.i("呵呵", value1 + ":" + value2);
                } else if (first.isChecked()) {
                    Log.i("呵呵", "计时类型：正常");
                } else if (third.isChecked()) {
                    Log.i("呵呵", "计时类型：忽略");
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            // 返回软件管理界面（取消）
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // 遣散
                // 移除dialog_view
//                ((ViewGroup) dialog_view.getParent()).removeView(dialog_view);
            }
        }).show();
    }

    // 初始化NumberPicker
    private void initPicker(NumberPicker picker1, NumberPicker picker2) {
        picker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 不可编辑
        picker1.setFormatter(this);
        picker1.setOnScrollListener(this);
        picker1.setOnValueChangedListener(this);
        picker1.setMaxValue(23);
        picker1.setMinValue(0);
        picker1.setValue(0);

        picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker2.setFormatter(this);
        picker2.setOnScrollListener(this);
        picker2.setOnValueChangedListener(this);
        picker2.setMaxValue(59);
        picker2.setMinValue(0);
        picker2.setValue(30);
    }

    // 时间滚动条界面
    private void formatPickers() {
        int value1 = targetPicker1.getValue();
        targetPicker1.setValue(value1 + 1);
    }

    // 重写接口方法——————

    @Override
    // 格式化时间
    public String format(int value) {
        String tempStr = String.valueOf(value);
        if (value < 10) {
            tempStr = "0" + tempStr;
        }
        return tempStr;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        // do nothing
    }

    @Override
    // 监测值的改变
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (oldVal == 59 && newVal == 0) {
            formatPickers();
        }
    }

    // 可选的类型—————————————————————————————————————————
//    final String[] styles = new String[]{"正常", "目标", "忽略"};
    final String[] targets = new String[]{"1hour", "2hour", "3hour", "4hour", "5hour", "6hour"};

    // 定义一个内部类继承自BaseAdapter
// 这是ListView的数据适配器
// （实现"目标"滚动条加载并展示）
    private class TargetListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
//            Log.i("呵呵", targets.length + "");
            return targets.length;
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            // 使用ViewHolder优化————
            TargetViewHolder tHolder;
            View v2;
            // 判断条目是否有缓存
            if (view == null) {
                tHolder = new TargetViewHolder();
                // 没有缓存时，才会将布局文件填充成一个View对象（然后加载数据）
                v2 = View.inflate(mActivity, R.layout.item_target_list, null);
                // 通过资源id查找组件
                tHolder.target = (TextView) v2.findViewById(R.id.tv_item_target);
                v2.setTag(tHolder);
            } else {
                tHolder = (TargetViewHolder) view.getTag();
                v2 = view;
            }
            // 获取数据并显示至对应组件
            tHolder.target.setText(targets[position]);
            Log.i("目标选择", targets[position]);

            // 设置点击侦听（点击操作）
            v2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // ...
                }
            });
            return v2;
        }
    }

    // TargetViewHolder
    private static class TargetViewHolder {
        TextView target;
    }
}
