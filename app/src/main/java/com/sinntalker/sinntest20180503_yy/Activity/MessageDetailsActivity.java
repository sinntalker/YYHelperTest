package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.R;

public class MessageDetailsActivity extends Activity {

    ImageView mBackMDIV;
    TextView mMessageTypeMDTV;

    ListView mAllMDLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_message_details);

        int messageTypeId = (getIntent().getIntExtra("MessageType",0));

        mBackMDIV = findViewById(R.id.id_imageView_back_messageDetail);
        mMessageTypeMDTV = findViewById(R.id.id_textView_messageType_messageDetail);

        mAllMDLV = findViewById(R.id.id_list_item_message_details);

        init(messageTypeId);

        mBackMDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(int Id){
        if(Id == 0) {
            mMessageTypeMDTV.setText("用药消息");
            //向服务器请求信息

        }else if(Id == 1) {
            mMessageTypeMDTV.setText("健康消息");
            //向服务器请求信息

        }else if(Id == 2) {
            mMessageTypeMDTV.setText("药品过期提醒");
            //向服务器请求信息

        }else if(Id == 3) {
            mMessageTypeMDTV.setText("添加消息");
            //向服务器请求信息

        }else if(Id == 4) {
            mMessageTypeMDTV.setText("系统消息");
            //向服务器请求信息

        }else {
            mMessageTypeMDTV.setText("用药消息");
            //向服务器请求信息

        }
    }
}
