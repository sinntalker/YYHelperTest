package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.sinntalker.sinntest20180503_yy.R;

public class UserInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_user_info);
    }
}
