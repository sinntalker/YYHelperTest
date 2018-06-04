package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Common.CommonUnits;
import com.sinntalker.sinntest20180503_yy.Common.StringUnits;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ResetActivity extends Activity {

    //声明返回按键
    ImageView back_image;
    //声明 手机输入框、验证码、获取验证码按键、密码、显示密码、重置按键
    EditText mPhoneET_reset;
    EditText mCodeET_reset;
    EditText mPasswordET_reset;

    Button mSendCode_btn_reset;
    Button mReset_btn;

    CheckBox mPasswordCB_reset;

    protected Handler mHandler;

    private RegisterActivity.TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_reset);

        final String inputActivity = getIntent().getStringExtra("inputActivity");

        back_image = findViewById(R.id.id_common_back_resetToLogin);
        mPhoneET_reset = findViewById(R.id.id_phone_et_reset);
        mCodeET_reset = findViewById(R.id.id_code_et_reset);
        mPasswordET_reset = findViewById(R.id.id_password_et_reset);
        mSendCode_btn_reset = findViewById(R.id.id_send_btn_reset);
        mReset_btn = findViewById(R.id.id_reset_btn);
        mPasswordCB_reset = findViewById(R.id.id_chat_password_checkbox_reset);

        //返回按键
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ResetActivity.this, LoginActivity.class));
                ResetActivity.this.finish();
            }
        });

        mSendCode_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "发送验证码", Toast.LENGTH_SHORT).show();
                final String mPhoneStr = mPhoneET_reset.getText().toString().trim()
                        .replaceAll(" ", "");
                if (StringUnits.isEmpty(mPhoneStr)) {
                    CommonUnits.showToast(ResetActivity.this, "手机号不能为空");
                } else if (!StringUnits.isMobileNO(mPhoneStr)) {
                    CommonUnits.showToast(ResetActivity.this, "手机号输入有误");
                } else {
                    mSendCode_btn_reset.setText("获取中...");
                    BmobSMS.requestSMSCode(mPhoneET_reset.getText().toString().trim().replaceAll(" ", ""), getResources().getString(R.string.app_name), new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            if (ex == null) {//验证码发送成功
//                                                LogUtil.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                                time.start();// 开始倒计时
                                CommonUnits.showToast(ResetActivity.this, "验证码已经发送");
//                                                mPhoneET.setTextColor(getResources().getColor(R.color.grey));
                                mPhoneET_reset.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });

        mReset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "重置", Toast.LENGTH_SHORT).show();
                final String mPhoneStr = mPhoneET_reset.getText().toString().trim()
                        .replaceAll(" ", "");
                final String mPasswordStr = mPasswordET_reset.getText().toString().trim();
                final String mCodeStr = mCodeET_reset.getText().toString().trim();
                if (StringUnits.isEmpty(mPhoneStr)) {
                    CommonUnits.showToast(ResetActivity.this, "手机号不能为空");
                } else if (!StringUnits.isMobileNO(mPhoneStr)) {
                    CommonUnits.showToast(ResetActivity.this, "手机号输入格式有误");
                } else if (StringUnits.isEmpty(mPasswordStr)) {
                    CommonUnits.showToast(ResetActivity.this, "密码不能为空");
                } else if (StringUnits.isEmpty(mCodeStr)) {
                    CommonUnits.showToast(ResetActivity.this, "验证码不能为空");
                } else {
                    // 先查询手机号是否已注册
                    BmobQuery<AllUserBean> bmobQuery = new BmobQuery<AllUserBean>();
                    //查询mobile叫mPhoneStr的数据
                    bmobQuery.addWhereEqualTo("username", mPhoneStr);
                    bmobQuery.findObjects(new FindListener<AllUserBean>() {
                        @Override
                        public void done(List<AllUserBean> list, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                if (list.size() == 1) {
                                    String mPasswordStr = mPasswordET_reset.getText().toString().trim();
                                    AllUserBean userBean = new AllUserBean();
                                    userBean.setPassword(mPasswordStr);
                                    userBean.update(list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(cn.bmob.v3.exception.BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(ResetActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                                                if (inputActivity.equals("login")) {
                                                    startActivity(new Intent(ResetActivity.this, LoginActivity.class));
                                                } else {
                                                    finish();
                                                }
                                            } else {
                                                Toast.makeText(ResetActivity.this, "更新失败，请稍后重试"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ResetActivity.this, "更新失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ResetActivity.this, "注册失败，请稍后重试" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
