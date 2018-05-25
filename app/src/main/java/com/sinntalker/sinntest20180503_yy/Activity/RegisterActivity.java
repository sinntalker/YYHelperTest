package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Common.CommonUnits;
import com.sinntalker.sinntest20180503_yy.Common.Constant_Java;
import com.sinntalker.sinntest20180503_yy.Common.StringUnits;
import com.sinntalker.sinntest20180503_yy.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends Activity {

    //声明返回按键
    ImageView back_image;
    //声明 手机输入框、验证码、获取验证码按键、密码、显示密码、同意用户协议、注册按键
    EditText mPhoneET_reg;
    EditText mCodeET_reg;
    EditText mPasswordET_reg;

    Button mSendCode_btn_reg;
    Button mReg_btn;

    CheckBox mPasswordCB_reg;
    CheckBox mProtocolCB_reg; //显示软件使用协议按钮
    TextView mProtocolTV_reg;

    LinearLayout mProtocolLayout;

    protected Handler mHandler;

    private TimeCount time;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_register);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constant_Java.Tags.LOAD_DATA_SUCCESS:
                        final String phone = mPhoneET_reg.getText().toString().trim(); //获取注册手机号
                        final String password = mPasswordET_reg.getText().toString().trim(); //获取注册密码
                        final String code = mCodeET_reg.getText().toString().trim(); //获取手机验证码
                        AllUserBean userBean = new AllUserBean();
                        //设置登陆模式
                        userBean.setSnsType("phone");
                        //设置登陆信息
                        userBean.setPassword(password); // 设置密码
                        userBean.setUsername(phone); //设置用户名（唯一不变）
                        userBean.setMobilePhoneNumber(phone); //设置用户登陆手机号
                        userBean.setCode(Integer.parseInt(code)); //设置用户注册验证码
                        //设置第三方登陆信息
                        userBean.setAccessToken("null");
                        userBean.setExpiresIn(Long.valueOf(0));
                        userBean.setUserId("null");
                        //设置个人信息
                        userBean.setUserNick(phone);
                        userBean.setUserAvatar("");
                        userBean.setSignature("null");
                        //设置详细信息
                        userBean.setBirth("1990-01-01");
                        userBean.setSex("男");
                        userBean.setArea("北京市-东城区");
                        userBean.setHeight("170");
                        userBean.setIDCardType("身份证");
                        userBean.setIDNumber("");
                        userBean.signUp(new SaveListener<AllUserBean>() {
                            @Override
                            public void done(AllUserBean userBean, cn.bmob.v3.exception.BmobException e) {
                                if (e == null) {
//                                    LogUtil.i("TAG", "reg success");
                                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    if (e.getErrorCode() == 202) {
                                        CommonUnits.showToast(RegisterActivity.this, "该用户已注册");
                                    } else {
                                        CommonUnits.showToast(RegisterActivity.this, "注册失败");
                                    }
//                                    LogUtil.i(TAG, "reg error=" + e.getMessage());
                                }
                            }
                        });
                        break;
                }
            }
        };

        back_image = findViewById(R.id.id_common_back_regToLogin);
        mPhoneET_reg = findViewById(R.id.id_phone_et_reg);
        mCodeET_reg = findViewById(R.id.id_code_et_reg);
        mPasswordET_reg = findViewById(R.id.id_password_et_reg);
        mSendCode_btn_reg = findViewById(R.id.id_send_btn_reg);
        mReg_btn = findViewById(R.id.id_reg_btn);
        mPasswordCB_reg = findViewById(R.id.id_chat_password_checkbox_reg);
        mProtocolCB_reg = findViewById(R.id.id_register_protocol_checkbox);
        mProtocolTV_reg = findViewById(R.id.id_register_protocol_tv);
        mProtocolLayout = (LinearLayout) findViewById(R.id.register_protocol_checkbox_layout);

        //返回按键
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });

        mSendCode_btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "发送验证码", Toast.LENGTH_SHORT).show();
                final String mPhoneStr = mPhoneET_reg.getText().toString().trim()
                        .replaceAll(" ", "");
                if (StringUnits.isEmpty(mPhoneStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "手机号不能为空");
                } else if (!StringUnits.isMobileNO(mPhoneStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "手机号输入有误");
                } else {
                    // 先查询手机号是否已注册
                    BmobQuery<AllUserBean> bmobQuery = new BmobQuery<AllUserBean>();
                    //查询mobile叫mPhoneStr的数据
                    bmobQuery.addWhereEqualTo("username", mPhoneStr);
                    bmobQuery.findObjects(new FindListener<AllUserBean>() {
                        @Override
                        public void done(List<AllUserBean> list, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                if (list.size() > 0) {
                                    Toast.makeText(RegisterActivity.this, "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
                                    mCodeET_reg.setText("");
                                    mPasswordET_reg.setText("");
                                } else {
                                    mSendCode_btn_reg.setText("获取中...");
                                    BmobSMS.requestSMSCode(mPhoneET_reg.getText().toString().trim().replaceAll(" ", ""), getResources().getString(R.string.app_name), new QueryListener<Integer>() {
                                        @Override
                                        public void done(Integer smsId, BmobException ex) {
                                            if (ex == null) {//验证码发送成功
//                                                LogUtil.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                                                time.start();// 开始倒计时
                                                CommonUnits.showToast(RegisterActivity.this, "验证码已经发送");
//                                                mPhoneET.setTextColor(getResources().getColor(R.color.grey));
                                                mPhoneET_reg.setEnabled(false);
                                            }
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败，请稍后重试"+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        mProtocolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProtocolCB_reg.isChecked()) {
                    mProtocolCB_reg.setChecked(false);
                    mProtocolTV_reg.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorAccent));
                } else {
                    mProtocolCB_reg.setChecked(true);
                    mProtocolTV_reg.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.colorPrimary));
                }
            }
        });

        mReg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "注册", Toast.LENGTH_SHORT).show();
                final String mPhoneStr = mPhoneET_reg.getText().toString().trim()
                        .replaceAll(" ", "");
                final String mPasswordStr = mPasswordET_reg.getText().toString().trim();
                final String mCodeStr = mCodeET_reg.getText().toString().trim();

                if (StringUnits.isEmpty(mPhoneStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "手机号不能为空");
                } else if (!StringUnits.isMobileNO(mPhoneStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "手机号输入格式有误");
                } else if (StringUnits.isEmpty(mPasswordStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "密码不能为空");
                } else if (StringUnits.isEmpty(mCodeStr)) {
                    CommonUnits.showToast(RegisterActivity.this, "验证码不能为空");
                } else if (!mProtocolCB_reg.isChecked()) {
                    CommonUnits.showToast(RegisterActivity.this, "您还未同意用户协议");
                } else {
//                    CommonUnits.showProgressDialog(RegisterActivity.this, "正在注册");
                    // 先查询手机号是否已注册
                    BmobQuery<AllUserBean> bmobQuery = new BmobQuery<AllUserBean>();
                    //查询mobile叫mPhoneStr的数据
                    bmobQuery.addWhereEqualTo("username", mPhoneStr);
                    bmobQuery.findObjects(new FindListener<AllUserBean>() {
                        @Override
                        public void done(List<AllUserBean> list, cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                if (list.size() > 0) {
//                                    CommonUnits.hideProgressDialog();
                                    Toast.makeText(RegisterActivity.this, "该手机号已注册，请直接登录", Toast.LENGTH_SHORT).show();
                                    mCodeET_reg.setText("");
                                    mPasswordET_reg.setText("");
                                } else {
                                    BmobSMS.verifySmsCode(mPhoneET_reg.getText()
                                            .toString().replaceAll(" ", ""), mCodeStr, new UpdateListener() {
                                        @Override
                                        public void done(BmobException ex) {
//                                            CommonUnits.hideProgressDialog();
                                            if (ex == null) {//短信验证码已验证成功
//                                                LogUtil.i("bmob", "验证通过");
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mHandler.sendEmptyMessage(Constant_Java.Tags.LOAD_DATA_SUCCESS);
                                                    }
                                                }).start();
                                            } else {
                                                CommonUnits.showToast(RegisterActivity.this, "验证码验证失败"+ ",msg = " + ex.getLocalizedMessage());
//                                                LogUtil.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                                            }
                                        }
                                    });
                                }

                            } else {
//                                CommonUnits.hideProgressDialog();
                                Toast.makeText(RegisterActivity.this, "注册失败，请稍后重试"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mSendCode_btn_reg.setClickable(true);
            mSendCode_btn_reg.setText("重新获取");
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mSendCode_btn_reg.setClickable(false);
            mSendCode_btn_reg.setText(millisUntilFinished / 1000 + "s");
        }
    }

}
