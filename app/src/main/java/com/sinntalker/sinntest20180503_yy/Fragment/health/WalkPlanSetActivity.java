package com.sinntalker.sinntest20180503_yy.Fragment.health;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WalkPlanSetActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackWPSAIV; //返回
    EditText mStepSetWPSAET; //
    CheckBox mIsRemindWPSACB; //
    TextView mRemindTimeWPSATV; //
    Button mSaveBtn; //

    private String strWalkPlan;
    private String strRemind;
    private String strAchieveTime;

    private SharedPreferencesUtils_StepCounter mSharedPreferencesUtils_StepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_walk_plan_set);

        //实例化
        mBackWPSAIV = findViewById(R.id.id_imageView_back_setWalkPlan);
        mStepSetWPSAET = findViewById(R.id.id_editView_stepSetPlan_walkPlanSet);
        mIsRemindWPSACB = findViewById(R.id.id_checkBox_setAlarm_walkPlanSet);
        mRemindTimeWPSATV = findViewById(R.id.id_textView_remindTime_walkPlanSet);
        mSaveBtn = findViewById(R.id.id_button_saveSet_walkPlanSet);

        initData();
        addListener();
    }

    public void initData() {//获取锻炼计划
        mSharedPreferencesUtils_StepCounter = new SharedPreferencesUtils_StepCounter(this);
        String mWalkPlanStr = (String) mSharedPreferencesUtils_StepCounter.getParam("WalkPlan", "5000");
        String mRemind = (String) mSharedPreferencesUtils_StepCounter.getParam("remind", "1");
        String mAchieveTime = (String) mSharedPreferencesUtils_StepCounter.getParam("achieveTime", "20:00");
        if (!mWalkPlanStr.isEmpty()) {
            if ("0".equals(mWalkPlanStr)) {
                mStepSetWPSAET.setText("5000");
            } else {
                mStepSetWPSAET.setText(mWalkPlanStr);
            }
        }
        if (!mRemind.isEmpty()) {
            if ("0".equals(mRemind)) {
                mIsRemindWPSACB.setChecked(false);
            } else if ("1".equals(mRemind)) {
                mIsRemindWPSACB.setChecked(true);
            }
        }

        if (!mAchieveTime.isEmpty()) {
            mRemindTimeWPSATV.setText(mAchieveTime);
        }

    }

    public void addListener() {
        mSaveBtn.setOnClickListener(this);
        mRemindTimeWPSATV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBackWPSAIV) {
            finish();
        }
        if (v == mSaveBtn) {
            save();
        }
        if (v == mRemindTimeWPSATV) {
            showTimeDialog1();
        }
    }

    private void save() {
        strWalkPlan = mStepSetWPSAET.getText().toString().trim();
//        remind = "";
        if (mIsRemindWPSACB.isChecked()) {
            strRemind = "1";
        } else {
            strRemind = "0";
        }
        strAchieveTime = mRemindTimeWPSATV.getText().toString().trim();
        if (strWalkPlan.isEmpty() || "0".equals(strWalkPlan)) {
            mSharedPreferencesUtils_StepCounter.setParam("WalkPlan", "5000");
        } else {
            mSharedPreferencesUtils_StepCounter.setParam("WalkPlan", strWalkPlan);
        }
        mSharedPreferencesUtils_StepCounter.setParam("remind", strRemind);

        if (strAchieveTime.isEmpty()) {
            mSharedPreferencesUtils_StepCounter.setParam("achieveTime", "21:00");
            this.strAchieveTime = "21:00";
        } else {
            mSharedPreferencesUtils_StepCounter.setParam("achieveTime", strAchieveTime);
        }
        finish();
    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
//        String time = tv_remind_time.getText().toString().trim();
        final DateFormat df = new SimpleDateFormat("HH:mm");
//        Date date = null;
//        try {
//            date = df.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (null != date) {
//            calendar.setTime(date);
//        }
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                mRemindTimeWPSATV.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }
}
