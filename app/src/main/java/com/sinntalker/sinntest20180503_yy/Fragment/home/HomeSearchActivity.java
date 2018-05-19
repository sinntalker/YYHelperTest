package com.sinntalker.sinntest20180503_yy.Fragment.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.sinntalker.sinntest20180503_yy.R;

public class HomeSearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_home_search);
    }
}
