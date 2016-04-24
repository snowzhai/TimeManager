package com.tm.timemanager.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tm.timemanager.R;

public class SetingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        final Switch swith = (Switch) findViewById(R.id.switch1);
        swith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.i("哈哈1",""+isChecked);
                }else {
                    Log.i("哈哈2",""+isChecked);
                }
            }
        });
    }
}
