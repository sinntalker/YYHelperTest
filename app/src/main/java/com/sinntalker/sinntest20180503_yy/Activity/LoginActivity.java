package com.sinntalker.sinntest20180503_yy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.EditText;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sinntalker.sinntest20180503_yy.AllUserBean;
import com.sinntalker.sinntest20180503_yy.Common.CommonUnits;
import com.sinntalker.sinntest20180503_yy.Common.Constant;
import com.sinntalker.sinntest20180503_yy.Common.StringUnits;
import com.sinntalker.sinntest20180503_yy.R;
import com.sinntalker.sinntest20180503_yy.Weibo.WBAuthActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.sinntalker.sinntest20180503_yy.Common.Constant.APP_ID_QQ;

public class LoginActivity extends Activity {

    //声明：账号输入框、密码输入框
    EditText mUserNameET; //UserName edit
    EditText mPasswordET; //Password edit

    //声明：登陆按键、忘记密码、注册
    Button mLoginBtn; //Login button
    TextView mResetTV; //Reset textView
    TextView mRegisterTV; //Register textView

    //声明第三方登陆控件
    ImageView mQQ_image; //QQ登陆
    ImageView mWX_image; //微信登陆
    ImageView mWB_image; //微博登陆

    //QQ登陆
    public Tencent mTencent;
    private IUiListener loginListener;

    //微信登陆
    public IWXAPI api_wx; //第三方APP和微信通信的openapi接口

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);

        //Bmob 第一：默认初始化 -- 应用名称“ThirdLogin
        Bmob.initialize(this, "e9572e950523fd526c4750e301ac444f");
        //初始化微博SDK
        WbSdk.install(this,new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL,
                Constant.SCOPE));

        //将应用注册到微信
        mUserNameET = findViewById(R.id.id_userName_tv);
        mPasswordET = findViewById(R.id.id_userPassword_tv);

        mLoginBtn = findViewById(R.id.id_login_btn);
        mResetTV = findViewById(R.id.id_reset_tv);
        mRegisterTV = findViewById(R.id.id_register_tv);

        mQQ_image = findViewById(R.id.qq_login_image);
        mWX_image = findViewById(R.id.weixin_login_image);
        mWB_image = findViewById(R.id.weibo_login_image);

        //QQ登陆
        mTencent = Tencent.createInstance(APP_ID_QQ, LoginActivity.this.getApplicationContext());

        //通过WXAPIFactory工厂获取IWXApI的示例
        api_wx = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WX,true);
        //将应用的appid注册到微信
        api_wx.registerApp(Constant.APP_ID_WX);

        //登陆
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "登陆", Toast.LENGTH_SHORT).show();
                String mUserNameStr = mUserNameET.getText().toString().trim().replaceAll("", "");
                String mPasswordStr = mPasswordET.getText().toString().trim();

                if (StringUnits.isEmpty(mUserNameStr)) { //判断手机号输入是否为空
                    CommonUnits.showToast(LoginActivity.this, "用户名不能为空");
                } else if (StringUnits.isEmpty(mPasswordStr)) { //判断密码是否为控
                    CommonUnits.showToast(LoginActivity.this, "密码不能为空");
                } else {
                    //Toast显示输入的账号密码，账号密码未进行加密
//                    CommonUnits.showToast(LoginActivity.this, "用户名： "+mUserNameStr + "\n密码： " + mPasswordStr);

                    AllUserBean bu = new AllUserBean();
//                    bu.setMobilePhoneNumber(mUserNameStr);
                    bu.setUsername(mUserNameStr);
                    bu.setPassword(mPasswordStr);
                    bu.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();

                                //登陆主界面，并传递登陆类型（LoginAccountType -- phone、qq、weibo、weixin）参数：phone
                                startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("LoginAccountType", "phone"));
                                //登陆完成，登陆Activity关闭
                                LoginActivity.this.finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();

                                //如果登陆失败，将账号输入框和密码输入框清空
                                mUserNameET.setText("");
                                mPasswordET.setText("");
                            }
                        }
                    });
                }
            }
        });

        //重置密码
        mResetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "重置密码", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
                LoginActivity.this.finish();
            }
        });

        //注册
        mRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast显示 注册 的点击效果，用于测试
//                Toast.makeText(getApplicationContext(), "注册", Toast.LENGTH_SHORT).show();
                //进行注册，点击后跳转注册Activity
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                //注册完成后直接登陆
                LoginActivity.this.finish();
            }
        });

        //QQ登陆
        mQQ_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast显示 QQ登陆 的点击效果，用于测试
