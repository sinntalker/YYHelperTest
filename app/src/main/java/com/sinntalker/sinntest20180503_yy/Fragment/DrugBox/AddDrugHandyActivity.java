package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sinntalker.sinntest20180503_yy.R;

public class AddDrugHandyActivity extends Activity {

    ImageView mBackADHIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_drug_handy);

        mBackADHIV = findViewById(R.id.id_imageview_back_addDrugHandy);

        mBackADHIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
