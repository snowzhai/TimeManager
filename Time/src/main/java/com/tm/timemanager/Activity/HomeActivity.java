package com.tm.timemanager.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tm.timemanager.R;
import com.tm.timemanager.Service.Lookservice;
import com.tm.timemanager.application.MyApplication;
import com.tm.timemanager.fragment.ContentFragment;
import com.tm.timemanager.fragment.LeftMenuFragment;
import com.tm.timemanager.fragment.ManagementFragment;
import com.tm.timemanager.fragment.SettingFragment;
import com.tm.timemanager.fragment.TrendFragment;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends SlidingFragmentActivity {

    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int phoneWidth = MyApplication.getPhoneWidth(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        SlidingMenu slidingMenu = getSlidingMenu();
        setBehindContentView(R.layout.layout_leftmenu);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffset(phoneWidth / 2);
        initFragment();

        //开启收集数据的服务
        Intent intent = new Intent(this, Lookservice.class);
        startService(intent);

       /* DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(this);
        for (int i=0;i<10;i++){
            dbOpenHelperdao.insertBlackNumber("haha",1111,1111,i);
        }*/


    }

    public void getimage(View view) {
        startActivity(new Intent(this, MytestActivity.class));
    }

    private void initFragment() {

        //layout_leftmenu和activity_home都是空的FrameLayout，用fragment去替换
        fragmentManager = getFragmentManager(); // 改为全局
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_content, new ContentFragment(), "contentfragment");
        fragmentTransaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), "leftmenufragment");
        fragmentTransaction.commit();

//        Fragment fragmentByTag = fragmentManager.findFragmentByTag();
    }

    //通过tag获取主界面activity的fragment，方便后面调用
    public Fragment getFragment(String tag) {

        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager.findFragmentByTag(tag); // 直接返回
    }

    // 替换新的fragment，供外界调用（i对应左侧边栏按钮，比如i=2时替换为ManagementFragment）
    public void replaceFragment(int i) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (i) {
            case 0:
                fragmentTransaction.replace(R.id.fl_main_content, new ContentFragment());
                fragmentTransaction.commit(); // 提交事务
                break;
            case 1:
                fragmentTransaction.replace(R.id.fl_main_content, new TrendFragment());
                fragmentTransaction.commit(); // 提交事务
                break;
            case 2:
                fragmentTransaction.replace(R.id.fl_main_content, new ManagementFragment());
                fragmentTransaction.commit(); // 提交事务
                break;
            case 3:
                fragmentTransaction.replace(R.id.fl_main_content, new SettingFragment());
                fragmentTransaction.commit(); // 提交事务
                break;
            default:
                // ...
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }


    //主界面从新获取焦点是重新加载
    @Override
    protected void onResume() {
        super.onResume();
        initFragment();
    }
    public void skip(View view){

        startActivity(new Intent(HomeActivity.this,SimpleAdapterActivity.class));

    }
}
