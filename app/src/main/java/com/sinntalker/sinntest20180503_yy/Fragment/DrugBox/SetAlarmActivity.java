package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SetAlarmActivity extends Activity{

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
    TextView mSaveSetSATV; //保存设置

    Boolean mAlarm_Using = false;
    Boolean mAlarm_Buy = false;

    String strUserName;

    int [] time_hour = null;
    int [] time_min = null;
    int timeH = 0;
    int timeM = 0;

    String strSetNum;

//    static int count_time = 0;
    static int count_num_request = 0;
    static int flag = 0;

    String boxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_set_alarm);

        final AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
        strUserName = mCurrentUser.getUsername();
        Log.i("bmob", "DrugBoxActivity:当前用户：" + mCurrentUser.toString());
        Log.i("bmob", "DrugBoxActivity:当前用户名称：" + strUserName);

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
        mSaveSetSATV = findViewById(R.id.id_textView_saveSet_setAlarm);

        // 从Intent获取数据
        boxNum = getIntent().getStringExtra("drug_boxNum"); //当前药箱 boxNum
        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName
        final String dosage = getIntent().getStringExtra("drug_dosage");//用法用量 dosage

        mAlarm_Using = false;
        mAlarm_Buy = false;

        //设置数据
        drugName.setText(genericName);
        drugDosage.setText(dosage);

        //设置用药预设功能不可编辑
        preSetNum_edit.setFocusable(false);
        preSetNum_edit.setFocusableInTouchMode(false);
        firstAlarm_btn.setEnabled(false);
        secondAlarm_btn.setEnabled(false);
        thirdAlarm_btn.setEnabled(false);
        forthAlarm_btn.setEnabled(false);
        //设置复购天数功能不可编辑
        daysBuyAlarm_edit.setFocusable(false);
        daysBuyAlarm_edit.setFocusableInTouchMode(false);

        //根据用户名和药箱编号和药品名称查询数据
        //查询条件1 用户名
        BmobQuery<DrugUsingDataBean> query_eq1 = new BmobQuery<DrugUsingDataBean>();
        query_eq1.addWhereEqualTo("userName", strUserName);
        //查询条件2 药箱编号
        BmobQuery<DrugUsingDataBean> query_eq2 = new BmobQuery<DrugUsingDataBean>();
        query_eq2.addWhereEqualTo("boxNumber", boxNum);
        //查询条件3 药品名称
        BmobQuery<DrugUsingDataBean> query_eq3 = new BmobQuery<DrugUsingDataBean>();
        query_eq3.addWhereEqualTo("genericName", genericName);

        //最后组装完整的and条件
        List<BmobQuery<DrugUsingDataBean>> queries = new ArrayList<BmobQuery<DrugUsingDataBean>>();
        queries.add(query_eq1);
        queries.add(query_eq2);
        queries.add(query_eq3);

        BmobQuery<DrugUsingDataBean> query = new BmobQuery<DrugUsingDataBean>();
        query.and(queries);

        query.findObjects(new FindListener<DrugUsingDataBean>() {
            @Override
            public void done(List<DrugUsingDataBean> list, BmobException e) {
                if(e==null){
                    Log.i("bmob","查询用户药品用药提醒设置数据成功。"+"共"+list.size()+"条数据");
                    //获取用药提醒 -- 提醒开关、吃药数量、时间段（1~4） -- 6项
                    preSetNum_edit.setText(list.get(0).getUsingDrugNumber());
//                    Log.i("bmob","查询用户药品用药提醒设置:getUsingDrugNumber()"+list.get(0).getUsingDrugNumber());
                    firstAlarm_btn.setText(list.get(0).getUsingDrugTimeNo1());
//                    Log.i("bmob","查询用户药品用药提醒设置:getUsingDrugTimeNo1()"+list.get(0).getUsingDrugTimeNo1());
                    secondAlarm_btn.setText(list.get(0).getUsingDrugTimeNo2());
//                    Log.i("bmob","查询用户药品用药提醒设置:getUsingDrugTimeNo2()"+list.get(0).getUsingDrugTimeNo2());
                    thirdAlarm_btn.setText(list.get(0).getUsingDrugTimeNo3());
//                    Log.i("bmob","查询用户药品用药提醒设置:getUsingDrugTimeNo3()"+list.get(0).getUsingDrugTimeNo3());
                    forthAlarm_btn.setText(list.get(0).getUsingDrugTimeNo4());
//                    Log.i("bmob","查询用户药品用药提醒设置:getUsingDrugTimeNo4()"+list.get(0).getUsingDrugTimeNo4());
                    //复购提醒 -- 提醒开关、提醒开始时间、倒计时天数 -- 3项
                    mAlarm_Buy = Boolean.valueOf(list.get(0).getRemindBuyDrug());
                    daysBuyAlarm_edit.setText(list.get(0).getDayLeft());
//                    Log.i("bmob","查询用户药品用药提醒设置getRemindUsingDrug:"+list.get(0).getRemindUsingDrug());
//                    Log.i("bmob","mAlarm_Using_before:"+mAlarm_Using);
                    mAlarm_Using = Boolean.valueOf(list.get(0).getRemindUsingDrug());
//                    Log.i("bmob","mAlarm_Using_after:"+mAlarm_Using);
                    alarmUsing_switch.setChecked(mAlarm_Using);
                    alarmBuy_switch.setChecked(mAlarm_Buy);

                }else{
                    Log.i("bmob","查询数据失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        //设置监听
        mBackSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetAlarmActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                finish();
            }
        });

        alarmUsing_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(), "开启用药提醒", Toast.LENGTH_LONG).show();
                    preSetNum_edit.setFocusableInTouchMode(true);
                    preSetNum_edit.setFocusable(true);
                    preSetNum_edit.requestFocus();
                    mAlarm_Using = true;
                    firstAlarm_btn.setEnabled(true);
                    secondAlarm_btn.setEnabled(true);
                    thirdAlarm_btn.setEnabled(true);
                    forthAlarm_btn.setEnabled(true);
                    firstAlarm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimeDialog_time(firstAlarm_btn);
                        }
                    });
                    secondAlarm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimeDialog_time(secondAlarm_btn);
                        }
                    });
                    thirdAlarm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimeDialog_time(thirdAlarm_btn);
                        }
                    });
                    forthAlarm_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimeDialog_time(forthAlarm_btn);
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "关闭用药提醒", Toast.LENGTH_LONG).show();
                    preSetNum_edit.setFocusable(false);
                    preSetNum_edit.setFocusableInTouchMode(false);
                    mAlarm_Using = false;
                    firstAlarm_btn.setEnabled(false);
                    secondAlarm_btn.setEnabled(false);
                    thirdAlarm_btn.setEnabled(false);
                    forthAlarm_btn.setEnabled(false);
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
                    mAlarm_Buy = true;
                }else {
                    Toast.makeText(getApplicationContext(), "关闭复购提醒", Toast.LENGTH_LONG).show();
                    daysBuyAlarm_edit.setFocusable(false);
                    daysBuyAlarm_edit.setFocusableInTouchMode(false);
                    mAlarm_Buy = false;
                }
            }
        });

        mSaveSetSATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                //获取系统的日期
                //年
                final int year = calendar.get(Calendar.YEAR);
                //月
                final int month = calendar.get(Calendar.MONTH)+1;
                //日
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                //根据用户名和药箱编号和药品名称在用药情况设置表中查询数据
                //查询条件1 用户名
                BmobQuery<DrugUsingDataBean> query_eq1 = new BmobQuery<DrugUsingDataBean>();
                query_eq1.addWhereEqualTo("userName", strUserName);
                //查询条件2 药箱编号
                BmobQuery<DrugUsingDataBean> query_eq2 = new BmobQuery<DrugUsingDataBean>();
                query_eq2.addWhereEqualTo("boxNumber", boxNum);
                //查询条件3 药品名称
                BmobQuery<DrugUsingDataBean> query_eq3 = new BmobQuery<DrugUsingDataBean>();
                query_eq3.addWhereEqualTo("genericName", genericName);

                //最后组装完整的and条件
                List<BmobQuery<DrugUsingDataBean>> queries = new ArrayList<BmobQuery<DrugUsingDataBean>>();
                queries.add(query_eq1);
                queries.add(query_eq2);
                queries.add(query_eq3);

                BmobQuery<DrugUsingDataBean> query = new BmobQuery<DrugUsingDataBean>();
                query.and(queries);

                query.findObjects(new FindListener<DrugUsingDataBean>() {
                    @Override
                    public void done(List<DrugUsingDataBean> list, BmobException e) {
                        if(e==null){
                            //获取用户名、药箱编号、药品名
                            final DrugUsingDataBean mDrugUsingDataBean = new DrugUsingDataBean();
                            mDrugUsingDataBean.setUserName(strUserName);
                            mDrugUsingDataBean.setBoxNumber(boxNum);
                            mDrugUsingDataBean.setGenericName(genericName);
                            //获取用药提醒 -- 提醒开关、吃药数量、时间段（1~4） -- 6项
                            mDrugUsingDataBean.setRemindUsingDrug(mAlarm_Using.toString());
                            strSetNum = String.valueOf(preSetNum_edit.getText());
                            if (strSetNum == null || strSetNum.equals("") || strSetNum.equals("0")) {
                                strSetNum = "1";
                            }
                            mDrugUsingDataBean.setUsingDrugNumber(strSetNum);
                            mDrugUsingDataBean.setUsingDrugTimeNo1(String.valueOf(firstAlarm_btn.getText()));
                            mDrugUsingDataBean.setUsingDrugTimeNo2(String.valueOf(secondAlarm_btn.getText()));
                            mDrugUsingDataBean.setUsingDrugTimeNo3(String.valueOf(thirdAlarm_btn.getText()));
                            mDrugUsingDataBean.setUsingDrugTimeNo4(String.valueOf(forthAlarm_btn.getText()));
                            //复购提醒 -- 提醒开关、提醒开始时间、倒计时天数 -- 3项
                            mDrugUsingDataBean.setRemindBuyDrug(mAlarm_Buy.toString());
                            mDrugUsingDataBean.setRemindBuyDate(year + "-" + month + "-" + day);
                            mDrugUsingDataBean.setDayLeft(String.valueOf(daysBuyAlarm_edit.getText()));
//                            toast("查询成功：共"+object.size()+"条数据。");
                            if (list.size() > 0) {
                                mDrugUsingDataBean.update(list.get(0).getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","更新成功");
                                            flag = 1;
                                            setAlarm(mCurrentUser, genericName, Integer.parseInt(boxNum));
                                            startActivity(new Intent(SetAlarmActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                                            finish();
//                                            count_time = 0;
//                                            setAlarm(mCurrentUser, genericName);
//                                            startActivity(new Intent(SetAlarmActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
//                                            finish();
                                        }else{
                                            flag = 0;
                                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                            Toast.makeText(SetAlarmActivity.this, "当前数据更新失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                mDrugUsingDataBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","保存成功！");
                                            flag = 2;
                                            setAlarm(mCurrentUser, genericName, Integer.parseInt(boxNum));
                                            startActivity(new Intent(SetAlarmActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
                                            finish();
                                        }else{
                                            flag = 0;
                                            Log.i("bmob","保存失败："+e.getMessage()+","+e.getErrorCode());
                                            Toast.makeText(SetAlarmActivity.this, "当前数据保存失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

//                            Log.i("bmob", "当前flag：" + flag);
//                            if (flag == 1 || flag == 2) {
//
//                            } else {
//                                Toast.makeText(SetAlarmActivity.this, "当前数据设置失败，请稍后重试！", Toast.LENGTH_SHORT).show();
//                            }

                        }else{
                            Log.i("bmob","查询失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        });

    }

    private void setAlarm(final AllUserBean mCurrentUser, final String genericName, final int drugNum) {
        Log.i("bmob", "当前flag：" + flag);

        if (!firstAlarm_btn.getText().toString().equals("未设置")) {
            AlarmBean alarmBean = new AlarmBean();
            alarmBean.setUser(mCurrentUser); //设置当前用户
            alarmBean.setDrug(genericName); //设置当前药品名称
            alarmBean.setDrugNum(drugNum);
            alarmBean.setDosage("每次" + strSetNum + "片");
            alarmBean.setNum(1);
            alarmBean.setTimeH(Integer.valueOf(firstAlarm_btn.getText().toString().substring(0, 2)));
            alarmBean.setTimeM(Integer.parseInt(firstAlarm_btn.getText().toString().substring(3)));
            alarmBean.setTouched(false);
            alarmBean.setTouchedTime("");
            alarmBean.setRequestCode(count_num_request%50);
            alarmBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "闹钟时间保存成功！第i条：" );
                        Intent intent =new Intent(SetAlarmActivity.this, ReceiveAlarm.class);
                        intent.setAction("short");
                        intent.putExtra("msg", "该吃" + genericName + "药了,每次" + strSetNum + "片。");
                        intent.putExtra("drugNum", drugNum);
                        intent.putExtra("drugName", genericName);
                        intent.putExtra("user", mCurrentUser.getUsername());
                        PendingIntent sender=
                                PendingIntent.getBroadcast(SetAlarmActivity.this, count_num_request % 50, intent, 0);
                        Log.i("bmob", "闹钟回调 requestCode:" + count_num_request % 50);
                        count_num_request ++;
                        //设定一个五秒后的时间
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                                (Calendar.DAY_OF_MONTH), Integer.parseInt(firstAlarm_btn.getText().toString().substring(0, 2)), Integer.parseInt(firstAlarm_btn.getText().toString().substring(3)), 5);
//                        calendar.setTimeInMillis(System.currentTimeMillis());
//                        calendar.add(Calendar.SECOND, 5 + count_time);
                        Calendar c_cur = Calendar.getInstance();
                        if(c_cur.getTimeInMillis() >  calendar.getTimeInMillis()){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        }
                        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    } else {
                        Log.i("bmob", "闹钟时间保存失败！" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        } else {
            Log.i("bmob", "时间结点错误！");
        }

        if (!secondAlarm_btn.getText().toString().equals("未设置")) {
            AlarmBean alarmBean = new AlarmBean();
            //锁定当前药物
            alarmBean.setUser(mCurrentUser); //设置当前用户
            alarmBean.setDrug(genericName); //设置当前药品名称
            alarmBean.setDrugNum(drugNum); //设置当前药箱编号
            //确定当前药物用药方式
            alarmBean.setDosage("每次" + strSetNum + "片");
            //设置药品用药编号
            alarmBean.setNum(2);
            //设置是否已读
            alarmBean.setTouched(false);
            alarmBean.setTouchedTime(""); //设置已读时间
            //设置闹钟时间
            alarmBean.setTimeH(Integer.valueOf(secondAlarm_btn.getText().toString().substring(0, 2)));
            alarmBean.setTimeM(Integer.parseInt(secondAlarm_btn.getText().toString().substring(3)));
            //设置回调参数
            alarmBean.setRequestCode(count_num_request%50);
            alarmBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "闹钟时间保存成功！第i条：" );
                        Intent intent =new Intent(SetAlarmActivity.this, ReceiveAlarm.class);
                        intent.setAction("short");
                        intent.putExtra("msg", "该吃" + genericName + "药了,每次" + strSetNum + "片。");
                        intent.putExtra("drugNum", drugNum);
                        intent.putExtra("drugName", genericName);
                        intent.putExtra("user", mCurrentUser.getUsername());
                        PendingIntent sender=
                                PendingIntent.getBroadcast(SetAlarmActivity.this, count_num_request % 50, intent, 0);
                        Log.i("bmob", "闹钟回调 requestCode:" + count_num_request % 50);
                        count_num_request ++;
                        //设定一个五秒后的时间
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                                (Calendar.DAY_OF_MONTH), Integer.valueOf(secondAlarm_btn.getText().toString().substring(0, 2)), Integer.parseInt(secondAlarm_btn.getText().toString().substring(3)), 5);
//                        calendar.setTimeInMillis(System.currentTimeMillis());
//                        calendar.add(Calendar.SECOND, 5 + count_time);
                        Calendar c_cur = Calendar.getInstance();
                        if(c_cur.getTimeInMillis() >  calendar.getTimeInMillis()){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        }
                        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    } else {
                        Log.i("bmob", "闹钟时间保存失败！" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        } else {
            Log.i("bmob", "时间结点错误！");
        }

        if (!thirdAlarm_btn.getText().toString().equals("未设置")) {
            AlarmBean alarmBean = new AlarmBean();
            alarmBean.setUser(mCurrentUser); //设置当前用户
            alarmBean.setDrug(genericName); //设置当前药品名称
            alarmBean.setDrugNum(drugNum);
            alarmBean.setDosage("每次" + strSetNum + "片");
            alarmBean.setNum(3);
            //设置闹钟时间
            alarmBean.setTimeH(Integer.valueOf(thirdAlarm_btn.getText().toString().substring(0, 2)));
            alarmBean.setTimeM(Integer.parseInt(thirdAlarm_btn.getText().toString().substring(3)));
            alarmBean.setTouched(false);
            alarmBean.setTouchedTime("");
            alarmBean.setRequestCode(count_num_request % 50);
            alarmBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "闹钟时间保存成功！第i条：" );
                        Intent intent =new Intent(SetAlarmActivity.this, ReceiveAlarm.class);
                        intent.setAction("short");
                        intent.putExtra("msg", "该吃" + genericName + "药了,每次" + strSetNum + "片。");
                        intent.putExtra("drugNum", drugNum);
                        intent.putExtra("drugName", genericName);
                        intent.putExtra("user", mCurrentUser.getUsername());
                        PendingIntent sender=
                                PendingIntent.getBroadcast(SetAlarmActivity.this, count_num_request % 50, intent, 0);
                        Log.i("bmob", "闹钟回调 requestCode:" + count_num_request % 50);
                        count_num_request ++;
                        //设定一个五秒后的时间
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                                (Calendar.DAY_OF_MONTH), Integer.valueOf(thirdAlarm_btn.getText().toString().substring(0, 2)), Integer.parseInt(thirdAlarm_btn.getText().toString().substring(3)), 5);
//                        calendar.setTimeInMillis(System.currentTimeMillis());
//                        calendar.add(Calendar.SECOND, 5 + count_time);
                        Calendar c_cur = Calendar.getInstance();
                        if(c_cur.getTimeInMillis() >  calendar.getTimeInMillis()){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        }
                        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    } else {
                        Log.i("bmob", "闹钟时间保存失败！" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        } else {
            Log.i("bmob", "时间结点错误！");
        }

        if (!forthAlarm_btn.getText().toString().equals("未设置")) {
            AlarmBean alarmBean = new AlarmBean();
            alarmBean.setUser(mCurrentUser); //设置当前用户
            alarmBean.setDrug(genericName); //设置当前药品名称
            alarmBean.setDrugNum(drugNum);
            alarmBean.setDosage("每次" + strSetNum + "片");
            alarmBean.setNum(4);
            //设置闹钟时间
            alarmBean.setTimeH(Integer.valueOf(forthAlarm_btn.getText().toString().substring(0, 2)));
            alarmBean.setTimeM(Integer.parseInt(forthAlarm_btn.getText().toString().substring(3)));
            alarmBean.setTouched(false);
            alarmBean.setTouchedTime("");
            alarmBean.setRequestCode(count_num_request % 50);
            alarmBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "闹钟时间保存成功！第i条：" );
                        Intent intent =new Intent(SetAlarmActivity.this, ReceiveAlarm.class);
                        intent.setAction("short");
                        intent.putExtra("msg", "该吃" + genericName + "药了,每次" + strSetNum + "片。");
                        intent.putExtra("drugNum", drugNum);
                        intent.putExtra("drugName", genericName);
                        intent.putExtra("user", mCurrentUser.getUsername());
                        intent.putExtra("msg", "该吃" + genericName + "药了,每次" + strSetNum + "片。");
                        PendingIntent sender=
                                PendingIntent.getBroadcast(SetAlarmActivity.this, count_num_request % 50, intent, 0);
                        Log.i("bmob", "闹钟回调 requestCode:" + count_num_request % 50);
                        count_num_request ++;
                        //设定一个五秒后的时间
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                                (Calendar.DAY_OF_MONTH), Integer.valueOf(forthAlarm_btn.getText().toString().substring(0, 2)), Integer.valueOf(forthAlarm_btn.getText().toString().substring(3)), 5);
                        Calendar c_cur = Calendar.getInstance();
                        if(c_cur.getTimeInMillis() >  calendar.getTimeInMillis()){
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        }
                        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    } else {
                        Log.i("bmob", "闹钟时间保存失败！" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        } else {
            Log.i("bmob", "时间结点错误！");
        }

    }

    private void showTimeDialog_time(final Button button) {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = timeFormat.parse(hourOfDay + ":" + minute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                button.setText(timeFormat.format(date));
            }
        }, hour, minute, true).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(SetAlarmActivity.this, DrugBoxActivity.class).putExtra("DrugBoxNum", boxNum));
        finish();
    }


}
