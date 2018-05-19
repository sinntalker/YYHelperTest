package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

public class SetAlarmActivity extends Activity implements View.OnClickListener{

    private ImageView mBackSAIV; //返回按钮
    private TextView drugName; //药品名称
    private TextView drugDosage; //药品用法用量
    private Switch alarmUsing_switch; //用药提醒
    private Switch alarmBuy_switch; //买药提醒
    private EditText preSetNum_edit; //预设吃药数量
    private EditText daysBuyAlarm_edit; //复购天数倒计时设计
    private Button firstAlarm_btn; //第一次提醒
    private Button secondAlarm_btn; //第二次提醒
    private Button thirdAlarm_btn; //第三次提醒
    private Button forthAlarm_btn; //第四次提醒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_set_alarm);

        //实例化
        mBackSAIV = findViewById(R.id.id_imageView_back_setAlarm);
        drugName = findViewById(R.id.drugName_textView);
        drugDosage = findViewById(R.id.dosage_textView);
        alarmUsing_switch = findViewById(R.id.drugUsingAlarm_switch);
        alarmBuy_switch = findViewById(R.id.drugBuyAlarm_switch);
        preSetNum_edit = findViewById(R.id.preSetNum_editText);
        daysBuyAlarm_edit = findViewById(R.id.daysBuyAlarm_editText);
        firstAlarm_btn = findViewById(R.id.firstAlarm_button);
        secondAlarm_btn = findViewById(R.id.secondAlarm_button);
        thirdAlarm_btn = findViewById(R.id.thirdAlarm_button);
        forthAlarm_btn= findViewById(R.id.forthAlarm_button);
        // 从Intent获取数据
        String phone = getIntent().getStringExtra("drug_user"); //药品所有者 phone
        String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName
        String dosage = getIntent().getStringExtra("drug_dosage");//用法用量 dosage

        //设置数据
        drugName.setText(genericName);
        drugDosage.setText(dosage);

        //设置用药预设功能不可编辑
        preSetNum_edit.setFocusable(false);
        preSetNum_edit.setFocusableInTouchMode(false);
        //设置复购天数功能不可编辑
        daysBuyAlarm_edit.setFocusable(false);
        daysBuyAlarm_edit.setFocusableInTouchMode(false);

        //设置监听
        mBackSAIV.setOnClickListener(this);
        alarmUsing_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "开启用药提醒", Toast.LENGTH_LONG).show();
                    preSetNum_edit.setFocusableInTouchMode(true);
                    preSetNum_edit.setFocusable(true);
                    preSetNum_edit.requestFocus();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭用药提醒", Toast.LENGTH_LONG).show();
                    preSetNum_edit.setFocusable(false);
                    preSetNum_edit.setFocusableInTouchMode(false);
                }
            }
        });
        alarmBuy_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "开启复购提醒", Toast.LENGTH_LONG).show();
                    daysBuyAlarm_edit.setFocusableInTouchMode(true);
                    daysBuyAlarm_edit.setFocusable(true);
                    daysBuyAlarm_edit.requestFocus();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭用药提醒", Toast.LENGTH_LONG).show();
                    daysBuyAlarm_edit.setFocusable(false);
                    daysBuyAlarm_edit.setFocusableInTouchMode(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == mBackSAIV) {
            finish();
        }
    }
}
