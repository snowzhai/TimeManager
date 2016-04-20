package com.tm.timemanager.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.tm.timemanager.R;
import com.tm.timemanager.dao.DBOpenHelperdao;

public class MytestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_te);


    }
    public void get(View view){

        DBOpenHelperdao dbOpenHelperdao = new DBOpenHelperdao(this);
        Bitmap getimagefrom = dbOpenHelperdao.getimagefrom("8");
        ImageView imageView = (ImageView) findViewById(R.id.zhai);
        imageView.setImageBitmap(getimagefrom);
    }
}
