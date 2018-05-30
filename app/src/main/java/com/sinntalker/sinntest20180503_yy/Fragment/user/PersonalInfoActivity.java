package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PersonalInfoActivity extends Activity {

    //声明控件
    ImageView mBackPIIV; //返回
    TextView mSaveInfoPITV; //保存信息 / 更新信息
    EditText mNickNamePIET; //昵称
    TextView mBirthDayPITV; //出生日期
    RadioButton mMalePIRB; //男性
    RadioButton mFemalePIRB; //女性
    TextView mAreaPITV; //所处地区
    TextView mHeightPITV; //身高
    RadioButton mIDCardPIRB; //身份证
    RadioButton mPassportPIRB; //护照
    EditText mIDCardNumberPIET; //编号

    String strNickName; //姓名
    String strBirthDay; //生日
    String strSex; //性别 “男”、“女”
    String strArea; //地区
    String strHeight; //身高
    String strIDcardType; //证件类型 “身份证”、“护照”
    String strIDNumber; //证件号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_personal_info);

        //实例化
        mBackPIIV = findViewById(R.id.id_imageView_back_personalInfo);
        mSaveInfoPITV = findViewById(R.id.id_textView_savePersonInfo_personalInfo);
        mNickNamePIET = findViewById(R.id.id_editText_nickName_personalInfo);
        mBirthDayPITV = findViewById(R.id.id_textView_birthday_personalInfo);
        mMalePIRB = findViewById(R.id.id_radioButton_male_personalInfo);
        mFemalePIRB = findViewById(R.id.id_radioButton_female_personalInfo);
        mAreaPITV = findViewById(R.id.id_textView_area_personalInfo);
        mHeightPITV = findViewById(R.id.id_textView_height_personalInfo);
        mIDCardPIRB = findViewById(R.id.id_radioButton_idCard_personalInfo);
        mPassportPIRB = findViewById(R.id.id_radioButton_passport_personalInfo);
        mIDCardNumberPIET = findViewById(R.id.id_editText_idNumber_personalInfo);

        //确定当前登陆用户
        BmobUser mCurrentUser = BmobUser.getCurrentUser();
        String username = mCurrentUser.getUsername();

        //查询当前用户信息，并展示所能修改的个人信息
        BmobQuery<AllUserBean> query = new BmobQuery<AllUserBean>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<AllUserBean>() {
            @Override
            public void done(List<AllUserBean> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功，当前用户存在，开始获取用户信息");
                    for (int i = 0; i < object.size(); i ++) {
                        mNickNamePIET.setText(object.get(i).getUserNick());
                        mBirthDayPITV.setText(object.get(i).getBirth());
                        if (object.get(i).getSex().equals("男")) {
                            mMalePIRB.setChecked(true);
                            mFemalePIRB.setChecked(false);
                        } else {
                            mMalePIRB.setChecked(false);
                            mFemalePIRB.setChecked(true);
                        }
                        mAreaPITV.setText(object.get(i).getArea());
                        mHeightPITV.setText(object.get(i).getHeight());
                        if (object.get(i).getIDCardType().equals("身份证")) {
                            mIDCardPIRB.setChecked(true);
                            mPassportPIRB.setChecked(false);
                        } else {
                            mIDCardPIRB.setChecked(false);
                            mPassportPIRB.setChecked(true);
                        }
                        mIDCardNumberPIET.setText(object.get(i).getIDNumber());
                    }

                } else {
                    Log.i("bmob", "查询用户信息失败，错误信息：" + e.getErrorCode() + e.getMessage());
                }
            }
        });

        mBackPIIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBirthDayPITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog_date();
            }
        });

        mMalePIRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSex = "男";
                Log.i("bmob", "当前性别：" + strSex);
            }
        });

        mFemalePIRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSex = "女";
                Log.i("bmob", "当前性别：" + strSex);
            }
        });

        mIDCardPIRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strIDcardType = "身份证";
                Log.i("bmob", "当前证件类型：" + strIDcardType);
            }
        });

        mPassportPIRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strIDcardType = "护照";
                Log.i("bmob", "当前证件类型：" + strIDcardType);
            }
        });

        mSaveInfoPITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strNickName = mNickNamePIET.getText().toString().trim();
                strBirthDay = mBirthDayPITV.getText().toString().trim();
//                strSex = ;

                strArea = mAreaPITV.getText().toString().trim();
                strHeight = mHeightPITV.getText().toString().trim();
//                strIDcardType = ;
                strIDNumber = mIDCardNumberPIET.getText().toString().trim();
            }
        });
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
                mBirthDayPITV.setText(dateFormat.format(date));
            }
        }, year, month, day).show();
    }
}
