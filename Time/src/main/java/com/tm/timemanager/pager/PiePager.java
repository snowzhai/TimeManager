package com.tm.timemanager.pager;

import android.app.Activity;
import android.view.View;

import com.tm.timemanager.R;

/**
 * Created by CHENQIAO on 2016/4/20.
 */
public class PiePager extends BasePager{


    public View mView;
    public Activity mActivity;

    public PiePager(Activity activity) {
        super(activity);
        mActivity = activity ;
        initView();
    }


    public void initView() {
        mView = View.inflate(mActivity, R.layout.pager_pie, null);

    }

}
