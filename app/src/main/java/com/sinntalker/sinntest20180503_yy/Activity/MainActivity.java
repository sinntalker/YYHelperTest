package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.AlarmBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.DrugUsingDataBean;
import com.sinntalker.sinntest20180503_yy.Fragment.DrugBox.ReceiveAlarm;
import com.sinntalker.sinntest20180503_yy.Fragment.family.FamilyFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriendManager;
import com.sinntalker.sinntest20180503_yy.Fragment.family.event.RefreshEvent;
import com.sinntalker.sinntest20180503_yy.Fragment.family.util.IMMLeaks;
import com.sinntalker.sinntest20180503_yy.Fragment.health.HealthFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.home.HomeFragment;
import com.sinntalker.sinntest20180503_yy.Fragment.user.UserFragment;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Sql.MessageDataBean;
import com.sinntalker.sinntest20180503_yy.Sql.util.MessageDataBeanDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static MainActivity test_a = null;
    private List<MessageDataBean> mDatas;
    private static int FLAG_MESSAGE_ISCHECKED = 0;

    AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
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

        //查询当前用户
        query_drugUsingData();
        query_drugAlarmData();
    }

    private void query_drugAlarmData() {
        BmobQuery<AlarmBean> query1 = new BmobQuery<AlarmBean>();
        query1.addWhereEqualTo("user", user);
        query1.findObjects(new FindListener<AlarmBean>() {
            @Override
            public void done(List<AlarmBean> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i ++) {
                        Intent intent =new Intent(MainActivity.this, ReceiveAlarm.class);
                        intent.setAction("short");
                        intent.putExtra("msg", "该吃" + list.get(i).getDrug() + "药了," + list.get(i).getDosage() + "。");
                        intent.putExtra("drugNum", list.get(i).getDrugNum());
                        intent.putExtra("drugName", list.get(i).getDrug());
                        intent.putExtra("user", user.getUsername());
                        intent.putExtra("timeH", list.get(i).getTimeH());
                        intent.putExtra("timeM", list.get(i).getTimeM());
                        PendingIntent sender=
                                PendingIntent.getBroadcast(MainActivity.this, list.get(i).getRequestCode(), intent, 0);
                        Log.i("bmob", "闹钟回调 requestCode:" + list.get(i).getRequestCode());
                        //设定一个五秒后的时间
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                                (Calendar.DAY_OF_MONTH), list.get(i).getTimeH(), list.get(i).getTimeM(), 5);
                        Log.i("bmob", "闹钟回调 时间:" + list.get(i).getTimeH() + " XXXXX " + list.get(i).getTimeM());
                        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);

                        Calendar c_cur = Calendar.getInstance();
                        if(c_cur.getTimeInMillis() >  calendar.getTimeInMillis()){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        }
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    }
                } else {
                    Log.i("bmob", "获取闹钟信息失败：" + e.getMessage() + e.getErrorCode());
                }
            }
        });
    }

    public MessageDataBeanDao getMessageDataBeanDao() {
        // 通过 MyApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((BmobIMApplication) this.getApplicationContext()).getDaoSession().getMessageDataBeanDao();
    }

    public SQLiteDatabase getDb() {
        // 通过 MyApplication 类提供的 getDb() 获取具体 db
        return ((BmobIMApplication) this.getApplicationContext()).getDb();
    }

    private void query_drugUsingData() {
        BmobQuery<DrugUsingDataBean> query = new BmobQuery<DrugUsingDataBean>();
        query.addWhereEqualTo("userName", user.getUsername());
        query.findObjects(new FindListener<DrugUsingDataBean>() {
            @Override
            public void done(List<DrugUsingDataBean> list, BmobException e) {
                if (e == null) {
//                    Toast.makeText(MainActivity.this, "刷新成功",Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "DrugUsingDataBean获取用药信息成功" + list.size());
                    if (list.size() == 0) {
                        getMessageDataBeanDao().deleteAll();
                    } else if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i ++) {
//                            if (list.get(i).getRemindUsingDrug().equals("true"))
                            {
                                Log.i("bmob", "list.get(i).getRemindUsingDrug()" + list.get(i).getRemindUsingDrug());
                                if (!list.get(i).getUsingDrugTimeNo1().equals("未设置")) {
                                    Log.i("bmob", "AlarmBean获取用药信息成功");
                                    AlarmBean alarmBean = new AlarmBean();
                                    alarmBean.setUser(user);
                                    alarmBean.setDrugNum(Integer.valueOf(list.get(i).getDrugNumber()));
                                    alarmBean.setDrug(list.get(i).getGenericName());
                                    alarmBean.setDosage("每次" + list.get(i).getUsingDrugNumber() + "片");
                                    alarmBean.setNum(1);
                                    alarmBean.setTimeH(Integer.valueOf(list.get(i).getUsingDrugTimeNo1().substring(0, 2)));
                                    alarmBean.setTimeM(Integer.parseInt(list.get(i).getUsingDrugTimeNo1().substring(3)));
                                    alarmBean.setTouched(false);
                                    alarmBean.setTouchedTime("");
                                    alarmBean.setRequestCode(i*4);
                                    alarmBean.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Log.i("bmob", "AlarmBean获取用药信息成功1");
                                            } else {
                                                Log.i("bmob", "AlarmBean获取用药信息失败1：" + e.getMessage() + e.getErrorCode());
                                            }
                                        }
                                    });
                                }
                                if (!list.get(i).getUsingDrugTimeNo2().equals("未设置")) {
                                    AlarmBean alarmBean = new AlarmBean();
                                    alarmBean.setUser(user);
                                    alarmBean.setDrugNum(Integer.valueOf(list.get(i).getDrugNumber()));
                                    alarmBean.setDrug(list.get(i).getGenericName());
                                    alarmBean.setDosage("每次" + list.get(i).getUsingDrugNumber() + "片");
                                    alarmBean.setNum(2);
                                    alarmBean.setTimeH(Integer.valueOf(list.get(i).getUsingDrugTimeNo2().substring(0, 2)));
                                    alarmBean.setTimeM(Integer.parseInt(list.get(i).getUsingDrugTimeNo2().substring(3)));
                                    alarmBean.setTouched(false);
                                    alarmBean.setTouchedTime("");
                                    alarmBean.setRequestCode(i*4+1);
                                    alarmBean.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Log.i("bmob", "AlarmBean获取用药信息成功2");
                                            } else {
                                                Log.i("bmob", "AlarmBean获取用药信息失败2：" + e.getMessage() + e.getErrorCode());
                                            }
                                        }
                                    });
                                }
                                if (!list.get(i).getUsingDrugTimeNo3().equals("未设置")) {
                                    AlarmBean alarmBean = new AlarmBean();
                                    alarmBean.setUser(user);
                                    alarmBean.setDrugNum(Integer.parseInt(list.get(i).getDrugNumber()));
                                    alarmBean.setDrug(list.get(i).getGenericName());
                                    alarmBean.setDosage("每次" + list.get(i).getUsingDrugNumber() + "片");
                                    alarmBean.setNum(3);
                                    alarmBean.setTimeH(Integer.valueOf(list.get(i).getUsingDrugTimeNo3().substring(0, 2)));
                                    alarmBean.setTimeM(Integer.parseInt(list.get(i).getUsingDrugTimeNo3().substring(3)));
                                    alarmBean.setTouched(false);
                                    alarmBean.setTouchedTime("");
                                    alarmBean.setRequestCode(i*4+2);
                                    alarmBean.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Log.i("bmob", "AlarmBean获取用药信息成功3");
                                            } else {
                                                Log.i("bmob", "AlarmBean获取用药信息失败3：" + e.getMessage() + e.getErrorCode());
                                            }
                                        }
                                    });
                                }
                                if (!list.get(i).getUsingDrugTimeNo4().equals("未设置")) {
                                    AlarmBean alarmBean = new AlarmBean();
                                    alarmBean.setUser(user);
                                    alarmBean.setDrugNum(Integer.parseInt(list.get(i).getDrugNumber()));
                                    alarmBean.setDrug(list.get(i).getGenericName());
                                    alarmBean.setDosage("每次" + list.get(i).getUsingDrugNumber() + "片");
                                    alarmBean.setNum(4);
                                    alarmBean.setTimeH(Integer.valueOf(list.get(i).getUsingDrugTimeNo4().substring(0, 2)));
                                    alarmBean.setTimeM(Integer.parseInt(list.get(i).getUsingDrugTimeNo4().substring(3)));
                                    Log.i("bmob", "getUsingDrugTimeNo4  hour:" + list.get(i).getUsingDrugTimeNo4().substring(0, 2));
                                    Log.i("bmob", "getUsingDrugTimeNo4  hour:" + list.get(i).getUsingDrugTimeNo4().substring(3));
                                    alarmBean.setTouched(false);
                                    alarmBean.setTouchedTime("");
                                    alarmBean.setRequestCode(i*4+3);
                                    alarmBean.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Log.i("bmob", "AlarmBean获取用药信息成功4");
                                            } else {
                                                Log.i("bmob", "AlarmBean获取用药信息失败4：" + e.getMessage() + e.getErrorCode());
                                            }
                                        }
                                    });
                                }
                            }
