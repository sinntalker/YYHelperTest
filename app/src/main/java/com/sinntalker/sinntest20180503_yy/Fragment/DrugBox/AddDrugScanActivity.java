package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.sinntalker.sinntest20180503_yy.R;

public class AddDrugScanActivity extends Activity {

    ImageView mBackADSIV;
    TextView mScanResultADSTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_drug_scan);

        String scanResult = getIntent().getStringExtra("ScanResult");

        //
        mBackADSIV = findViewById(R.id.id_imageView_back_addDrugScan);
        mScanResultADSTV = findViewById(R.id.id_textView_scanResult_addDrugScan);

        mScanResultADSTV.setText(scanResult);

        mBackADSIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
