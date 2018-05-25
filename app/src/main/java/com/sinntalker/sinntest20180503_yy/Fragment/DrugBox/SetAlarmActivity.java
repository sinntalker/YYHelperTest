package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import android.app.Activity;
import android.app.TimePickerDialog;
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
        mSaveSetSATV = findViewById(R.id.id_textView_saveSet_setAlarm);

        // 从Intent获取数据
        final String username = getIntent().getStringExtra("drug_user"); //当前用户 username
        final String boxNum = getIntent().getStringExtra("drug_boxNum"); //当前药箱 boxNum
        final String genericName = getIntent().getStringExtra("drug_genericName");//药品通用名称 genericName
        String dosage = getIntent().getStringExtra("drug_dosage");//用法用量 dosage

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

        //设置监听
        mBackSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    mAlarm_Buy = false;
                }else {
                    Toast.makeText(getApplicationContext(), "关闭用药提醒", Toast.LENGTH_LONG).show();
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

                //获取用户名、药箱编号、药品名
                final DrugUsingDataBean mDrugUsingDataBean = new DrugUsingDataBean();
                mDrugUsingDataBean.setUserName(username);
                mDrugUsingDataBean.setBoxNumber(boxNum);
                mDrugUsingDataBean.setGenericName(genericName);

                //根据用户名和药箱编号和药品名称查询数据
                //查询条件1 用户名
                BmobQuery<DrugDataBean> query_eq1 = new BmobQuery<DrugDataBean>();
                query_eq1.addWhereEqualTo("username", username);
                //查询条件2 药箱编号
                BmobQuery<DrugDataBean> query_eq2 = new BmobQuery<DrugDataBean>();
                query_eq2.addWhereEqualTo("boxNumber", boxNum);
                //查询条件3 药品名称
                BmobQuery<DrugDataBean> query_eq3 = new BmobQuery<DrugDataBean>();
                query_eq3.addWhereEqualTo("genericName", genericName);

                //最后组装完整的and条件
                List<BmobQuery<DrugDataBean>> queries = new ArrayList<BmobQuery<DrugDataBean>>();
                queries.add(query_eq1);
                queries.add(query_eq2);
                queries.add(query_eq3);

                BmobQuery<DrugDataBean> query = new BmobQuery<DrugDataBean>();
                query.and(queries);

                query.findObjects(new FindListener<DrugDataBean>() {
                    @Override
                    public void done(List<DrugDataBean> list, BmobException e) {
                        if(e==null){
                            //获取用药提醒 -- 提醒开关、吃药数量、时间段（1~4） -- 6项
                            mDrugUsingDataBean.setRemindUsingDrug(mAlarm_Using.toString());
                            mDrugUsingDataBean.setUsingDrugNumber(String.valueOf(preSetNum_edit.getText()));
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
                                mDrugUsingDataBean.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","更新成功");
                                        }else{
                                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            } else {
                                mDrugUsingDataBean.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","保存成功！");
                                        }else{
                                            Log.i("bmob","保存失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }
                        }else{
                            Log.i("bmob","查询失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

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


}
