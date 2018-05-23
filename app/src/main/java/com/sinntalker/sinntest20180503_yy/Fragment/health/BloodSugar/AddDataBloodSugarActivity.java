package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Fragment.health.StepCounter.DbUtils;
import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDataBloodSugarActivity extends Activity implements View.OnClickListener{

    //声明控件
    ImageView mBackADBSAIV;
    EditText mSugarValueADBSAET;
    RadioButton mTimeNo1ADBSARB;
    RadioButton mTimeNo2ADBSARB;
    RadioButton mTimeNo3ADBSARB;
    RadioButton mTimeNo4ADBSARB;
    RadioButton mTimeNo5ADBSARB;
    RadioButton mTimeNo6ADBSARB;
    RadioButton mTimeNo7ADBSARB;
    TextView mDateSetADBSATV;
    TextView mTimeSetADBSATV;
    Button mSaveADBSABtn;

    String strRadioButtonChecked = "";
    String strSugarValueET = "";
    String strCurrentDate = "";
    String strCurrentTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_data_blood_sugar);

        initView();

    }

    private void initView() {
        mBackADBSAIV = findViewById(R.id.id_imageView_back_addDataBloodSugar);
        mSugarValueADBSAET = findViewById(R.id.id_editView_measureBloodSugar_addDataBloodSugar);
        mTimeNo1ADBSARB = findViewById(R.id.id_radioButton_timeNo1_addDataBloodSugar);
        mTimeNo2ADBSARB = findViewById(R.id.id_radioButton_timeNo2_addDataBloodSugar);
        mTimeNo3ADBSARB = findViewById(R.id.id_radioButton_timeNo3_addDataBloodSugar);
        mTimeNo4ADBSARB = findViewById(R.id.id_radioButton_timeNo4_addDataBloodSugar);
        mTimeNo5ADBSARB = findViewById(R.id.id_radioButton_timeNo5_addDataBloodSugar);
        mTimeNo6ADBSARB = findViewById(R.id.id_radioButton_timeNo6_addDataBloodSugar);
        mTimeNo7ADBSARB = findViewById(R.id.id_radioButton_timeNo7_addDataBloodSugar);
        mDateSetADBSATV = findViewById(R.id.id_textView_dateSetData_addDataBloodSugar);
        mTimeSetADBSATV = findViewById(R.id.id_textView_timeSetData_addDataBloodSugar);
        mSaveADBSABtn = findViewById(R.id.id_button_saveData_addDataBloodSugar);

        strRadioButtonChecked = "早餐前";
        mTimeNo1ADBSARB.setChecked(true);
        mTimeNo2ADBSARB.setChecked(false);
        mTimeNo3ADBSARB.setChecked(false);
        mTimeNo4ADBSARB.setChecked(false);
        mTimeNo5ADBSARB.setChecked(false);
        mTimeNo6ADBSARB.setChecked(false);
        mTimeNo7ADBSARB.setChecked(false);

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

        mDateSetADBSATV.setText(strCurrentDate);
        mTimeSetADBSATV.setText(strCurrentTime);

        mBackADBSAIV.setOnClickListener(this);
        mDateSetADBSATV.setOnClickListener(this);
        mTimeSetADBSATV.setOnClickListener(this);
        mSaveADBSABtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBackADBSAIV) {
            finish();
        } else if (v == mDateSetADBSATV) {
            showTimeDialog_date();
        } else if (v == mTimeSetADBSATV) {
            showTimeDialog_time();
        } else if (v == mSaveADBSABtn) {
            save();
        } else {
            switch (v.getId()) {
                case R.id.id_radioButton_timeNo1_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(true);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "早餐前";
                    break;
                case R.id.id_radioButton_timeNo2_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(true);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "早餐后";
                    break;
                case R.id.id_radioButton_timeNo3_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(true);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "午餐前";
                    break;
                case R.id.id_radioButton_timeNo4_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(true);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "午餐后";
                    break;
                case R.id.id_radioButton_timeNo5_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(true);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "晚餐前";
                    break;
                case R.id.id_radioButton_timeNo6_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(true);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "晚餐后";
                    break;
                case R.id.id_radioButton_timeNo7_addDataBloodSugar:
                    mTimeNo1ADBSARB.setChecked(false);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(true);
                    strRadioButtonChecked = "睡前";
                    break;
                default:
                    mTimeNo1ADBSARB.setChecked(true);
                    mTimeNo2ADBSARB.setChecked(false);
                    mTimeNo3ADBSARB.setChecked(false);
                    mTimeNo4ADBSARB.setChecked(false);
                    mTimeNo5ADBSARB.setChecked(false);
                    mTimeNo6ADBSARB.setChecked(false);
                    mTimeNo7ADBSARB.setChecked(false);
                    strRadioButtonChecked = "早餐前";
                    break;
            }
        }
    }

    private void save() {
        strSugarValueET = mSugarValueADBSAET.getText().toString().trim();

        if (strSugarValueET.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写血糖值", Toast.LENGTH_SHORT).show();
        }else {
            BloodSugarData data = new BloodSugarData();
            data.setTime(strCurrentDate+"\n"+strCurrentTime);
            data.setSugarValue(strSugarValueET);
            data.setDuring(strRadioButtonChecked);
            DbUtils.insert(data);

            startActivity(new Intent(getApplicationContext(), BloodSugerActivity.class)); //打开对应Activity可以刷新数据
            this.finish();
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
                mDateSetADBSATV.setText(dateFormat.format(date));
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
                mTimeSetADBSATV.setText(timeFormat.format(date));
            }
        }, hour, minute, true).show();
    }
}
