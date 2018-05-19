package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;

public class MessageActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackMIV; //返回按键

    TableRow mDrugUsageMessageTR; //用药消息
    TextView mDrugUsageMessageNewTV; //用药消息

    TableRow mHealthMessageTR; //健康消息
    TextView mHealthMessageNewTV; //健康消息

    TableRow mDrugValidityMessageTR; //药品过期消息
    TextView mDrugValidityMessageNewTV; //药品过期消息

    TableRow mAddMemberMessageTR; //添加消息
    TextView mAddMemberMessageNewTV; //添加消息

    TableRow mSystemMessageTR; //系统消息
    TextView mSystemMessageNewTV; //系统消息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_message);

        //实例化控件
        //返回
        mBackMIV = findViewById(R.id.id_imageView_back_message);
        //用药信息
        mDrugUsageMessageTR = findViewById(R.id.id_tableRow_drugUsage_message);
        mDrugUsageMessageNewTV = findViewById(R.id.id_textView_new_drugUsage_message);
        //健康信息
        mHealthMessageTR = findViewById(R.id.id_tableRow_health_message);
        mHealthMessageNewTV = findViewById(R.id.id_textView_new_health_message);
        //药品过期消息
        mDrugValidityMessageTR = findViewById(R.id.id_tableRow_drugValidity_message);
        mDrugValidityMessageNewTV = findViewById(R.id.id_textView_new_drugValidity_message);
        //添加消息
        mAddMemberMessageTR = findViewById(R.id.id_tableRow_addMember_message);
        mAddMemberMessageNewTV = findViewById(R.id.id_textView_new_addMember_message);
        //系统消息
        mSystemMessageTR = findViewById(R.id.id_tableRow_system_message);
        mSystemMessageNewTV = findViewById(R.id.id_textView_new_system_message);

        mBackMIV.setOnClickListener(this);
        mDrugUsageMessageTR.setOnClickListener(this);
        mHealthMessageTR.setOnClickListener(this);
        mDrugValidityMessageTR.setOnClickListener(this);
        mAddMemberMessageTR.setOnClickListener(this);
        mSystemMessageTR.setOnClickListener(this);

        initView();
    }

    @Override
    public void onClick(View view) {
        if (view == mBackMIV) {
            finish();
        }
        if (view == mDrugUsageMessageTR) {
            Toast.makeText(getApplicationContext(), "用药消息", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 0));
        }
        if (view == mHealthMessageTR) {
            Toast.makeText(getApplicationContext(), "健康消息", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 1));
        }
        if (view == mDrugValidityMessageTR) {
            Toast.makeText(getApplicationContext(), "药品过期消息", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 2));
        }
        if (view == mAddMemberMessageTR) {
            Toast.makeText(getApplicationContext(), "添加消息", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 3));
        }
        if (view == mSystemMessageTR) {
            Toast.makeText(getApplicationContext(), "系统消息", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 4));
        }
    }

    private void initView() {
        //获取最新消息，同时将最新消息显示在消息activity中，若没有，则显示“暂无新消息”。
        mDrugUsageMessageNewTV.setText("暂无新消息");
        mHealthMessageNewTV.setText("暂无新消息");
        mDrugValidityMessageNewTV.setText("暂无新消息");
        mAddMemberMessageNewTV.setText("暂无新消息");
        mSystemMessageNewTV.setText("暂无新消息");
    }
}
