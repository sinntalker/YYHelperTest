package com.sinntalker.sinntest20180503_yy.Fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Fragment.user.city.ScrollerNumberPicker;
import com.sinntalker.sinntest20180503_yy.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateFamilyMemberActivity extends Activity {

    ImageView mBackCFMIV; //返回
    ImageView mAvatarCFMIV; //头像
    TextView mAvatarCFMTV; // 自定义头像
    Spinner mRelationCFMS; //选择关系
    EditText mAgeCFMET; //年龄
    TextView mAreaCFMS; //地址
    EditText mPhoneCFMET; //手机号
    Button mSaveCFMBtn; //保存

    String strAge;
    String strArea;
    String strRelation;
    String strPhone;

    String [] arrRelation = new String[] { "爸爸", "妈妈", "爷爷", "奶奶", "外公", "外婆", "老公", "老婆", "岳父", "岳母", "公公", "婆婆" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_create_family_member);

        mBackCFMIV = findViewById(R.id.id_imageView_back_createFamilyMember);
        mAvatarCFMIV = findViewById(R.id.id_imageView_avatar_createFamilyMember);
        mAvatarCFMTV = findViewById(R.id.id_textView_editAvatar_createFamilyMember);
        mRelationCFMS = findViewById(R.id.id_spinner_relation_createFamilyMember);
        mAgeCFMET = findViewById(R.id.id_editText_age_createFamilyMember);
        mAreaCFMS = findViewById(R.id.id_textView_area_createFamilyMember);
        mPhoneCFMET = findViewById(R.id.id_editText_phone_createFamilyMember);
        mSaveCFMBtn = findViewById(R.id.id_button_save_createFamilyMember);

        strRelation = "爸爸";

        mBackCFMIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAvatarCFMIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateFamilyMemberActivity.this, "暂不支持修改功能", Toast.LENGTH_SHORT).show();
            }
        });

        mAvatarCFMTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateFamilyMemberActivity.this, "暂不支持修改功能", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> arrayAgeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrRelation);
        //设置显示的数据
        // 为下拉列表定义一个适配器
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrRelation);
        // 为适配器设置下拉列表下拉时的菜单样式，有好几种样式，请根据喜好选择
        arrayAgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 将适配器添加到下拉列表上
        mRelationCFMS.setAdapter(arrayAgeAdapter);
        // 为下拉框设置事件的响应
        mRelationCFMS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param adapterView
             * @param view   显示的布局
             * @param i      在布局显示的位置id
             * @param l      将要显示的数据
             */
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                strRelation = adapter.getItem(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAreaCFMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(CreateFamilyMemberActivity.this);
                View view = LayoutInflater.from(CreateFamilyMemberActivity.this).inflate(R.layout.addressdialog, null);
                builder.setView(view);
                LinearLayout addressdialog_linearlayout = (LinearLayout)view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker)view.findViewById(R.id.province);
                final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker)view.findViewById(R.id.city);
                final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker)view.findViewById(R.id.couny);
                final AlertDialog dialog = builder.show();
                addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mAreaCFMS.setText(provincePicker.getSelectedText() + " - " + cityPicker.getSelectedText() + " - " + counyPicker.getSelectedText());
                        Log.i("bmob",provincePicker.getSelectedText()+cityPicker.getSelectedText()+counyPicker.getSelectedText());
                        dialog.dismiss();

                    }
                });
            }
        });

        mSaveCFMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save() {
        strAge = mAgeCFMET.getText().toString().trim();
        strPhone = mPhoneCFMET.getText().toString().trim();
        strArea = mAreaCFMS.getText().toString().trim();

        if (strAge.length() != 0 && strArea.length() != 0 && strRelation.length() != 0 && strPhone.length() != 0) {
            AllUserBean user = BmobUser.getCurrentUser(AllUserBean.class);
            FamilyBean familyBean = new FamilyBean();
            familyBean.setUser(user);
            familyBean.setRelations(strRelation);
            familyBean.setAge(Integer.parseInt(strAge));
            familyBean.setArea(strArea);
            familyBean.setPhone(strPhone);
            familyBean.setName("");
            familyBean.setBirth("");
            familyBean.setSex("男");
            familyBean.setHeight("");
            familyBean.setIDCardType("身份证");
            familyBean.setIDNumber("");
            familyBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "家庭成员创建成功！");
                        startActivity(new Intent(CreateFamilyMemberActivity.this, MyFamilyActivity.class));
                        finish();
                    } else {
                        Log.i("bmob", "家庭成员创建失败，" + e.getMessage() + e.getErrorCode());
                    }
                }
            });
        } else {
            Toast.makeText(CreateFamilyMemberActivity.this, "请补充完整信息！", Toast.LENGTH_SHORT).show();
        }
    }
}
