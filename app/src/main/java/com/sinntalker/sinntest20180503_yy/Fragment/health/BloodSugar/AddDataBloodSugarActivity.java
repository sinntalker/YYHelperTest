package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

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
import android.widget.RadioButton;
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

    String strChecked = "";
    String strSugarValueET = "";
    String strCurrentDate = "";
    String strCurrentTime = "";

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_add_data_blood_sugar);

        type = getIntent().getStringExtra("family");

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

        strChecked = "早餐前";
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

        mBackADBSAIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BloodSugerActivity.class)); //打开对应Activity可以刷新数据
                finish();
            }
        });
        mDateSetADBSATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog_date();
            }
        });
        mTimeSetADBSATV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog_time();
            }
        });
        mSaveADBSABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mTimeNo1ADBSARB.setOnClickListener(this);
        mTimeNo2ADBSARB.setOnClickListener(this);
        mTimeNo3ADBSARB.setOnClickListener(this);
        mTimeNo4ADBSARB.setOnClickListener(this);
        mTimeNo5ADBSARB.setOnClickListener(this);
        mTimeNo6ADBSARB.setOnClickListener(this);
        mTimeNo7ADBSARB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_radioButton_timeNo1_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(true);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "早餐前";
                break;
            case R.id.id_radioButton_timeNo2_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(true);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "早餐后";
                break;
            case R.id.id_radioButton_timeNo3_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(true);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "午餐前";
                break;
            case R.id.id_radioButton_timeNo4_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(true);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "午餐后";
                break;
            case R.id.id_radioButton_timeNo5_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(true);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "晚餐前";
                break;
            case R.id.id_radioButton_timeNo6_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(true);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "晚餐后";
                break;
            case R.id.id_radioButton_timeNo7_addDataBloodSugar:
                mTimeNo1ADBSARB.setChecked(false);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(true);
                strChecked = "睡前";
                break;
            default:
                mTimeNo1ADBSARB.setChecked(true);
                mTimeNo2ADBSARB.setChecked(false);
                mTimeNo3ADBSARB.setChecked(false);
                mTimeNo4ADBSARB.setChecked(false);
                mTimeNo5ADBSARB.setChecked(false);
                mTimeNo6ADBSARB.setChecked(false);
                mTimeNo7ADBSARB.setChecked(false);
                strChecked = "早餐前";
                break;
        }

    }

    private void save() {
        strSugarValueET = mSugarValueADBSAET.getText().toString().trim();

        if (strSugarValueET.length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写血糖值", Toast.LENGTH_SHORT).show();
        }else {
//            BloodSugarData data = new BloodSugarData();
//            data.setTime(strCurrentDate+"\n"+strCurrentTime);
//            data.setSugarValue(strSugarValueET);
//            data.setDuring(strChecked);
//            DbUtils.insert(data);

//            AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);
            AllUserBean mCurrentUser = BmobUser.getCurrentUser(AllUserBean.class);

            if (type != null && type.length() > 0) {
                FamilySugarValueBean familySugarValueBean = new FamilySugarValueBean();
                familySugarValueBean.setSetTime(strCurrentDate+"\n"+strCurrentTime);
                familySugarValueBean.setUser(mCurrentUser);
                familySugarValueBean.setRelations(type);
                familySugarValueBean.setDuring(strChecked);
                familySugarValueBean.setSugarValue(strSugarValueET);
                familySugarValueBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "保存成功");
                            startActivity(new Intent(AddDataBloodSugarActivity.this, BloodSugerActivity.class)
                                    .putExtra("family", type)); //打开对应Activity可以刷新数据
                            AddDataBloodSugarActivity.this.finish();
                        } else {
                            Log.i("bmob", "保存失败" + e.getErrorCode() + e.getMessage());
                        }
                    }
                });
            } else {
                SugarValueBean sugarValueBean = new SugarValueBean();
                sugarValueBean.setSetTime(strCurrentDate+"\n"+strCurrentTime);
                sugarValueBean.setUser(mCurrentUser);
                sugarValueBean.setDuring(strChecked);
                sugarValueBean.setSugarValue(strSugarValueET);
                sugarValueBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.i("bmob", "保存成功");
                            startActivity(new Intent(AddDataBloodSugarActivity.this, BloodSugerActivity.class)); //打开对应Activity可以刷新数据
                            AddDataBloodSugarActivity.this.finish();
                        } else {
                            Log.i("bmob", "保存失败" + e.getErrorCode() + e.getMessage());
                        }
                    }
                });
            }

//            SugarValueBean bloodSugarBean = new SugarValueBean();
////            bloodSugarBean.setDuring(strChecked);
//            Log.i("bmob", "保存当前数据：" + strChecked);
//            bloodSugarBean.setUser(mCurrentUser);
//            Log.i("bmob", "保存当前数据：" + mCurrentUser);
//            bloodSugarBean.setSugarValue(strSugarValueET);
//            Log.i("bmob", "保存当前数据:" + strSugarValueET);
//            bloodSugarBean.setSetTime(strCurrentDate+" "+strCurrentTime);
//            Log.i("bmob", "保存当前数据:" + strCurrentDate+" "+strCurrentTime);
//            bloodSugarBean.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null) {
//                        Log.i("bmob", "血糖值保存成功！");
//                    } else {
//                        Log.i("bmob", "血糖值保存失败！");
//                    }
//                }
//            });

//            startActivity(new Intent(getApplicationContext(), BloodSugerActivity.class)); //打开对应Activity可以刷新数据
//            this.finish();
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
