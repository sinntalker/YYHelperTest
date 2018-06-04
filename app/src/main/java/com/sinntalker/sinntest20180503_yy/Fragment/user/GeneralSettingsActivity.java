package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

public class GeneralSettingsActivity extends Activity {

    ImageView mBackGSIV; //返回
    Switch mAgreeMesS; //加好友需要验证
    Switch mNoticeFS; //推荐通讯录朋友
    Switch mNoticeMesS; //接收新消息提醒
    Switch mAlarmS; //接收新消息震动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_general_settings);

        mBackGSIV = findViewById(R.id.id_imageView_back_generalSetting);
        mAgreeMesS = findViewById(R.id.id_switch_addFriend_generalSetting);
        mNoticeFS = findViewById(R.id.id_switch_noticeContact_generalSetting);
        mNoticeMesS = findViewById(R.id.id_switch_noticeNew_generalSetting);
        mAlarmS = findViewById(R.id.id_switch_alarmNew_generalSetting);

        mAgreeMesS.setChecked(true);
        mNoticeFS.setChecked(true);
        mNoticeMesS.setChecked(true);
        mAlarmS.setChecked(true);

        mBackGSIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAgreeMesS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "开启验证", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭验证", Toast.LENGTH_LONG).show();
                }
            }
        });

        mNoticeFS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "开启推荐好友", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭推荐好友", Toast.LENGTH_LONG).show();
                }
            }
        });

        mNoticeMesS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "接收新消息提醒", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭新消息提醒", Toast.LENGTH_LONG).show();
                }
            }
        });

        mAlarmS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "接受新消息振动", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "关闭新消息振动", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
