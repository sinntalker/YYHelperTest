package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.NewFriendActivity;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Sql.MessageDataBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MessageActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackMIV; //返回按键

    TableRow mDrugUsageMessageTR; //用药消息
    TextView mDrugUsageMessageNewTV; //用药消息
    ImageView mTipDrugUsingIV; //红点-未读提醒

    TableRow mHealthMessageTR; //健康消息
    TextView mHealthMessageNewTV; //健康消息
    ImageView mTipHealthIV; //红点-未读提醒

    TableRow mDrugValidityMessageTR; //药品过期消息
    TextView mDrugValidityMessageNewTV; //药品过期消息
    ImageView mTipDrugValuedIV; //红点-未读提醒

    TableRow mAddMemberMessageTR; //添加消息
    TextView mAddMemberMessageNewTV; //添加消息
    ImageView mTipAddMemberIV; //红点-未读提醒

    TableRow mSystemMessageTR; //系统消息
    TextView mSystemMessageNewTV; //系统消息
    ImageView mTipSystemIV; //红点-未读提醒

    AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);

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
        mTipDrugUsingIV = findViewById(R.id.id_imageView_tips_drugUsing_message);
        mTipDrugUsingIV.setVisibility(View.GONE);
        //健康信息
        mHealthMessageTR = findViewById(R.id.id_tableRow_health_message);
        mHealthMessageNewTV = findViewById(R.id.id_textView_new_health_message);
        mTipHealthIV = findViewById(R.id.id_imageView_tips_health_message);
        mTipHealthIV.setVisibility(View.GONE);
        //药品过期消息
        mDrugValidityMessageTR = findViewById(R.id.id_tableRow_drugValidity_message);
        mDrugValidityMessageNewTV = findViewById(R.id.id_textView_new_drugValidity_message);
        mTipDrugValuedIV = findViewById(R.id.id_imageView_tips_drugValued_message);
        mTipDrugValuedIV.setVisibility(View.GONE);
        //添加消息
        mAddMemberMessageTR = findViewById(R.id.id_tableRow_addMember_message);
        mAddMemberMessageNewTV = findViewById(R.id.id_textView_new_addMember_message);
        mTipAddMemberIV = findViewById(R.id.id_imageView_tips_addFriend_message);
        mTipAddMemberIV.setVisibility(View.GONE);
        //系统消息
        mSystemMessageTR = findViewById(R.id.id_tableRow_system_message);
        mSystemMessageNewTV = findViewById(R.id.id_textView_new_system_message);
        mTipSystemIV = findViewById(R.id.id_imageView_tips_system_message);
        mTipSystemIV.setVisibility(View.GONE);

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
            Log.i("bmob","添加消息");
            Toast.makeText(getApplicationContext(), "添加消息", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), MessageDetailsActivity.class).putExtra("MessageType", 3));
            startActivity(new Intent(getApplicationContext(), NewFriendActivity.class));
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
        //查询数据
        query_drugUsing();
    }

    private void query_drugUsing() {
        BmobQuery<MessageDataBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user.getUsername());
        query.findObjects(new FindListener<MessageDataBean>() {
            @Override
            public void done(List<MessageDataBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mTipDrugUsingIV.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < list.size(); i ++) {
                            if (!list.get(i).getIsTouched()) { //如果是false的话
                                mTipDrugUsingIV.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        mDrugUsageMessageNewTV.setText("该吃" + list.get(list.size()-1).getDrug() + "药了， 用法："
                                + list.get(list.size()-1).getDosage());
                    }
                } else {
                    Log.i("bmob", "查询未读消息失败");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //查询数据
        query_drugUsing();
    }
}
