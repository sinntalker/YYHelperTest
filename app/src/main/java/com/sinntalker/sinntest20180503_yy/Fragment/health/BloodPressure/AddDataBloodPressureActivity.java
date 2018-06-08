package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddDataBloodPressureActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackADBPIV; //返回
    EditText mDiastolicADBPET; //舒张压
    EditText mSystolicADBPET; //收缩压
    TextView mDateADBPTV; //日期
    TextView mTimeADBPTV; //时间
    Button mSaveBtn; //保存

    String strBloodPressure_Diastolic; //舒张压 字符串 用于获取控件中字符串完成保存功能
    String strBloodPressure_Systolic; //收缩压 字符串 用于获取控件中字符串完成保存功能
    String strPressureMeasure_Date; //当前日期 字符串 用于获取控件中字符串完成保存功能
    String strPressureMeasure_Time; //当前时间 字符串 用于获取控件中字符串完成保存功能

    String type;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_data_blood_pressure);

        type = getIntent().getStringExtra("family");

        //实例化
        mBackADBPIV = findViewById(R.id.id_imageView_back_addDataBloodPressure);
        mDiastolicADBPET = findViewById(R.id.id_editView_diastolicData_addDataBloodPressure);
        mSystolicADBPET = findViewById(R.id.id_editView_systolic_addDataBloodPressure);
        mDateADBPTV = findViewById(R.id.id_textView_dateSetData_addDataBloodPressure);
        mTimeADBPTV = findViewById(R.id.id_textView_timeSetData_addDataBloodPressure);
        mSaveBtn = findViewById(R.id.id_button_saveData_addDataBloodPressure);

        //初始化控件中数据
        initial_data();

        //监听控件点击事件
        mBackADBPIV.setOnClickListener(this); //返回 -- 退出
        mSaveBtn.setOnClickListener(this);  //保存
        mDateADBPTV.setOnClickListener(this); //设置当前日期
        mTimeADBPTV.setOnClickListener(this); //设置当前时间
    }


    @Override
    public void onClick(View v) {
        if (v == mBackADBPIV) {
            startActivity(new Intent(getApplicationContext(), BloodPressureActivity.class)); //打开对应Activity可以刷新数据
            this.finish();
        }
        if (v == mSaveBtn) {
            save();
        }
        if (v == mDateADBPTV) {
            showTimeDialog_date();
        }
        if (v == mTimeADBPTV) {
            showTimeDialog_time();
        }
    }

    private void initial_data() {

        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);

        strBloodPressure_Diastolic = "60";
        strBloodPressure_Systolic = "90";
        strPressureMeasure_Date = year + "-" + month + "-" + day;
        strPressureMeasure_Time = hour + ":" + minute;

        mDiastolicADBPET.setText(strBloodPressure_Diastolic);
        mSystolicADBPET.setText(strBloodPressure_Systolic);
        mDateADBPTV.setText(strPressureMeasure_Date);
        mTimeADBPTV.setText(strPressureMeasure_Time);
    }

    private void save() {

        strBloodPressure_Diastolic = mDiastolicADBPET.getText().toString().trim();
        strBloodPressure_Systolic = mSystolicADBPET.getText().toString().trim();
        strPressureMeasure_Date = mDateADBPTV.getText().toString().trim();
        strPressureMeasure_Time = mTimeADBPTV.getText().toString().trim();

//        BloodPressureData data = new BloodPressureData();
//        data.setToday(strPressureMeasure_Date+"\n"+strPressureMeasure_Time);
//        data.setMaxPressure(strBloodPressure_Systolic);
//        data.setMinPressure(strBloodPressure_Diastolic);
//        DbUtils.insert(data);

        AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
        if (type != null && type.length() > 0) {
            FamilyBloodDataBean familyBloodDataBean = new FamilyBloodDataBean();
            familyBloodDataBean.setSetTime(strPressureMeasure_Date + " " + strPressureMeasure_Time);
            familyBloodDataBean.setUser(mCurrentUser);
            familyBloodDataBean.setRelations(type);
            familyBloodDataBean.setBloodDiastolic(strBloodPressure_Diastolic);
            familyBloodDataBean.setBloodSystolic(strBloodPressure_Systolic);
            familyBloodDataBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "保存成功");
                        startActivity(new Intent(AddDataBloodPressureActivity.this, BloodPressureActivity.class)
                            .putExtra("family", type)); //打开对应Activity可以刷新数据
                        AddDataBloodPressureActivity.this.finish();
                    } else {
                        Log.i("bmob", "保存失败" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        } else {
            BloodDataBean bloodDataBean = new BloodDataBean();
            bloodDataBean.setSetTime(strPressureMeasure_Date + " " + strPressureMeasure_Time);
            bloodDataBean.setUser(mCurrentUser);
            bloodDataBean.setBloodDiastolic(strBloodPressure_Diastolic);
            bloodDataBean.setBloodSystolic(strBloodPressure_Systolic);
            bloodDataBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "保存成功");
                        startActivity(new Intent(AddDataBloodPressureActivity.this, BloodPressureActivity.class)); //打开对应Activity可以刷新数据
                        AddDataBloodPressureActivity.this.finish();
                    } else {
                        Log.i("bmob", "保存失败" + e.getErrorCode() + e.getMessage());
                    }
                }
            });
        }
    }

    private void showTimeDialog_date() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(year + "-" + (++month) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDateADBPTV.setText(dateFormat.format(date));
            }
        }, year, month, day).show();
    }

    private void showTimeDialog_time() {
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
                mTimeADBPTV.setText(timeFormat.format(date));
            }
        }, hour, minute, true).show();
    }
}
