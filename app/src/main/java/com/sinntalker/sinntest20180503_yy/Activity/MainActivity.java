package com.sinntalker.sinntest20180503_yy.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.family.FamilyFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;
import com.sinntalker.sinntest20180503_yy.Fragment.family.util.IMMLeaks;
import com.sinntalker.sinntest20180503_yy.Fragment.family.event.RefreshEvent;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriendManager;
import com.sinntalker.sinntest20180503_yy.Fragment.health.HealthFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.home.HomeFragment;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Fragment.user.UserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static MainActivity test_a = null;

    //RadioButton
    private ImageView rb_home;    //home button
    private ImageView rb_health;  //health button
    private ImageView rb_relation;//relation button
    private ImageView rb_user;     //user button

    private ImageView message_image;
    private ImageView contact_image;
    //TextView titleBar
    private TextView title_bar_txt;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    public BmobIMApplication mBmobIMApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        test_a = this;

        if (mBmobIMApplication == null) {
            mBmobIMApplication = (BmobIMApplication) getApplication();
            mBmobIMApplication.addActivity_(MainActivity.this);
        }

        initView();//实例化

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.frameLayout_content, new HomeFragment());
        transaction.commit();
//        rb_home.setChecked(true);
        title_bar_txt.setText("医药库");
        rb_home.setImageResource(R.drawable.home_blue);
        rb_health.setImageResource(R.drawable.health_gray);
        rb_relation.setImageResource(R.drawable.relatives_gray);
        rb_user.setImageResource(R.drawable.user_gray);

        final AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getUserAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    Toast.makeText(MainActivity.this, status.getMsg(), Toast.LENGTH_SHORT).show();
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());

    }

    private void initView() {
        rb_home = findViewById(R.id.rbHome);
        rb_health = findViewById(R.id.rbHealth);
        rb_relation = findViewById(R.id.rbRelation);
        rb_user = findViewById(R.id.rbUser);

        message_image = findViewById(R.id.message_image);
        contact_image = findViewById(R.id.iv_contact_tips);
        title_bar_txt = findViewById(R.id.title_bar_txt);

        rb_home.setOnClickListener(this);
        rb_health.setOnClickListener(this);
        rb_relation.setOnClickListener(this);
        rb_user.setOnClickListener(this);

        message_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == message_image) {
//            Toast.makeText(MainActivity.this, "消息提示", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, MessageActivity.class)); //打开通知消息界面
        }else {
            transaction = manager.beginTransaction();

            switch (v.getId()) {
                case R.id.rbHome:
                    transaction.replace(R.id.frameLayout_content, new HomeFragment());
                    title_bar_txt.setText("医药库");
                    rb_home.setImageResource(R.drawable.home_blue);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "医药库", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbHealth:
                    transaction.replace(R.id.frameLayout_content, new HealthFragment());
                    title_bar_txt.setText("健康管理");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_blue);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "健康", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbRelation:
                    transaction.replace(R.id.frameLayout_content, new FamilyFragment());
                    title_bar_txt.setText("家人");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_blue);
                    rb_user.setImageResource(R.drawable.user_gray);
//                    Toast.makeText(MainActivity.this, "家人", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rbUser:
                    transaction.replace(R.id.frameLayout_content, new UserFragment());
                    title_bar_txt.setText("用户中心");
                    rb_home.setImageResource(R.drawable.home_gray);
                    rb_health.setImageResource(R.drawable.health_gray);
                    rb_relation.setImageResource(R.drawable.relatives_gray);
                    rb_user.setImageResource(R.drawable.user_blue);
//                    Toast.makeText(MainActivity.this, "用户中心", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            transaction.commit();
        }
    }

//    @Override
//    public void onBackPressed() {
//        CommonUtils.showExitDialog(MainActivity.this);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }

    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
//            iv_conversation_tips.setVisibility(View.VISIBLE);
            Log.i("bmob", "有会话");
        } else {
//            iv_conversation_tips.setVisibility(View.GONE);
            Log.i("bmob", "无会话");
        }
        //TODO 好友管理：是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            contact_image.setVisibility(View.VISIBLE);
            Log.i("bmob", "好友添加请求");
        } else {
            contact_image.setVisibility(View.GONE);
            Log.i("bmob", "无好友添加请求");
        }
    }

}
