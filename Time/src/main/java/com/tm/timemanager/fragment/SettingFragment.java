package com.tm.timemanager.fragment;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tm.timemanager.R;
import com.tm.timemanager.application.MyApplication;

/**
 * Created by CHENQIAO on 2016/4/21.
 */
public class SettingFragment extends BaseFragment {

    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.fragment_setting, null);
        final Switch swith = (Switch) view.findViewById(R.id.switch1);
        swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    MyApplication.setapptime("isChecked",!isChecked);
                    Log.i("哈哈1",""+isChecked);
                }else {
                    MyApplication.setapptime("isChecked",!isChecked);
                    Log.i("哈哈2",""+isChecked);
                }
            }
        });


        return view;
    }
}
