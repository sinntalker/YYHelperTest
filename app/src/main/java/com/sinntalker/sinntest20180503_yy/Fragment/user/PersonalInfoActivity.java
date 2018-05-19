package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sinntalker.sinntest20180503_yy.R;

public class PersonalInfoActivity extends Activity {

    ImageView mBackPIIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_personal_info);

        //
        mBackPIIV = findViewById(R.id.id_imageView_back_personalInfo);

        mBackPIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
