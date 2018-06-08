package com.sinntalker.sinntest20180503_yy.Fragment.health.Weight;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class AddDataWeightActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackWAIV; //返回
    EditText mWeightWAET; //体重
    TextView mDateWATV; //日期
    TextView mTimeWATV; //时间
    Button mSaveBtn; //保存

    String strWeightET = "";
    String strCurrentDate = "";
    String strCurrentTime = "";

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_data_weight);

        type = getIntent().getStringExtra("family");

        mBackWAIV = findViewById(R.id.id_imageView_back_addDataWeight);
        mWeightWAET = findViewById(R.id.id_editView_weight_addDataWeight);
        mDateWATV = findViewById(R.id.id_textView_dateSetData_addDataWeight);
        mTimeWATV = findViewById(R.id.id_textView_timeSetData_addDataWeight);
        mSaveBtn = findViewById(R.id.id_button_saveData_addDataWeight);

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

        strCurrentDate = year + "-" + month + "-" + day;
        strCurrentTime = hour + ":" + minute;

        mDateWATV.setText(strCurrentDate);
        mTimeWATV.setText(strCurrentTime);

        mBackWAIV.setOnClickListener(this);
        mDateWATV.setOnClickListener(this);
        mTimeWATV.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBackWAIV) {
            finish();
        }
        if (v == mDateWATV) {
            showTimeDialog_date();
        }
        if (v == mTimeWATV) {
            showTimeDialog_time();
        }
        if (v == mSaveBtn) {
            save();
        }
    }

    private void save() {
        strWeightET = mWeightWAET.getText().toString().trim();

        if (strWeightET.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写体重", Toast.LENGTH_SHORT).show();
        }else {
//            WeightData data = new WeightData();
//            data.setTime(strCurrentDate+" "+strCurrentTime);
//            data.setWeight(strWeightET);
//            DbUtils.insert(data);

            if (type != null && type.length() > 0) {
                AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
                FamilyWeightBean weightBean = new FamilyWeightBean();
                weightBean.setUser(mCurrentUser);
                weightBean.setRelations(type);
                weightBean.setSetTime(strCurrentDate+" "+strCurrentTime);
                weightBean.setWeight(strWeightET);
                weightBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "save success.");
                            startActivity(new Intent(getApplicationContext(), WeightActivity.class)
                                    .putExtra("family", type)); //打开对应Activity可以刷新数据
                            AddDataWeightActivity.this.finish();
                        } else {
                            Log.i("bmob", "save failed, error: " + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
            } else {
                AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
                WeightBean weightBean = new WeightBean();
                weightBean.setUser(mCurrentUser);
                weightBean.setSetTime(strCurrentDate+" "+strCurrentTime);
                weightBean.setWeight(strWeightET);
                weightBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "save success.");
                            startActivity(new Intent(getApplicationContext(), WeightActivity.class)); //打开对应Activity可以刷新数据
                            AddDataWeightActivity.this.finish();
                        } else {
                            Log.i("bmob", "save failed, error: " + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
            }
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
                mDateWATV.setText(dateFormat.format(date));
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
                mTimeWATV.setText(timeFormat.format(date));
            }
        }, hour, minute, true).show();
    }
}