//                            else {
//                                Log.i("bmob", "获取用药信息失败：" + e.getMessage() + e.getErrorCode());
//                            }
                        }
                    }
                } else {
                    Log.i("bmob", "获取用药信息失败：" + e.getMessage() + e.getErrorCode());
//                    Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        //检查是否有未读用药消息
        checkUsingDrugData();
        //每次进来应用都检查会话和好友请求的情况
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();

        if (FLAG_MESSAGE_ISCHECKED == 1) {
            contact_image.setVisibility(View.VISIBLE);
        } else {
            contact_image.setVisibility(View.GONE);
        }
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
            FLAG_MESSAGE_ISCHECKED = 1;
            Log.i("bmob", "好友添加请求"  + FLAG_MESSAGE_ISCHECKED);
        } else {
            FLAG_MESSAGE_ISCHECKED = 3;
            contact_image.setVisibility(View.GONE);
            Log.i("bmob", "无好友添加请求"  + FLAG_MESSAGE_ISCHECKED);
        }
    }

    private void checkUsingDrugData() {
        mDatas = new ArrayList<MessageDataBean>();
        //查询 并且出生在1970的用户
//        Query query = getMessageDataBeanDao().queryBuilder()
//                .where(MessageDataBeanDao.Properties.User.eq(user.getUsername()))
//                .orderAsc(MessageDataBeanDao.Properties.Id)
//                .build();
//        mDatas = query.list();
        BmobQuery<MessageDataBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user.getUsername());
        query.findObjects(new FindListener<MessageDataBean>() {
            @Override
            public void done(List<MessageDataBean> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        FLAG_MESSAGE_ISCHECKED = 2;
                        Log.i("bmob", "不存在未读消息" + FLAG_MESSAGE_ISCHECKED);
                        contact_image.setVisibility(View.GONE);
                    } else {
                        int i = 0;
                        for (; i < list.size(); i ++) {
                            if (!list.get(i).getIsTouched()) { //如果是false的话
                                FLAG_MESSAGE_ISCHECKED = 1;
                                contact_image.setVisibility(View.VISIBLE);
                                Log.i("bmob", "存在未读消息" + FLAG_MESSAGE_ISCHECKED);
                                break;
                            }
                        }
                        if (FLAG_MESSAGE_ISCHECKED != 1) {
                            FLAG_MESSAGE_ISCHECKED = 2;
                            contact_image.setVisibility(View.GONE);
                            Log.i("bmob", "不存在未读消息" + FLAG_MESSAGE_ISCHECKED);
                        }
                    }
                } else {
                    Log.i("bmob", "查询未读消息失败");
                }
            }
        });
//        if (mDatas.size() > 0) {
//            FLAG_MESSAGE_ISCHECKED = 1;
//            Log.i("bmob", "存在未读消息" + FLAG_MESSAGE_ISCHECKED);
////            contact_image.setVisibility(View.VISIBLE);
//        } else {
//            FLAG_MESSAGE_ISCHECKED = 2;
//            Log.i("bmob", "不存在未读消息" + FLAG_MESSAGE_ISCHECKED);
////            contact_image.setVisibility(View.GONE);
//        }
//        //查询first name=Maria并且出生在1977的用户
//        query.setParameter(0, "Maria");
//        query.setParameter(1, 1977);
//        List mariasOf1977 = query.list();
    }

}
