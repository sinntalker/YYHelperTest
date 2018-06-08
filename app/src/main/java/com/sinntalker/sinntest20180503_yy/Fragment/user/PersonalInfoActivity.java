package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.user.city.ScrollerNumberPicker;
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
import cn.bmob.v3.listener.UpdateListener;

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

    String objectID;

    String type;
    String objectIDFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_personal_info);

        type = getIntent().getStringExtra("family");

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
        objectID = mCurrentUser.getObjectId();

        if (type != null && type.length() != 0) {
            AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
            //查询条件一：用户名称
            BmobQuery<FamilyBean> queryUserName = new BmobQuery<FamilyBean>();
            queryUserName.addWhereEqualTo("user",user);

            //查询条件二：药箱编号
            BmobQuery<FamilyBean> queryDrugBoxNum = new BmobQuery<FamilyBean>();
            queryDrugBoxNum.addWhereEqualTo("relations",type);

            //最后查询时完整的条件
            List<BmobQuery<FamilyBean>> allQueries = new ArrayList<BmobQuery<FamilyBean>>();
            allQueries.add(queryUserName);
            allQueries.add(queryDrugBoxNum);

            //查询
            BmobQuery<FamilyBean> query = new BmobQuery<FamilyBean>();
            query.and(allQueries);
            query.findObjects(new FindListener<FamilyBean>() {
                @Override
                public void done(List<FamilyBean> object, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "查询家人信息成功！");
                        objectIDFamily = object.get(0).getObjectId();
                        for (int i = 0; i < object.size(); i ++) {
                            mNickNamePIET.setText(object.get(i).getName());
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
                        Log.i("bmob", "查询家人信息失败！" + e.getMessage() + e.getErrorCode());
                    }
                }
            });
        } else {
            //查询当前用户信息，并展示所能修改的个人信息
            BmobQuery<AllUserBean> query = new BmobQuery<AllUserBean>();
            query.addWhereEqualTo("username", username);
            query.findObjects(new FindListener<AllUserBean>() {
                @Override
                public void done(List<AllUserBean> object, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "查询成功，当前用户存在，开始获取用户信息");
                        for (int i = 0; i < object.size(); i ++) {
                            mNickNamePIET.setText(object.get(i).getName());
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
        }



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

        mAreaPITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(PersonalInfoActivity.this);
                View view = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.addressdialog, null);
                builder.setView(view);
                LinearLayout addressdialog_linearlayout = (LinearLayout)view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker)view.findViewById(R.id.province);
                final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker)view.findViewById(R.id.city);
                final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker)view.findViewById(R.id.couny);
                final AlertDialog dialog = builder.show();
                addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAreaPITV.setText(provincePicker.getSelectedText() + " - " + cityPicker.getSelectedText() + " - " + counyPicker.getSelectedText());
                        Log.i("bmob",provincePicker.getSelectedText()+cityPicker.getSelectedText()+counyPicker.getSelectedText());
                        dialog.dismiss();

                    }
                });
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

        mHeightPITV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = {"50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
                        "60", "61", "62", "63", "64", "65", "66", "67", "68", "69",
                        "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
                        "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
                        "90", "91", "92", "93", "94", "95", "96", "97", "98", "99",
                        "100", "101", "102", "103", "104", "105", "106", "107", "108", "109",
                        "110", "111", "112", "113", "114", "115", "116", "117", "118", "119",
                        "120", "121", "122", "123", "124", "125", "126", "127", "128", "129",
                        "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
                        "140", "141", "142", "143", "144", "145", "146", "147", "148", "149",
                        "150", "151", "152", "153", "154", "155", "156", "157", "158", "159",
                        "160", "161", "162", "163", "164", "165", "166", "167", "168", "169",
                        "170", "171", "172", "173", "174", "175", "176", "177", "178", "179",
                        "180", "181", "182", "183", "184", "185", "186", "187", "188", "189",
                        "190", "191", "192", "193", "194", "195", "196", "197", "198", "199",
                        "200", "201", "202", "203", "204", "205", "206", "207", "208", "209",
                        "210", "211", "212", "213", "214", "215", "216", "217", "218", "219",
                        "220", "221", "222", "223", "224", "225", "226", "227", "228", "229",
                        "230", "231", "232", "233", "234", "235", "236", "237", "238", "239",
                        "240", "241", "242", "243", "244", "245", "246", "247", "248", "249",
                        "250" };
                AlertDialog dialog = new AlertDialog.Builder(PersonalInfoActivity.this)
//                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("身高（单位：cm）")//设置对话框的标题
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(PersonalInfoActivity.this, items[which], Toast.LENGTH_SHORT).show();
                                mHeightPITV.setText(items[which]);
                                Log.i("bmob", "当前身高为 " + items[which]);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHeightPITV.setText(items[which]);
                                Log.i("bmob", "当前身高为 " + items[which]);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
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

                if (type != null && type.length() != 0) {
                    FamilyBean mFamily = new FamilyBean();
                    mFamily.setName(strNickName);
                    mFamily.setBirth(strBirthDay);
                    mFamily.setSex(strSex);
                    mFamily.setArea(strArea);
                    mFamily.setHeight(strHeight);
                    mFamily.setIDCardType(strIDcardType);
                    mFamily.setIDNumber(strIDNumber);
                    mFamily.update(objectIDFamily, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(PersonalInfoActivity.this, "个人信息更新成功！", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","个人信息更新成功");
                                finish();
                            }else{
                                Toast.makeText(PersonalInfoActivity.this, "个人信息更新失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","个人信息更新失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                } else {
                    AllUserBean mUser = new AllUserBean();
                    mUser.setName(strNickName);
                    mUser.setBirth(strBirthDay);
                    mUser.setSex(strSex);
                    mUser.setArea(strArea);
                    mUser.setHeight(strHeight);
                    mUser.setIDCardType(strIDcardType);
                    mUser.setIDNumber(strIDNumber);
                    mUser.update(objectID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(PersonalInfoActivity.this, "个人信息更新成功！", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","个人信息更新成功");
                                finish();
                            }else{
                                Toast.makeText(PersonalInfoActivity.this, "个人信息更新失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                                Log.i("bmob","个人信息更新失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }
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