//                Toast.makeText(getApplicationContext(), "QQ登陆", Toast.LENGTH_SHORT).show();
                //QQ登陆方法
                qqLogin(); // --最初的方法
            }
        });

        //微信登陆
        mWX_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast显示 微信登陆 的点击效果，用于测试
                Toast.makeText(getApplicationContext(), "微信登陆", Toast.LENGTH_SHORT).show();
                //微信第三方登陆暂未开发，因为微信登陆需要进行官方审核，极有可能审核不通过
                //Coding......
            }
        });

        //微博登陆
        mWB_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast显示 微博登陆 的点击效果，用于测试
//                Toast.makeText(getApplicationContext(), "微博登陆", Toast.LENGTH_SHORT).show();
                //微博登陆，点击后转向微博授权页面
                startActivity(new Intent(LoginActivity.this, WBAuthActivity.class));
                //微博登陆如果成功，则不需要LoginActivity；若失败，则返回loginActivity。
                LoginActivity.this.finish();
            }
        });

    }

    /**
     * QQ 第三方登陆
     */
    private void qqLogin() {
        mTencent.login(this, "all", loginListener);
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) { //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                JSONObject object = (JSONObject) o;
                try {
                    final String accessToken = object.getString("access_token");
                    final String expires = object.getString("expires_in");
                    final String openID = object.getString("openid");

                    mTencent.setAccessToken(accessToken, expires);
                    mTencent.setOpenId(openID);

                    //首先查询该用户有无注册 -- 查询对应的username
                    BmobQuery<AllUserBean> bmobQuery = new BmobQuery<AllUserBean>();
                    //查询mobile叫mPhoneStr的数据
                    bmobQuery.addWhereEqualTo("userId", openID);
                    bmobQuery.findObjects(new FindListener<AllUserBean>() {
                        @Override
                        public void done(List<AllUserBean> list, BmobException e) {
                            if (e == null) {
                                if (list.size() > 0) { //已经注册到用户系统，直接登陆
                                    Toast.makeText(LoginActivity.this, "该QQ用户已注册，请直接登录", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("LoginAccountType", "weibo"));
                                    LoginActivity.this.finish();
                                } else { //没有注册到系统，现在注册
//                                        Toast.makeText(WBAuthActivity.this, "该微博尚未注册", Toast.LENGTH_SHORT).show();
                                    //注册对应用户
                                    AllUserBean userBean = new AllUserBean();
                                    //设置登陆模式
                                    userBean.setSnsType("qq");
                                    //设置登陆信息
                                    userBean.setPassword("qq123456"); // 设置密码
                                    userBean.setUsername("QQ"+openID); //设置用户名（唯一不变）
//                                        userBean.setMobilePhoneNumber(""); //设置用户登陆手机号
                                    userBean.setCode(0); //设置用户注册验证码
                                    //设置第三方登陆信息
                                    userBean.setAccessToken(accessToken);
                                    userBean.setExpiresIn(Long.valueOf(expires));
                                    userBean.setUserId(openID);
                                    //设置个人信息
                                    userBean.setUserNick("QQ"+openID);
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
                                                Toast.makeText(getApplicationContext(), "注册成功，已登录", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            } else {
                                                if (e.getErrorCode() == 202) {
                                                    CommonUnits.showToast(getApplicationContext(), "该用户已注册");
                                                } else {
                                                    CommonUnits.showToast(getApplicationContext(), "注册失败"+e.toString());
//                                                        LogUtil.i(TAG_WB, "reg error=" + e.getMessage());
                                                }
//                                                    LogUtil.i(TAG_WB, "reg error=" + e.getMessage());
                                            }
                                        }
                                    });
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                            } else { //注册到系统失败 -- 无动作 --查询失败
                                Toast.makeText(LoginActivity.this, "注册失败，请稍后重试"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {

            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);

                Tencent.handleResultData(data, loginListener);

                UserInfo info = new UserInfo(this, mTencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Toast.makeText(LoginActivity.this, "获取qq用户信息成功", Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONObject info = (JSONObject) o;
//                            String nickName = info.getString("nickname");//获取用户昵称
//                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url
//                            Log.i("TAG", "Username" + nickName);
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onError(UiError uiError) {

                        Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                        Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                        Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
